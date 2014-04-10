package opticnav.ardd.broker.ard;

import java.io.IOException;
import java.io.InputStream;

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
    private final InstanceMap instanceMap;
    private Thread subscriberThread;

    public ARDInstanceImpl(Channel channel, InstanceMap instanceMap) {
        this.input = channel.getInputStream();
        this.output = PrimitiveUtil.writer(channel);
        this.instanceMap = instanceMap;
    }

    @Override
    public void close() throws IOException {
        if (this.subscriberThread == null) {
            throw new IllegalStateException("A subscriber was never set! This could (or did) cause serious problems!");
        }
        
        try {
            this.output.close();
            this.subscriberThread.interrupt();
            this.subscriberThread.join();
        } catch (InterruptedException e) {
            LOG.catching(e);
        }
    }
    
    @Override
    public void setSubscriber(final ARDInstanceSubscriber subscriber) {
        if (this.subscriberThread != null) {
            throw new IllegalStateException("A subscriber was already set!");
        }
        
        this.subscriberThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new ARDInstanceSubscriberListener(input, subscriber).call();
                } catch (IOException e) {
                    LOG.catching(e);
                }
            }
        });
        this.subscriberThread.start();
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
