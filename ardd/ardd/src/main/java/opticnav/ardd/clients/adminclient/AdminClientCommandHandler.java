package opticnav.ardd.clients.adminclient;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.ardd.ARDListsManager;
import opticnav.ardd.Instance;
import opticnav.ardd.clients.AnnotatedCommandHandler;
import opticnav.ardd.clients.AnnotatedCommandHandler.Command;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.protocol.Protocol.AdminClient.Commands;

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
        final String mapName;
        final boolean hasMapImage;
        final Instance.Anchor[] anchors = new Instance.Anchor[3];
        
        mapName = in.readString();
        hasMapImage = in.readUInt8() != 0;
        LOG.debug("Map name: " + mapName);
        LOG.debug("Has map image: " + hasMapImage);
        
        if (hasMapImage) {
            final String mapImageType;
            final int mapImageSize;
            
            mapImageType = in.readString();
            mapImageSize = in.readUInt31();
            if (mapImageSize > Protocol.AdminClient.MAX_MAP_IMAGE_SIZE) {
                throw new IllegalArgumentException("Image exceed max size");
            }
            
            // TODO - save to a temporary file
            try (OutputStream mapImageOutput = new NullOutputStream()) {
                in.readFixedBlobToOutputStream(mapImageSize, mapImageOutput);
            }
            
            // Anchors
            for (int i = 0; i < 3; i++) {
                final int lng = in.readSInt32();
                final int lat = in.readSInt32();
                final int localX = in.readSInt32();
                final int localY = in.readSInt32();
                anchors[i] = new Instance.Anchor(lng, lat, localX, localY);
            }
        }
        
        // Markers
        int markerSize = in.readUInt31();
        LOG.debug(markerSize + " markers");
        final List<Instance.StaticMarker> staticMarkers;
        staticMarkers = new ArrayList<>(markerSize);
        for (int i = 0; i < markerSize; i++) {
            final String markerName = in.readString();
            final int lng = in.readSInt32();
            final int lat = in.readSInt32();
            staticMarkers.add(new Instance.StaticMarker(markerName, lng, lat));
        }
        
        // TODO - replace with constants
        out.writeUInt8(0);
        out.writeUInt31(111);
        out.flush();
    }
}
