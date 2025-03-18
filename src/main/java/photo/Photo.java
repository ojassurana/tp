package photo;

import exception.MissingCompulsoryParameter;

import java.time.LocalDateTime;


/**
 * Photo class stores details of a photo.
 */
public class Photo {
    private String filePath;
    private String photoName;
    private String caption;
    private String location;
    private LocalDateTime datetime; // Using LocalDateTime for datetime

    public Photo(String filePath, String photoName, String caption, String location, LocalDateTime datetime)
            throws MissingCompulsoryParameter {
        if (filePath == null || photoName == null) {
            String[] parameters = {(filePath == null) ? "filepath" : null,(photoName == null) ? "photoname" : null};
            throw new MissingCompulsoryParameter(parameters);
        }
        this.filePath = filePath;
        this.photoName = photoName;
        this.caption = (caption == null) ? "no caption" : caption;
        this.location = (location == null) ? "no location" : location;
        this.datetime = datetime;
    }

    public Photo(String filePath, String photoName, String caption, String location) throws MissingCompulsoryParameter {
        this(filePath, photoName, caption, location, LocalDateTime.now());
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

    @Override
    public String toString() {
        return photoName;
    }
}
