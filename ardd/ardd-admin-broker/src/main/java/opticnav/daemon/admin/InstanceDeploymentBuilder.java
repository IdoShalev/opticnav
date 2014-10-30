package opticnav.daemon.admin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimeType;

import opticnav.daemon.admin.InstanceDeployment.ARDIdentifier;
import opticnav.daemon.admin.InstanceDeployment.Anchor;
import opticnav.daemon.admin.InstanceDeployment.Marker;

/**
 * The InstanceDeploymentBuilder class uses the Builder pattern to create an InstanceDeployment object.
 * Because construction of an InstanceDeployment is customizable with many parameters, the Builder pattern is better
 * suited than a simple constructor.
 */
public class InstanceDeploymentBuilder {
    private String mapName;
    private MimeType mapImageType;
    private int mapImageSize;
    private InputStream mapImageInput;
    private List<Anchor> mapAnchors;
    private List<Marker> mapMarkers;
    private List<ARDIdentifier> ardList;
    
    public InstanceDeploymentBuilder() {
        this.mapName = "Untitled";
        this.mapMarkers = new ArrayList<>();
    }
    
    public InstanceDeployment build() {
        
        return new InstanceDeployment(mapName, mapImageType, mapImageSize, mapImageInput, mapAnchors,
                                      mapMarkers, ardList);
    }

    public InstanceDeploymentBuilder setMapName(String mapName) {
        this.mapName = mapName;
        return this;
    }

    public InstanceDeploymentBuilder setMapImage(MimeType mapImageType, int mapImageSize, InputStream mapImageInput,
            List<InstanceDeployment.Anchor> mapAnchors) {
        this.mapImageType  = mapImageType;
        this.mapImageSize  = mapImageSize;
        this.mapImageInput = mapImageInput;
        this.mapAnchors    = mapAnchors;
        return this;
    }

    public InstanceDeploymentBuilder setMapMarkers(List<InstanceDeployment.Marker> mapMarkers) {
        this.mapMarkers = mapMarkers;
        return this;
    }

    public InstanceDeploymentBuilder setARDList(List<ARDIdentifier> ardList) {
        this.ardList = ardList;
        return this;
    }
}
