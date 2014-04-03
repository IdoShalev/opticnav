package opticnav.ardd.protocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Utility class for creating temporary resources
 */
public class TemporaryResourceUtil {
    private static final XLogger LOG = XLoggerFactory
            .getXLogger(TemporaryResourceUtil.class);
    
    /**
     * A temporary resource is a resource (image, other data) that is only meant to be kept around for a short amount
     * of time.
     */
    public interface TemporaryResource {
        /**
         * Get an input stream - the means to reading the contents of the resource.
         * @return The InputStream object used for reading
         * @throws IOException Thrown if there's a problem accessing the resource
         */
        public InputStream getInputStream() throws IOException;
        /**
         * Get the type of resource as a MIME type.
         * @return A valid mime-type string. Never null.
         */
        public String getMimeType();
        /**
         * Mark the resource as being unused. The implementation can decide to do nothing or to postpone deletion, but
         * it's wise for implementations to perform deletion as soon as possible.
         * @throws IOException Thrown if there's a problem deleting the resource
         */
        public void delete() throws IOException;
    }
    
    public interface TemporaryResourceBuilder {
        /**
         * Get an output stream - the means to writing the contents of the resource.
         * Note that it's only safe to call this method exactly ONCE before calling build().
         * 
         * @return The OutputStream object used for writing
         * @throws IllegalStateException Thrown if this method got called more than once
         * @throws IOException Thrown if there's a problem acquiring the resource
         */
        public OutputStream getOutputStream() throws IOException, IllegalStateException;
        /**
         * Constructs a TemporaryResource object.
         * @return A TemporaryResource object
         * @throws IllegalStateException Thrown if getOutputStream() never got called
         * @throws IOException
         */
        public TemporaryResource build() throws IOException, IllegalStateException;
    }
    
    private static final class FileTemporaryResource implements TemporaryResource {
        private final File tempFile;
        private final String mimeType;
        
        public FileTemporaryResource(File tempFile, String mimeType) {
            if (mimeType == null) {
                throw new IllegalArgumentException("Must provide a MIME type");
            }
            this.tempFile = tempFile;
            this.mimeType = mimeType;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new FileInputStream(this.tempFile);
        }
        
        @Override
        public String getMimeType() {
            return this.mimeType;
        }

        @Override
        public void delete() {
            this.tempFile.delete();
            LOG.debug("Deleted temporary resource file: " + this.tempFile.getAbsolutePath());
        }
    }
    
    private static final class FileTemporaryResourceBuilder implements TemporaryResourceBuilder {
        private final String mimeType;
        private final File tempFile;
        private boolean acquiredOutputStream;
        
        public FileTemporaryResourceBuilder(String mimeType) throws IOException {
            if (mimeType == null) {
                throw new IllegalArgumentException("Must provide a MIME type");
            }
            this.mimeType = mimeType;
            this.tempFile = File.createTempFile("opticnav", null);
            LOG.debug("Created temporary resource file: " + this.tempFile.getAbsolutePath());
            this.acquiredOutputStream = false;
            // Request that the file is deleted when the VM exits (it's _temporary_!)
            this.tempFile.deleteOnExit();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            if (this.acquiredOutputStream) {
                throw new IllegalStateException("getOutputStream() was already invoked");
            }
            
            final OutputStream out = new FileOutputStream(this.tempFile);
            this.acquiredOutputStream = true;
            return out;
        }

        @Override
        public TemporaryResource build() throws IOException {
            if (!this.acquiredOutputStream) {
                throw new IllegalStateException("getOutputStream() was never fully invoked");
            }
            
            return new FileTemporaryResource(this.tempFile, this.mimeType);
        }
    }
    
    public static TemporaryResourceBuilder createTemporaryResourceBuilder(String mapImageType) throws IOException {
        return new FileTemporaryResourceBuilder(mapImageType);
    }
}
