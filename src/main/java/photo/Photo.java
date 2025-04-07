package photo;

import com.drew.imaging.ImageProcessingException;
import exception.NoMetaDataException;
import exception.TravelDiaryException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Photo {
    private String filePath;
    private String photoName;
    private String caption;
    private String locationName;
    private LocalDateTime datetime;
    private Location location;

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
            throws TravelDiaryException, IOException, ImageProcessingException, NoMetaDataException {
        if (filePath == null || photoName == null || caption == null) {
            throw new TravelDiaryException("Missing required tag(s) for add_photo. Required: f# (filename), " +
                    "n# (photoname), c# (caption).");
        }
        this.filePath = filePath;
        this.photoName = photoName;
        this.caption = caption;

        extractData(filePath, datetime);

    }

    // Overloaded constructor without datetime parameter.
    public Photo(String filePath, String photoName, String caption) throws TravelDiaryException,
            ImageProcessingException, IOException, NoMetaDataException {
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

    public Location getLocation() {
        return this.location;
    }

    public LocalDateTime getDatetime() {
        return this.datetime;
    }


    /**
     * Returns true if the extracted location name is valid (not null, not empty, and not "Location not found").
     */
    public boolean hasLocationName() {
        return locationName != null && !locationName.isEmpty() && !locationName.equals("Location not found");
    }

    private void extractData(String filePath, LocalDateTime datetime) throws ImageProcessingException,
            IOException, NoMetaDataException {
        // Use PhotoMetadataExtractor to extract metadata from the image.
        PhotoMetadataExtractor extractor = new PhotoMetadataExtractor(filePath);
        Map<String, Object> metadata = extractor.getMetadataMap();

        // Set location (as a string), latitude, and longitude.
        this.locationName = (String) metadata.get("location");
        Object latObj = metadata.get("latitude");
        Object lonObj = metadata.get("longitude");
        double latitude = (latObj instanceof Number) ? ((Number) latObj).doubleValue() : 0.0;
        double longitude = (lonObj instanceof Number) ? ((Number) lonObj).doubleValue() : 0.0;
        LocalDateTime extractedDateTime;
        // Set the datetime: use provided datetime if non-null, otherwise fallback to metadata or current time.
        if (datetime != null) {
            extractedDateTime = datetime;
        } else if (metadata.get("datetime") != null) {
            extractedDateTime = (LocalDateTime) metadata.get("datetime");
        } else {
            extractedDateTime = LocalDateTime.now();
        }
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");
        this.datetime = extractedDateTime.minusHours(8); // Convert to GMT +8.

        // Store longitude, latitude and locationname as Location class.
        this.location = new Location(latitude, longitude, locationName);
    }

    @Override
    public String toString() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");
        return String.format("%s (%s) %s \n\t\t%s", photoName, locationName, datetime.format(outputFormatter), caption);
    }
}
