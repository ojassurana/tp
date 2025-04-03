package Tracker;

import javax.xml.stream.Location;

public class Tracker {
    private static final int EARTH_RADIUS = 6371;
    double calculateDist(Photo p1, Photo p2) {
        Location l1 = p1.getLocation();
        Location l2 = p2.getLocation();
        double lat1 = l1.getLatitude();
        double lon1 = l1.getLongitude();
        double lat2 = l2.getLatitude();
        double lon2 = l12getLongitude();

        double dLatitude = Math.toRadians(lat2 - lat1);
        double dLongitude = Math.toRadians(lon2 - lon1);
        // Formula to calculate distance in km
        double a = Math.sin(dLatitude / 2) * Math.sin(dLatitude/ 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLongitude/ 2) * Math.sin(dLongitude / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
