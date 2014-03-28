package opticnav.web.controllers.rest.pojo;

import org.springframework.context.MessageSource;

public class LocaleMessage extends Message {
    public LocaleMessage(MessageSource m, String key, Object... params) {
        this.message = m.getMessage(key, params, null);
    }
}
