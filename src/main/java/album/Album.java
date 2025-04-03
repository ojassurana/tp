package album;

import com.drew.imaging.ImageProcessingException;
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

public class Album {

    public final List<Photo> photos = new ArrayList<>();
    public Photo selectedPhoto = null;
    private boolean silentMode = false;

    /**
     * Enable or disable silent mode to prevent console output during operations
     */
    public void setSilentMode(boolean silentMode) {
        this.silentMode = silentMode;
    }

    /**
     * Returns current silent mode state
     */
    public boolean isSilentMode() {
        return silentMode;
    }

    public void addPhoto(String filePath, String photoName, String caption, LocalDateTime datetime)
            throws TravelDiaryException, ImageProcessingException, IOException {
        photos.add(new Photo(filePath, photoName, caption, datetime));
        if (!silentMode) {
            System.out.printf("\tPhoto [%s] has been added successfully.\n", photoName);
        }
    }

    public void addPhoto(String filePath, String photoName, String caption)
            throws TravelDiaryException, ImageProcessingException, IOException {
        photos.add(new Photo(filePath, photoName, caption));
        if (!silentMode) {
            System.out.printf("\tPhoto [%s] has been added successfully.\n", photoName);
        }
    }

    public void deletePhoto(int index) {
        if (index < 0 || index >= photos.size()) {
            System.out.println("Invalid photo index.");
            return;
        }
        Photo photo = photos.get(index);
        photos.remove(index);
        System.out.printf("\tPhoto [%s] has been deleted successfully.\n", photo.getPhotoName());
    }

    public void viewPhotos() {
        if (photos.isEmpty()) {
            System.out.println("No photos are found.");
        } else {
            System.out.println("\n\tHere are all your photos:");
            System.out.println(this);
        }
    }

    public List<Photo> getPhotos() {
        return this.photos;
    }

    public void setSelectedPhoto(Photo selectedPhoto) {
        this.selectedPhoto = selectedPhoto;
    }

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

    @Override
    public String toString() {
        Tracker.sortPhotosByDate(photos);
        String albumDetails = "\n";
        for (int i = 0; i < photos.size(); i++) {
            if (i > 0) {
                albumDetails += String.format("\t\t\t\t|\t%s km%n",
                        Tracker.calculateDist(photos.get(i-1), photos.get(i)));
            }
            albumDetails += String.format("\t%d) %s%n", i + 1, photos.get(i));
        }
        return albumDetails;
    }
}
