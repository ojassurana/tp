package trip;

import album.Album;
import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import tracker.Tracker;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents trip in the Travel Diary application.
 * A Trip is a core entity that contains information about a travel experience,
 * including a name, description, and an album of photos taken during the trip.
 * This class provides functionality to create and manage trip data.
 */
public class Trip {
    private static final Logger logger = Logger.getLogger(Trip.class.getName());

    /** The name of the trip */
    public String name;

    /** The description of the trip */
    public String description;

    /** The album containing photos from this trip */
    public Album album;

    /**
     * Constructs a new Trip with the specified name and description.
     * Initializes a new empty album for the trip. Validates that required
     * parameters are provided.
     *
     * @param name The name of the trip
     * @param description The description of the trip
     * @throws TravelDiaryException If there is a general error creating the trip
     * @throws MissingCompulsoryParameter If name or description is null
     */
    public Trip(String name, String description) throws TravelDiaryException, MissingCompulsoryParameter {
        logger.info("Initializing Trip object");

        // Ensure the name, description, and location attributes are provided
        if (name == null || description == null) {
            logger.severe("Missing required tag(s) for add_trip");
            throw new MissingCompulsoryParameter("trip", "name, description");
        }

        this.name = name;
        this.description = description;
        this.album = new Album();

        // Assertions to validate non-null attributes
        assert this.name != null : "Trip name should not be null";
        assert this.description != null : "Trip description should not be null";
        assert this.album != null : "Trip album should not be null";

        logger.info("Trip object successfully created: " + this);
    }

    /**
     * Returns a string representation of the album details associated with this trip.
     *
     * @return A string containing the album details
     */
    public String getAlbumDetails() {
        return this.album.toString();
    }

    /**
     * Gets the name of the trip.
     *
     * @return The name of the trip
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the album associated with this trip.
     *
     * @return The Album object containing photos from this trip
     */
    public Album getAlbum() {
        return this.album;
    }

    /**
     * Returns a string representation of the trip.
     * The string includes the trip name, description, and, if photos exist,
     * the date range of the photos in the trip. If no photos exist, it indicates
     * that there are no photos in the trip.
     *
     * @return A formatted string representing the trip
     */
    @Override
    public String toString() {
        // Check if there are no photos in the trip directly
        if (this.album.getPhotos().isEmpty()) {
            return String.format("%s\n\t\t%s (No Photos in trip)\n", name, description);
        }

        List<String> period = Tracker.getPeriod(this.album);
        return String.format("%s\n\t\t%s (%s - %s)\n", name, description, period.get(0),
                period.get(1));
    }
}