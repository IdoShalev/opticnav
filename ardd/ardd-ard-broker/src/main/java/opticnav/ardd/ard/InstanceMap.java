package opticnav.ardd.ard;

import java.io.IOException;
import java.io.InputStream;

import opticnav.ardd.protocol.TemporaryResourceUtil.TemporaryResource;

public class InstanceMap {
    private final MapTransform transform;
    private final TemporaryResource imageResource;
    
    public InstanceMap(MapTransform transform, TemporaryResource imageResource) {
        this.transform = transform;
        this.imageResource = imageResource;
    }
    
    public InputStream getImageInputStream() throws IOException {
        if (this.imageResource == null) {
            return null;
        } else {
            return this.imageResource.getInputStream();
        }
    }
    
    public MapTransform getTransform() {
        return this.transform;
    }
}
