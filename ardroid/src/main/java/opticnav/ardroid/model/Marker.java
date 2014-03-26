package opticnav.ardroid.model;

public class Marker {
    private final String name;
    private final Coordinate coordinate;
    private final boolean hasDirection;
    private float direction;

    public Marker(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
        this.hasDirection = false;
    }

    public Marker(String name, Coordinate coordinate, float direction) {
        this.name = name;
        this.coordinate = coordinate;
        this.hasDirection = true;
        this.direction = direction;
    }

    public String getName() {
        return this.name;
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public boolean hasDirection() {
        return this.hasDirection;
    }

    /**
     * Gets the direction the marker is facing, if any
     *
     * @return
     * 0.00 = North
     * 0.25 = East
     * 0.50 = South
     * 0.75 = West
     * 1.00 = North
     * */
    public Float getDirection() {
        if (this.hasDirection) {
            return this.direction;
        } else {
            return null;
        }
    }

    public Marker withNewCoordinate(Coordinate coordinate) {
        if (this.hasDirection) {
            return new Marker(this.name, coordinate, this.direction);
        } else {
            return new Marker(this.name, coordinate);
        }
    }

    public Marker withNewCoordinateAndDirection(Coordinate coordinate, float direction) {
        return new Marker(this.name, coordinate, direction);
    }
}
