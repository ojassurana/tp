package trip;

import exception.DuplicateNameException;
import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import exception.IndexOutOfRangeException;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Manages the collection of trips in the Travel Diary application.
 * This class serves as the central repository for all trips, providing methods for
 * creating, selecting, viewing, and deleting trips. It maintains the state of the
 * currently selected trip and offers both standard and silent operating modes.
 */
public class TripManager {
    private static final Logger logger = Logger.getLogger(TripManager.class.getName());

    /** The collection of all trips in the application */
    private final List<Trip> trips = new ArrayList<>();

    /** The currently selected trip, or null if no trip is selected */
    private Trip selectedTrip = null;

    /** Flag to control whether operations produce console output */
    private boolean silentMode = false;

    /**
     * Enables or disables silent mode to prevent console output during operations.
     * When silent mode is enabled, methods will not produce output to the console.
     * This is useful during batch operations or when loading data.
     *
     * @param silentMode true to enable silent mode, false to disable it
     */
    public void setSilentMode(boolean silentMode) {
        this.silentMode = silentMode;
    }

    /**
     * Returns the current silent mode state.
     *
     * @return true if silent mode is enabled, false otherwise
     */
    public boolean isSilentMode() {
        return silentMode;
    }

    /**
     * Adds a trip to the collection and displays the updated list.
     * Creates a new Trip object with the provided name and description,
     * adds it to the collection, and optionally displays a confirmation message
     * unless silent mode is enabled.
     *
     * @param name The name of the trip to add
     * @param description The description of the trip
     * @throws TravelDiaryException If there is a general error creating the trip
     * @throws DuplicateNameException If a trip with the same name already exists
     * @throws MissingCompulsoryParameter If name or description is null
     */
    public void addTrip(String name, String description) throws TravelDiaryException, DuplicateNameException,
            MissingCompulsoryParameter {
        logger.info("Adding a new trip: " + name);
        boolean anyContainsDuplicateName = trips.stream()
                .anyMatch(s -> s.getName().equals(name));
        if (anyContainsDuplicateName){
            throw new DuplicateNameException("trip", name);
        }
        trips.add(new Trip(name, description));
        logger.info("Trip added successfully: " + name);
        System.out.printf("\tTrip [%s] has been added successfully.\n", name);
    }

    /**
     * Adds a trip silently without displaying the updated list.
     * Creates a new Trip object with the provided name and description
     * and adds it to the collection. Unlike addTrip, this method returns
     * the newly created Trip object and does not produce console output.
     *
     * @param name The name of the trip to add
     * @param description The description of the trip
     * @return The newly created Trip object
     * @throws TravelDiaryException If there is a general error creating the trip
     * @throws MissingCompulsoryParameter If name or description is null
     * @throws DuplicateNameException If a trip with the same name already exists
     */
    public Trip addTripSilently(String name, String description) throws TravelDiaryException,
            MissingCompulsoryParameter, DuplicateNameException {
        logger.info("Adding a new trip silently: " + name);
        boolean anyContainsDuplicateName = trips.stream()
                .anyMatch(s -> s.getName().equals(name));
        if (anyContainsDuplicateName){
            throw new DuplicateNameException("trip", name);
        }
        Trip newTrip = new Trip(name, description);
        trips.add(newTrip);
        logger.info("Trip added silently: " + name);
        return newTrip;
    }

    /**
     * Sets the currently selected trip.
     * Updates the currently selected trip to the provided trip.
     *
     * @param selectedTrip The trip to select, or null to clear the selection
     */
    public void setSelectedTrip(Trip selectedTrip) {
        logger.info("Setting selected trip: " + (selectedTrip != null ? selectedTrip.name : "null"));
        this.selectedTrip = selectedTrip;
    }

    /**
     * Deletes a trip at the specified index.
     * Removes the trip at the given index from the collection and
     * displays a confirmation message unless silent mode is enabled.
     *
     * @param index The index of the trip to delete
     * @throws IndexOutOfRangeException If the index is out of range
     */
    public void deleteTrip(int index) throws IndexOutOfRangeException {
        logger.info("Attempting to delete trip at index: " + index);
        if (index < 0 || index >= trips.size()) {
            throw new IndexOutOfRangeException();
        }
        logger.info("Trip deleted: " + trips.get(index).name);
        trips.remove(index);

        if (!silentMode) {
            System.out.println("Trip deleted successfully.");
        }
    }

    /**
     * Displays all trips in the collection.
     * Lists all trips with their indices and details, or displays a message
     * if no trips are available. This method does nothing if silent mode is enabled.
     */
    public void viewTrips() {
        logger.info("Viewing all trips.");

        if (silentMode) {
            return; // Skip printing in silent mode
        }

        if (trips.isEmpty()) {
            logger.warning("No trips available.");
            System.out.println("\n\tNo trips available. Start adding a new trip now!");
        } else {
            for (int i = 0; i < trips.size(); i++) {
                System.out.println(i + 1 + ": " + trips.get(i)); // Display index with trip details
            }
        }
    }

    /**
     * Selects a trip at the specified index.
     * Updates the currently selected trip to the trip at the given index
     * and displays a confirmation message unless silent mode is enabled.
     *
     * @param index The index of the trip to select
     * @throws IndexOutOfRangeException If the index is out of range
     */
    public void selectTrip(int index) throws IndexOutOfRangeException {
        logger.info("Selecting trip at index: " + index);
        if (index < 0 || index >= trips.size()) {
            logger.severe("Invalid trip index: " + index);
            throw new IndexOutOfRangeException();
        }
        selectedTrip = trips.get(index);
        logger.info("Selected trip: " + selectedTrip.name);

        if (!silentMode) {
            System.out.println("\tSelected trip: " + selectedTrip);
        }
    }

    /**
     * Gets the currently selected trip.
     * Returns the Trip object that is currently selected. This method
     * includes an assertion to ensure that a trip is selected.
     *
     * @return The currently selected Trip object
     */
    public Trip getSelectedTrip() {
        logger.info("Retrieving selected trip.");
        assert selectedTrip != null : "Selected trip should not be null";
        return this.selectedTrip;
    }

    /**
     * Notifies that trips have been fully loaded.
     * This method is called after loading all trips from storage to trigger
     * a single UI update. It displays a confirmation message unless silent mode is enabled.
     */
    public void notifyTripsLoaded() {
        if (!silentMode) {
            System.out.println("All trips loaded successfully.");
        }
    }

    /**
     * Returns a string representation of all trips in the collection.
     * Creates a formatted string containing details of all trips, with each trip
     * numbered according to its index.
     *
     * @return A formatted string representation of all trips
     */
    @Override
    public String toString() {
        StringBuilder tripsDetails = new StringBuilder();
        for (int i = 0; i < trips.size(); i++) {
            tripsDetails.append("\t").append(i + 1).append(") ")
                    .append(trips.get(i).toString());
        }
        return tripsDetails.toString();
    }

    /**
     * Gets the list of all trips in the collection.
     * Returns the complete list of Trip objects managed by this TripManager.
     *
     * @return The list of Trip objects
     */
    public List<Trip> getTrips() {
        return trips;
    }
}
