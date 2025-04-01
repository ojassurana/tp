package trip;

import album.Album;
import exception.TravelDiaryException;
import photo.Photo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    public List<String> periodTracker(){
        List<LocalDateTime> dateTimes = new ArrayList<>(List.of());
        for (Photo photo : this.album.photos){
            dateTimes.add(photo.getDatetime());
        }
        // Find the minimum date-time
        LocalDateTime minDateTime = dateTimes.stream().min(LocalDateTime::compareTo).orElse(null);
        LocalDateTime maxDateTime = dateTimes.stream().max(LocalDateTime::compareTo).orElse(null);
        List<String> minMaxDates = new ArrayList<>(List.of());
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma");
        minMaxDates.add((minDateTime != null) ? minDateTime.format(outputFormatter) : "No Date Available");
        minMaxDates.add((maxDateTime != null) ? maxDateTime.format(outputFormatter) : "No Date Available");
        return minMaxDates;
    }

    @Override
    public String toString() {
        return String.format("%s\n\t\t%s (%s - %s)\n", name, description, periodTracker().get(0),
                periodTracker().get(1));
    }
}
