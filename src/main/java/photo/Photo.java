package photo;

import com.drew.imaging.ImageProcessingException;
import exception.MetadataFilepathNotFound;
import exception.NoMetaDataException;
import exception.TravelDiaryException;
import exception.UnsupportedImageFormatException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Represents a Photo with metadata including file path, name, caption,
 * location, and datetime. This class extracts and stores metadata from
 * the photo file using the PhotoMetadataExtractor.
 */
public class Photo {
    private static final Logger logger = Logger.getLogger(Photo.class.getName());

    private String filePath;
    private String photoName;
    private String caption;
    private String locationName;
    private LocalDateTime datetime;
    private Location location;

    /**
     * Constructs a Photo object with all specified parameters.
     * If datetime is null, it attempts to extract the datetime from
     * the photo metadata or defaults to the current time.
     *
     * @param filePath The file path of the photo.
     * @param photoName The name of the photo.
     * @param caption The caption for the photo.
     * @param datetime The optional datetime; null to use metadata or current time.
     * @throws TravelDiaryException If required parameters are missing.
     * @throws IOException If an error occurs while accessing the file.
     * @throws ImageProcessingException If an error occurs while processing the image.
     * @throws NoMetaDataException If the photo has no metadata.
     */
    public Photo(String filePath, String photoName, String caption, LocalDateTime datetime)
            throws TravelDiaryException, ImageProcessingException, NoMetaDataException, MetadataFilepathNotFound {
        logger.info("Initializing Photo object...");

        assert filePath != null && !filePath.isEmpty() : "File path cannot be null or empty.";
        assert photoName != null && !photoName.isEmpty() : "Photo name cannot be null or empty.";
        assert caption != null && !caption.isEmpty() : "Caption cannot be null or empty.";

        if (filePath == null || photoName == null || caption == null) {
            logger.severe("Required fields are missing: filePath, photoName, or caption.");
            throw new TravelDiaryException("Missing required tag(s) for add_photo. Required: f# (filename), " +
                    "n# (photoname), c# (caption).");
        }

        // Check if the file has a .jpg extension
        if (!filePath.toLowerCase().endsWith(".jpg")) {
            logger.warning("Unsupported file format: " + filePath);
            throw new UnsupportedImageFormatException(filePath);
        }

        this.filePath = filePath;
        this.photoName = photoName;
        this.caption = caption;

        logger.info(String.format("Photo metadata extraction started for file: %s", filePath));
        extractData(filePath, datetime);
    }

    /**
     * Constructs a Photo object without specifying datetime.
     * Defaults datetime to metadata or current time.
     *
     * @param filePath The file path of the photo.
     * @param photoName The name of the photo.
     * @param caption The caption for the photo.
     * @throws TravelDiaryException If required parameters are missing.
     * @throws IOException If an error occurs while accessing the file.
     * @throws ImageProcessingException If an error occurs while processing the image.
     * @throws NoMetaDataException If the photo has no metadata.
     */
    public Photo(String filePath, String photoName, String caption) throws TravelDiaryException,
            ImageProcessingException, NoMetaDataException, MetadataFilepathNotFound {
        this(filePath, photoName, caption, null);
    }

    /**
     * Returns the file path of the photo.
     * @return The file path of the photo.
     */
    public String getFilePath() {
        logger.fine("Retrieving file path: " + filePath);
        return this.filePath;
    }

    public String getLocationName() {
        return locationName;
    }

    /**
     * Returns the name of the photo.
     * @return The photo name.
     */
    public String getPhotoName() {
        logger.fine("Retrieving photo name: " + photoName);
        return this.photoName;
    }

    /**
     * Returns the caption of the photo.
     * @return The photo caption.
     */
    public String getCaption() {
        logger.fine("Retrieving caption: " + caption);
        return this.caption;
    }

    /**
     * Returns the location associated with the photo.
     * @return The photo's location as a Location object.
     */
    public Location getLocation() {
        assert location != null : "Location object cannot be null.";
        logger.fine("Retrieving location: " + location);
        return this.location;
    }

    /**
     * Returns the datetime associated with the photo.
     * @return The datetime of the photo as a LocalDateTime object.
     */
    public LocalDateTime getDatetime() {
        assert datetime != null : "Datetime object cannot be null.";
        logger.fine("Retrieving datetime: " + datetime);
        return this.datetime;
    }

    /**
     * Checks whether the photo has a valid location name.
     * A valid location name is not null, not empty, and not "Location not found".
     *
     * @return True if the location name is valid; false otherwise.
     */
    public boolean hasLocationName() {
        logger.fine("Checking if location name is valid...");
        return locationName != null && !locationName.isEmpty() && !locationName.equals("Location not found");
    }

    /**
     * Extracts metadata from the photo file including location and datetime.
     * If the datetime is provided, it uses that instead of metadata.
     *
     * @param filePath The file path of the photo.
     * @param datetime The optional datetime; null to use metadata or current time.
     * @throws ImageProcessingException If an error occurs while processing the image.
     * @throws IOException If an error occurs while accessing the file.
     * @throws NoMetaDataException If the photo has no metadata.
     */
    private void extractData(String filePath, LocalDateTime datetime) throws ImageProcessingException,
            NoMetaDataException, MetadataFilepathNotFound {
        logger.info("Starting metadata extraction...");

        // Use PhotoMetadataExtractor to extract metadata from the image.
        PhotoMetadataExtractor extractor = new PhotoMetadataExtractor(filePath);
        Map<String, Object> metadata = extractor.getMetadataMap();

        // Log and set location (as a string), latitude, and longitude.
        this.locationName = (String) metadata.get("location");
        logger.fine("Extracted location name: " + locationName);
        Object latObj = metadata.get("latitude");
        Object lonObj = metadata.get("longitude");
        double latitude = (latObj instanceof Number) ? ((Number) latObj).doubleValue() : 0.0;
        double longitude = (lonObj instanceof Number) ? ((Number) lonObj).doubleValue() : 0.0;

        LocalDateTime extractedDateTime;

        // Log and set datetime
        if (datetime != null) {
            extractedDateTime = datetime;
        } else if (metadata.get("datetime") != null) {
            extractedDateTime = (LocalDateTime) metadata.get("datetime");
        } else {
            extractedDateTime = LocalDateTime.now();
        }
        logger.fine("Extracted datetime: " + extractedDateTime);

        this.datetime = extractedDateTime.minusHours(8); // Convert to GMT +8.

        // Store longitude, latitude, and locationName as a Location class.
        this.location = new Location(latitude, longitude, locationName);
        logger.info("Metadata extraction completed successfully.");
    }

    /**
     * Returns a string representation of the Photo object, including the photo name,
     * location, datetime, and caption.
     *
     * @return A formatted string with the photo's details.
     */
    @Override
    public String toString() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");
        String result = String.format("%s (%s) %s \n\t\t%s", photoName,
                locationName, datetime.format(outputFormatter), caption);
        logger.fine("Photo toString: " + result);
        return result;
    }
}
