package storage;

import album.Album;
import com.drew.imaging.ImageProcessingException;
import exception.NoMetaDataException;
import exception.FileFormatException;
import exception.TravelDiaryException;
import exception.MissingCompulsoryParameter;
import exception.MetadataFilepathNotFound;
import exception.DuplicateFilepathException;
import exception.DuplicateNameException;
import exception.TripLoadException;
import exception.PhotoLoadException;
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

/**
 * Class responsible for reading trip data from storage files.
 * This class provides functionality to parse trip data stored in files
 * and load them into the TripManager for the Travel Diary application.
 */
public class StorageReader {
    private static final Logger logger = Logger.getLogger(StorageReader.class.getName());
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static boolean isTripCorrupted = false;

    /**
     * Reads trips data from a file and adds them to the trip manager.
     * This method processes the file line by line, parsing trip, album, and photo information.
     *
     * @param tripManager The TripManager instance to which trips should be added
     * @param dataFile The file containing trip data
     * @param filePath The path of the file (used for error reporting)
     * @throws FileFormatException If the file format is invalid
     * @throws NoMetaDataException If required metadata is missing
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
     * Processes all lines in the file, parsing trip, album, and photo data.
     * This method reads each line, identifies its type based on markers, and
     * processes it accordingly.
     *
     * @param reader The BufferedReader to read lines from
     * @param tripManager The TripManager instance to which trips should be added
     * @param filePath The path of the file (used for error reporting)
     * @throws FileFormatException If the file format is invalid
     * @throws IOException If an I/O error occurs
     * @throws NoMetaDataException If required metadata is missing
     */
    private static void processFileLines(BufferedReader reader, TripManager tripManager, String filePath)
            throws FileFormatException, IOException, NoMetaDataException {
        List<Trip> trips = new ArrayList<>();
        String line;
        Trip currentTrip = null;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null) {
            lineNumber++;
            String[] parts = splitByDelimiter(line, Storage.DELIMITER);
            // if a trip is corrupted, skip the photos and album inside the corrupted trip until a
            // new trip is encountered
            if (isTripCorrupted && !parts[0].equals(Storage.TRIP_MARKER)){
                continue;
            }
            isTripCorrupted = false;
            try {
                // Improved splitting logic to handle DELIMITER correctly

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
            } catch (TripLoadException  e) {
                // Propagate these exceptions without wrapping
                System.out.println(10);
                isTripCorrupted = true;
            } catch (PhotoLoadException e) {
                // Propagate these exceptions without wrapping
                System.out.println(11);
            } catch (Exception e) {
                // Only wrap if it's not already a FileFormatException
                if (!(e instanceof FileFormatException)) {
                    throw new FileFormatException(filePath, lineNumber, e);
                }
                System.out.println(e.getMessage());
                //throw e;
            }
        }

