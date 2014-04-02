package opticnav.ardd.integration.ard;

import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;

import opticnav.ardd.ard.ARDConnected;
import opticnav.ardd.ard.ARDConnectionStatus;
import opticnav.ardd.ard.ARDGatekeeper;
import opticnav.ardd.ard.InstanceInfo;
import opticnav.ardd.broker.ard.ARDBroker;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelUtil;

public class ARDIntegrationDriver {
    private static void unexpected() {
        throw new Error("Unexpected outcome");
    }
    
    private static void makeAssertion(boolean expected, boolean actual) {
        if (expected != actual) {
            throw new AssertionError("Assertion failed");
        }
    }
    
    public static void main(String[] args) throws Exception {
        final Socket socket = new Socket("localhost", Protocol.DEFAULT_ARD_PORT);
        final Channel channel = ChannelUtil.fromSocket(socket);

        try (Scanner in = new Scanner(System.in)) {
            try (final ARDBroker broker = new ARDBroker(channel, Executors.newCachedThreadPool())) {
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
    
    private static void brokerConnect(Scanner in, ARDBroker broker, PassCode passCode) throws Exception {
        final ARDConnectionStatus status = broker.connect(passCode);
        
        System.out.println("Status: " + status.getStatus());
        if (status.getStatus() == ARDConnectionStatus.Status.CONNECTED) {
            final ARDConnected connected = status.getConnection();
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
                // TODO
            }
        }
    }
    
    private static void requestPassCode(ARDBroker broker) throws Exception {
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
