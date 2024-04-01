package ecomarkets.vdn.user;

import java.util.Locale;
import java.util.UUID;

import com.vaadin.flow.component.Component;
import com.vaadin.quarkus.annotation.VaadinSessionScoped;

import ecomarkets.core.i18n.I18NService;
import ecomarkets.core.user.UserDetails;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
//VaadinSession?
public class UserSession {
    @Inject
    UserDetails userDetails;



    public UserDetails getUserDetails() {
        return userDetails;
    }
    
}
