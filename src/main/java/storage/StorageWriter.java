package storage;

import exception.FileWriteException;
import exception.PhotoSaveException;
import photo.Location;
import photo.Photo;
import trip.Trip;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Class responsible for writing trip data to storage files.
 * This class provides functionality to serialize trip data and save it to files
 * for the Travel Diary application. It handles the formatting and encoding of
 * trips, albums, and photos to ensure proper storage.
 */
public class StorageWriter {
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Writes a list of trips to a file.
     * This method iterates through all trips and calls writeTrip for each one,
     * handling any IO exceptions that may occur during the writing process.
     *
     * @param trips The list of Trip objects to write
     * @param dataFile The File object representing the destination file
     * @param filePath The path of the file as a String (used for error reporting)
     * @throws FileWriteException If an error occurs while writing to the file
     */
    protected static void writeTripsToFile(List<Trip> trips, File dataFile, String filePath) throws FileWriteException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            for (Trip trip : trips) {
                writeTrip(writer, trip, filePath);
            }
        } catch (IOException e) {
            throw new FileWriteException(filePath, e);
        }
    }

    /**
     * Writes a single trip and its photos to the file.
     * This method handles the serialization of a Trip object, including its album and photos.
     * It first writes the trip data, then the album marker, and finally each photo in the album.
     *
     * @param writer The BufferedWriter used to write to the file
     * @param trip The Trip object to write
     * @param filePath The path of the file (used for error reporting)
     * @throws IOException If an error occurs during the writing process
     * @throws PhotoSaveException If an error occurs while saving a photo
     */
    private static void writeTrip(BufferedWriter writer, Trip trip, String filePath)
            throws IOException, PhotoSaveException {
        // Write trip data
        writer.write(formatTripLine(trip));
        writer.newLine();

        // Skip if no album
        if (trip.album == null) {
            return;
        }

        // Write album marker with proper escaping for trip name
        writer.write(Storage.ALBUM_MARKER + Storage.DELIMITER + StringEncoder.encodeString(trip.name));
        writer.newLine();

        // Skip if no photos
        if (trip.album.photos == null || trip.album.photos.isEmpty()) {
            return;
        }

        // Write each photo
        for (Photo photo : trip.album.photos) {
            try {
                writer.write(formatPhotoLine(photo));
                writer.newLine();
            } catch (Exception e) {
                throw new PhotoSaveException(photo.getPhotoName(), filePath, e);
            }
        }
    }

    /**
     * Formats a trip into a line for storage.
     * This method creates a properly formatted and encoded string representation
     * of a Trip object suitable for storage in a file.
     *
     * @param trip The Trip object to format
     * @return A formatted string containing the trip data
     */
    private static String formatTripLine(Trip trip) {
        return Storage.TRIP_MARKER + Storage.DELIMITER +
                StringEncoder.encodeString(trip.name) + Storage.DELIMITER +
                StringEncoder.encodeString(trip.description == null ? "" : trip.description);
    }

    /**
     * Formats a photo into a line for storage.
     * This method creates a properly formatted and encoded string representation
     * of a Photo object suitable for storage in a file. It includes the photo's
     * file path, name, caption, datetime, and location information.
     *
     * @param photo The Photo object to format
     * @return A formatted string containing the photo data
     */
    private static String formatPhotoLine(Photo photo) {
        String dateTimeString = "";
        if (photo.getDatetime() != null) {
            dateTimeString = photo.getDatetime().format(DATETIME_FORMAT);
        }

        // Get location information from Location object
        Location location = photo.getLocation();
        String locationName = "";
        double latitude = 0.0;
        double longitude = 0.0;

        if (location != null) {
            locationName = location.getLocationName() != null ? location.getLocationName() : "";
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        return Storage.PHOTO_MARKER + Storage.DELIMITER +
                StringEncoder.encodeString(photo.getFilePath()) + Storage.DELIMITER +
                StringEncoder.encodeString(photo.getPhotoName()) + Storage.DELIMITER +
                StringEncoder.encodeString(photo.getCaption() == null ? "" : photo.getCaption()) + Storage.DELIMITER +
                dateTimeString + Storage.DELIMITER +
                StringEncoder.encodeString(locationName) + Storage.DELIMITER +
                latitude + Storage.DELIMITER +
                longitude;
    }
}
