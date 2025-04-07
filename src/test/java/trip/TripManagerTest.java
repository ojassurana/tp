package trip;

import exception.IndexOutOfRangeException;
import exception.TravelDiaryException;
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
        // Test that adding a trip doesn't throw an exception
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                tripManager.addTrip("Japan Trip", "Skiing in Hokkaido"));
        org.junit.jupiter.api.Assertions.assertEquals(1, tripManager.getTrips().size());
        org.junit.jupiter.api.Assertions.assertEquals("Japan Trip", tripManager.getTrips().get(0).name);
    }

    @Test
    void testDeleteTrip() throws TravelDiaryException {
        tripManager.addTrip("Japan Trip", "Skiing in Hokkaido");
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> tripManager.deleteTrip(0));
        org.junit.jupiter.api.Assertions.assertEquals(0, tripManager.getTrips().size());
    }

    @Test
    void testDeleteTripInvalidIndex() {
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(IndexOutOfRangeException.class,
                () -> tripManager.deleteTrip(0));
        org.junit.jupiter.api.Assertions.assertNotNull(exception);
    }

    @Test
    void testSelectTrip() throws TravelDiaryException {
        tripManager.addTrip("Japan Trip", "Skiing in Hokkaido");
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> tripManager.selectTrip(0));
        org.junit.jupiter.api.Assertions.assertEquals("Japan Trip", tripManager.getSelectedTrip().name);
    }

    @Test
    void testSelectTripInvalidIndex() {
        org.junit.jupiter.api.Assertions.assertThrows(IndexOutOfRangeException.class,
                () -> tripManager.selectTrip(0));
    }

    @Test
    void testAddTripSilently() throws TravelDiaryException {
        Trip trip = tripManager.addTripSilently("Korea Trip", "Cherry Blossoms in Seoul");
        org.junit.jupiter.api.Assertions.assertNotNull(trip);
        org.junit.jupiter.api.Assertions.assertEquals("Korea Trip", trip.name);
        org.junit.jupiter.api.Assertions.assertEquals(1, tripManager.getTrips().size());
    }

    @Test
    void testSilentModePreventsOutput() throws TravelDiaryException {
        tripManager.setSilentMode(true);
        tripManager.addTrip("Bali Trip", "Relaxing by the beach");
        // We assume no output is shown in silent mode, but we can still check the list size
        org.junit.jupiter.api.Assertions.assertEquals(1, tripManager.getTrips().size());
    }

    @Test
    void testToStringNotEmptyAfterAdd() throws TravelDiaryException {
        tripManager.addTrip("Italy Trip", "Exploring Rome");
        String output = tripManager.toString();
        org.junit.jupiter.api.Assertions.assertTrue(output.contains("Italy Trip"));
    }

    @Test
    void testNotifyTripsLoadedSilentMode() {
        tripManager.setSilentMode(true);
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                tripManager.notifyTripsLoaded()); // should silently skip output
    }

    @Test
    void testNotifyTripsLoadedNormalMode() {
        tripManager.setSilentMode(false);
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                tripManager.notifyTripsLoaded()); // will print to console
    }
}
