package opticnav.ardd.clients.adminclient;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.ardd.ARDListsManager;
import opticnav.ardd.Instance;
import opticnav.ardd.TemporaryResourceUtil;
import opticnav.ardd.TemporaryResourceUtil.TemporaryResource;
import opticnav.ardd.TemporaryResourceUtil.TemporaryResourceBuilder;
import opticnav.ardd.clients.AnnotatedCommandHandler;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.protocol.consts.ARDdAdminProtocol;
import opticnav.ardd.protocol.consts.ARDdAdminProtocol.Commands;

public class AdminClientCommandHandler extends AnnotatedCommandHandler {
    private static final XLogger LOG = XLoggerFactory
            .getXLogger(AdminClientCommandHandler.class);
    
    private ARDListsManager ardListsManager;

    public AdminClientCommandHandler(ARDListsManager ardListsManager) {
        super(AdminClientCommandHandler.class);
        this.ardListsManager = ardListsManager;
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
        final String instName;
        final boolean hasMapImage;
        final TemporaryResource mapImage;
        final Instance.Anchor[] mapAnchors = new Instance.Anchor[3];
        
        instName = in.readString();
        hasMapImage = in.readUInt8() != 0;
        LOG.debug("Instance name: " + instName);
        LOG.debug("Has map image: " + hasMapImage);
        
        if (hasMapImage) {
            final String mapImageType;
            final int mapImageSize;
            
            mapImageType = in.readString();
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
            for (int i = 0; i < 3; i++) {
                final int lng = in.readSInt32();
                final int lat = in.readSInt32();
                final int localX = in.readSInt32();
                final int localY = in.readSInt32();
                mapAnchors[i] = new Instance.Anchor(lng, lat, localX, localY);
            }
        } else {
            mapImage = null;
        }
        
        // Markers
        final int markerSize = in.readUInt31();
        LOG.debug(markerSize + " markers");
        final List<Instance.StaticMarker> staticMarkers;
        staticMarkers = new ArrayList<>(markerSize);
        for (int i = 0; i < markerSize; i++) {
            final String markerName = in.readString();
            final int lng = in.readSInt32();
            final int lat = in.readSInt32();
            staticMarkers.add(new Instance.StaticMarker(markerName, lng, lat));
        }
        
        // Invited ARDs
        final int ardSize = in.readUInt31();
        LOG.debug(ardSize + " invited ARDs");
        if (ardSize == 0) {
            LOG.warn(String.format("There are no invited ARDs for this instance (%s). " + 
                                   "We'll allow it, but how will anyone join it?", instName));
        }
        final List<Instance.ARDIdentifier> ards;
        ards = new ArrayList<>(ardSize);
        for (int i = 0; i < ardSize; i++) {
            final int ardID = in.readUInt31();
            final String name = in.readString();
            ards.add(new Instance.ARDIdentifier(ardID, name));
        }
        
        // Create the instance!
        final Instance instance = new Instance(instName, mapImage, mapAnchors, staticMarkers, ards);
        final int instanceID = ardListsManager.getInstancesList().addInstance(instance);
        
        // TODO - replace with constant
        out.writeUInt8(0);
        out.writeUInt31(instanceID);
        out.flush();
    }
}
