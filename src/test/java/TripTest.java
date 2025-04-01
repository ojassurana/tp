import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import trip.Trip;

public class TripTest {

    @Test
    void testTripCreation() {

        Trip trip = assertDoesNotThrow(() -> new Trip("Japan Trip", "Skiing in Hokkaido"));
        assertEquals("Japan Trip", trip.name);
        assertEquals("Skiing in Hokkaido", trip.description);
    }

    @Test
    void testToStringFormat() {
        Trip trip = assertDoesNotThrow(() -> new Trip("Japan Trip", "Skiing in Hokkaido"));
        String expected = "Japan Trip\n" +
                "\t\tSkiing in Hokkaido\n";
        assertEquals(expected, trip.toString());
    }
}