        // Add the last trip to the list if there is one
        addRemainingTrip(currentTrip, trips);
    }

    /**
     * Processes a trip marker line and manages the trip collection.
     * This method adds the previous trip to the collection before creating a new one.
     *
     * @param parts The parts of the line split by delimiter
     * @param tripManager The TripManager instance
     * @param filePath The file path for error reporting
     * @param currentTrip The current trip being processed
     * @param trips The list of trips collected
     * @return A new Trip object
     * @throws TripLoadException If the trip cannot be loaded
     * @throws FileFormatException If the file format is invalid
     */
    private static Trip processTripMarker(String[] parts, TripManager tripManager, String filePath,
                                          Trip currentTrip, List<Trip> trips)
            throws TripLoadException, FileFormatException {
        // Add previous trip to list before creating a new one
        addRemainingTrip(currentTrip, trips);
        return createTrip(parts, tripManager, filePath);
    }

    /**
     * Adds the current trip to the list if it exists.
     * This method is used to ensure the current trip is included in the collection
     * before moving on to process a new trip.
     *
     * @param currentTrip The current trip to add
     * @param trips The list of trips to add to
     */
    private static void addRemainingTrip(Trip currentTrip, List<Trip> trips) {
        if (currentTrip != null) {
            trips.add(currentTrip);
        }
    }

    /**
     * Validates that we have a trip when processing an album marker.
     * This method checks that the album marker appears in the correct context
     * and has the expected format.
     *
     * @param currentTrip The current trip being processed
     * @param parts The parts of the line split by delimiter
     * @param filePath The file path for error reporting
     * @throws FileFormatException If validation fails
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
     * Validates that we have proper context for adding a photo.
     * This method checks that a photo appears in the context of a trip and album.
     *
     * @param currentTrip The current trip being processed
     * @param filePath The file path for error reporting
     * @throws FileFormatException If validation fails
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
     * Splits a string by delimiter, respecting escaped delimiters.
     * This method handles the case where delimiters may be escaped in the input string.
     *
     * @param line The line to split
     * @param delimiter The delimiter to split by
     * @return An array of parts split by the delimiter
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
     * Checks if the current position in the string is a delimiter.
     * This helper method determines if the characters starting at the given position
     * match the delimiter string.
     *
     * @param line The line being processed
     * @param position The current position in the line
     * @param delimiter The delimiter to check for
     * @return true if the delimiter is found at the position, false otherwise
     */
    private static boolean checkDelimiter(String line, int position, String delimiter) {
        if (position > line.length() - delimiter.length()) {
            return false;
        }

        return line.substring(position, position + delimiter.length()).equals(delimiter);
    }

    /**
     * Creates a trip object from the parsed line parts.
     * This method validates the trip format, decodes the trip name and description,
     * and adds the trip to the trip manager.
     *
     * @param parts The parts of the line split by delimiter
     * @param tripManager The TripManager instance
     * @param filePath The file path for error reporting
     * @return A new Trip object
     * @throws TripLoadException If the trip cannot be loaded
     * @throws FileFormatException If the file format is invalid
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
        } catch (TravelDiaryException|MissingCompulsoryParameter|DuplicateNameException e) {
            String tripName = "unknown";
            if (parts.length > 1) {
                tripName = StringEncoder.decodeString(parts[1]);
            }
            throw new TripLoadException(tripName, e);
        }
    }

    /**
     * Validates that the trip marker format has the required fields.
     * This method checks that the trip line has the minimum required number of parts.
     *
     * @param parts The parts of the line split by delimiter
     * @param filePath The file path for error reporting
     * @throws FileFormatException If validation fails
     */
    private static void validateTripFormat(String[] parts, String filePath) throws FileFormatException {
        if (parts.length < 3) {
            isTripCorrupted = true;
            throw new FileFormatException(filePath, String.join(Storage.DELIMITER, parts));
        }
    }

    /**
     * Ensures the trip has an album.
     * If the trip doesn't have an album, a new one is created.
     *
     * @param trip The trip to check and update
     */
    private static void ensureTripHasAlbum(Trip trip) {
        if (trip.album == null) {
            trip.album = new Album();
        }
    }

    /**
     * Adds a photo to a trip from a parsed line.
     * This method validates the photo line format, extracts photo details,
     * and adds the photo to the trip's album.
     *
     * @param parts The parts of the line split by delimiter
     * @param currentTrip The current trip to add the photo to
     * @param filePath The file path for error reporting
     * @param lineNumber The line number for error reporting
     * @throws PhotoLoadException If the photo cannot be loaded
     * @throws FileFormatException If the file format is invalid
     * @throws NoMetaDataException If required metadata is missing
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
        } catch (TravelDiaryException | ImageProcessingException | MetadataFilepathNotFound |
                 DuplicateNameException | DuplicateFilepathException e) {
            String photoName = extractPhotoNameForError(parts, 2);
            String photoPath = extractPhotoNameForError(parts, 1);
            throw new PhotoLoadException(photoName, photoPath, e);
        }
    }

    /**
     * Adds a photo to an album with preserved silent mode.
     * This method temporarily sets the album to silent mode, adds the photo,
     * and then restores the original silent mode setting.
     *
     * @param trip The trip containing the album
     * @param photoPath The path to the photo file
     * @param photoName The name of the photo
     * @param caption The caption for the photo
     * @param photoTime The timestamp of the photo, or null if not available
     * @throws TravelDiaryException If a general travel diary error occurs
     * @throws ImageProcessingException If the image cannot be processed
     * @throws MetadataFilepathNotFound If the metadata filepath is not found
     * @throws NoMetaDataException If required metadata is missing
     * @throws DuplicateNameException If the photo name is a duplicate
     * @throws DuplicateFilepathException If the photo path is a duplicate
     */
    private static void addPhotoWithSilentMode(Trip trip, String photoPath, String photoName,
                                               String caption, LocalDateTime photoTime)
            throws TravelDiaryException, ImageProcessingException, MetadataFilepathNotFound, NoMetaDataException,
            DuplicateNameException, DuplicateFilepathException {
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
     * Extracts a field for error reporting, handling missing fields gracefully.
     * This method safely extracts a field from the parts array, returning "unknown"
     * if the field is not available.
     *
     * @param parts The parts of the line split by delimiter
     * @param index The index of the field to extract
     * @return The decoded field value, or "unknown" if not available
     */
    private static String extractPhotoNameForError(String[] parts, int index) {
        if (parts.length > index) {
            return StringEncoder.decodeString(parts[index]);
        }
        return "unknown";
    }

    /**
     * Validates that the photo line format has the minimum required fields.
     * This method checks that the photo line has at least 4 parts.
     *
     * @param parts The parts of the line split by delimiter
     * @param filePath The file path for error reporting
     * @throws FileFormatException If validation fails
     */
    private static void validatePhotoLineFormat(String[] parts, String filePath) throws FileFormatException {
        if (parts.length < 4) {
            throw new FileFormatException(filePath, String.join(Storage.DELIMITER, parts));
        }
    }

    /**
     * Extracts photo timestamp from parts.
     * This method attempts to parse the timestamp from the parts array, returning null
     * if the timestamp is not available or invalid.
     *
     * @param parts The parts of the line split by delimiter
     * @return The parsed LocalDateTime, or null if not available
     * @throws DateTimeParseException If the timestamp cannot be parsed
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