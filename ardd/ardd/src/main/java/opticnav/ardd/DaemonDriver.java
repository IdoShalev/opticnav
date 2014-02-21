package opticnav.ardd;

import opticnav.ardd.net.ARDListener;
import opticnav.ardd.net.AdminListener;

public class DaemonDriver {
    public static void main(String[] args) throws Exception {
        int adminPort = opticnav.ardd.protocol.Protocol.DEFAULT_ADMIN_PORT;
        int ardPort   = opticnav.ardd.protocol.Protocol.DEFAULT_ARD_PORT;
        
        AdminListener adminServer = new AdminListener(adminPort);
        ARDListener   ardServer   = new ARDListener(ardPort);
        new Thread(adminServer).start();
        new Thread(ardServer).start();
    }
}
