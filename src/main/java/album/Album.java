package album;

import com.drew.imaging.ImageProcessingException;
import exception.MetadataFilepathNotFound;
import exception.NoMetaDataException;
import exception.TravelDiaryException;
import exception.InvalidIndexException;
import photo.Photo;
import photo.PhotoFrame;
import photo.PhotoPrinter;
import tracker.Tracker;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an album that contains a collection of photos.
 * Each trip in the Travel Diary has one album to organize its photos.
 * Photos can be added, deleted, and viewed within the album.
 */
public class Album {

    /** List of photos in this album */
    public final List<Photo> photos = new ArrayList<>();
    
    /** Currently selected photo in the album, or null if none selected */
    public Photo selectedPhoto = null;
    
    /** Flag to control console output during operations */
    private boolean silentMode = false;

    /**
     * Enable or disable silent mode to prevent console output during operations.
     *
     * @param silentMode true to enable silent mode, false to disable
     */
    public void setSilentMode(boolean silentMode) {
        this.silentMode = silentMode;
    }

    /**
     * Returns current silent mode state.
     *
     * @return true if silent mode is enabled, false otherwise
     */
    public boolean isSilentMode() {
        return silentMode;
    }

    /**
     * Adds a new photo to the album with the specified details and datetime.
     *
     * @param filePath the file path of the photo
     * @param photoName the name of the photo
     * @param caption the caption for the photo
     * @param datetime the datetime when the photo was taken
     * @throws TravelDiaryException if required parameters are missing
     * @throws ImageProcessingException if there is an error processing the image
     * @throws IOException if there is an error reading the file
     * @throws NoMetaDataException if the photo has no metadata
     */
    public void addPhoto(String filePath, String photoName, String caption, LocalDateTime datetime)
            throws TravelDiaryException, ImageProcessingException, MetadataFilepathNotFound, NoMetaDataException {
        photos.add(new Photo(filePath, photoName, caption, datetime));
        if (!silentMode) {
            System.out.printf("\tPhoto [%s] has been added successfully.\n", photoName);
        }
    }

    /**
     * Adds a new photo to the album with the specified details.
     * The datetime will be extracted from the photo metadata or set to current time.
     *
     * @param filePath the file path of the photo
     * @param photoName the name of the photo
     * @param caption the caption for the photo
     * @throws TravelDiaryException if required parameters are missing
     * @throws ImageProcessingException if there is an error processing the image
     * @throws IOException if there is an error reading the file
     * @throws NoMetaDataException if the photo has no metadata
     */
    public void addPhoto(String filePath, String photoName, String caption)
            throws TravelDiaryException, ImageProcessingException, NoMetaDataException, MetadataFilepathNotFound {
        photos.add(new Photo(filePath, photoName, caption));
        if (!silentMode) {
            System.out.printf("\tPhoto [%s] has been added successfully.\n", photoName);
        }
    }

    /**
     * Deletes a photo from the album at the specified index.
     *
     * @param index the index of the photo to delete
     */
    public void deletePhoto(int index) {
        if (index < 0 || index >= photos.size()) {
            System.out.println("Invalid photo index.");
            return;
        }
        Photo photo = photos.get(index);
        photos.remove(index);
        System.out.printf("\tPhoto [%s] has been deleted successfully.\n", photo.getPhotoName());
    }

    /**
     * Displays all photos in the album.
     * If the album is empty, shows a message indicating no photos are found.
     */
    public void viewPhotos() {
        if (photos.isEmpty()) {
            System.out.println("No photos are found.");
        } else {
            System.out.println("\n\tHere are all your photos:");
            System.out.println(this);
        }
    }

    /**
     * Returns the list of photos in this album.
     *
     * @return the list of photos
     */
    public List<Photo> getPhotos() {
        return this.photos;
    }

    /**
     * Sets the currently selected photo.
     *
     * @param selectedPhoto the photo to select
     */
    public void setSelectedPhoto(Photo selectedPhoto) {
        this.selectedPhoto = selectedPhoto;
    }

    /**
     * Selects a photo from the album at the specified index and displays it.
     *
     * @param index the index of the photo to select
     * @throws InvalidIndexException if the index is out of bounds
     */
    public void selectPhoto(int index) throws InvalidIndexException {
        if (index < 0 || index >= photos.size()) {
            throw new InvalidIndexException();
        }
        selectedPhoto = photos.get(index);
        System.out.println("\t" + selectedPhoto);
        try {
            PhotoFrame photoFrame = PhotoPrinter.createFrame(selectedPhoto);
            PhotoPrinter.display(photoFrame);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns a string representation of the album, including all photos
     * and distances between consecutive photos.
     *
     * @return a formatted string with all photo details and distances
     */
    @Override
    public String toString() {
        Tracker.sortPhotosByDate(photos);
        String albumDetails = "\n";
        for (int i = 0; i < photos.size(); i++) {
            if (i > 0) {
                albumDetails += String.format("\t\t\t\t|\t%s km%n",
                        Tracker.getDist(photos.get(i-1), photos.get(i)));
            }
            albumDetails += String.format("\t%d) %s%n", i + 1, photos.get(i));
        }
        return albumDetails;
    }
}
