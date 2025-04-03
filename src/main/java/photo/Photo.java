package photo;

import com.drew.imaging.ImageProcessingException;
import exception.TravelDiaryException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class Photo {
    private String filePath;
    private String photoName;
    private String caption;
    private String location;
    private LocalDateTime datetime;
    private double latitude;
    private double longitude;

    /**
     * Constructs a Photo object.
     * Inputs:
     *  - filePath: the photo file path
     *  - photoName: name of the photo
     *  - caption: caption for the photo
     *  - datetime: optional; if null, metadata or current time is used.
     *
     * The location and coordinates are extracted from the photo file via PhotoMetadataExtractor.
     */
    public Photo(String filePath, String photoName, String caption, LocalDateTime datetime)
            throws TravelDiaryException, IOException, ImageProcessingException {
        if (filePath == null || photoName == null || caption == null) {
            throw new TravelDiaryException("Missing required tag(s) for add_photo. Required: f# (filename), " +
                    "n# (photoname), c# (caption).");
        }
        this.filePath = filePath;
        this.photoName = photoName;
        this.caption = caption;

        // Use PhotoMetadataExtractor to extract metadata from the image.
        PhotoMetadataExtractor extractor = new PhotoMetadataExtractor(filePath);
        Map<String, Object> metadata = extractor.getMetadataMap();

        // Set location (as a string), latitude, and longitude.
        this.location = (String) metadata.get("location");
        Object latObj = metadata.get("latitude");
        Object lonObj = metadata.get("longitude");
        this.latitude = (latObj instanceof Number) ? ((Number) latObj).doubleValue() : 0.0;
        this.longitude = (lonObj instanceof Number) ? ((Number) lonObj).doubleValue() : 0.0;

        // Set the datetime: use provided datetime if non-null, otherwise fallback to metadata or current time.
        if (datetime != null) {
            this.datetime = datetime;
        } else if (metadata.get("datetime") != null) {
            this.datetime = (LocalDateTime) metadata.get("datetime");
        } else {
            this.datetime = LocalDateTime.now();
        }
    }

    // Overloaded constructor without datetime parameter.
    public Photo(String filePath, String photoName, String caption) throws TravelDiaryException,
            ImageProcessingException, IOException {
        this(filePath, photoName, caption, null);
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getPhotoName() {
        return this.photoName;
    }

    public String getCaption() {
        return this.caption;
    }

    public String getLocation() {
        return this.location;
    }

    public LocalDateTime getDatetime() {
        return this.datetime;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Returns true if either latitude or longitude is non-zero.
     */
    public boolean hasCoordinates() {
        return latitude != 0 || longitude != 0;
    }

    /**
     * Returns true if the extracted location name is valid (not null, not empty, and not "Location not found").
     */
    public boolean hasLocationName() {
        return location != null && !location.isEmpty() && !location.equals("Location not found");
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %s \n\t\t%s", photoName, location, datetime, caption);
    }
}
