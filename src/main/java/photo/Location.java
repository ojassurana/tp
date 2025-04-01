package photo;

public class Location {
    private double latitude;
    private double longitude;
    private String locationName;

    public Location(double latitude, double longitude, String locationName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
    }

    public Location(double latitude, double longitude) {
        this(latitude, longitude, null);
    }

    public static Location fromCoordinates(double latitude, double longitude) {
        String locationName = PhotoMetadataExtractor.getLocationFromCoordinates(latitude, longitude);
        return new Location(latitude, longitude, locationName);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public String toString() {
        return locationName != null ? locationName :
                String.format("(%.6f, %.6f)", latitude, longitude);
    }

    public boolean hasCoordinates() {
        return latitude != 0 || longitude != 0;
    }

    public boolean hasLocationName() {
        return locationName != null && !locationName.isEmpty() &&
                !locationName.equals("Location not found");
    }
}
