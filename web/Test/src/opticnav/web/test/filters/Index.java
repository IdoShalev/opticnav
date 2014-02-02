package opticnav.web.test.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter("/index.jsp")
public class Index implements Filter {

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
    
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filter) throws IOException, ServletException {
        request.setAttribute("msg", "Hello!<b>");
        filter.doFilter(request, response);
    }

}
