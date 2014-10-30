package opticnav.daemon;

import java.io.File;

import org.apache.commons.math3.random.ISAACRandom;
import org.apache.commons.math3.random.RandomGenerator;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.daemon.net.ARDListener;
import opticnav.daemon.net.AdminListener;
import opticnav.daemon.persistence.FilePersistence;
import opticnav.daemon.persistence.Persistence;

/**
 * The main entry point to the daemon application
 * 
 * @author Danny Spencer
 *
 */
public class DaemonDriver {
    static final XLogger logger = XLoggerFactory.getXLogger(DaemonDriver.class);
    
    public static void main(String[] args) throws Exception {
        int adminPort = opticnav.daemon.protocol.Protocol.DEFAULT_ADMIN_PORT;
        int ardPort   = opticnav.daemon.protocol.Protocol.DEFAULT_ARD_PORT;
        
        // TODO - read from properties or something
        final File ardListFile = new File("/tmp/ard-list.txt");
        final File indexesFile = new File("/tmp/indexes.txt");
        
        final Persistence persistence = new FilePersistence(ardListFile, indexesFile);
        
        final ARDPendingList   pending   = new ARDPendingList();
        final ARDPersistedList persisted = new ARDPersistedList(persistence);
        final ARDConnectedList connected = new ARDConnectedList(persisted);
        final InstancesList    instances = new InstancesList();
        final RandomGenerator  randomGen = new ISAACRandom();
        
        final ARDListsManager ardListsManager;
        ardListsManager = new ARDListsManager(pending, persisted, connected, instances, randomGen);
        
        AdminListener adminServer = new AdminListener(adminPort, ardListsManager);
        ARDListener   ardServer   = new ARDListener(ardPort, ardListsManager);
        new Thread(adminServer, "Admin server").start();
        new Thread(ardServer, "ARD server").start();
    }
}
