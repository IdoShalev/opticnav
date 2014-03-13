package opticnav.ardd.ard;

import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import opticnav.ardd.ard.ARDConnection.Cancellation;
import opticnav.ardd.broker.ard.ARDBroker;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.chan.ChannelUtil;

public class TestMain {
    public static void main(String[] args) throws Exception {
        Socket sock = new Socket("localhost", 4444);
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ARDConnection conn = new ARDBroker(ChannelUtil.fromSocket(sock), threadPool);
        
        conn.requestPassConfCodes(new ARDConnection.RequestPassConfCodesCallback() {
            @Override
            public void registered(PassCode passCode, int ardID) {
                System.out.println("Passcode: " + passCode);
                System.out.println("ID: " + ardID);
            }
            
            @Override
            public void confCode(ConfCode confCode, Cancellation cancellation) {
                System.out.println("Confcode: " + confCode);
            }

            @Override
            public void couldnotregister() {
                System.out.println("Could not register");
            }

            @Override
            public void cancelled() {
                System.out.println("Cancelled");
            }
        });
        
        System.out.println("Done");
        
        sock.getOutputStream().flush();
        sock.getOutputStream().close();
        conn.close();
        threadPool.shutdown();
    }
}
