package opticnav.ardd.net;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.io.IOUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelUtil;

public final class Listener implements Callable<Void> {
    private static final XLogger logger = XLoggerFactory.getXLogger(Listener.class);
    
    public interface ConnectionSpawner {
        public Callable<Void> create(Channel channel, ExecutorService threadPool);
    }
    
    private ServerSocket socket;
    private ConnectionSpawner cs;
    private Class<?> clazzSource;
    
    public Listener(Class<?> clazzSource, int port, ConnectionSpawner cs)
            throws IOException {
        this.clazzSource = clazzSource;
        this.socket = new ServerSocket(port);
        this.cs = cs;

        logger.info("Listening on port " + port);
    }

    @Override
    public Void call() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket client = socket.accept();
                    dispatchClient(client);
                } catch (IOException e) {
                    logger.catching(e);
                }
            }
        } catch (InterruptedException e) {
            // Thread has been interrupted - let it fall through to the end
        }
        
        IOUtils.closeQuietly(this.socket);
        return null;
    }

    private void dispatchClient(Socket client)
            throws InterruptedException, IOException
    {
        logger.info(clazzSource.getSimpleName() + " connection accepted: " + client.getInetAddress());
        
        final String loggerName = client.getInetAddress() + " - " + clazzSource.getSimpleName();
        
        ExecutorService threadPool = Executors.newCachedThreadPool(new ThreadFactory() {
            private int threadNum = 0;
            @Override
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, loggerName+"-"+threadNum++);
            }
        });
        
        Channel channel = ChannelUtil.fromSocket(client);
        threadPool.submit(this.cs.create(channel, threadPool));
    }
}
