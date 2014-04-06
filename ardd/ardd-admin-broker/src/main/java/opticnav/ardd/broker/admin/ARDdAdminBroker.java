package opticnav.ardd.broker.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opticnav.ardd.admin.ARDdAdmin;
import opticnav.ardd.admin.ARDdAdminException;
import opticnav.ardd.admin.ARDdAdminStartInstanceStatus;
import opticnav.ardd.admin.InstanceDeployment;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.GeoCoordFine;
import opticnav.ardd.protocol.InstanceDeploymentInfo;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.protocol.consts.ARDdAdminProtocol;
import opticnav.ardd.protocol.consts.ARDdAdminProtocol.Commands;
import opticnav.ardd.protocol.chan.Channel;

public class ARDdAdminBroker implements ARDdAdmin {
    private PrimitiveReader input;
    private PrimitiveWriter output;
    
    public ARDdAdminBroker(Channel channel) {
        this.input = new PrimitiveReader(channel.getInputStream());
        this.output = new PrimitiveWriter(channel.getOutputStream());
    }

    @Override
    public void close() throws IOException {
        this.output.close();
    }
    
    @Override
    public int registerARD(ConfCode code)
            throws ARDdAdminException {
        try {
            this.output.writeUInt8(Commands.REGARD);
            this.output.writeFixedBlob(code.getByteArray());
            this.output.flush();
            return this.input.readUInt31();
        } catch (IOException e) {
            throw new ARDdAdminException(e);
        }
    }
    
    @Override
    public ARDdAdminStartInstanceStatus deployInstance(final long owner, final InstanceDeployment d)
            throws ARDdAdminException {
        if (d.getMapImageSize() > ARDdAdminProtocol.MAX_MAP_IMAGE_SIZE) {
            return new ARDdAdminStartInstanceStatus(ARDdAdminStartInstanceStatus.Status.IMAGE_TOO_BIG);
        }
        
        try {
            final boolean hasMapImage = d.hasMapImage();
            
            this.output.writeUInt8(Commands.DEPLOY_INSTANCE);
            this.output.writeSInt64(owner);
            this.output.writeString(d.getMapName());
            this.output.writeUInt8(hasMapImage?1:0);
            if (hasMapImage) {
                this.output.writeString(d.getMapImageType().getPrimaryType());
                this.output.writeUInt31(d.getMapImageSize());
                this.output.writeFixedBlobFromInputStream(d.getMapImageSize(), d.getMapImageInput());
                
                // Anchors
                assert d.getMapAnchors().size() == 3;
                for (InstanceDeployment.Anchor anchor: d.getMapAnchors()) {
                    final GeoCoordFine geoCoord = anchor.getGeoCoordFine();
                    this.output.writeSInt32(geoCoord.getLongitudeInt());
                    this.output.writeSInt32(geoCoord.getLatitudeInt());
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
            
            // Invited ARDs
            this.output.writeUInt31(d.getArdList().size());
            for (InstanceDeployment.ARDIdentifier ard: d.getArdList()) {
                this.output.writeUInt31(ard.getArdID());
                this.output.writeString(ard.getName());
            }
            
            this.output.flush();
            
            final int result = this.input.readUInt8();
            
            // TODO - replace with constants
            if (result == 0) {
                // instance deployment successful
                final int instanceID = this.input.readUInt31();
                return new ARDdAdminStartInstanceStatus(instanceID);
            } else {
                // TODO
                throw new UnsupportedOperationException();
            }
        } catch (IOException e) {
            throw new ARDdAdminException(e);
        }
    }

    @Override
    public List<InstanceDeploymentInfo> listInstancesByOwner(final long owner) throws ARDdAdminException {
        try {
            this.output.writeUInt8(Commands.LIST_INSTANCES_BY_OWNER);
            output.writeSInt64(owner);
            output.flush();
            
            final int instancesSize = input.readUInt8();
            final List<InstanceDeploymentInfo> instances = new ArrayList<>(instancesSize);
            
            for (int i = 0; i < instancesSize; i++) {
                final int id;
                final long instOwner;
                final String name;
                final long startTime;
                final int invitedArdsSize;
                final List<InstanceDeploymentInfo.ARDIdentifier> invitedArds;
                
                id        = input.readUInt31();
                instOwner = input.readSInt64();
                name      = input.readString();
                startTime = input.readSInt64();
                
                invitedArdsSize = input.readUInt31();
                invitedArds     = new ArrayList<>(invitedArdsSize);
                for (int j = 0; j < invitedArdsSize; j++) {
                    final int ardID = input.readUInt31();
                    final String ardName = input.readString();
                    invitedArds.add(new InstanceDeploymentInfo.ARDIdentifier(ardID, ardName));
                }
                
                final InstanceDeploymentInfo inst = new InstanceDeploymentInfo(id, instOwner, name, startTime, invitedArds);
                instances.add(inst);
            }
            return instances;
        } catch (IOException e) {
            throw new ARDdAdminException(e);
        }
    }
}
