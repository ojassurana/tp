package trip;

import exception.TravelDiaryException;
import org.junit.jupiter.api.Test;

public class TripTest {

    @Test
    void testTripCreation() {
        Trip trip = org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                new Trip("Japan Trip", "Skiing in Hokkaido"));
        org.junit.jupiter.api.Assertions.assertEquals("Japan Trip", trip.name);
        org.junit.jupiter.api.Assertions.assertEquals("Skiing in Hokkaido", trip.description);
    }

    @Test
    void testToStringFormatNoPhotos() {
        Trip trip = org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                new Trip("Japan Trip", "Skiing in Hokkaido"));
        String expected = "Japan Trip\n" + "\t\tSkiing in Hokkaido (No Photos in trip)\n";
        org.junit.jupiter.api.Assertions.assertEquals(expected, trip.toString());
    }

    @Test
    void testGetAlbumNotNull() {
        Trip trip = org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                new Trip("Italy Trip", "Visiting Venice"));
        org.junit.jupiter.api.Assertions.assertNotNull(trip.getAlbum());
    }

    @Test
    void testConstructorThrowsExceptionForNullName() {
        Exception e = org.junit.jupiter.api.Assertions.assertThrows(TravelDiaryException.class, () ->
                new Trip(null, "Description"));
        org.junit.jupiter.api.Assertions.assertEquals(
                "Missing required tag(s) for add_trip. Required: n# (name), d# (description). ",
                e.getMessage());
    }

    @Test
    void testConstructorThrowsExceptionForNullDescription() {
        Exception e = org.junit.jupiter.api.Assertions.assertThrows(TravelDiaryException.class, () ->
                new Trip("Trip", null));
        org.junit.jupiter.api.Assertions.assertEquals(
                "Missing required tag(s) for add_trip. Required: n# (name), d# (description). ",
                e.getMessage());
    }
}
