import org.junit.jupiter.api.Test;
import photo.Photo;
import album.Album;
import tracker.Tracker;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TrackerTest {

    /**
     * Tests sorting photos by date.
     * Verifies that the photos are correctly sorted based on their datetime.
     */
    @Test
    void sortPhotosByDate_validPhotoList_expectSortCorrectly() {
        try {
            Photo photo1 = new Photo("./data/photos/hongkong_1.jpeg", "photo 1 name", "photo 1 caption");
            Photo photo2 = new Photo("./data/photos/hongkong_2.jpeg", "photo 2 name", "photo 2 caption");
            Photo photo3 = new Photo("./data/photos/hongkong_3.jpeg", "photo 3 name", "photo 3 caption");
            List<Photo> photoList = new ArrayList<>();
            photoList.add(photo1);
            photoList.add(photo2);
            photoList.add(photo3);

            Tracker.sortPhotosByDate(photoList);

            // Verify the order of sorted photos
            assertEquals(photo2, photoList.get(0)); // Earliest photo
            assertEquals(photo1, photoList.get(1)); // Second earliest photo
            assertEquals(photo3, photoList.get(2)); // Latest photo
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /**
     * Tests calculating the distance between two photos based on their locations.
     * Verifies the accuracy of the calculated distance using expected values.
     */
    @Test
    void getDist_twoValidPhotos_expectCalculateCorrectDistance() {
        try {
            Photo photo1 = new Photo("./data/photos/hongkong_1.jpeg", "photo 1 name", "photo 1 caption");
            Photo photo2 = new Photo("./data/photos/hongkong_2.jpeg", "photo 2 name", "photo 2 caption");
            // Calculate the distance
            double distance = Tracker.getDist(photo1, photo2);
            System.out.println(distance);
            assertEquals(14.0, distance);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Tests retrieving the period of an album by finding the earliest and latest dates of its photos.
     * Verifies that the period is correctly calculated.
     */
    @Test
    void getPeriod_validAlbum_expectCorrectPeriod() {
        try {
            Album album = new Album();
            album.addPhoto("./data/photos/hongkong_1.jpeg", "photo 1 name", "photo 1 caption");
            album.addPhoto("./data/photos/hongkong_2.jpeg", "photo 2 name", "photo 2 caption");
            album.addPhoto("./data/photos/hongkong_2.jpeg", "photo 3 name", "photo 3 caption");

            // Get the period
            List<String> minMaxDates = Tracker.getPeriod(album);

            // Verify the start and end dates
            assertEquals("2024-11-16 12:32PM", minMaxDates.get(0)); // Earliest date
            assertEquals("2024-11-17 1:16PM", minMaxDates.get(1)); // Latest date
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
