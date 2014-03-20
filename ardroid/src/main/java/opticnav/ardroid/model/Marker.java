package opticnav.ardroid.model;

public class Marker {
    private String name;
    private Coordinate coordinate;

    public Marker(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

    public String getName() {
        return this.name;
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public Marker withNewCoordinate(Coordinate coordinate) {
        return new Marker(this.name, coordinate);
    }
}
