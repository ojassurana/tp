package trip;

public class Trip {
    public int id;
    public String name;
    public String description;
    public String location;

    public Trip(int id, String name, String description, String location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
    }

    @Override
    public String toString() {
        return id + ". " + name + " - " + description + " (" + location + ")";
    }
}
