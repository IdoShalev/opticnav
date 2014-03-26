package opticnav.ardroid.model;

public class Coordinate {
    private final double x;
    private final double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static Coordinate lerp(Coordinate begin, Coordinate end, double phase) {
        final double x = lerp(begin.x, end.x, phase);
        final double y = lerp(begin.y, end.y, phase);
        return new Coordinate(x, y);
    }

    private static double lerp(double begin, double end, double phase) {
        return (end-begin)*phase + begin;
    }
}
