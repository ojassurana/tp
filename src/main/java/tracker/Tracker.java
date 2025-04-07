package tracker;

import photo.Location;
import photo.Photo;
import photo.PhotoDateTimeComparator;
import album.Album;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.List;

/**
 * The Tracker class provides utility methods for managing and analyzing photos and albums.
 * This includes sorting photos by date, calculating the distance between two photos based
 * on their geographical locations, and retrieving the date range of photos in an album.
 */
public class Tracker {

    // Constant representing the Earth's radius in kilometers, used in distance calculations.
    public static final int EARTH_RADIUS = 6371;

    // Logger instance to track operations and log information for debugging purposes.
    private static final Logger logger = Logger.getLogger(Tracker.class.getName());

    /**
     * Sorts a list of photos in ascending order based on their date and time.
     * Utilizes the PhotoDateTimeComparator class for sorting logic.
     *
     * @param photoList The list of photos to be sorted.
     */
    public static void sortPhotosByDate(List<Photo> photoList) {
        photoList.sort(new PhotoDateTimeComparator());
    }

    /**
     * Calculates the geographical distance between two photos based on their locations.
     * This method uses the Haversine formula to calculate the distance in kilometers.
     *
     * @param photo1 The first photo containing location information.
     * @param photo2 The second photo containing location information.
     * @return The distance between the two photos in kilometers.
     */
    public static double getDist(Photo photo1, Photo photo2) {
        // Retrieve the locations of both photos
        Location location1 = photo1.getLocation();
        Location location2 = photo2.getLocation();

        // Calculate the distance using the Haversine formula
        double distance = calculateHaversineDistance(
                location1.getLatitude(), location1.getLongitude(),
                location2.getLatitude(), location2.getLongitude()
        );

        // Log the calculated distance along with photo names
        logger.info(String.format("Distance between %s and %s: %skm",
                photo1.getPhotoName(), photo2.getPhotoName(), distance));

        return distance;
    }

    /**
     * Calculates the geographical distance between two points based on latitude and longitude.
     * This method uses the Haversine formula to compute the distance in kilometers.
     *
     * @param lat1 The latitude of the first location in degrees.
     * @param lon1 The longitude of the first location in degrees.
     * @param lat2 The latitude of the second location in degrees.
     * @param lon2 The longitude of the second location in degrees.
     * @return The distance between the two locations in kilometers.
     */
    public static double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        // Calculate the differences in latitude and longitude in radians
        double deltaLatitude = Math.toRadians(lat2 - lat1);
        double deltaLongitude = Math.toRadians(lon2 - lon1);

        // Apply the Haversine formula
        double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance and round the result
        return Math.round(EARTH_RADIUS * c);
    }

    /**
     * Retrieves the date range of photos within a given album.
     * This method identifies the earliest and latest dates from the photos
     * and formats them as strings for display.
     *
     * @param album The album containing the photos to analyze.
     * @return A list containing two formatted strings: the earliest date and the latest date.
     */
    public static List<String> getPeriod(Album album) {
        // Extract all date-time values from the photos in the album.
        List<LocalDateTime> dateTimeList = new ArrayList<>();
        for (Photo photo : album.getPhotos()) {
            dateTimeList.add(photo.getDatetime());
        }

        // Find the minimum and maximum date-time values.
        LocalDateTime minimumDateTime = dateTimeList.stream().min(LocalDateTime::compareTo).orElse(null);
        LocalDateTime maximumDateTime = dateTimeList.stream().max(LocalDateTime::compareTo).orElse(null);

        // Define the format for date-time representation.
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");

        // Return the formatted date range as a list of strings.
        return List.of(
                formatDate(minimumDateTime, dateTimeFormatter),
                formatDate(maximumDateTime, dateTimeFormatter)
        );
    }

    /**
     * Formats a LocalDateTime object into a readable string based on a specified pattern.
     * If the date-time is null, returns "No Date Available".
     *
     * @param dateTime The LocalDateTime object to format.
     * @param formatter The DateTimeFormatter used for formatting the date-time.
     * @return A formatted string representing the date-time, or "No Date Available" if null.
     */
    private static String formatDate(LocalDateTime dateTime, DateTimeFormatter formatter) {
        if (dateTime != null) {
            return dateTime.format(formatter);
        } else {
            return "No Date Available";
        }
    }
}
