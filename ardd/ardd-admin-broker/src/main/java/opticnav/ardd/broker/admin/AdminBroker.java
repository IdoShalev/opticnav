package opticnav.ardd.broker.admin;

import java.io.IOException;

import opticnav.ardd.admin.AdminConnection;
import opticnav.ardd.admin.AdminConnectionException;
import opticnav.ardd.admin.AdminStartInstanceState;
import opticnav.ardd.admin.InstanceDeployment;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
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
    public AdminStartInstanceState deployInstance(InstanceDeployment d)
            throws AdminConnectionException {
        try {
            final boolean hasMapImage = d.hasMapImage();
            
            this.output.writeString(d.getMapName());
            this.output.writeUInt8(hasMapImage?1:0);
            if (hasMapImage) {
                this.output.writeString(d.getMapImageType().getPrimaryType());
                this.output.writeBlobFromInputStream(d.getMapImageSize(), d.getMapImageInput());
                
                assert d.getMapAnchors().size() == 3;
                for (InstanceDeployment.Anchor anchor: d.getMapAnchors()) {
                    this.output.writeSInt32(anchor.getLng());
                    this.output.writeSInt32(anchor.getLat());
                    this.output.writeSInt32(anchor.getLocalX());
                    this.output.writeSInt32(anchor.getLocalY());
                }
            }
            
            this.output.writeUInt31(d.getMapMarkers().size());
            for (InstanceDeployment.Marker marker: d.getMapMarkers()) {
                this.output.writeString(marker.getName());
                this.output.writeSInt32(marker.getLng());
                this.output.writeSInt32(marker.getLat());
            }
            
            this.output.flush();
            int result = this.input.readUInt8();
            
            // TODO - replace with constants
            if (result == 0) {
                int instanceID = this.input.readUInt31();
                return new AdminStartInstanceState();
            } else {
                
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
