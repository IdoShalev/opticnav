package opticnav.ardd;

import org.apache.commons.math3.random.ISAACRandom;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import opticnav.ardd.net.ARDListener;
import opticnav.ardd.net.AdminListener;

public class DaemonDriver {
    static final Logger logger = LogManager.getLogger();
    
    public static void main(String[] args) throws Exception {
        int adminPort = opticnav.ardd.protocol.Protocol.DEFAULT_ADMIN_PORT;
        int ardPort   = opticnav.ardd.protocol.Protocol.DEFAULT_ARD_PORT;
        
        ARDPendingList   pending   = new ARDPendingList();
        ARDPersistedList persisted = new ARDPersistedList();
        RandomGenerator  randomGen = new ISAACRandom();
        
        ARDListsManager ardListsManager;
        ardListsManager = new ARDListsManager(pending, persisted, randomGen);
        
        AdminListener adminServer = new AdminListener(adminPort, ardListsManager);
        ARDListener   ardServer   = new ARDListener(ardPort, ardListsManager);
        new Thread(adminServer, "Admin server").start();
        new Thread(ardServer, "ARD server").start();
    }
}
