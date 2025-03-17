package album;

import exception.TravelDiaryException;
import photo.Photo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Album {
    private final List<Photo> photos = new ArrayList<>();
    private Photo selectedPhoto = null;

    public void addPhoto(String filePath, String photoName, String caption, String location, LocalDateTime datetime) throws TravelDiaryException {
        photos.add(new Photo(filePath, photoName, caption, location, datetime));
    }

    public void addPhoto(String filePath, String photoName, String caption, String location) throws TravelDiaryException {
        photos.add(new Photo(filePath, photoName, caption, location));
    }

    public void deletePhoto(int index) {
        if (index < 0 || index >= photos.size()) {
            System.out.println("Invalid photo index.");
            return;
        }
        photos.remove(index);
        System.out.println("Photo deleted successfully.");
    }

    public void viewPhotos() {
        if (photos.isEmpty()) {
            System.out.println("No photos found.");
        } else {
            for (int i = 0; i < photos.size(); i++) {
                System.out.println(i + ": " + photos.get(i)); // Display index with photos detail
            }
        }
        System.out.println(photos.size());
    }

    public void selectPhoto(int index) {
        if (index < 0 || index >= photos.size()) {
            System.out.println("Invalid photo index.");
        }
        selectedPhoto = photos.get(index);
        System.out.println("Name: " + selectedPhoto.getPhotoName() + " caption: " + selectedPhoto.getCaption() +
                " location: " + selectedPhoto.getLocation());
    }
}
