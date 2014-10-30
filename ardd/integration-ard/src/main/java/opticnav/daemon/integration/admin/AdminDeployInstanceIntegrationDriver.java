package opticnav.daemon.integration.admin;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimeType;

import opticnav.daemon.admin.ARDdAdmin;
import opticnav.daemon.admin.ARDdAdminBroker;
import opticnav.daemon.admin.ARDdAdminStartInstanceStatus;
import opticnav.daemon.admin.InstanceDeployment;
import opticnav.daemon.admin.InstanceDeployment.Anchor;
import opticnav.daemon.admin.InstanceDeployment.Marker;
import opticnav.daemon.admin.InstanceDeploymentBuilder;
import opticnav.daemon.protocol.GeoCoordFine;
import opticnav.daemon.protocol.Protocol;
import opticnav.daemon.protocol.chan.Channel;
import opticnav.daemon.protocol.chan.ChannelUtil;

public class AdminDeployInstanceIntegrationDriver {
    private static List<Anchor> saitAnchors() {
        // hard-coded anchor points for SAIT
        final int[] local = {463,346, 714,409, 937,200};
        final int[] internalGPS = {-41072424,18383378, -41071669,18383259, -41071005,18383656};
        final List<InstanceDeployment.Anchor> anchors = new ArrayList<>(3);
    
        for (int i = 0; i < 3; i++) {
            GeoCoordFine geo = new GeoCoordFine(internalGPS[i * 2 + 0]<<5, internalGPS[i * 2 + 1]<<5);
            Anchor anchor = new Anchor(geo, local[i*2+0], local[i*2+1]);
            anchors.add(anchor);
        }
        return anchors;
    }
    
    private static List<Marker> saitMarkers() {
        final List<InstanceDeployment.Marker> marker = new ArrayList<>();

        final int[] internalGPS = {-41072839,18382877, -41072650,18383699, -41071723,18383285};

        for (int i = 0; i < 3; i++) {
            GeoCoordFine geo = new GeoCoordFine(internalGPS[i * 2 + 0]<<5, internalGPS[i * 2 + 1]<<5);
            marker.add(new Marker("A"+i, geo.getLongitudeInt(), geo.getLatitudeInt()));
        }
        return marker;
    }
    
    public static void main(String[] args) throws Exception {
        final InputStream mapImageInput = AdminDeployInstanceIntegrationDriver.class.getResourceAsStream("/saitcampus.png");
        // In this case, available() returns the resource size
        final int mapImageSize = mapImageInput.available();
        final List<InstanceDeployment.Anchor> mapAnchors = saitAnchors();
        final List<InstanceDeployment.Marker> mapMarkers = saitMarkers();
        
        final Socket socket = new Socket("localhost", Protocol.DEFAULT_ADMIN_PORT);
        final Channel channel = ChannelUtil.fromSocket(socket);
        
        try (ARDdAdmin broker = new ARDdAdminBroker(channel)) {
            final List<InstanceDeployment.ARDIdentifier> ardList = new ArrayList<>();
            ardList.add(new InstanceDeployment.ARDIdentifier(1, "Bob"));
            ardList.add(new InstanceDeployment.ARDIdentifier(2, "Kenny"));
            ardList.add(new InstanceDeployment.ARDIdentifier(3, "Kyle"));
            ardList.add(new InstanceDeployment.ARDIdentifier(4, "Stan"));
            ardList.add(new InstanceDeployment.ARDIdentifier(5, "Randy"));
            ardList.add(new InstanceDeployment.ARDIdentifier(6, "Adam"));

            final InstanceDeployment deployment;
            deployment = new InstanceDeploymentBuilder()
                           .setARDList(ardList)
                           .setMapName("Integration Test Map")
                           .setMapImage(new MimeType("image/png"), mapImageSize, mapImageInput, mapAnchors)
                           .setMapMarkers(mapMarkers)
                           .build();
            
            final ARDdAdminStartInstanceStatus status = broker.deployInstance(0, deployment);
            System.out.println("Status: " + status.getStatus());
        }
    }
}
