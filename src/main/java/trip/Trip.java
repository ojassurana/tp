package trip;

import album.Album;

public class Trip {
    public String name;
    public String description;
    public String location;
    public Album album;

    public Trip(String name, String description, String location) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.album = new Album();
    }

    @Override
    public String toString() {
        return name + " - " + description + " (" + location + ")";
    }
}
