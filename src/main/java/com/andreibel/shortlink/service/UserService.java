package com.andreibel.shortlink.service;


import com.andreibel.shortlink.dtos.LoginRequest;
import com.andreibel.shortlink.moduels.User;
import com.andreibel.shortlink.repository.UserRepository;
import com.andreibel.shortlink.security.jqt.JwtAuthenticationResponse;
import com.andreibel.shortlink.security.jqt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for user registration, authentication, and lookup.
 */
@Service
@AllArgsConstructor
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private AuthenticationManager authManager;
    private JwtUtils jwtUtils;

    /**
     * Registers a new user with encoded password.
     *
     * @param user the user to register
     * @return the saved User entity
     */
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    /**
     * Authenticates a user and returns a JWT authentication response.
     *
     * @param user the login request
     * @return JwtAuthenticationResponse containing the JWT token
     */
    public JwtAuthenticationResponse loginUser(LoginRequest user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Checks if a username already exists.
     *
     * @param username the username to check
     * @return true if exists, false otherwise
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if an email already exists.
     *
     * @param email the email to check
     * @return true if exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Finds a user by username.
     *
     * @param name the username
     * @return the User entity
     * @throws UsernameNotFoundException if user not found
     */
    public User findByUsername(String name) {
        return userRepository.findByUsername(name).orElseThrow(
                () -> new UsernameNotFoundException("Username " + name + " not found")
        );
    }
}
