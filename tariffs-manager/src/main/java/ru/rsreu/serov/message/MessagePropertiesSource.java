package ru.rsreu.serov.message;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessagePropertiesSource {
    private final MessageSource messageSource;

    public MessagePropertiesSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, null, Locale.ROOT);
    }
}
