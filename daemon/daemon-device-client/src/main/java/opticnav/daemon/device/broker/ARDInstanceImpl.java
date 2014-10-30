package opticnav.daemon.device.broker;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.daemon.device.ARDInstance;
import opticnav.daemon.device.ARDInstanceException;
import opticnav.daemon.device.ARDInstanceSubscriber;
import opticnav.daemon.device.InstanceMap;
import opticnav.daemon.protocol.GeoCoordFine;
import opticnav.daemon.protocol.PrimitiveUtil;
import opticnav.daemon.protocol.PrimitiveWriter;
import opticnav.daemon.protocol.chan.Channel;
import opticnav.daemon.protocol.consts.ARDdARDProtocol.Connected.Instance.Commands;

class ARDInstanceImpl implements ARDInstance {
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
