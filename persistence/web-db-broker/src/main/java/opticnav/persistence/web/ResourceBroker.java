package opticnav.persistence.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Connection;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import org.apache.commons.io.IOUtils;

public class ResourceBroker {
    private static final int GROUP_SIZE = 100;
    
    private String resourcePath;
    private Connection conn;
    
    public ResourceBroker(String resourcePath, Connection conn)
            throws ResourceBrokerExcpetion {
        try {
            File path = new File(resourcePath).getCanonicalFile();
            if (!path.exists()) {
                throw new IOException("Resource path doesn't exist");
            }
            if (!path.isDirectory()) {
                throw new IOException("Resource path must be a directory");
            }
            
            this.resourcePath = path.toString();
            this.conn = conn;
        } catch (IOException e) {
            throw new ResourceBrokerExcpetion(e);
        }
    }
    
    public int createResource(String mimeType, InputStream input)
            throws ResourceBrokerExcpetion {
        try {
            return createResource(new MimeType(mimeType), input);
        } catch (MimeTypeParseException e) {
            throw new ResourceBrokerExcpetion(e);
        }
    }

    public int createResource(MimeType mimeType, InputStream input)
            throws ResourceBrokerExcpetion {
        try {
            // TODO - store entry and get ID from database
            // mimeType.getBaseType() for storing in DB
            int id = 0;
            
            File file = getFileFromResourceID(id);
            // create the group directory if it doesn't exist
            file.getParentFile().mkdirs();
            
            try (FileOutputStream output = new FileOutputStream(file)) {
                IOUtils.copy(input, output);
            }
            
            // commit
            
            return id;
        } catch (IOException e) {
            throw new ResourceBrokerExcpetion(e);
        }
    }
    
    public Resource getResource(int id)
            throws ResourceBrokerExcpetion {
        // TODO - read row in DB first to get mimetype
        File file = getFileFromResourceID(id);
        
        try {
            return new FileResource(new MimeType("image/png"), file);
        } catch (MimeTypeParseException e) {
            throw new ResourceBrokerExcpetion(e);
        }
    }
    
    private File getFileFromResourceID(int id) {
        int group = id / GROUP_SIZE;
        int member = id % GROUP_SIZE;
        
        return Paths.get(
                this.resourcePath,
                Integer.toString(group),
                Integer.toString(member)).toFile();
    }
}
