package opticnav.ardd.broker.ard;

import opticnav.ardd.ard.ARDConnectedException;
import opticnav.ardd.ard.ARDConnected;
import opticnav.ardd.ard.ARDInstance;
import opticnav.ardd.ard.ARDInstanceJoinStatus;
import opticnav.ardd.ard.InstanceInfo;
import opticnav.ardd.ard.InstanceMap;
import opticnav.ardd.ard.MapTransform;
import opticnav.ardd.protocol.GeoCoordFine;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveUtil;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.TemporaryResourceUtil;
import opticnav.ardd.protocol.TemporaryResourceUtil.TemporaryResource;
import opticnav.ardd.protocol.TemporaryResourceUtil.TemporaryResourceBuilder;
import static opticnav.ardd.protocol.consts.ARDdARDProtocol.Connected.*;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelMultiplexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

class ARDConnectedImpl implements ARDConnected {
    private static final XLogger LOG = XLoggerFactory
            .getXLogger(ARDConnectedImpl.class);
    
    private final PrimitiveReader input;
    private final PrimitiveWriter output;
    private final ChannelMultiplexer mpxr;
    private final ExecutorService threadPool;
    private final Channel instanceChannel;

    public ARDConnectedImpl(Channel connectedChannel, ChannelMultiplexer mpxr, ExecutorService threadPool) {
        this.input  = PrimitiveUtil.reader(connectedChannel);
        this.output = PrimitiveUtil.writer(connectedChannel);
        this.mpxr = mpxr;
        this.threadPool = threadPool;
        // TODO - yuck
        this.instanceChannel = mpxr.createChannel(55);
    }
    
    @Override
    public void close() throws IOException {
        this.output.close();
    }

    @Override
    public List<InstanceInfo> listInstances() throws ARDConnectedException {
        try {
            output.writeUInt8(Commands.LIST_INSTANCES);
            output.flush();

            int count = input.readUInt16();
            List<InstanceInfo> instanceList = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                int id              = input.readUInt16();
                String name         = input.readString();
                int numberConnected = input.readUInt31();
                long startTime      = input.readSInt64();
                InstanceInfo info = new InstanceInfo(id, name, numberConnected, startTime);
                instanceList.add(info);
            }
            
            return instanceList;
        } catch (IOException e) {
            throw new ARDConnectedException(e);
        }
    }
    
    private MapTransform.Anchor readAnchor() throws IOException {
        final int localX = input.readUInt31();
        final int localY = input.readUInt31();
        final int lng = input.readSInt32();
        final int lat = input.readSInt32();
        
        return new MapTransform.Anchor(localX, localY, new GeoCoordFine(lng, lat));
    }

    @Override
    public ARDInstanceJoinStatus joinInstance(int instanceID) throws ARDConnectedException {
        try {
            output.writeUInt8(Commands.JOIN_INSTANCE);
            output.writeUInt31(instanceID);
            output.flush();
            
            final int response = input.readUInt8();
            // TODO - replace with constants
            if (response == 0) {
                // could not join - no instance found
                return new ARDInstanceJoinStatus(ARDInstanceJoinStatus.Status.NOINSTANCE);
            } else if (response == 1) {
                // joined
                final InstanceMap instanceMap;
                final boolean hasMapImage;
                
                hasMapImage = input.readUInt8() != 0;
                
                if (hasMapImage) {
                    final String mapImageType;
                    final int mapImageSize;
                    mapImageType = input.readString();
                    mapImageSize = input.readUInt31();

                    final TemporaryResourceBuilder builder;
                    builder = TemporaryResourceUtil.createTemporaryResourceBuilder(mapImageType);

                    LOG.debug("Map image size: " + mapImageSize);
                    input.readFixedBlobToOutputStream(mapImageSize, builder.getOutputStream());
                    final TemporaryResource mapImageResource = builder.build();
                    
                    final MapTransform.Anchor a1, a2, a3;
                    a1 = readAnchor();
                    a2 = readAnchor();
                    a3 = readAnchor();
                    
                    LOG.debug("Read anchors");
                    
                    final MapTransform mapTransform = new MapTransform(a1, a2, a3);
                    
                    instanceMap = new InstanceMap(mapTransform, mapImageResource);
                } else {
                    instanceMap = null;
                }
                
                // discarded at the last minute
                input.readUInt8();
                
                final ARDInstance instance;
                instance = new ARDInstanceImpl(instanceChannel, instanceMap);
                return new ARDInstanceJoinStatus(instance);
            } else {
                throw new IllegalStateException("Unexpected response code: " + response);
            }
            
        } catch (IOException e) {
            throw new ARDConnectedException(e);
        }
    }
}
