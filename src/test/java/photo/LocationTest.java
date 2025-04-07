package photo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for the Location class.
 * Verifies functionality, including constructor behavior, coordinate validation, and location naming,
 * while considering Equivalence Partitioning, Boundary Value Analysis, and Combining Multiple Test Inputs.
 */
public class LocationTest {

    /**
     * Tests the creation of a Location object with boundary values
     * for latitude and longitude (Boundary Value Analysis).
     * Verifies that the latitude and longitude are correctly set for edge cases.
     */
    @Test
    void constructor_withBoundaryCoordinates_expectSetFieldsCorrectly() {
        double minLatitude = -90.0; // Minimum valid latitude
        double maxLatitude = 90.0; // Maximum valid latitude
        double minLongitude = -180.0; // Minimum valid longitude
        double maxLongitude = 180.0; // Maximum valid longitude

        Location locationMin = new Location(minLatitude, minLongitude);
        Location locationMax = new Location(maxLatitude, maxLongitude);

        // Assertions for minimum boundaries
        assertEquals(minLatitude, locationMin.getLatitude(),
                "Minimum latitude does not match.");
        assertEquals(minLongitude, locationMin.getLongitude(),
                "Minimum longitude does not match.");

        // Assertions for maximum boundaries
        assertEquals(maxLatitude, locationMax.getLatitude(),
                "Maximum latitude does not match.");
        assertEquals(maxLongitude, locationMax.getLongitude(),
                "Maximum longitude does not match.");
    }

    /**
     * Tests the creation of a Location object with invalid coordinates (Equivalence Partitioning).
     * Verifies that invalid coordinates are handled properly.
     */
    @Test
    void constructor_withInvalidCoordinates_expectHandleInvalidCoordinates() {
        double invalidLatitude = 95.0; // Invalid latitude (> 90)
        double invalidLongitude = 190.0; // Invalid longitude (> 180)

        Location invalidLocation = new Location(invalidLatitude, invalidLongitude);

        // Assertions
        assertEquals(invalidLatitude, invalidLocation.getLatitude(),
                "Latitude should match data source even if invalid.");
        assertEquals(invalidLongitude, invalidLocation.getLongitude(),
                "Longitude should match data source, even if invalid.");
    }

    /**
     * Tests combining multiple inputs for the fromCoordinates method (Combining Multiple Test Inputs).
     * Verifies the interaction between latitude, longitude, and the derived location name.
     */
    @Test
    void fromCoordinates_withValidInputs_shouldReturnCorrectLocation() {
        double latitude = 37.7749;
        double longitude = -122.4194;

        Location location = Location.fromCoordinates(latitude, longitude);

        // Assertions
        assertEquals(latitude, location.getLatitude(),
                "Latitude does not match.");
        assertEquals(longitude, location.getLongitude(),
                "Longitude does not match.");
        assertEquals("San Francisco, United States",
                location.getLocationName(),
                "Location name does not match.");
    }

    /**
     * Tests the hasCoordinates method with valid and zero coordinates.
     * Verifies the method's response for edge and neutral cases.
     */
    @Test
    void hasCoordinates_withValidAndZeroCoordinates_shouldReturnCorrectBoolean() {
        Location locationWithCoordinates = new Location(34.0522, -118.2437); // Valid coordinates for Los Angeles
        Location locationWithoutCoordinates = new Location(0.0, 0.0); // Zero coordinates

        // Assertions
        assertTrue(locationWithCoordinates.hasCoordinates(),
                "hasCoordinates should return true for valid coordinates.");
        assertFalse(locationWithoutCoordinates.hasCoordinates(),
                "hasCoordinates should return false for zero coordinates.");
    }

    /**
     * Tests the toString method for different cases of location name availability.
     * Verifies the method's output for both name-present and name-missing cases.
     */
    @Test
    void toString_withLocationNameAndCoordinates_shouldReturnCorrectRepresentation() {
        Location locationWithName = new Location(51.5074, -0.1278, "London");
        Location locationWithoutName = new Location(51.5074, -0.1278);

        // Assertions
        assertEquals("London", locationWithName.toString(),
                "toString should return the location name if available.");
        assertEquals("(51.507400, -0.127800)", locationWithoutName.toString(),
                "toString should return coordinates if location name is unavailable.");
    }
}
