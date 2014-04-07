package opticnav.ardd.broker.ard;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.ardd.ard.ARDInstance;
import opticnav.ardd.ard.ARDInstanceException;
import opticnav.ardd.ard.ARDInstanceSubscriber;
import opticnav.ardd.ard.InstanceMap;
import opticnav.ardd.protocol.GeoCoordFine;
import opticnav.ardd.protocol.PrimitiveUtil;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.consts.ARDdARDProtocol.Connected.Instance.Commands;

public class ARDInstanceImpl implements ARDInstance {
    private static final XLogger LOG = XLoggerFactory
            .getXLogger(ARDInstanceImpl.class);
    
    private final InputStream input;
    private final PrimitiveWriter output;
    private final ExecutorService threadPool;
    private final InstanceMap instanceMap;
    private Future<Void> subscriberResult;

    public ARDInstanceImpl(Channel channel, InstanceMap instanceMap, ExecutorService threadPool) {
        this.input = channel.getInputStream();
        this.output = PrimitiveUtil.writer(channel);
        this.threadPool = threadPool;
        this.instanceMap = instanceMap;
    }

    @Override
    public void close() throws IOException {
        if (this.subscriberResult != null) {
            throw new IllegalStateException("A subscriber was never set! This could (or did) cause serious problems!");
        }
        
        try {
            this.output.close();
            this.subscriberResult.get();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
            LOG.catching(e);
        }
    }
    
    @Override
    public void setSubscriber(ARDInstanceSubscriber subscriber) {
        if (this.subscriberResult != null) {
            throw new IllegalStateException("A subscriber was already set!");
        }
        
        this.subscriberResult = threadPool.submit(new ARDInstanceSubscriberListener(input, subscriber));
    }

    @Override
    public void move(GeoCoordFine geoCoord) throws ARDInstanceException {
        try {
            this.output.writeUInt8(Commands.MOVE);
            
            this.output.writeSInt32(geoCoord.getLongitudeInt());
            this.output.writeSInt32(geoCoord.getLatitudeInt());
            this.output.flush();
        } catch (IOException e) {
            throw new ARDInstanceException(e);
        }
    }

    @Override
    public InstanceMap getMap() {
        return this.instanceMap;
    }
}
