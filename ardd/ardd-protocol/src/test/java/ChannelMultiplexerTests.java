import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Callable;

import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelMultiplexer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TODO: More thorough testing, especially with multiple channels and ordering
 *
 */
public class ChannelMultiplexerTests {
    private Thread clientThread, serverThread;
    private Channel clientChan0, serverChan0;
    
    private class TestRunnable implements Runnable {
        private Callable<Void> callable;

        public TestRunnable(Callable<Void> callable) {
            this.callable = callable;
        }
        
        @Override
        public void run() {
            try {
                this.callable.call();
            } catch (Exception e) {
                System.err.println("Thread: " + Thread.currentThread().getName());
                e.printStackTrace();
            }
            System.err.println("Finished thread: " + Thread.currentThread().getName());
        }
    }
    
    @Before
    public void before() throws Exception {
        final PipedInputStream ic = new PipedInputStream();
        final PipedInputStream is = new PipedInputStream();
        
        final PipedOutputStream oc = new PipedOutputStream(is);
        final PipedOutputStream os = new PipedOutputStream(ic);
        
        Channel clientChannel = new Channel(ic, oc);
        Channel serverChannel = new Channel(is, os);
        ChannelMultiplexer clientMp = new ChannelMultiplexer(clientChannel);
        ChannelMultiplexer serverMp = new ChannelMultiplexer(serverChannel);

        // Establish the expected channels
        clientChan0 = clientMp.createChannel(0);
        serverChan0 = serverMp.createChannel(0);
        
        ChannelMultiplexer.Listener clientListener = clientMp.createListener();
        ChannelMultiplexer.Listener serverListener = serverMp.createListener();
        
        clientThread = new Thread(new TestRunnable(clientListener), "Client");
        serverThread = new Thread(new TestRunnable(serverListener), "Server");
        clientThread.start();
        serverThread.start();
    }
    
    @After
    public void after() throws Exception {
        clientThread.join();
        serverThread.join();
    }
    
    @Test
    public void test() throws Exception {
        PrimitiveWriter w0;
        w0 = new PrimitiveWriter(clientChan0.getOutputStream());
        w0.writeString("Hello");
        w0.writeUInt16(342);
        w0.flush();
        w0.close();
        
        new Thread(new TestRunnable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                PrimitiveReader r = new PrimitiveReader(serverChan0.getInputStream());
                System.out.println(r.readString());
                System.out.println(r.readUInt16());
                return null;
            }
        }), "serverChan0_in").start();
    }
}
