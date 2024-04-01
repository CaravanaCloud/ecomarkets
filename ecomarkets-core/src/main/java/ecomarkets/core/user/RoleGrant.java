package ecomarkets.core.user;

package tdc.core.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity()
@Table(name = "role_grant", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "email", "role" })
})
@NamedQuery(name = "roles.byEmail", query = "SELECT rg.role FROM RoleGrant rg WHERE rg.email = :email")
public class RoleGrant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    Integer id;

    String email;

    @Enumerated(EnumType.STRING)
    UserRole role;
}