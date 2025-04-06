package storage;

import album.Album;
import com.drew.imaging.ImageProcessingException;
import exception.FileFormatException;
import exception.PhotoLoadException;
import exception.TravelDiaryException;
import exception.TripLoadException;
import photo.Photo;
import trip.Trip;
import trip.TripManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StorageReader {
    private static final Logger logger = Logger.getLogger(StorageReader.class.getName());
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Reads trips from a file and adds them to the trip manager
     */
    protected static void readTripsFromFile(TripManager tripManager, File dataFile, String filePath)
            throws FileFormatException {
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            processFileLines(reader, tripManager, filePath);
        } catch (IOException e) {
            // Log the error but don't throw an exception
            logger.warning("Error reading file: " + filePath + ". " + e.getMessage());
        }
    }

    /**
     * Process all lines in the file
     */
    private static List<Trip> processFileLines(BufferedReader reader, TripManager tripManager, String filePath)
            throws IOException, FileFormatException {
        List<Trip> trips = new ArrayList<>();
        String line;
        Trip currentTrip = null;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null) {
            lineNumber++;
            try {
                // Improved splitting logic to handle DELIMITER correctly
                String[] parts = splitByDelimiter(line, Storage.DELIMITER);

                if (parts.length == 0) {
                    throw new FileFormatException(filePath, line);
                }

                String marker = parts[0];
                switch (marker) {
                case Storage.TRIP_MARKER:
                    // Add previous trip to list before creating a new one
                    if (currentTrip != null) {
                        trips.add(currentTrip);
                    }
                    currentTrip = createTrip(parts, tripManager, filePath);
                    break;
                case Storage.ALBUM_MARKER:
                    if (currentTrip == null) {
                        throw new FileFormatException(filePath, "Album marker found without a trip");
                    }
                    validateAlbumMarker(parts, filePath);
                    break;
                case Storage.PHOTO_MARKER:
                    if (currentTrip == null || currentTrip.album == null) {
                        throw new FileFormatException(filePath, "Photo marker found without a trip or album");
                    }
                    addPhotoToTrip(parts, currentTrip, filePath, lineNumber);
                    break;
                default:
                    throw new FileFormatException(filePath, "Unknown marker: " + marker);
                }
            } catch (TripLoadException | PhotoLoadException e) {
                // Propagate these exceptions without wrapping
                throw e;
            } catch (Exception e) {
                // Wrap any other exceptions
                if (!(e instanceof FileFormatException)) {
                    throw new FileFormatException(filePath, lineNumber, e);
                } else {
                    throw e;
                }
            }
        }

        // Add the last trip to the list
        if (currentTrip != null) {
            trips.add(currentTrip);
        }

        return trips;
    }

    /**
     * Splits a string by delimiter, respecting escaped delimiters
     */
    private static String[] splitByDelimiter(String line, String delimiter) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        boolean escaped = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (escaped) {
                currentPart.append(c);
                escaped = false;
                continue;
            }

            if (c == '\\') {
                escaped = true;
                continue;
            }

            // Check if we're at the start of a delimiter
            if (i <= line.length() - delimiter.length() &&
                    line.substring(i, i + delimiter.length()).equals(delimiter)) {
                parts.add(currentPart.toString());
                currentPart = new StringBuilder();
                i += delimiter.length() - 1; // Skip the delimiter
                continue;
            }

            currentPart.append(c);
        }

        // Add the last part
        parts.add(currentPart.toString());

        return parts.toArray(new String[0]);
    }

    /**
     * Creates a trip from a line
     */
    private static Trip createTrip(String[] parts, TripManager tripManager, String filePath)
            throws TripLoadException, FileFormatException {
        if (parts.length < 3) {
            throw new FileFormatException(filePath, String.join(Storage.DELIMITER, parts));
        }

        try {
            String name = StringEncoder.decodeString(parts[1]);
            String description = StringEncoder.decodeString(parts[2]);

            // Use the existing addTripSilently method to respect silent mode flag
            Trip currentTrip = tripManager.addTripSilently(name, description);

            // Ensure the trip has an album
            if (currentTrip.album == null) {
                currentTrip.album = new Album();
            }

            return currentTrip;
        } catch (TravelDiaryException e) {
            throw new TripLoadException(parts.length > 1 ? StringEncoder.decodeString(parts[1]) : "unknown", e);
        }
    }

    /**
     * Validates album marker format
     */
    private static void validateAlbumMarker(String[] parts, String filePath) throws FileFormatException {
        if (parts.length <= 1) {
            throw new FileFormatException(filePath, String.join(Storage.DELIMITER, parts));
        }
        // Currently, the album name (parts[1]) is not used further
    }

    /**
     * Adds a photo to a trip from a line
     */
    private static void addPhotoToTrip(String[] parts, Trip currentTrip, String filePath, int lineNumber)
            throws PhotoLoadException, FileFormatException {
        if (parts.length < 4) {
            throw new FileFormatException(filePath, String.join(Storage.DELIMITER, parts));
        }

        try {
            if (parts.length < 4) {
                throw new FileFormatException(filePath, String.join(" | ", parts));
            }

            String photoPath = StringEncoder.decodeString(parts[1]);
            String photoName = StringEncoder.decodeString(parts[2]);
            String caption = StringEncoder.decodeString(parts[3]);

            // Extract photo timestamp
            LocalDateTime photoTime = extractPhotoTime(parts);

            // Get current album silent mode setting before adding photo
            boolean originalSilentMode = false;
            if (currentTrip.album != null) {
                originalSilentMode = currentTrip.album.isSilentMode();
                // Use the trip's silent mode setting for the album during loading
                currentTrip.album.setSilentMode(true);
            }

            try {
                // Create the photo with all available data
                if (photoTime != null) {
                    currentTrip.album.addPhoto(photoPath, photoName, caption, photoTime);
                } else {
                    currentTrip.album.addPhoto(photoPath, photoName, caption);
                }
            } finally {
                // Restore the original silent mode setting
                if (currentTrip.album != null) {
                    currentTrip.album.setSilentMode(originalSilentMode);
                }
            }

        } catch (DateTimeParseException e) {
            throw new FileFormatException(filePath, lineNumber, e);
        } catch (TravelDiaryException | ImageProcessingException | IOException e) {
            throw new PhotoLoadException(
                    parts.length > 2 ? StringEncoder.decodeString(parts[2]) : "unknown",
                    parts.length > 1 ? StringEncoder.decodeString(parts[1]) : "unknown",
                    e
            );
        }
    }

    /**
     * Extracts photo timestamp from parts
     */
    private static LocalDateTime extractPhotoTime(String[] parts) throws DateTimeParseException {
        if (parts.length <= 4 || parts[4] == null || parts[4].isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(parts[4], DATETIME_FORMAT);
    }
}