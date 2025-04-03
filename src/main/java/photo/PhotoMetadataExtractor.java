package photo;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.ZoneId;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PhotoMetadataExtractor {
    private String location;
    private LocalDateTime datetime;
    private double latitude;
    private double longitude;

    public PhotoMetadataExtractor(String filepath) {
        File imageFile = new File(filepath); // Change this to your image path
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
            ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            if (exifDirectory != null) {
                // Extract original datetime from metadata
                Date originalDate = exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                if (originalDate != null) {
                    LocalDateTime localDate = originalDate.toInstant()
                            .atZone(ZoneId.of("Asia/Singapore"))
                            .toLocalDateTime();
                    this.datetime = localDate;
                } else {
                    System.out.println("No DateTime metadata found.");
                }
                GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
                if (gpsDirectory != null && gpsDirectory.containsTag(GpsDirectory.TAG_LATITUDE)
                        && gpsDirectory.containsTag(GpsDirectory.TAG_LONGITUDE)) {
                    double latitude = gpsDirectory.getGeoLocation().getLatitude();
                    double longitude = gpsDirectory.getGeoLocation().getLongitude();
                    this.latitude = latitude;
                    this.longitude = longitude;
                    this.location = getLocationFromCoordinates(latitude, longitude);
                } else {
                    System.out.println("No GPS Data Found");
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading metadata: " + e.getMessage());
        }
    }

    /**
     * Returns a HashMap containing the extracted metadata:
     * { "location": "<City>, <Country>", "latitude": <latitude>, "longitude": <longitude>, "datetime": <datetime> }
     */
    public Map<String, Object> getMetadataMap() {
        Map<String, Object> metadataMap = new HashMap<>();
        metadataMap.put("location", this.location);
        metadataMap.put("latitude", this.latitude);
        metadataMap.put("longitude", this.longitude);
        metadataMap.put("datetime", this.datetime);
        return metadataMap;
    }

    /**
     * Modified getLocationFromCoordinates:
     * Instead of doing an HTTP request, this version reads from the local CSV file,
     * builds a KD-tree (if not already built), and performs a nearest neighbor search using the Haversine formula.
     * It returns a string in the format "<City>, <Country>".
     */
    public static String getLocationFromCoordinates(double latitude, double longitude) {
        // Lazy initialization: build KD-tree on first call.
        if (kdTree == null) {
            try {
                List<City> cities = loadCities("data/assets/1000cities.csv");
                kdTree = buildKDTree(cities, 0);
            } catch (IOException e) {
                return "Error loading city data: " + e.getMessage();
            }
        }
        City initialBest = kdTree.city;
        double initDist = haversine(latitude, longitude, initialBest.lat, initialBest.lon);
        City nearest = searchKDTree(kdTree, latitude, longitude, 0, initialBest, initDist);
        return nearest.name + ", " + nearest.country;
    }

    // ---------------------- Offline KD-Tree Reverse Geocoding Helpers ----------------------

    // Simple class to hold city data.
    private static class City {
        String name;
        String country;
        double lat, lon;

        City(String name, String country, double lat, double lon) {
            this.name = name;
            this.country = country;
            this.lat = lat;
            this.lon = lon;
        }
    }

    // Node for the KD-Tree.
    private static class KDNode {
        City city;
        KDNode left, right;

        KDNode(City city) {
            this.city = city;
        }
    }

    // Static KD-tree instance, built from the CSV file.
    private static KDNode kdTree = null;

    // Loads cities from a semicolon-delimited CSV file.
    // Expects the CSV format to be:
    // Geoname ID;Name;ASCII Name;Alternate Names;...;Country name EN;...;Coordinates
    // Where the "Name" is at index 1, "Country name EN" is at index 7,
    // and "Coordinates" (format: "lat, lon") is at index 19.
    private static List<City> loadCities(String filePath) throws IOException {
        List<City> cities = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        // Skip header line if present.
        line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length < 20) continue;
            String cityName = parts[1].trim();
            String country = parts[7].trim();
            String coords = parts[19].trim(); // Format: "lat, lon"
            String[] xy = coords.split(",");
            if (xy.length < 2) continue;
            try {
                double lat = Double.parseDouble(xy[0].trim());
                double lon = Double.parseDouble(xy[1].trim());
                cities.add(new City(cityName, country, lat, lon));
            } catch (NumberFormatException e) {
                // Skip invalid entry.
            }
        }
        br.close();
        return cities;
    }

    // Recursively builds a KD-Tree from the list of cities.
    private static KDNode buildKDTree(List<City> cities, int depth) {
        if (cities.isEmpty()) return null;
        int axis = depth % 2; // 0: latitude, 1: longitude.
        // Sort the cities by the chosen axis.
        cities.sort((c1, c2) -> axis == 0
                ? Double.compare(c1.lat, c2.lat)
                : Double.compare(c1.lon, c2.lon));
        int medianIndex = cities.size() / 2;
        City medianCity = cities.get(medianIndex);
        KDNode node = new KDNode(medianCity);
        node.left = buildKDTree(new ArrayList<>(cities.subList(0, medianIndex)), depth + 1);
        node.right = buildKDTree(new ArrayList<>(cities.subList(medianIndex + 1, cities.size())), depth + 1);
        return node;
    }

    // Haversine formula to compute the great-circle distance between two points.
    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0; // Earth's radius in kilometers.
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // Recursively searches the KD-Tree for the nearest city.
    private static City searchKDTree(KDNode node, double lat, double lon, int depth, City best, double bestDist) {
        if (node == null) return best;
        double d = haversine(lat, lon, node.city.lat, node.city.lon);
        City currentBest = best;
        double currentBestDist = bestDist;
        if (d < currentBestDist) {
            currentBest = node.city;
            currentBestDist = d;
        }
        int axis = depth % 2;
        KDNode goodSide, badSide;
        if (axis == 0) {
            if (lat < node.city.lat) {
                goodSide = node.left;
                badSide = node.right;
            } else {
                goodSide = node.right;
                badSide = node.left;
            }
        } else {
            if (lon < node.city.lon) {
                goodSide = node.left;
                badSide = node.right;
            } else {
                goodSide = node.right;
                badSide = node.left;
            }
        }
        currentBest = searchKDTree(goodSide, lat, lon, depth + 1, currentBest, currentBestDist);
        currentBestDist = haversine(lat, lon, currentBest.lat, currentBest.lon);

        double delta;
        if (axis == 0) {
            delta = Math.abs(lat - node.city.lat) * 111.0; // approximate km per degree latitude.
        } else {
            delta = Math.abs(lon - node.city.lon) * 111.0 * Math.cos(Math.toRadians(lat));
        }
        if (delta < currentBestDist) {
            currentBest = searchKDTree(badSide, lat, lon, depth + 1, currentBest, currentBestDist);
        }
        return currentBest;
    }
}
