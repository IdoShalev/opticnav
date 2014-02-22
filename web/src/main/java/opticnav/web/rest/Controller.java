package opticnav.web.rest;

import javax.servlet.http.HttpServletResponse;

import opticnav.web.rest.pojo.LocaleMessage;
import opticnav.web.rest.pojo.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;

public class Controller {
    @Autowired
    private MessageSource msg;
    
    private HttpServletResponse resp;
    
    // If an exception is thrown, override default error page with a POJO
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Message handleException(Exception ex) {
      return new Message(ex.getMessage());
    }

    public Message ok(String key, Object...params) {
        return new LocaleMessage(msg, key, params);
    }
    
    public Message badRequest(String key, Object...params) {
        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        return new LocaleMessage(msg, key, params);
    }
    
    @InitBinder
    public void init(HttpServletResponse resp) {
        this.resp = resp;
    }
}
