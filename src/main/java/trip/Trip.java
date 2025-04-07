package trip;

import album.Album;
import exception.TravelDiaryException;
import tracker.Tracker;
import java.util.List;
import java.util.logging.Logger;

public class Trip {
    private static final Logger logger = Logger.getLogger(Trip.class.getName());

    public String name;
    public String description;
    public Album album;

    public Trip(String name, String description) throws TravelDiaryException {
        logger.info("Initializing Trip object");

        // Ensure the name, description, and location attributes are provided
        if (name == null || description == null) {
            logger.severe("Missing required tag(s) for add_trip");
            throw new TravelDiaryException("Missing required tag(s) for add_trip. Required: n# (name), " +
                    "d# (description). ");
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

    public String getAlbumDetails() {
        return this.album.toString();
    }

    public Album getAlbum() {
        return this.album;
    }

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
