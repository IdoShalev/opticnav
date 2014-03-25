package opticnav.ardd;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.ardd.protocol.PassCode;

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
    
    public synchronized void removeConnectedByID(int ardID) {
        LOG.debug("Removed ARDConnected by id: " + ardID);
        this.list.remove(ardID);
    }
    
    public synchronized ARDConnection getConnectedByID(int ardID) {
        return this.list.get(ardID);
    }
}
