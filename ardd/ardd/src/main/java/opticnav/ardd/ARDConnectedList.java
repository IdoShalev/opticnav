package opticnav.ardd;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.ardd.protocol.PassCode;

/**
 * The ARDConnectedList class contains a list of all currently connected devices.
 * When a device is authenticated, it gets added to this list.
 * When a device is disconnected, it gets removed from this list.
 * 
 * This class is thread-safe.
 * 
 * @author Danny Spencer
 *
 */
public class ARDConnectedList {
    private static final XLogger LOG = XLoggerFactory
            .getXLogger(ARDConnectedList.class);
    
    private final ARDPersistedList persisted;
    private final Map<Integer, ARDConnection> list;
    
    public ARDConnectedList(ARDPersistedList persisted) {
        this.persisted = persisted;
        this.list = new HashMap<>();
    }
    
    /**
     * Create an ARDConnected instance, unique to the provided passCode.
     * 
     * @param passCode
     * @return An ARDConnected instance bound to the passCode, or null if passCode doesn't exist or already connected
     */
    public synchronized ARDConnection createConnected(PassCode passCode) {
        Integer ardID = this.persisted.getARDIDByPassCode(passCode);
        
        if (ardID == null) {
            LOG.error("Tried to create ARDConnection from non-existent passCode: " + passCode);
            
            return null;
        } else {
            if (this.list.get(ardID) != null) {
                LOG.error("ARDConnection for id ("+ardID+") already exists");
                
                return null;
            } else {
                LOG.debug("Created ARDConnection with id ("+ardID+") using passCode: " + passCode);
                
                final ARDConnection ardConnected = new ARDConnection(ardID, this);
                this.list.put(ardID, ardConnected);
                return ardConnected;
            }
        }
    }
    
    /**
     * Remove the ARD identified by the ARD ID from the connected list
     * 
     * @param ardID The ARD ID of the device to be removed.
     * @throws IllegalArgumentException Thrown if the device to be removed wasn't in the list.
     */
    public synchronized void removeConnectedByID(int ardID) throws IllegalArgumentException {
        if (this.list.remove(ardID) == null) {
            throw new IllegalArgumentException("Device doesn't exist: " + ardID);
        }
        
        LOG.debug("Removed ARDConnected by id: " + ardID);
    }
    
    /**
     * Get the ARDConnection object associated with the device ID
     * 
     * @param ardID The ID of the device to be retrieved.
     * @return A valid ARDConnection object
     */
    public synchronized ARDConnection getConnectedByID(int ardID) {
        return this.list.get(ardID);
    }
}
