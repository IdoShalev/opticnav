package opticnav.persistence.web;

import java.io.IOException;
import java.io.InputStream;

import javax.activation.MimeType;

/**
 * Abstract class describing how resources should be handled
 * 
 * @author Danny Spencer
 */
public abstract class Resource {
    private MimeType mimeType;
    
    /**
     * Constructor
     * Sets the mimeType
     * 
     * @param mimeType The type of the resource (jpeg/png/txt)
     */
    public Resource(MimeType mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Get the mimeType of the resource
     * 
     * @return The mimeType
     */
    public MimeType getMimeType() {
        return mimeType;
    }

    /**
     * Get the InputStream  for a resource file
     * 
     * @return A InputStream for the resource file
     * @throws IOException
     */
    public abstract InputStream getInputStream() throws IOException;

    /**
     * Get the file size of a resource file
     * 
     * @return The file size of a resource file
     */
    public abstract int getSize();
}
