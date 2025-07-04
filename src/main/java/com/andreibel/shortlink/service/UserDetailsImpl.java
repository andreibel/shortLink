package com.andreibel.shortlink.service;

import com.andreibel.shortlink.moduels.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link UserDetails} for Spring Security authentication.
 * <p>
 * Encapsulates user information such as id, username, email, password, and authorities.
 * Used by Spring Security to perform authentication and authorization.
 */
@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    /**
     * Serialization identifier for the class.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier of the user.
     */
    private Long id;

    /**
     * Username of the user.
     */
    private String username;

    /**
     * Email address of the user.
     */
    private String email;

    /**
     * Encrypted password of the user.
     */
    private String password;

    /**
     * Collection of authorities granted to the user.
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructs a new {@code UserDetailsImpl} with the specified user details and authorities.
     *
     * @param id          the user's unique identifier
     * @param username    the user's username
     * @param email       the user's email address
     * @param password    the user's encrypted password
     * @param authorities the authorities granted to the user
     */
    public UserDetailsImpl(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Builds a {@code UserDetailsImpl} instance from a {@link User} entity.
     *
     * @param user the user entity
     * @return a new {@code UserDetailsImpl} instance
     */
    public static UserDetailsImpl build(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), List.of(authority) // Wrap the authority in a list
        );
    }

    /**
     * Returns the authorities granted to the user.
     *
     * @return the authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username used to authenticate the user.
     *
     * @return the username
     */
    @Override
    public String getUsername() {
        return username;
    }
}