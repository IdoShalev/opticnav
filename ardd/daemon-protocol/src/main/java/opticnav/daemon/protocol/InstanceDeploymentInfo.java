package opticnav.daemon.protocol;

import java.util.Collections;
import java.util.List;

/**
 * This class describes an instance and its information on deployment. As long as an instance is running, the
 * deployment info never changes! In turn, this class is immutable.
 *
 */
public final class InstanceDeploymentInfo {
    public static final class ARDIdentifier {
        private int id;
        private String name;
        
        public ARDIdentifier(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
    
    private final int id;
    private final long owner;
    private final String name;
    private final long startTime;
    private final List<ARDIdentifier> invitedArds;
    
    /**
     * @param id An ID number identifying the instance
     * @param owner 
     * @param name The name for the instance
     * @param startTime The UNIX timestamp*1000 (milliseconds elapsed since January 1, 1970)
     * @param invitedArds The list of invited devices by their IDs
     */
    public InstanceDeploymentInfo(int id, long owner, String name, long startTime, List<ARDIdentifier> invitedArds) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.startTime = startTime;
        this.invitedArds = Collections.unmodifiableList(invitedArds);
    }

    public int getId() {
        return id;
    }

    public long getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public long getStartTime() {
        return startTime;
    }

    public List<ARDIdentifier> getArds() {
        return invitedArds;
    }
}
