package ecomarkets.vdn.user;

import java.util.Locale;
import java.util.UUID;

import com.vaadin.flow.component.Component;
import com.vaadin.quarkus.annotation.VaadinSessionScoped;

import ecomarkets.core.auth.UserDetails;
import ecomarkets.core.i18n.I18NService;
import jakarta.inject.Inject;

@VaadinSessionScoped
public class UserSession {
    @Inject
    UserDetails user;

    public String format(String key, Object... args) {
        return user.format(key, args);
    }

    public UserDetails getUser() {
        return user;
    }
    
}
