package ecomarkets.core.auth;

public enum UserRole {
    customer,
    operator,
    admin;

    public boolean hasRole(UserDetails user){
       return user.hasRole(this);
    }    
}