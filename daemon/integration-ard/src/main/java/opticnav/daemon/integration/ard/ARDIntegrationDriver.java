package opticnav.daemon.integration.ard;

import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;

import opticnav.daemon.device.ARDConnected;
import opticnav.daemon.device.ARDConnectionStatus;
import opticnav.daemon.device.ARDGatekeeper;
import opticnav.daemon.device.ARDInstance;
import opticnav.daemon.device.ARDInstanceJoinStatus;
import opticnav.daemon.device.ARDInstanceSubscriber;
import opticnav.daemon.device.InstanceInfo;
import opticnav.daemon.device.MapTransform;
import opticnav.daemon.device.broker.ARDGatekeeperBroker;
import opticnav.daemon.protocol.ConfCode;
import opticnav.daemon.protocol.GeoCoordFine;
import opticnav.daemon.protocol.PassCode;
import opticnav.daemon.protocol.Protocol;
import opticnav.daemon.protocol.chan.Channel;
import opticnav.daemon.protocol.chan.ChannelUtil;

public class ARDIntegrationDriver {
    private static void unexpected() {
        throw new Error("Unexpected outcome");
    }
    
    public static void main(String[] args) throws Exception {
        final Socket socket = new Socket("localhost", Protocol.DEFAULT_ARD_PORT);
        final Channel channel = ChannelUtil.fromSocket(socket);

        try (Scanner in = new Scanner(System.in)) {
            try (final ARDGatekeeperBroker broker = new ARDGatekeeperBroker(channel, Executors.newCachedThreadPool())) {
                final boolean passCodeProvided;
                final String passCodeString;
                
                System.out.print("Passcode (leave blank and enter if none): ");
                passCodeString = in.nextLine();
                passCodeProvided =  passCodeString != null && !passCodeString.isEmpty();
                
                if (passCodeProvided) {
                    final PassCode passCode = new PassCode(passCodeString);
                    brokerConnect(in, broker, passCode);
                } else {
                    requestPassCode(broker);
                    System.out.println("Run the integration test again with the passCode provided");
                }
            }
        }
    }
    
    private static void brokerConnect(Scanner in, ARDGatekeeperBroker broker, PassCode passCode) throws Exception {
        final ARDConnectionStatus status = broker.connect(passCode);
        
        System.out.println("Status: " + status.getStatus());
        if (status.getStatus() == ARDConnectionStatus.Status.CONNECTED) {
            try (final ARDConnected connected = status.getConnection()) {
                final List<InstanceInfo> instancesList;
                instancesList = connected.listInstances();
                
                System.out.println("List of instances:");
                for (InstanceInfo i: instancesList) {
                    System.out.println(i);
                }
                System.out.println();
                
                if (instancesList.isEmpty()) {
                    System.out.println("No instances to join.");
                } else {
                    System.out.print("Enter the instance ID to join: ");
                    final int instanceID = Integer.parseInt(in.nextLine());
                    joinInstance(connected, instanceID);
                }
            }
        }
    }
    
    private static class LogARDInstanceSubscriber implements ARDInstanceSubscriber {
        @Override
        public void markerCreate(int id, String name, GeoCoordFine geoCoord) {
            System.out.println("Marker created: " + id + ", " + name + ", " + geoCoord);
        }

        @Override
        public void markerMove(int id, GeoCoordFine geoCoord) {
            System.out.println("Marker moved: " + id + ", " + geoCoord);
        }

        @Override
        public void markerRemove(int id) {
            System.out.println("Marker removed: " + id);
        }
    }
    
    private static void joinInstance(ARDConnected connected, int instanceID) throws Exception {
        final ARDInstanceJoinStatus status;
        status = connected.joinInstance(instanceID);
        
        System.out.println("Status: " + status.getStatus());
        if (status.getStatus() == ARDInstanceJoinStatus.Status.JOINED) {
            try (final ARDInstance instance = status.getInstance()) {
                final ARDInstanceSubscriber subscriber;
                subscriber = new LogARDInstanceSubscriber();
                instance.setSubscriber(subscriber);
                
                // move in a circle across the dimensions of the map
                final MapTransform transform = instance.getMap().getTransform();
                
                final int imageWidth = 1000;
                final int imageHeight = 1000;
                final int STEPS = 40;
                
                for (int i = 0; i < STEPS; i++) {
                    double r = 2*Math.PI * (double)i/STEPS;
                    double x = (Math.cos(r)+1)/2 * imageWidth;
                    double y = (Math.sin(r)+1)/2 * imageHeight;
                    
                    instance.move(transform.imageLocalToGeo(x, y));
                    Thread.sleep(500);
                }
            }
        }
    }
    
    private static void requestPassCode(ARDGatekeeperBroker broker) throws Exception {
        broker.requestPassConfCodes(new ARDGatekeeper.RequestPassConfCodesCallback() {
            @Override
            public void registered(PassCode passCode) {
                System.out.println("Registered!");
                System.out.println("passCode: " + passCode);
            }
            
            @Override
            public void couldNotRegister() { unexpected(); }
            
            @Override
            public void confCode(ConfCode confCode, ARDGatekeeper.Cancellation cancellation) {
                System.out.println("Confirmation code (enter this into AdminClient): " + confCode);
            }
            
            @Override
            public void cancelled() { unexpected(); }
        });
    }
}
