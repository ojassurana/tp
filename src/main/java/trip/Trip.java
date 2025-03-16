package trip;

public class Trip {
    public String name;
    public String description;
    public String location;

    public Trip(String name, String description, String location) {
        this.name = name;
        this.description = description;
        this.location = location;
    }

    @Override
    public String toString() {
        return name + " - " + description + " (" + location + ")";
    }
}
