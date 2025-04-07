package photo;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.MetadataFilepathNotFound;
import exception.NoDateTimeMetaDataException;
import exception.NoGPSMetaDataException;
import exception.NoMetaDataException;
import tracker.Tracker;

public class PhotoMetadataExtractor {

    // Static KD-tree instance, built from the CSV file.
    private static KDNode kdTree = null;

    private String location;
    private LocalDateTime datetime;
    private double latitude;
    private double longitude;

    /**
     * Constructs a PhotoMetadataExtractor to read metadata from the given file path.
     *
     * @param filepath the path to the image file
     */
    public PhotoMetadataExtractor(String filepath) throws ImageProcessingException, NoMetaDataException,
            MetadataFilepathNotFound {
        File imageFile = new File(filepath);
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
                    throw new NoDateTimeMetaDataException();
                }

                GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
                if (gpsDirectory != null
                        && gpsDirectory.containsTag(GpsDirectory.TAG_LATITUDE)
                        && gpsDirectory.containsTag(GpsDirectory.TAG_LONGITUDE)) {
                    double extractedLat = gpsDirectory.getGeoLocation().getLatitude();
                    double extractedLon = gpsDirectory.getGeoLocation().getLongitude();
                    this.latitude = extractedLat;
                    this.longitude = extractedLon;
                    this.location = getLocationFromCoordinates(extractedLat, extractedLon);
                } else {
                    throw new NoGPSMetaDataException();
                }
            } else {
                throw new NoMetaDataException();
            }
        } catch (IOException e) {
            throw new MetadataFilepathNotFound(filepath);
        }
    }

    /**
     * Returns a map of the extracted metadata: location, latitude, longitude, and datetime.
     *
     * @return a map containing metadata fields
     */
    public Map<String, Object> getMetadataMap() {
        Map<String, Object> metadataMap = new HashMap<>();
        metadataMap.put("location", this.location);
        metadataMap.put("latitude", this.latitude);
        metadataMap.put("longitude", this.longitude);
        metadataMap.put("datetime", this.datetime);
        return metadataMap;
    }

    public static String getLocationFromCoordinates(double latitude, double longitude) {
        if (kdTree == null) {
            try {
                List<City> cities = loadCities("data/assets/1000cities.csv");
                kdTree = buildKDTree(cities, 0);
            } catch (IOException e) {
                return "Error loading city data: " + e.getMessage();
            }
        }
        City initialBest = kdTree.city;
        double initDist = Tracker.calculateHaversineDistance(latitude, longitude, initialBest.lat, initialBest.lon);
        City nearest = searchKDTree(kdTree, latitude, longitude, 0, initialBest, initDist);
        return nearest.name + ", " + nearest.country;
    }

    // ---------------------- Offline KD-Tree Reverse Geocoding Helpers ----------------------

    /**
     * A simple class to hold city data.
     */
    private static class City {
        String name;
        String country;
        double lat;
        double lon;

        City(String name, String country, double lat, double lon) {
            this.name = name;
            this.country = country;
            this.lat = lat;
            this.lon = lon;
        }
    }

    /**
     * A KD-Tree node containing city data and left/right child pointers.
     */
    private static class KDNode {
        City city;
        KDNode left;
        KDNode right;

        KDNode(City city) {
            this.city = city;
        }
    }

    /**
     * Loads cities from a semicolon-delimited CSV file.
     * Expects the CSV format:
     * Geoname ID;Name;ASCII Name;Alternate Names;...;Country name EN;...;Coordinates
     * where "Name" is at index 1, "Country name EN" is at index 7, and "Coordinates" is at index 19
     * in the format "lat, lon".
     *
     * @param filePath the path to the CSV file
     * @return a list of City objects
     * @throws IOException if there's an error reading the file
     */
    private static List<City> loadCities(String filePath) throws IOException {
        List<City> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header line if present
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 20) {
                    continue;
                }
                String cityName = parts[1].trim();
                String country = parts[7].trim();
                String coords = parts[19].trim(); // Format: "lat, lon"
                String[] xy = coords.split(",");
                if (xy.length < 2) {
                    continue;
                }
                try {
                    double lat = Double.parseDouble(xy[0].trim());
                    double lon = Double.parseDouble(xy[1].trim());
                    cities.add(new City(cityName, country, lat, lon));
                } catch (NumberFormatException e) {
                    // Skip invalid entry
                }
            }
        }
        return cities;
    }

    /**
     * Recursively builds a KD-Tree from a list of cities.
     *
     * @param cities the list of City objects
     * @param depth  the current tree depth (used to alternate between lat/long)
     * @return the root KDNode
     */
    private static KDNode buildKDTree(List<City> cities, int depth) {
        if (cities.isEmpty()) {
            return null;
        }
        int axis = depth % 2; // 0: latitude, 1: longitude
        cities.sort((c1, c2) -> axis == 0
                ? Double.compare(c1.lat, c2.lat)
                : Double.compare(c1.lon, c2.lon));
        int medianIndex = cities.size() / 2;
        City medianCity = cities.get(medianIndex);
        KDNode node = new KDNode(medianCity);
        List<City> leftSub = new ArrayList<>(cities.subList(0, medianIndex));
        List<City> rightSub = new ArrayList<>(cities.subList(medianIndex + 1, cities.size()));
        node.left = buildKDTree(leftSub, depth + 1);
        node.right = buildKDTree(rightSub, depth + 1);
        return node;
    }

    /**
     * Recursively searches the KD-Tree for the nearest city to (lat, lon).
     *
     * @param node     the current KDNode
     * @param lat      target latitude
     * @param lon      target longitude
     * @param depth    current depth (to pick lat or lon as the axis)
     * @param best     the current best City
     * @param bestDist the distance to the current best City
     * @return the nearest City
     */
    private static City searchKDTree(KDNode node, double lat, double lon,
                                     int depth, City best, double bestDist) {
        if (node == null) {
            return best;
        }
        double d = Tracker.calculateHaversineDistance(lat, lon, node.city.lat, node.city.lon);
        City currentBest = best;
        double currentBestDist = bestDist;
        if (d < currentBestDist) {
            currentBest = node.city;
            currentBestDist = d;
        }
        int axis = depth % 2;
        KDNode goodSide;
        KDNode badSide;
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
        currentBestDist = Tracker.calculateHaversineDistance(lat, lon, currentBest.lat, currentBest.lon);

        double delta;
        if (axis == 0) {
            delta = Math.abs(lat - node.city.lat) * 111.0;
        } else {
            delta = Math.abs(lon - node.city.lon) * 111.0 * Math.cos(Math.toRadians(lat));
        }
        if (delta < currentBestDist) {
            currentBest = searchKDTree(badSide, lat, lon, depth + 1, currentBest, currentBestDist);
        }
        return currentBest;
    }
}
