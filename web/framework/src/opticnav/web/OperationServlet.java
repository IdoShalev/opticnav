package opticnav.web;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opticnav.web.util.InputUtil;

/**
 * Operation servlets perform backend operations for the web application.
 * It is typically used to run operations concurrently in the web browser
 * without the need to navigate away from the current page.
 * 
 * An operation servlet may optionally forward to another page if the
 * "forward" parameter is set. Results will be passed to this page in the
 * form of request attributes. This is useful if Javascript is disabled on the
 * client's web browser.
 * 
 * An operation servlet may receive parameters using a mix of GET and POST.
 *
 */
public abstract class OperationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public abstract void operation(ResourceBundle text, HttpServletRequest req, ResponseObject response);

    private void processOperation(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        /* JSON requires specific headers to work properly */
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        /* FIXME: Make another context parameter not associated with JSTL */
        String bundlePath = getServletContext().getInitParameter("javax.servlet.jsp.jstl.fmt.localizationContext");
        
        ResourceBundle bundle = ResourceBundle.getBundle(bundlePath, req.getLocale());
        String forward = req.getParameter("forward");
        boolean doRedirect = InputUtil.isEntered(forward);
        ResponseObject responseParameters = new ResponseObject();
        
        // Run the operation
        operation(bundle, req, responseParameters);
        
        if (doRedirect) {
            // Forward and pass request attributes
            // TODO
            throw new UnsupportedOperationException();
        } else {
            // Output JSON parameters
            try (OutputStreamWriter w = new OutputStreamWriter(resp.getOutputStream(), "UTF-8")) {
                responseParameters.toJSON(w);
            }
        }
    }
    
    @Override
    public final void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processOperation(req, resp);
    }

    @Override
    public final void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processOperation(req, resp);
    }
}
