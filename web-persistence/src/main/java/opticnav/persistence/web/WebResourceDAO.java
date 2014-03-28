package opticnav.persistence.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.sql.DataSource;

import opticnav.persistence.web.exceptions.WebResourceDAOExcpetion;

import org.apache.commons.io.IOUtils;

public class WebResourceDAO implements AutoCloseable {
    private static final int GROUP_SIZE = 100;
    
    private String resourcePath;
    private Connection conn;
    
    public WebResourceDAO(String resourcePath, DataSource dataSource)
            throws IOException, SQLException {
        File path = new File(resourcePath).getCanonicalFile();
        if (!path.exists()) {
            throw new IOException("Resource path doesn't exist");
        }
        if (!path.isDirectory()) {
            throw new IOException("Resource path must be a directory");
        }
        
        this.resourcePath = path.toString();
        this.conn = DBUtil.getConnectionFromDataSource(dataSource);
    }
    
    @Override
    public void close() throws WebResourceDAOExcpetion {
        try {
            this.conn.close();
        } catch (SQLException e) {
            throw new WebResourceDAOExcpetion(e);
        }
    }

    public int createResource(String mimeType, InputStream input)
            throws WebResourceDAOExcpetion {
        try {
            return createResource(new MimeType(mimeType), input);
        } catch (MimeTypeParseException e) {
            throw new WebResourceDAOExcpetion(e);
        }
    }

    public int createResource(MimeType mimeType, InputStream input)
            throws WebResourceDAOExcpetion {
        try (CallableStatement cs = conn.prepareCall("{? = call addResource(?)}")){
            // store entry and get ID from database
            // mimeType.getBaseType() for storing in DB
            cs.setString(2, mimeType.getBaseType());
            cs.registerOutParameter(1, Types.INTEGER);
            
            cs.execute();
            int id = cs.getInt(1);
            
            File file = getFileFromResourceID(id);
            // create the group directory if it doesn't exist
            file.getParentFile().mkdirs();
            
            try (FileOutputStream output = new FileOutputStream(file)) {
                IOUtils.copy(input, output);
            }
            
            // commit
            conn.commit();
            
            return id;
        } catch (IOException | SQLException e) {
            throw new WebResourceDAOExcpetion(e);
        }
    }
    
    /**
     * Get a Resource (type and InputStream of Resource, as if it were a file).
     * 
     * @param id The ID of the resource
     * @return A Resource object, or null if not found
     * @throws WebResourceDAOExcpetion
     */
    public Resource getResource(int id)
            throws WebResourceDAOExcpetion {
        // read row in DB first to get mimetype and that it's
        // supposed to exist
        String type = null;
        try (CallableStatement cs = conn.prepareCall("{? = call getResourceType(?)}")){
            cs.setInt(2, id);
            cs.registerOutParameter(1, Types.VARCHAR);
            
            cs.execute();
            type = cs.getString(1);
            
            
        } catch (SQLException e1) {
            throw new WebResourceDAOExcpetion(e1);
        }
        
        File file = getFileFromResourceID(id);
        
        try {
            return new FileResource(new MimeType(type), file);
        } catch (MimeTypeParseException e) {
            throw new WebResourceDAOExcpetion(e);
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
