package opticnav.ardd.clients.ardclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.math3.util.Pair;

import opticnav.ardd.ARDConnection;
import opticnav.ardd.InstancesList;
import opticnav.ardd.BlockingValue;
import opticnav.ardd.clients.AnnotatedCommandHandler;
import opticnav.ardd.instance.Entity;
import opticnav.ardd.instance.EntitySubscriber;
import opticnav.ardd.instance.Instance;
import opticnav.ardd.instance.InstanceInfo;
import opticnav.ardd.protocol.GeoCoordFine;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import static opticnav.ardd.protocol.consts.ARDdARDProtocol.Connected.*;

public class ConnectedCommandHandler extends AnnotatedCommandHandler {
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
        out.writeSInt32(anchor.getLng());
        out.writeSInt32(anchor.getLat());
    }
    
    @Command(Commands.JOIN_INSTANCE)
    public void joinInstance(PrimitiveReader in, final PrimitiveWriter out) throws Exception {
        final int instanceID = in.readUInt31();
        
        final int lng = in.readSInt32();
        final int lat = in.readSInt32();
        
        final GeoCoordFine initialLocation = new GeoCoordFine(lng, lat);
        
        instances.joinInstance(instanceID, connection.getARDID(),
                initialLocation, new InstancesList.JoinInstanceCallbacks() {
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
