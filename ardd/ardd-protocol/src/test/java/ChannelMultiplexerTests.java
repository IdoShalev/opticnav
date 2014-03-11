import java.io.BufferedOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelMultiplexer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A series of unit tests for the channel multiplexer system.
 * 
 * Yes, multi-threaded unit tests are usually flimsy. On the other hand, it's
 * not _concurrency_ that's being tested for, but integrity of the protocol.
 * Threads are just used because the streams block.
 *
 */
public class ChannelMultiplexerTests {
    /** The maximum data size for ChannelMultiplexer. It's small here to test
     * for extreme cases. */
    private static final int MAX_DATA_SIZE = 8;
    
    private ExecutorService pool;
    private List<Future<?>> tasks;
    private Thread clientThread, serverThread;
    private ChannelMultiplexer clientMp, serverMp;
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
                e.printStackTrace();
            }
        }
    }
    
    @Before
    public void before() throws Exception {
        this.pool = Executors.newCachedThreadPool();
        this.tasks = new ArrayList<>();
        // Create piped streams that do not buffer
        final PipedInputStream ic = new PipedInputStream(1);
        final PipedInputStream is = new PipedInputStream(1);
        final PipedOutputStream oc = new PipedOutputStream(is);
        final PipedOutputStream os = new PipedOutputStream(ic);
        
        Channel clientChannel = new Channel(ic, oc);
        Channel serverChannel = new Channel(is, os);
        clientMp = new ChannelMultiplexer(clientChannel, MAX_DATA_SIZE);
        serverMp = new ChannelMultiplexer(serverChannel, MAX_DATA_SIZE);

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
        for (Future<?> f: this.tasks) {
            // Wait for the task to complete
            // If there was an exception, it'll be thrown here
            // If it takes too long, a TimeoutException will be thrown
            f.get(2, TimeUnit.SECONDS);
        }
        pool.shutdown();
        
        clientThread.join();
        serverThread.join();
    }
    
    private void addTask(Callable<?> callable) {
        this.tasks.add(this.pool.submit(callable));
    }
    
    private PrimitiveReader getPrimitiveReader(Channel channel) {
        return new PrimitiveReader(channel.getInputStream());
    }
    
    private PrimitiveWriter getPrimitiveWriter(Channel channel) {
        return new PrimitiveWriter(new BufferedOutputStream(channel.getOutputStream()));
    }
    
    @Test
    public void test() throws Exception {
        addTask(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                PrimitiveReader r = getPrimitiveReader(serverChan0);
                assertEquals("Hello", r.readString());
                assertEquals(342, r.readUInt16());
                return null;
            }
        });
        
        PrimitiveWriter c0 = getPrimitiveWriter(clientChan0);
        
        c0.writeString("Hello");
        c0.writeUInt16(342);
        c0.flush();
        c0.close();
    }
    
    @Test
    public void ordering1() throws Exception {
        final Channel clientChan1;
        final Channel serverChan1;
        
        clientChan1 = clientMp.createChannel(1);
        serverChan1 = serverMp.createChannel(1);
        addTask(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                PrimitiveReader r = getPrimitiveReader(serverChan0);
                assertEquals("Foo", r.readString());
                assertEquals("Hoo", r.readString());
                return null;
            }
        });
        
        addTask(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                PrimitiveReader r = getPrimitiveReader(serverChan1);
                assertEquals("Bar", r.readString());
                assertEquals("Har", r.readString());
                return null;
            }
        });

        PrimitiveWriter c0, c1;
        c0 = getPrimitiveWriter(clientChan0);
        c1 = getPrimitiveWriter(clientChan1);
        
        c0.writeString("Foo");
        c0.flush();
        
        c1.writeString("Bar");
        c1.flush();
        
        c0.writeString("Hoo");
        c0.close();
        
        c1.writeString("Har");
        c1.close();
    }
}
