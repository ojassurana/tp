package trip;
import java.util.List;
import java.util.ArrayList;

public class TripManager {
    private final List<Trip> trips = new ArrayList<>();
    private int nextId = 1;
    private Trip selectedTrip = null;

    public void addTrip(String name, String description, String location) {
        trips.add(new Trip(nextId++, name, description, location));
        System.out.println("Trip added successfully.");
    }

    public void deleteTrip(int id) {
        trips.removeIf(trip -> trip.id == id);
        System.out.println("Trip deleted successfully.");
    }

    public void viewTrips() {
        if (trips.isEmpty()) {
            System.out.println("No trips available.");
        } else {
            for (Trip trip : trips) {
                System.out.println(trip);
            }
        }
    }

    public void selectTrip(int id) {
        for (Trip trip : trips) {
            if (trip.id == id) {
                selectedTrip = trip;
                System.out.println("Selected trip: " + trip.name);
                return;
            }
        }
        System.out.println("Trip not found.");
    }
}
