package opticnav.ardd;

import opticnav.ardd.net.NetAdminServer;

public class DaemonDriver {
    public static void main(String[] args) throws Exception {
        int adminPort = opticnav.ardd.net.Protocol.DEFAULT_ADMIN_PORT;
    
        NetAdminServer server = new NetAdminServer(adminPort);
        server.run();
    }
}
