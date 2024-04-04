package ecomarkets.core.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.*;

@ApplicationScoped
public class UserService {
    @Inject
    EntityManager em;

    public List<UserRole> listRolesByEmail(String email) {
        List<UserRole> roles = em.createNamedQuery("roles.byEmail", UserRole.class)
                .setParameter("email", email)
                .getResultList();
        if (roles.isEmpty()){
            return List.of(UserRole.user);
        }
        return roles;
    }

}
