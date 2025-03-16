import trip.TripManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripManagerTest {
    private TripManager tripManager;

    @BeforeEach
    void setUp() {
        tripManager = new TripManager();
    }

    @Test
    void testAddTrip() {
        tripManager.addTrip("Japan Trip", "Skiing in Hokkaido", "Japan");
        tripManager.viewTrips(); // Check console output
    }

    @Test
    void testDeleteTrip() {
        tripManager.addTrip("Japan Trip", "Skiing in Hokkaido", "Japan");
        tripManager.deleteTrip(1);
        tripManager.viewTrips(); // Should show no trips
    }

    @Test
    void testSelectTrip() {
        tripManager.addTrip("Japan Trip", "Skiing in Hokkaido", "Japan");
        tripManager.selectTrip(1);
    }
}
