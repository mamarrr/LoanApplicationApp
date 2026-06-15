package mamarr.loanapplicationapp.app.security;

import mamarr.loanapplicationapp.domain.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AuthenticatedUser implements UserDetails {
    private final UUID id;
    private final String email;
    private final UserRole role;

    public AuthenticatedUser(UUID id, String email, UserRole role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public UUID id() {
        return id;
    }

    public UserRole role() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return email;
    }
}
