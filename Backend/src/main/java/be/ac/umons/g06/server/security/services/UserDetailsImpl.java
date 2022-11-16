package be.ac.umons.g06.server.security.services;

import be.ac.umons.g06.server.model.Role;
import be.ac.umons.g06.server.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private User user;
    private Collection<SimpleGrantedAuthority> authorities;

    private UserDetailsImpl(User user) {
        this.user = user;
        authorities = new ArrayList<SimpleGrantedAuthority>();
        for (Role role: user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName().name()));
        }
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(user);
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl other = (UserDetailsImpl) o;
        return Objects.equals(this.getUsername(), other.getUsername());
    }
}
