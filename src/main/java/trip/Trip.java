package trip;

import album.Album;
import exception.TravelDiaryException;

public class Trip {
    public String name;
    public String description;
    public String location;
    public Album album;

    public Trip(String name, String description, String location) throws TravelDiaryException {
        // make sure the name attribute is provided
        if (name == null){
            throw new TravelDiaryException();
        }
        this.name = name;
        this.description = (description == null) ? "no description" : description;;
        this.location = (location == null) ? "no location" : location;
        this.album = new Album();
    }

    @Override
    public String toString() {
        return name + " - " + description + " (" + location + ")";
    }
}
