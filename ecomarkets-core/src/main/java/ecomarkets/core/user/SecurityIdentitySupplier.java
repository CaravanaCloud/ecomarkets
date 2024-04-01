package ecomarkets.core.user;

package tdc.core.auth;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import tdc.core.service.AuthService;

import java.util.function.Supplier;

@Dependent
class SecurityIdentitySupplier implements Supplier<SecurityIdentity> {

    @Inject
    UserService users;

    private SecurityIdentity identity;

    @Override
    @ActivateRequestContext
    public SecurityIdentity get() {
        if (identity.isAnonymous()){
            return identity;
        }
        var principal = identity.getPrincipal();
        var email = (String) null;
        if (principal instanceof DefaultJWTCallerPrincipal djcp) {
            email = djcp.getClaim("email");
        }
        if (email == null) {
            //TODO: Handle identities without email
            return identity;
        }
        var builder = QuarkusSecurityIdentity.builder(identity);
        var roles = users.listRolesByEmail(email);
        // UserRoleEntity.<userRoleEntity>streamAll()
        //        .filter(role -> user.equals(role.user))
        //        .forEach(role -> builder.addRole(role.role));
        roles.stream().forEach(role -> builder.addRole(role.name()));
        return builder.build();
    }

    public void setIdentity(SecurityIdentity identity) {
        this.identity = identity;
    }
}