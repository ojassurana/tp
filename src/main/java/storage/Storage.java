package storage;

import trip.Trip;
import trip.TripManager;
import photo.Photo;
import exception.FileReadException;
import exception.FileFormatException;
import exception.FileWriteException;
import exception.PhotoLoadException;
import exception.PhotoSaveException;
import exception.TripLoadException;
import exception.TravelDiaryException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import album.Album;

public class Storage {
    private static final String TRIP_MARKER = "T";
    private static final String PHOTO_MARKER = "P";
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void saveTasks(List<Trip> trips, String filePath) throws FileWriteException {
        File dataFile = new File(filePath);
        dataFile.getParentFile().mkdirs(); // Ensure directory exists

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            for (Trip trip : trips) {
                writer.write(formatTripLine(trip));
                writer.newLine();

                if (trip.album != null) {
                    writer.write("A | " + encodeString(trip.name));
                    writer.newLine();
                    if (trip.album.photos != null) {
                        for (Photo photo : trip.album.photos) {
                            try {
                                writer.write(formatPhotoLine(photo));
                                writer.newLine();
                            } catch (Exception e) {
                                throw new PhotoSaveException(photo.getPhotoName(), filePath, e);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new FileWriteException(filePath, e);
        }
    }

    /**
     * Formats a trip into a line for storage
     */
    private static String formatTripLine(Trip trip) {
        return TRIP_MARKER + " | " +
                encodeString(trip.name) + " | " +
                encodeString(trip.description);
    }

    /**
     * Formats a photo into a line for storage
     */
    private static String formatPhotoLine(Photo photo) {
        String dateTimeString = "";
        if (photo.getDatetime() != null) {
            dateTimeString = photo.getDatetime().format(DATETIME_FORMAT);
        }

        return PHOTO_MARKER + " | " +
                encodeString(photo.getFilePath()) + " | " +
                encodeString(photo.getPhotoName()) + " | " +
                encodeString(photo.getCaption()) + " | " +
                dateTimeString;
    }

    public static List<Trip> loadTrips(TripManager tripManager, String filePath)
            throws FileReadException, FileFormatException {
        List<Trip> trips = new ArrayList<>();
        File dataFile = new File(filePath);

        if (!dataFile.exists()) {
            return trips;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            trips = processFileLines(reader, tripManager, filePath);
        } catch (IOException e) {
            throw new FileReadException(filePath, e);
        }

        return trips;
    }

    private static List<Trip> processFileLines(BufferedReader reader, TripManager tripManager, String filePath)
            throws IOException, FileFormatException {
        List<Trip> trips = new ArrayList<>();
        String line;
        Trip currentTrip = null;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null) {
            lineNumber++;
            try {
                String[] parts = line.split(" \\| ");

                if (parts.length == 0) {
                    throw new FileFormatException(filePath, line);
                }
                if (isNewTripMarker(parts)) {
                    currentTrip = handleTripCreation(parts, tripManager, currentTrip, trips, filePath, lineNumber);
                } else if (isAlbumMarker(parts, currentTrip)) {
                    handleAlbumMarker(parts, currentTrip, filePath, lineNumber);
                } else if (isPhotoMarker(parts, currentTrip)) {
                    handlePhotoAddition(parts, currentTrip, filePath, lineNumber);
                } else {
                    throw new FileFormatException(filePath, line);
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

    private static boolean isNewTripMarker(String[] parts) {
        return parts.length > 0 && parts[0].equals(TRIP_MARKER);
    }

    private static boolean isAlbumMarker(String[] parts, Trip currentTrip) {
        return parts.length > 0 && parts[0].equals("A") && currentTrip != null;
    }

    private static boolean isPhotoMarker(String[] parts, Trip currentTrip) {
        return parts.length > 0 && parts[0].equals(PHOTO_MARKER) && currentTrip != null && currentTrip.album != null;
    }

    private static Trip handleTripCreation(String[] parts,
                                           TripManager tripManager,
                                           Trip previousTrip,
                                           List<Trip> trips,
                                           String filePath,
                                           int lineNumber) throws TripLoadException, FileFormatException {
        // Add previous trip to the list if it exists

        if (previousTrip != null) {
            trips.add(previousTrip);
        }

        try {
            if (parts.length < 3) {
                throw new FileFormatException(filePath, String.join(" | ", parts));
            }
            String name = decodeString(parts[1]);
            String description = decodeString(parts[2]);

            // Use the existing addTrip method
            tripManager.addTrip(name, description);
            // Get the last added trip
            Trip currentTrip = tripManager.getTrips().get(tripManager.getTrips().size() - 1);
            // Ensure the trip has an album
            if (currentTrip.album == null) {
                currentTrip.album = new Album();
            }

            return currentTrip;
        } catch (TravelDiaryException e) {
            throw new TripLoadException(parts.length > 1 ? decodeString(parts[1]) : "unknown", e);
        }
    }

    private static void handleAlbumMarker(String[] parts, Trip currentTrip, String filePath, int lineNumber)
            throws FileFormatException {
        if (parts.length <= 1) {
            throw new FileFormatException(filePath, String.join(" | ", parts));
        }

        String albumName = decodeString(parts[1]);
    }

    private static void handlePhotoAddition(String[] parts, Trip currentTrip, String filePath, int lineNumber)
            throws PhotoLoadException, FileFormatException {
        try {
            if (parts.length < 4) {
                throw new FileFormatException(filePath, String.join(" | ", parts));
            }

            String photoPath = decodeString(parts[1]);
            String photoName = decodeString(parts[2]);
            String caption = decodeString(parts[3]);

            LocalDateTime photoTime = null;
            try {
                photoTime = extractPhotoTime(parts);
            } catch (DateTimeParseException e) {
                // Create a new exception with just the file path and line
                throw new FileFormatException(filePath, lineNumber, e);
            }

            if (photoTime != null) {
                currentTrip.album.addPhoto(
                        photoPath,
                        photoName,
                        caption,
                        "",
                        photoTime
                );
            } else {
                currentTrip.album.addPhoto(
                        photoPath,
                        photoName,
                        caption,
                        ""
                );
            }
        } catch (TravelDiaryException e) {
            throw new PhotoLoadException(
                    parts.length > 2 ? decodeString(parts[2]) : "unknown",
                    parts.length > 1 ? decodeString(parts[1]) : "unknown",
                    e
            );
        }
    }

    private static LocalDateTime extractPhotoTime(String[] parts) throws DateTimeParseException {
        return (parts.length > 4 && !parts[4].isEmpty())
                ? LocalDateTime.parse(parts[4], DATETIME_FORMAT)
                : null;
    }

    /**
     * Encodes a string to handle special characters.
     *
     * @param input String to encode
     * @return Encoded string
     */
    private static String encodeString(String input) {
        if (input == null) {
            return "";
        }

        String result = input;
        result = result.replace("|", "\\pipe");
        result = result.replace("\n", "\\newline");
        return result;
    }

    /**
     * Decodes a string to restore original characters.
     *
     * @param input String to decode
     * @return Decoded string
     */
    private static String decodeString(String input) {
        if (input == null) {
            return "";
        }

        String result = input;
        result = result.replace("\\pipe", "|");
        result = result.replace("\\newline", "\n");
        return result;
    }
}
