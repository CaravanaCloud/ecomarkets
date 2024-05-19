package ecomarkets.core.user;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import ecomarkets.core.i18n.I18NService;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.List;

@Dependent
public class UserDetails {
    String uuid = UUID.randomUUID().toString();
    Locale locale = Locale.getDefault();

    @Inject
    SecurityIdentity id;

    @Inject
    I18NService i18n;
    
    @Inject
    UserService users;

    public boolean isAnonymous() {
        var result = id.isAnonymous();
        var email = getEmail();
        var principal = id.getPrincipal();
        var name = principal.getName();
        return result;
    }

    public List<UserRole> getUserRoles(){
        return users.listRolesByEmail(getEmail());
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

    public String format(String key, Object... args) {
        return i18n.format(getLocaleCode(), key, args);
    }

    public String getLocaleCode() {
        return locale.getDisplayName();
    }

    public String getUUID() {
        return uuid;
    }

    public boolean isAdmin(){
        return this.getRoleNames().stream().anyMatch(r -> r.equals("admin"));
    }

}