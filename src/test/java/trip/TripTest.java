package trip;

import exception.MissingCompulsoryParameter;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Trip} class.
 */
public class TripTest {

    /**
     * Tests that a {@link Trip} object is successfully created with valid name and description,
     * and verifies that its fields are correctly set.
     */
    @Test
    void testTripCreation() {
        Trip trip = org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                new Trip("Japan Trip", "Skiing in Hokkaido"));
        org.junit.jupiter.api.Assertions.assertEquals("Japan Trip", trip.name);
        org.junit.jupiter.api.Assertions.assertEquals("Skiing in Hokkaido", trip.description);
    }

    /**
     * Tests the {@code toString()} method of {@link Trip} when no photos are added to the album.
     * It should return a formatted string indicating no photos in the trip.
     */
    @Test
    void testToStringFormatNoPhotos() {
        Trip trip = org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                new Trip("Japan Trip", "Skiing in Hokkaido"));
        String expected = "Japan Trip\n" + "\t\tSkiing in Hokkaido (No Photos in trip)\n";
        org.junit.jupiter.api.Assertions.assertEquals(expected, trip.toString());
    }

    /**
     * Tests that the {@code getAlbum()} method returns a non-null album
     * even if no photos have been added yet.
     */
    @Test
    void testGetAlbumNotNull() {
        Trip trip = org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                new Trip("Italy Trip", "Visiting Venice"));
        org.junit.jupiter.api.Assertions.assertNotNull(trip.getAlbum());
    }

    /**
     * Tests that the {@link Trip} constructor throws a {@link MissingCompulsoryParameter} exception
     * when the trip name is {@code null}.
     */
    @Test
    void testConstructorThrowsExceptionForNullName() {
        Exception e = org.junit.jupiter.api.Assertions.assertThrows(MissingCompulsoryParameter.class, () ->
                new Trip(null, "Description"));
        org.junit.jupiter.api.Assertions.assertEquals(
                "missing compulsory parameters for trip: name, description",
                e.getMessage());
    }

    /**
     * Tests that the {@link Trip} constructor throws a {@link MissingCompulsoryParameter} exception
     * when the trip description is {@code null}.
     */
    @Test
    void testConstructorThrowsExceptionForNullDescription() {
        Exception e = org.junit.jupiter.api.Assertions.assertThrows(MissingCompulsoryParameter.class, () ->
                new Trip("Trip", null));
        org.junit.jupiter.api.Assertions.assertEquals(
                "missing compulsory parameters for trip: name, description",
                e.getMessage());
    }
}
