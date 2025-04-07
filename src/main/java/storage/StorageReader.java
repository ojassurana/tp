package storage;

import album.Album;
import com.drew.imaging.ImageProcessingException;
import exception.FileFormatException;
import exception.NoMetaDataException;
import exception.PhotoLoadException;
import exception.TravelDiaryException;
import exception.TripLoadException;
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
            throws FileFormatException, NoMetaDataException {
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
            throws IOException, FileFormatException, NoMetaDataException {
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
                    currentTrip = processTripMarker(parts, tripManager, filePath, currentTrip, trips);
                    break;
                case Storage.ALBUM_MARKER:
                    validateAlbumAndMarker(currentTrip, parts, filePath);
                    break;
                case Storage.PHOTO_MARKER:
                    validatePhotoContext(currentTrip, filePath);
                    addPhotoToTrip(parts, currentTrip, filePath, lineNumber);
                    break;
                default:
                    throw new FileFormatException(filePath, "Unknown marker: " + marker);
                }
            } catch (TripLoadException | PhotoLoadException e) {
                // Propagate these exceptions without wrapping
                throw e;
            } catch (Exception e) {
                // Only wrap if it's not already a FileFormatException
                if (!(e instanceof FileFormatException)) {
                    throw new FileFormatException(filePath, lineNumber, e);
                }
                throw e;
            }
        }

        // Add the last trip to the list if there is one
        addRemainingTrip(currentTrip, trips);
        return trips;
    }

    /**
     * Process trip marker line and manage trip collection
     */
    private static Trip processTripMarker(String[] parts, TripManager tripManager, String filePath,
                                          Trip currentTrip, List<Trip> trips)
            throws TripLoadException, FileFormatException {
        // Add previous trip to list before creating a new one
        addRemainingTrip(currentTrip, trips);
        return createTrip(parts, tripManager, filePath);
    }

    /**
     * Adds the current trip to the list if it exists
     */
    private static void addRemainingTrip(Trip currentTrip, List<Trip> trips) {
        if (currentTrip != null) {
            trips.add(currentTrip);
        }
    }

    /**
     * Validates that we have a trip when processing an album marker
     */
    private static void validateAlbumAndMarker(Trip currentTrip, String[] parts, String filePath)
            throws FileFormatException {
        if (currentTrip == null) {
            throw new FileFormatException(filePath, "Album marker found without a trip");
        }

        if (parts.length <= 1) {
            throw new FileFormatException(filePath, String.join(Storage.DELIMITER, parts));
        }
        // Currently, the album name (parts[1]) is not used further
    }

    /**
     * Validates that we have proper context for adding a photo
     */
    private static void validatePhotoContext(Trip currentTrip, String filePath)
            throws FileFormatException {
        if (currentTrip == null) {
            throw new FileFormatException(filePath, "Photo marker found without a trip");
        }

        if (currentTrip.album == null) {
            throw new FileFormatException(filePath, "Photo marker found without an album");
        }
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

            // Check if this is the start of a delimiter
            boolean isDelimiter = checkDelimiter(line, i, delimiter);
            if (isDelimiter) {
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
     * Check if the current position in the string is a delimiter
     */
    private static boolean checkDelimiter(String line, int position, String delimiter) {
        if (position > line.length() - delimiter.length()) {
            return false;
        }

        return line.substring(position, position + delimiter.length()).equals(delimiter);
    }

    /**
     * Creates a trip from a line
     */
    private static Trip createTrip(String[] parts, TripManager tripManager, String filePath)
            throws TripLoadException, FileFormatException {
        validateTripFormat(parts, filePath);

        try {
            String name = StringEncoder.decodeString(parts[1]);
            String description = StringEncoder.decodeString(parts[2]);

            // Use the existing addTripSilently method to respect silent mode flag
            Trip newTrip = tripManager.addTripSilently(name, description);
            ensureTripHasAlbum(newTrip);
            return newTrip;
        } catch (TravelDiaryException e) {
            String tripName = "unknown";
            if (parts.length > 1) {
                tripName = StringEncoder.decodeString(parts[1]);
            }
            throw new TripLoadException(tripName, e);
        }
    }

    /**
     * Validates trip marker format has required fields
     */
    private static void validateTripFormat(String[] parts, String filePath) throws FileFormatException {
        if (parts.length < 3) {
            throw new FileFormatException(filePath, String.join(Storage.DELIMITER, parts));
        }
    }

    /**
     * Ensures the trip has an album
     */
    private static void ensureTripHasAlbum(Trip trip) {
        if (trip.album == null) {
            trip.album = new Album();
        }
    }

    /**
     * Adds a photo to a trip from a line
     */
    private static void addPhotoToTrip(String[] parts, Trip currentTrip, String filePath, int lineNumber)
            throws PhotoLoadException, FileFormatException, NoMetaDataException {
        validatePhotoLineFormat(parts, filePath);

        try {
            String photoPath = StringEncoder.decodeString(parts[1]);
            String photoName = StringEncoder.decodeString(parts[2]);
            String caption = StringEncoder.decodeString(parts[3]);
            LocalDateTime photoTime = extractPhotoTime(parts);

            addPhotoWithSilentMode(currentTrip, photoPath, photoName, caption, photoTime);
        } catch (DateTimeParseException e) {
            throw new FileFormatException(filePath, lineNumber, e);
        } catch (TravelDiaryException | ImageProcessingException | IOException e) {
            String photoName = extractPhotoNameForError(parts, 2);
            String photoPath = extractPhotoNameForError(parts, 1);
            throw new PhotoLoadException(photoName, photoPath, e);
        }
    }

    /**
     * Adds a photo to an album with preserved silent mode
     */
    private static void addPhotoWithSilentMode(Trip trip, String photoPath, String photoName,
                                               String caption, LocalDateTime photoTime)
            throws TravelDiaryException, ImageProcessingException, IOException, NoMetaDataException {
        // Get current album silent mode setting before adding photo
        boolean originalSilentMode = trip.album.isSilentMode();
        // Use silent mode during loading
        trip.album.setSilentMode(true);

        try {
            // Create the photo with all available data
            if (photoTime != null) {
                trip.album.addPhoto(photoPath, photoName, caption, photoTime);
            } else {
                trip.album.addPhoto(photoPath, photoName, caption);
            }
        } finally {
            // Restore the original silent mode setting
            trip.album.setSilentMode(originalSilentMode);
        }
    }

    /**
     * Extracts a field for error reporting, handling missing fields gracefully
     */
    private static String extractPhotoNameForError(String[] parts, int index) {
        if (parts.length > index) {
            return StringEncoder.decodeString(parts[index]);
        }
        return "unknown";
    }

    /**
     * Validates the photo line format has the minimum required fields
     */
    private static void validatePhotoLineFormat(String[] parts, String filePath) throws FileFormatException {
        if (parts.length < 4) {
            throw new FileFormatException(filePath, String.join(Storage.DELIMITER, parts));
        }
    }

    /**
     * Extracts photo timestamp from parts
     */
    private static LocalDateTime extractPhotoTime(String[] parts) throws DateTimeParseException {
        if (parts.length <= 4) {
            return null;
        }

        String timeStr = parts[4];
        if (timeStr == null || timeStr.isEmpty()) {
            return null;
        }

        return LocalDateTime.parse(timeStr, DATETIME_FORMAT);
    }
}
