package opticnav.daemon.clients.adminclient;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.daemon.ARDListsManager;
import opticnav.daemon.clients.AnnotatedCommandHandler;
import opticnav.daemon.instance.Instance;
import opticnav.daemon.instance.InstanceInfo;
import opticnav.daemon.protocol.ConfCode;
import opticnav.daemon.protocol.GeoCoordFine;
import opticnav.daemon.protocol.InstanceDeploymentInfo;
import opticnav.daemon.protocol.PrimitiveReader;
import opticnav.daemon.protocol.PrimitiveWriter;
import opticnav.daemon.protocol.Protocol;
import opticnav.daemon.protocol.TemporaryResourceUtil;
import opticnav.daemon.protocol.TemporaryResourceUtil.TemporaryResource;
import opticnav.daemon.protocol.TemporaryResourceUtil.TemporaryResourceBuilder;
import opticnav.daemon.protocol.consts.ARDdAdminProtocol;
import opticnav.daemon.protocol.consts.ARDdAdminProtocol.Commands;

/**
 * Handles all commands coming in from the AdminClient channel.
 * 
 * @author Danny Spencer
 *
 */
public class AdminClientCommandHandler extends AnnotatedCommandHandler {
    private static final XLogger LOG = XLoggerFactory
            .getXLogger(AdminClientCommandHandler.class);
    
    private ARDListsManager ardListsManager;
    private ExecutorService threadPool;

    public AdminClientCommandHandler(ARDListsManager ardListsManager, ExecutorService threadPool) {
        super(AdminClientCommandHandler.class);
        this.ardListsManager = ardListsManager;
        this.threadPool = threadPool;
    }

    @Override
    public void close() throws IOException {
        threadPool.shutdown();
    }
    
    @Command(Commands.REGARD)
    public void registerARD(PrimitiveReader in, PrimitiveWriter out) throws Exception {
        byte[] hexCode = in.readFixedBlob(Protocol.CONFCODE_BYTES);
        ConfCode confcode = new ConfCode(hexCode);
        
        int ard_id;
        ard_id = this.ardListsManager.persistPendingWithConfCode(confcode);

        out.writeUInt31(ard_id);
        out.flush();
    }
    
    @Command(Commands.DEPLOY_INSTANCE)
    public void deployInstance(PrimitiveReader in, PrimitiveWriter out) throws Exception {
        final long owner;
        final String instName;
        final boolean hasMapImage;
        final TemporaryResource mapImage;
        final InstanceInfo.Anchor[] mapAnchors;
        
        owner = in.readSInt64();
        instName = in.readString();
        hasMapImage = in.readUInt8() != 0;
        LOG.debug("Owner: " + owner);
        LOG.debug("Instance name: " + instName);
        LOG.debug("Has map image: " + hasMapImage);
        
        if (hasMapImage) {
            final String mapImageType;
            final int mapImageSize;
            
            mapImageType = in.readString();
            LOG.debug("Map image type: " + mapImageType);
            mapImageSize = in.readUInt31();
            if (mapImageSize > ARDdAdminProtocol.MAX_MAP_IMAGE_SIZE) {
                throw new IllegalArgumentException("Image exceeds max size");
            }
            
            final TemporaryResourceBuilder imageResBuilder;
            imageResBuilder = TemporaryResourceUtil.createTemporaryResourceBuilder(mapImageType);
            
            try (OutputStream mapImageOutput = imageResBuilder.getOutputStream()) {
                in.readFixedBlobToOutputStream(mapImageSize, mapImageOutput);
            }
            
            mapImage = imageResBuilder.build();
            
            // Anchors
            mapAnchors = new InstanceInfo.Anchor[3];
            for (int i = 0; i < 3; i++) {
                final int lng = in.readSInt32();
                final int lat = in.readSInt32();
                final int localX = in.readSInt32();
                final int localY = in.readSInt32();
                final GeoCoordFine geoCoord = new GeoCoordFine(lng, lat);
                mapAnchors[i] = new InstanceInfo.Anchor(geoCoord, localX, localY);
            }
        } else {
            mapImage = null;
            mapAnchors = null;
        }
        
        // Markers
        final int markerSize = in.readUInt31();
        LOG.debug(markerSize + " markers");
        final List<InstanceInfo.StaticMarker> staticMarkers;
        staticMarkers = new ArrayList<>(markerSize);
        for (int i = 0; i < markerSize; i++) {
            final String markerName = in.readString();
            final int lng = in.readSInt32();
            final int lat = in.readSInt32();
            final GeoCoordFine geoCoord = new GeoCoordFine(lng, lat);
            staticMarkers.add(new InstanceInfo.StaticMarker(markerName, geoCoord));
        }
        
        // Invited ARDs
        final int ardSize = in.readUInt31();
        LOG.debug(ardSize + " invited ARDs");
        final List<InstanceInfo.ARDIdentifier> ards;
        ards = new ArrayList<>(ardSize);
        for (int i = 0; i < ardSize; i++) {
            final int ardID = in.readUInt31();
            final String name = in.readString();
            ards.add(new InstanceInfo.ARDIdentifier(ardID, name));
        }
        if (ardSize == 0) {
            LOG.warn(String.format("There are no invited ARDs for this instance (%s). " + 
                                   "We'll allow it, but how will anyone join it?", instName));
        } else {
            LOG.debug("Invited ARDs: " + ards);
        }
        
        // Create the instance!
        final Date now = new Date();
        final InstanceInfo instanceInfo = new InstanceInfo(now, instName, mapImage, mapAnchors, staticMarkers, ards);
        final Instance instance = new Instance(instanceInfo);
        final int instanceID = ardListsManager.getInstancesList().addInstance(owner, instance);
        
        // TODO - replace with constant
        out.writeUInt8(0);
        out.writeUInt31(instanceID);
        out.flush();
    }
    
    @Command(Commands.LIST_INSTANCES_BY_OWNER)
    public void listInstancesByOwner(PrimitiveReader in, PrimitiveWriter out) throws Exception {
        final long owner;
        owner = in.readSInt64();
        
        final List<InstanceDeploymentInfo> instances;
        instances = this.ardListsManager.getInstancesList().listInstancesByOwner(owner);
        
        if (instances == null) {
            // no owner was found
            out.writeUInt8(0);
        } else {
            out.writeUInt8(instances.size());
            for (InstanceDeploymentInfo inst: instances) {
                out.writeUInt31(inst.getId());
                out.writeSInt64(inst.getOwner());
                out.writeString(inst.getName());
                out.writeSInt64(inst.getStartTime());
                
                out.writeUInt31(inst.getArds().size());
                for (InstanceDeploymentInfo.ARDIdentifier ard: inst.getArds()) {
                    out.writeUInt31(ard.getId());
                    out.writeString(ard.getName());
                }
            }
        }
        
        out.flush();
    }
    
    @Command(Commands.STOP_INSTANCE)
    public void stopInstance(PrimitiveReader in, PrimitiveWriter out) throws Exception {
        final int instanceID = in.readUInt31();
        
        final boolean existed = ardListsManager.getInstancesList().removeInstance(instanceID);
        
        out.writeUInt8(existed?1:0);
        out.flush();
    }
}
