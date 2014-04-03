package opticnav.ardd.ard;

import java.io.IOException;
import java.io.InputStream;

import opticnav.ardd.protocol.TemporaryResourceUtil.TemporaryResource;

public class InstanceMap {
    private final MapTransform transform;
    private final TemporaryResource imageResource;
    private final int imageWidth;
    private final int imageHeight;
    
    public InstanceMap(MapTransform transform, TemporaryResource imageResource, int imageWidth, int imageHeight) {
        this.transform = transform;
        this.imageResource = imageResource;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
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

    public int getImageWidth() {
        return this.imageWidth;
    }
    
    public int getImageHeight() {
        return this.imageHeight;
    }
}
