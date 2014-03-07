package opticnav.web.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import opticnav.web.rest.pojo.LocaleMessage;
import opticnav.web.rest.pojo.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class Controller {
    @Autowired
    private MessageSource msg;
    
    public class MessageException extends Exception {
        private static final long serialVersionUID = 1L;
        
        private Message message;
        
        public MessageException(String key, Object...params) {
            this.message = new LocaleMessage(msg, key, params);
        }
        
        public Message getMessageObject() {
            return this.message;
        }
    }
    
    public class NotFound extends MessageException {
        private static final long serialVersionUID = 1L;
        public NotFound(String key, Object...params) {
            super(key, params);
        }
    }
    
    public class BadRequest extends MessageException {
        private static final long serialVersionUID = 1L;
        public BadRequest(String key, Object...params) {
            super(key, params);
        }
    }
    
    // If an exception is thrown, override default error page with a POJO
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Message handleException(Exception ex) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        return new Message(ex.getMessage());
    }
    
    @ExceptionHandler(NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Message handleNotFound(MessageException ex) {
        return ex.getMessageObject();
    }
    
    @ExceptionHandler(BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Message handleBadRequest(MessageException ex) {
        return ex.getMessageObject();
    }

    public Message ok(String key, Object...params) {
        return new LocaleMessage(msg, key, params);
    }
}
