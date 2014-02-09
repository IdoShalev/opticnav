package opticnav.web.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import opticnav.web.arddbrokerpool.ARDdAdminPool;

public class ARDdAdminServletContextListener implements ServletContextListener {
    private ARDdAdminPool pool;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        
        String host = ctx.getInitParameter("ARDdAdminHost");
        int port = Integer.parseInt(ctx.getInitParameter("ARDdAdminPort"));
        
        this.pool = new ARDdAdminPool(host, port);
        ctx.setAttribute("ARDdAdminPool", this.pool);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            this.pool.close();
        } catch (Exception e) {
            // TODO Log exception
        }
    }
}

