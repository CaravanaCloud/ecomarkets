package ecomarkets.core.user;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.smallrye.mutiny.Uni;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class RolesAugmentor implements SecurityIdentityAugmentor {

    @Inject
    Instance<SecurityIdentitySupplier> identitySupplierInstance;

    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
        if(identity.isAnonymous()) {
            return Uni.createFrom().item(identity);
        }

        // Hibernate ORM is blocking
        SecurityIdentitySupplier identitySupplier = identitySupplierInstance.get();
        identitySupplier.setIdentity(identity);
        return context.runBlocking(identitySupplier);
    }
}