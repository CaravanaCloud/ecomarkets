package ecomarkets.core.auth;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import ecomarkets.core.i18n.I18NService;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class UserDetails {
    @Inject
    SecurityIdentity id;

    String uuid = UUID.randomUUID().toString();
    Locale locale = Locale.getDefault();

    @Inject
    I18NService i18n;
    
    public boolean isAnonymous() {
        return id.isAnonymous();
    }

    public String getEmail() {
        //@Inject
        //@Claim("email")
        //String email;
        var principal = id.getPrincipal();
        if (principal instanceof DefaultJWTCallerPrincipal djcp){
            return djcp.getClaim("email");
        }
        return null;
    }

    public Set<String> getRoleNames() {
        return id.getRoles();
    }

    public String getName() {
        return id.getPrincipal().getName();
    }

    public boolean hasRole(UserRole userRole) {
        if (isAnonymous()) return false;
        var roles = getRoleNames();
        boolean result = roles.contains(userRole.name());
        return result;
    }

    public void ifRole(String roleName, 
        Runnable ifRun,
        Runnable elseRun) {
        if ( getRoleNames().contains(roleName) ) {
            ifRun.run();
        }else {
            elseRun.run();
        }
    }

    public void ifAdmin(Runnable ifRun,
        Runnable elseRun) {
        ifRole("admin", ifRun, elseRun);
    }

    public String format(String key, Object... args) {
        return i18n.format(getLocaleCode(), key, args);
    }

    public String getLocaleCode() {
        return locale.getDisplayName();
    }

    public String getUUID() {
        return uuid;
    }

}