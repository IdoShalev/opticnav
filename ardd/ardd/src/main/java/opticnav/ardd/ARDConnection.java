package opticnav.ardd;

/**
 * An ARD Connection represents the state of a connected device.
 * It stores information about the instance it has joined (if any), and state such as the device location.
 * 
 * This class is not thread-safe!
 */
public class ARDConnection implements AutoCloseable {
    private final int ardID;
    /** Only used for closing */
    private final ARDConnectedList connectedList;
    private Instance currentInstance;
    
    public ARDConnection(int ardID, ARDConnectedList connectedList) {
        this.ardID = ardID;
        this.connectedList = connectedList;
        this.currentInstance = null;
    }

    @Override
    public void close() {
        this.connectedList.removeConnectedByID(ardID);
    }
    
    public int getARDID() {
        return this.ardID;
    }
    
    public Instance getCurrentInstance() {
        return this.currentInstance;
    }
}