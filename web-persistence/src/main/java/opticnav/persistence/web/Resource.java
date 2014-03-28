package opticnav.persistence.web;

import java.io.IOException;
import java.io.InputStream;

import javax.activation.MimeType;

public abstract class Resource {
    private MimeType mimeType;
    
    public Resource(MimeType mimeType) {
        this.mimeType = mimeType;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    public abstract InputStream getInputStream() throws IOException;

    public abstract int getSize();
}
