package opticnav.web.components.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Intercepts the Spring servlet to provide a view name attribute to the JSPs.
 * This is used to mark the current page in the header.
 * 
 * @author Danny Spencer
 *
 */
public class ViewNameInModelInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("currentPage", modelAndView.getViewName());
        }
        super.postHandle(request, response, handler, modelAndView);
    }
}
