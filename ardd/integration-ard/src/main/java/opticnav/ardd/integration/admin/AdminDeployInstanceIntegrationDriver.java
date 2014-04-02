package opticnav.ardd.integration.admin;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import opticnav.ardd.admin.ARDdAdmin;
import opticnav.ardd.admin.ARDdAdminStartInstanceStatus;
import opticnav.ardd.admin.InstanceDeployment;
import opticnav.ardd.admin.InstanceDeploymentBuilder;
import opticnav.ardd.broker.admin.ARDdAdminBroker;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelUtil;

public class AdminDeployInstanceIntegrationDriver {
    public static void main(String[] args) throws Exception {
        final Socket socket = new Socket("localhost", Protocol.DEFAULT_ADMIN_PORT);
        final Channel channel = ChannelUtil.fromSocket(socket);
        
        try (ARDdAdmin broker = new ARDdAdminBroker(channel)) {
            final List<InstanceDeployment.ARDIdentifier> ardList = new ArrayList<>();
            ardList.add(new InstanceDeployment.ARDIdentifier(1, "Bob"));
            ardList.add(new InstanceDeployment.ARDIdentifier(2, "Joe"));
            
            final InstanceDeployment deployment;
            deployment = new InstanceDeploymentBuilder()
                           .setARDList(ardList)
                           .setMapName("Integration Test Map")
                           .build();
            
            final ARDdAdminStartInstanceStatus status = broker.deployInstance(0, deployment);
            System.out.println("Status: " + status.getStatus());
        }
    }
}
