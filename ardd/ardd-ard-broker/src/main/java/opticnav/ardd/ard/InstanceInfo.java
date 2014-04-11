package opticnav.ardd.ard;

/**
 * Information about instances being queried from the {@link ARDConnected} broker.
 * 
 * @author Danny Spencer
 *
 */
public class InstanceInfo {
    private final int id;
    private final String name;
    private final int numberConnected;
    private final long startTime;

    public InstanceInfo(int id, String name, int numberConnected, long startTime) {
        this.id = id;
        this.name = name;
        this.numberConnected = numberConnected;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberConnected() {
        return numberConnected;
    }

    public long getStartTime() {
        return startTime;
    }

    
    @Override
    public String toString() {
        return String.format("ID: %d; Name: %s; # Connected: %d; Start Time: %d",
                this.id, this.name, this.numberConnected, this.startTime);
    }

}
