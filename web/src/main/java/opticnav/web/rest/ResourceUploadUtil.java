package opticnav.web.rest;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import opticnav.persistence.web.ResourceBroker;
import opticnav.persistence.web.ResourceBrokerExcpetion;
import opticnav.web.rest.Controller.MessageException;

public class ResourceUploadUtil {
    public static int upload(Controller c, ResourceBroker resourceBroker,
            HttpServletRequest request)
            throws MessageException, IOException, FileUploadException,
            ResourceBrokerExcpetion {
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw c.new BadRequest("resource.nofile");
        }
        
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        
        FileItemIterator iter = upload.getItemIterator(request);
        
        if (!iter.hasNext()) {
            throw c.new BadRequest("resource.nofile");
        }
        FileItemStream item = iter.next();
        String contentType = item.getContentType();

        if (contentType == null) {
            throw c.new BadRequest("resource.nofile");
        }
        
        int resourceID;
        
        try (InputStream input = item.openStream()) {
            resourceID = resourceBroker.createResource(contentType, input);
        }
        
        return resourceID;
    }
}
