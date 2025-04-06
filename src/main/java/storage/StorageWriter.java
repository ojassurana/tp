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

public class StorageWriter {
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Writes trips to a file
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
     * Writes a single trip and its photos to the file
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
     * Formats a trip into a line for storage
     */
    private static String formatTripLine(Trip trip) {
        return Storage.TRIP_MARKER + Storage.DELIMITER +
                StringEncoder.encodeString(trip.name) + Storage.DELIMITER +
                StringEncoder.encodeString(trip.description == null ? "" : trip.description);
    }

    /**
     * Formats a photo into a line for storage
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