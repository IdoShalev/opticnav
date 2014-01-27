package opticnav.web.test.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Authenticate implements Filter {
    private String bootPage;
    
	public void init(FilterConfig fConfig) throws ServletException {
	    this.bootPage = fConfig.getInitParameter("bootPage");
	}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		HttpSession session = req.getSession(false);
		
		boolean authorized = session != null && session.getAttribute("user") != null;
		
		if (authorized) {
		    /* user is allowed to see the page */
		    req.setAttribute("user", session.getAttribute("user"));
	        chain.doFilter(req, resp);
		} else {
		    /* boot the user to another page */
	        resp.sendRedirect(req.getContextPath() + bootPage);
		}
	}

    public void destroy() {
    }
}
