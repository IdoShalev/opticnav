package opticnav.web.components.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opticnav.web.components.UserSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Intercepts the Spring servlet to provide a "user" attribute to the JSP views.
 * 
 * @author Danny Spencer
 *
 */
public class UserHandlerInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserSession userSession;
    
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {
        UserSession.User user = this.userSession.getUser();
        
        request.setAttribute("user", user);
        return true;
    }
}
