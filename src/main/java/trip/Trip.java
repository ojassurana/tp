package trip;

import album.Album;
import exception.MissingCompulsoryParameter;

public class Trip {
    public String name;
    public String description;
    public String location;
    public Album album;


    public Trip(String name, String description, String location) throws MissingCompulsoryParameter {
        // make sure the name attribute is provided
        if (name == null) {
            String[] parameters = {"name"};
            throw new MissingCompulsoryParameter(parameters);
        }
        this.name = name;
        this.description = (description == null) ? "no description" : description;
        ;
        this.location = (location == null) ? "no location" : location;
        this.album = new Album();
    }

    @Override
    public String toString() {
        return name + " - " + description + " (" + location + ")";
    }
}
