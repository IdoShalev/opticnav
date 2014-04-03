package opticnav.ardd.broker.ard;

import java.io.EOFException;
import java.io.IOException;
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

public class ARDInstanceImpl implements ARDInstance {
    private static final XLogger LOG = XLoggerFactory
            .getXLogger(ARDInstanceImpl.class);
    
    private final PrimitiveWriter output;
    private final InstanceMap instanceMap;
    private final Future<Void> subscriberResult;

    public ARDInstanceImpl(Channel channel, InstanceMap instanceMap, ARDInstanceSubscriber subscriber,
            ExecutorService threadPool) {
        this.output = PrimitiveUtil.writer(channel);
        this.instanceMap = instanceMap;
        this.subscriberResult = threadPool.submit(new ARDInstanceSubscriberListener(channel.getInputStream(), subscriber));
    }

    @Override
    public void close() throws IOException {
        this.output.close();
        try {
            this.subscriberResult.get();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
            LOG.catching(e);
        }
    }

    @Override
    public void move(GeoCoordFine geoCoord) throws ARDInstanceException {
        // TODO Auto-generated method stub

    }

    @Override
    public InstanceMap getMap() {
        return this.instanceMap;
    }
}
