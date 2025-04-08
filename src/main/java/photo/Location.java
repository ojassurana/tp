package photo;

/**
 * Represents a geographical location with optional location name metadata.
 * A location can consist of only coordinates or include a human-readable name.
 * This class is used primarily for geotagging photos in the Travel Diary application.
 */
public class Location {
    private double latitude;
    private double longitude;
    private String locationName;

    /**
     * Constructs a {@code Location} object with latitude, longitude, and a location name.
     *
     * @param latitude      Latitude in decimal degrees.
     * @param longitude     Longitude in decimal degrees.
     * @param locationName  Human-readable name of the location (can be null).
     */
    public Location(double latitude, double longitude, String locationName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
    }

    /**
     * Constructs a {@code Location} object with latitude and longitude.
     * The location name is set to null.
     *
     * @param latitude   Latitude in decimal degrees.
     * @param longitude  Longitude in decimal degrees.
     */
    public Location(double latitude, double longitude) {
        this(latitude, longitude, null);
    }

    /**
     * Creates a {@code Location} object by extracting the location name
     * from the given coordinates using metadata services.
     *
     * @param latitude   Latitude in decimal degrees.
     * @param longitude  Longitude in decimal degrees.
     * @return           A new {@code Location} with an inferred location name.
     */
    public static Location fromCoordinates(double latitude, double longitude) {
        String locationName = PhotoMetadataExtractor.getLocationFromCoordinates(latitude, longitude);
        return new Location(latitude, longitude, locationName);
    }

    /**
     * Returns the latitude of this location.
     *
     * @return The latitude value.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude of this location.
     *
     * @return The longitude value.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Returns the human-readable location name, if available.
     *
     * @return The location name, or null if not set.
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * Sets the human-readable location name.
     *
     * @param locationName The new name to associate with the location.
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * Returns a string representation of the location.
     * If the location name is available, it is returned.
     * Otherwise, the coordinates are returned in "(lat, long)" format.
     *
     * @return A user-friendly string representation of the location.
     */
    @Override
    public String toString() {
        return locationName != null ? locationName :
                String.format("(%.6f, %.6f)", latitude, longitude);
    }

    /**
     * Checks if the location has valid coordinates (not both zero).
     *
     * @return True if either latitude or longitude is non-zero.
     */
    public boolean hasCoordinates() {
        return latitude != 0 || longitude != 0;
    }

    /**
     * Checks if the location has a valid and non-default name.
     *
     * @return True if the name is not null, not empty, and not "Location not found".
     */
    public boolean hasLocationName() {
        return locationName != null && !locationName.isEmpty() &&
                !locationName.equals("Location not found");
    }
}
