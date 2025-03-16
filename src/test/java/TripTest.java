import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import trip.Trip;

public class TripTest {

    @Test
    void testTripCreation() {
        Trip trip = new Trip("Japan Trip", "Skiing in Hokkaido", "Japan");
        assertEquals("Japan Trip", trip.name);
        assertEquals("Skiing in Hokkaido", trip.description);
        assertEquals("Japan", trip.location);
    }

    @Test
    void testToStringFormat() {
        Trip trip = new Trip("Japan Trip", "Skiing in Hokkaido", "Japan");
        String expected = "Japan Trip - Skiing in Hokkaido (Japan)";
        assertEquals(expected, trip.toString());
    }
}
