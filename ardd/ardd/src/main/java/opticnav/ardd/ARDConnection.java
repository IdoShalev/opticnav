package opticnav.ardd;

/**
 * An ARDConnection is a handle to a connected device.
 * 
 * @author Danny Spencer
 * 
 */
public class ARDConnection implements AutoCloseable {
    private final int ardID;
    /** Only used for closing. This was a bad design. */
    private final ARDConnectedList connectedList;
    
    /**
     * Construct an ARDConnection
     * 
     * @param ardID The ARD ID uniquely identifying the device
     * @param connectedList Used to remove the device when close() is called. This was a bad design.
     */
    public ARDConnection(int ardID, ARDConnectedList connectedList) {
        this.ardID = ardID;
        this.connectedList = connectedList;
    }

    @Override
    public void close() {
        this.connectedList.removeConnectedByID(ardID);
    }
    
    /**
     * Get the ARD ID uniquely identifying the device. No ARDConnection will share the same ID.
     * 
     * @return The ARD ID uniquely identifying the device
     */
    public int getARDID() {
        return this.ardID;
    }
}