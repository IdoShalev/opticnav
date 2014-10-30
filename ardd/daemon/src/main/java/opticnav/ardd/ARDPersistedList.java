package opticnav.ardd;

import java.io.IOException;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.ardd.persistence.Persistence;
import opticnav.ardd.protocol.PassCode;

/**
 * The ARDPersistedList class contains a list of all persisted devices.
 * All devices on the list have likely gone through an earlier registration phase
 * (though not necessarily if the device passCodes have been hard-coded in the ard-lists file).
 * 
 * @author Danny Spencer
 *
 */
public class ARDPersistedList {
    private static final XLogger logger = XLoggerFactory.getXLogger(ARDPersistedList.class);

    private final BidiMap<Integer, PassCode> list;
    private final Persistence persistence;
    
    public ARDPersistedList(Persistence persistence) throws IOException {
        this.persistence = persistence;
        this.list = new DualHashBidiMap<>();
        persistence.readARDList(list);
    }
    
    public boolean containsPassCode(PassCode passCode) {
        return this.list.containsValue(passCode);
    }

    public int registerARD(PassCode passCode) throws IOException {
        final int ardID;
        synchronized (this.persistence) {
            ardID = this.persistence.nextARDID();
            this.list.put(ardID, passCode);
            this.persistence.persistARDList(this.list.entrySet().iterator());
        }
        logger.debug("ARD persisted: " + passCode);
        return ardID;
    }

    /**
     * Get the persisted ARD id associated with a passCode
     * 
     * @param passCode The passCode to look up
     * @return The ARD id associated with the passCode, or null if not found
     */
    public Integer getARDIDByPassCode(PassCode passCode) {
        return this.list.getKey(passCode);
    }
}
