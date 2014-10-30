package opticnav.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.activation.MimeType;

/**
 * Holds a resource file and its mime type
 * 
 * @author Danny Spencer
 */
public class FileResource extends Resource {
    private File file;

    /**
     * Constructor
     * sets the mimeType and the resource file
     * 
     * @param mimeType The mimeType of the resource
     * @param file The resource file
     */
    public FileResource(MimeType mimeType, File file) {
        super(mimeType);
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }

    @Override
    public int getSize() {
        long length = this.file.length();
        if (length < 0 || length > Integer.MAX_VALUE) {
            throw new IllegalStateException("File size is out of range (too large)");
        }
        return (int)length;
    }
}
