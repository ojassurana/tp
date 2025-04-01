package photo;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.*;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.Scanner;
import java.time.LocalDateTime;

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
                LocalDateTime localDate = originalDate.toInstant()
                        .atZone(ZoneId.of("Asia/Singapore"))
                        .toLocalDateTime();
                if (originalDate != null) {
                    this.datetime = localDate;
                } else {
                    System.out.println("No DateTime metadata found.");
                }
                GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
                if (gpsDirectory != null && gpsDirectory.containsTag(GpsDirectory.TAG_LATITUDE)
                        && gpsDirectory.containsTag(GpsDirectory.TAG_LONGITUDE)) {
                    double latitude = gpsDirectory.getGeoLocation().getLatitude();
                    double longitude = gpsDirectory.getGeoLocation().getLongitude();
                    this.longitude = longitude;
                    this.latitude = latitude;
                    this.location = getLocationFromCoordinates(latitude, longitude);
                } else {
                    System.out.println("No GPS Data Found");
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading metadata: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        PhotoMetadataExtractor me = new PhotoMetadataExtractor("./data/photos/clem_with_metadata.jpg");
        System.out.println("location : " + me.location);
        System.out.println("datetime : " + me.datetime);
        System.out.println("longitude : " + me.longitude);
        System.out.println("latitude : " + me.latitude);
    }


    public static String getLocationFromCoordinates(double latitude, double longitude) {
        String urlString = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + latitude + "&lon=" + longitude;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Accept", "application/json");

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder jsonResponse = new StringBuilder();
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.nextLine());
            }
            scanner.close();
            conn.disconnect();

            // Extract location info from JSON response
            String response = jsonResponse.toString();
            if (response.contains("\"city\"")) {
                return response.split("\"city\":\"")[1].split("\"")[0]; // Extracts city name
            } else if (response.contains("\"town\"")) {
                return response.split("\"town\":\"")[1].split("\"")[0]; // Extracts town name
            } else if (response.contains("\"country\"")) {
                return response.split("\"country\":\"")[1].split("\"")[0]; // Extracts country name
            } else {
                return "Location not found";
            }

        } catch (IOException e) {
            return "Error fetching location: " + e.getMessage();
        }
    }
}
