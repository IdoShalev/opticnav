package opticnav.ardd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import opticnav.ardd.protocol.PassCode;

public class ARDPersistedList {
    private static final Logger logger = LogManager.getLogger();
    
    public boolean containsPassCode(PassCode passCode) {
        // TODO Auto-generated method stub
        return false;
    }

    public int registerARD(PassCode passCode) {
        logger.debug("ARD persisted: " + passCode);
        // TODO
        return 42;
    }
}
