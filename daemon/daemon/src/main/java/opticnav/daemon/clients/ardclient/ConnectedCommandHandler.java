package opticnav.daemon.clients.ardclient;

import java.io.IOException;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.math3.util.Pair;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.daemon.ARDConnection;
import opticnav.daemon.InstancesList;
import opticnav.daemon.BlockingValue;
import opticnav.daemon.clients.AnnotatedCommandHandler;
import opticnav.daemon.instance.Entity;
import opticnav.daemon.instance.EntitySubscriber;
import opticnav.daemon.instance.Instance;
import opticnav.daemon.instance.InstanceInfo;
import opticnav.daemon.protocol.PrimitiveReader;
import opticnav.daemon.protocol.PrimitiveWriter;
import static opticnav.daemon.protocol.consts.ARDdARDProtocol.Connected.*;

/**
 * Handles all commands coming in from the ARDClient "Connected" channel.
 * 
 * @author Danny Spencer
 *
 */
public class ConnectedCommandHandler extends AnnotatedCommandHandler {
    private static final XLogger LOG = XLoggerFactory
            .getXLogger(ConnectedCommandHandler.class);
    
    private final ARDChannelsManager ardChannelsManager;
    private final InstancesList instances;
    private final ARDConnection connection;
    
    public ConnectedCommandHandler(ARDChannelsManager ardChannelsManager, InstancesList instances,
                                   ARDConnection connection) {
        super(ConnectedCommandHandler.class);
        this.ardChannelsManager = ardChannelsManager;
        this.instances = instances;
        this.connection = connection;
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }
    
    @Command(Commands.LIST_INSTANCES)
    public void listInstances(PrimitiveReader in, PrimitiveWriter out) throws Exception {
        final Map<Integer, Instance> list;
        final int ardID = this.connection.getARDID();
        final int count;
        
        list = this.instances.getInstancesForARD(ardID);
        count = list.size();

        out.writeUInt16(count);

        for (Map.Entry<Integer, Instance> i: list.entrySet()) {
            final int instanceID = i.getKey();
            final Instance instance = i.getValue();
            final InstanceInfo info = instance.getInfo();
            final int numberConnected;
            
            numberConnected = instance.getEntities().size();

            out.writeUInt16(instanceID);
            out.writeString(info.name);
            out.writeUInt31(numberConnected);
            out.writeSInt64(info.startTime.getTime());
        }
        out.flush();
    }
    
    private static void writeAnchor(PrimitiveWriter out, InstanceInfo.Anchor anchor) throws IOException {
        out.writeUInt31(anchor.getLocalX());
        out.writeUInt31(anchor.getLocalY());
        out.writeSInt32(anchor.getGeoCoord().getLongitudeInt());
        out.writeSInt32(anchor.getGeoCoord().getLatitudeInt());
    }
    
    @Command(Commands.JOIN_INSTANCE)
    public void joinInstance(PrimitiveReader in, final PrimitiveWriter out) throws Exception {
        final int instanceID = in.readUInt31();
        
        instances.joinInstance(instanceID, connection.getARDID(),
                new InstancesList.JoinInstanceCallbacks() {
            @Override
            public EntitySubscriber joining(Instance instance, BlockingValue<Entity> entity) throws IOException {
                final InstanceInfo info = instance.getInfo();
                final Pair<Integer, EntitySubscriber> p = ardChannelsManager.startInstanceConnection(instance, entity);
                final int instanceChannelID = p.getFirst();
                
                // result: joined
                out.writeUInt8(1);

                out.writeUInt8(instance.getInfo().hasMapImage ? 1:0);
                
                if (info.hasMapImage) {
                    final String mapImageType = info.mapImage.getMimeType();
                    final int mapImageSize = info.mapImage.getSize();
                    final InputStream mapImageInput = info.mapImage.getInputStream();
                    
                    out.writeString(mapImageType);
                    out.writeUInt31(mapImageSize);
                    LOG.debug("Map image size: " + mapImageSize);
                    out.writeFixedBlobFromInputStream(mapImageSize, mapImageInput);

                    writeAnchor(out, info.mapAnchors[0]);
                    writeAnchor(out, info.mapAnchors[1]);
                    writeAnchor(out, info.mapAnchors[2]);
                }
                
                out.writeUInt8(instanceChannelID);
                out.flush();
                
                return p.getSecond();
            }
            
            @Override
            public void noInstanceFound() throws IOException {
                // result: could not join
                out.writeUInt8(0);
                out.flush();
            }
        });
    }
}
