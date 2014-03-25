package opticnav.ardd;

import java.io.IOException;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.ardd.protocol.PassCode;

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
}
