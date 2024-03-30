package ecomarkets.user;

import java.util.Locale;
import java.util.UUID;

import com.vaadin.flow.component.Component;
import com.vaadin.quarkus.annotation.VaadinSessionScoped;

import ecomarkets.i18n.I18NService;
import jakarta.inject.Inject;

@VaadinSessionScoped
public class UserSession {
    String uuid = UUID.randomUUID().toString();
    String locale_code = Locale.getDefault().getDisplayName();

    @Inject
    I18NService i18n;


    public String getUUID() {
        return uuid;
    }


    public String format(String key, Object... args) {
        return i18n.format(locale_code, key, args);
    }
    
}
