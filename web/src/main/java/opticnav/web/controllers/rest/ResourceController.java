package opticnav.web.controllers.rest;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import opticnav.persistence.web.Resource;
import opticnav.persistence.web.WebResourceDAO;
import opticnav.web.controllers.rest.pojo.Message;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resource/**")
public class ResourceController extends Controller {
    @Autowired
    private DataSource dbDataSource;
    
    @Value("${opticnav.resource.dir}")
    private String resourcePath;
    
    @RequestMapping(method=RequestMethod.POST,
            headers="content-type=multipart/form-data")
    public Message upload(HttpServletRequest request)
            throws Exception {
        int resourceID;
        
        try (WebResourceDAO dao = new WebResourceDAO(resourcePath, dbDataSource)) {
            resourceID = ResourceUploadUtil.upload(this, dao, request);
        }
        
        return new Message("Resource uploaded: " + resourceID);
    }
    
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public void get(@PathVariable("id") int id,
            HttpServletResponse resp) throws Exception {
        Resource resource;

        try (WebResourceDAO dao = new WebResourceDAO(resourcePath, dbDataSource)) {
            resource = dao.getResource(id);
        }
        
        resp.setContentType(resource.getMimeType().getBaseType());
        
        try (InputStream input = resource.getInputStream()) {
            IOUtils.copy(input, resp.getOutputStream());
        }
    }
}
