package album;

import exception.TravelDiaryException;
import exception.InvalidIndexException;
import photo.Photo;
import photo.PhotoFrame;
import photo.PhotoPrinter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Album {
    public final List<Photo> photos = new ArrayList<>();
    public Photo selectedPhoto = null;


    public void addPhoto(String filePath, String photoName, String caption, String location, LocalDateTime datetime)
            throws TravelDiaryException {
        photos.add(new Photo(filePath, photoName, caption, location, datetime));

    }

    public void addPhoto(String filePath, String photoName, String caption, String location)
            throws TravelDiaryException {
        photos.add(new Photo(filePath, photoName, caption, location));
        System.out.printf("\tPhoto [%s] has been added successfully.\n", photoName);
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
            System.out.println("No photos found.");
        } else {
            for (int i = 0; i < photos.size(); i++) {
                System.out.println(i + ": " + photos.get(i)); // Display index with photos detail
            }
        }
    }

    public void setSelectedPhoto(Photo selectedPhoto) {
        this.selectedPhoto = selectedPhoto;
    }

    public void selectPhoto(int index) throws InvalidIndexException {
        if (index < 0 || index >= photos.size()) {
            throw new InvalidIndexException();
        }
        selectedPhoto = photos.get(index);
        System.out.println("Name: " + selectedPhoto.getPhotoName() + " caption: " + selectedPhoto.getCaption() +
                " location: " + selectedPhoto.getLocation());
        try {
            PhotoFrame photoFrame = PhotoPrinter.createFrame(selectedPhoto);
            PhotoPrinter.display(photoFrame);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder albumDetails = new StringBuilder();
        for (int i = 0; i < photos.size(); i++) {
            albumDetails.append("\t").append(i + 1).append(") ")
                    .append(photos.get(i).toString()).append("\n"); // Append details
        }
        return albumDetails.toString(); // Convert StringBuilder to String
    }
}