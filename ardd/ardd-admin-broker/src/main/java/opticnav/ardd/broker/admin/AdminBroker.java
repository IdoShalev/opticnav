package opticnav.ardd.broker.admin;

import java.io.IOException;

import opticnav.ardd.admin.AdminConnection;
import opticnav.ardd.admin.AdminConnectionException;
import opticnav.ardd.admin.AdminStartInstanceStatus;
import opticnav.ardd.admin.InstanceDeployment;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.protocol.Protocol.AdminClient.Commands;
import opticnav.ardd.protocol.chan.Channel;

public class AdminBroker implements AdminConnection {
    private PrimitiveReader input;
    private PrimitiveWriter output;
    
    public AdminBroker(Channel channel) {
        this.input = new PrimitiveReader(channel.getInputStream());
        this.output = new PrimitiveWriter(channel.getOutputStream());
    }

    /**
     * @return 0 if no ARD was registered, ARD ID (1+) if registered
     */
    @Override
    public int registerARD(ConfCode code)
            throws AdminConnectionException {
        try {
            this.output.writeUInt8(Commands.REGARD);
            this.output.writeFixedBlob(code.getByteArray());
            this.output.flush();
            return this.input.readUInt31();
        } catch (IOException e) {
            throw new AdminConnectionException(e);
        }
    }
    
    @Override
    public AdminStartInstanceStatus deployInstance(InstanceDeployment d)
            throws AdminConnectionException {
        if (d.getMapImageSize() > Protocol.AdminClient.MAX_MAP_IMAGE_SIZE) {
            return new AdminStartInstanceStatus(AdminStartInstanceStatus.Status.IMAGE_TOO_BIG);
        }
        
        try {
            final boolean hasMapImage = d.hasMapImage();
            
            this.output.writeUInt8(Commands.DEPLOY_INSTANCE);
            this.output.writeString(d.getMapName());
            this.output.writeUInt8(hasMapImage?1:0);
            if (hasMapImage) {
                this.output.writeString(d.getMapImageType().getPrimaryType());
                this.output.writeUInt31(d.getMapImageSize());
                this.output.writeFixedBlobFromInputStream(d.getMapImageSize(), d.getMapImageInput());
                
                // Anchors
                assert d.getMapAnchors().size() == 3;
                for (InstanceDeployment.Anchor anchor: d.getMapAnchors()) {
                    this.output.writeSInt32(anchor.getLng());
                    this.output.writeSInt32(anchor.getLat());
                    this.output.writeSInt32(anchor.getLocalX());
                    this.output.writeSInt32(anchor.getLocalY());
                }
            }
            
            // Markers
            this.output.writeUInt31(d.getMapMarkers().size());
            for (InstanceDeployment.Marker marker: d.getMapMarkers()) {
                this.output.writeString(marker.getName());
                this.output.writeSInt32(marker.getLng());
                this.output.writeSInt32(marker.getLat());
            }
            
            this.output.flush();
            
            final int result = this.input.readUInt8();
            
            // TODO - replace with constants
            if (result == 0) {
                // instance deployment successful
                final int instanceID = this.input.readUInt31();
                return new AdminStartInstanceStatus(instanceID);
            } else {
                // TODO
                throw new UnsupportedOperationException();
            }
        } catch (IOException e) {
            throw new AdminConnectionException(e);
        }
    }

    @Override
    public void close() throws IOException {
        this.output.close();
    }
}
