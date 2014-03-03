package opticnav.web.components.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opticnav.web.components.UserSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AuthHandlerInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserSession userSession;
    
    private String bouncePage;
    
    public AuthHandlerInterceptor(String bouncePage) {
        this.bouncePage = bouncePage;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {
        UserSession.User user = this.userSession.getUser();
        
        boolean authenticated = user != null;
        
        if (authenticated) {
            return true;
        } else {
            // User is not logged in - bounce the user to another page
            response.sendRedirect(request.getContextPath() + this.bouncePage);
            return false;
        }
    }
}
