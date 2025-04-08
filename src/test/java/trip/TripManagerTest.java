package trip;

import exception.DuplicateNameException;
import exception.IndexOutOfRangeException;
import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the TripManager class.
 * Contains unit tests to verify the functionality of trip management operations
 * such as adding, deleting, and selecting trips, as well as testing the silent mode functionality.
 */
class TripManagerTest {
    private TripManager tripManager;

    /**
     * Sets up the test environment before each test method execution.
     * Initializes a new TripManager instance.
     */
    @BeforeEach
    void setUp() {
        tripManager = new TripManager();
    }

    /**
     * Tests the addTrip method to ensure it correctly adds a trip to the trip manager.
     * Verifies that no exceptions are thrown, the trip list size increases,
     * and the trip name is correctly set.
     */
    @Test
    void testAddTrip() {
        // Test that adding a trip doesn't throw an exception
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                tripManager.addTrip("Japan Trip", "Skiing in Hokkaido"));
        org.junit.jupiter.api.Assertions.assertEquals(1, tripManager.getTrips().size());
        org.junit.jupiter.api.Assertions.assertEquals("Japan Trip", tripManager.getTrips().get(0).name);
    }

    /**
     * Tests the deleteTrip method to ensure it correctly removes a trip from the trip manager.
     * Verifies that no exceptions are thrown when deleting a valid trip index
     * and the trip list size decreases as expected.
     *
     * @throws TravelDiaryException if there is a general error in the travel diary operations
     * @throws DuplicateNameException if a trip with the same name already exists
     * @throws MissingCompulsoryParameter if a required parameter is missing
     */
    @Test
    void testDeleteTrip() throws TravelDiaryException, DuplicateNameException, MissingCompulsoryParameter {
        tripManager.addTrip("Japan Trip", "Skiing in Hokkaido");
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> tripManager.deleteTrip(0));
        org.junit.jupiter.api.Assertions.assertEquals(0, tripManager.getTrips().size());
    }

    /**
     * Tests that the deleteTrip method throws an IndexOutOfRangeException when
     * attempting to delete a trip with an invalid index.
     */
    @Test
    void testDeleteTripInvalidIndex() {
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(IndexOutOfRangeException.class,
                () -> tripManager.deleteTrip(0));
        org.junit.jupiter.api.Assertions.assertNotNull(exception);
    }

    /**
     * Tests the selectTrip method to ensure it correctly selects a trip from the trip manager.
     * Verifies that no exceptions are thrown when selecting a valid trip index
     * and the selected trip has the expected name.
     *
     * @throws TravelDiaryException if there is a general error in the travel diary operations
     * @throws DuplicateNameException if a trip with the same name already exists
     * @throws MissingCompulsoryParameter if a required parameter is missing
     */
    @Test
    void testSelectTrip() throws TravelDiaryException, DuplicateNameException, MissingCompulsoryParameter {
        tripManager.addTrip("Japan Trip", "Skiing in Hokkaido");
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> tripManager.selectTrip(0));
        org.junit.jupiter.api.Assertions.assertEquals("Japan Trip", tripManager.getSelectedTrip().name);
    }

    /**
     * Tests that the selectTrip method throws an IndexOutOfRangeException when
     * attempting to select a trip with an invalid index.
     */
    @Test
    void testSelectTripInvalidIndex() {
        org.junit.jupiter.api.Assertions.assertThrows(IndexOutOfRangeException.class,
                () -> tripManager.selectTrip(0));
    }

    /**
     * Tests the addTripSilently method to ensure it correctly adds a trip to the trip manager
     * without producing output. Verifies that the returned trip is not null,
     * has the correct name, and the trip list size increases.
     *
     * @throws TravelDiaryException if there is a general error in the travel diary operations
     * @throws MissingCompulsoryParameter if a required parameter is missing
     * @throws DuplicateNameException if a trip with the same name already exists
     */
    @Test
    void testAddTripSilently() throws TravelDiaryException, MissingCompulsoryParameter, DuplicateNameException {
        Trip trip = tripManager.addTripSilently("Korea Trip", "Cherry Blossoms in Seoul");
        org.junit.jupiter.api.Assertions.assertNotNull(trip);
        org.junit.jupiter.api.Assertions.assertEquals("Korea Trip", trip.name);
        org.junit.jupiter.api.Assertions.assertEquals(1, tripManager.getTrips().size());
    }

    /**
     * Tests that when silent mode is enabled, trip operations do not produce output.
     * Adds a trip and verifies the trip list size increases as expected.
     *
     * @throws TravelDiaryException if there is a general error in the travel diary operations
     * @throws DuplicateNameException if a trip with the same name already exists
     * @throws MissingCompulsoryParameter if a required parameter is missing
     */
    @Test
    void testSilentModePreventsOutput() throws TravelDiaryException, DuplicateNameException,
            MissingCompulsoryParameter {
        tripManager.setSilentMode(true);
        tripManager.addTrip("Bali Trip", "Relaxing by the beach");
        // We assume no output is shown in silent mode, but we can still check the list size
        org.junit.jupiter.api.Assertions.assertEquals(1, tripManager.getTrips().size());
    }

    /**
     * Tests the toString method to ensure it correctly includes trip information
     * after a trip is added. Verifies that the output string contains the trip name.
     *
     * @throws TravelDiaryException if there is a general error in the travel diary operations
     * @throws DuplicateNameException if a trip with the same name already exists
     * @throws MissingCompulsoryParameter if a required parameter is missing
     */
    @Test
    void testToStringNotEmptyAfterAdd() throws TravelDiaryException, DuplicateNameException,
            MissingCompulsoryParameter {
        tripManager.addTrip("Italy Trip", "Exploring Rome");
        String output = tripManager.toString();
        org.junit.jupiter.api.Assertions.assertTrue(output.contains("Italy Trip"));
    }

    /**
     * Tests that the notifyTripsLoaded method does not throw an exception
     * when silent mode is enabled.
     */
    @Test
    void testNotifyTripsLoadedSilentMode() {
        tripManager.setSilentMode(true);
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                tripManager.notifyTripsLoaded()); // should silently skip output
    }

    /**
     * Tests that the notifyTripsLoaded method does not throw an exception
     * when in normal mode (silent mode disabled).
     */
    @Test
    void testNotifyTripsLoadedNormalMode() {
        tripManager.setSilentMode(false);
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                tripManager.notifyTripsLoaded()); // will print to console
    }
}
