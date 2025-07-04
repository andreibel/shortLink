package com.andreibel.shortlink.controller;

import com.andreibel.shortlink.dtos.LoginRequest;
import com.andreibel.shortlink.dtos.RegisterRequest;
import com.andreibel.shortlink.moduels.User;
import com.andreibel.shortlink.security.jqt.JwtAuthenticationResponse;
import com.andreibel.shortlink.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication-related endpoints.
 * <p>
 * Handles user login and registration requests.
 */
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    /**
     * Service for user-related operations such as authentication and registration.
     */
    private UserService userService;

    /**
     * Authenticates a user with the provided login credentials.
     *
     * @param loginRequest the login request containing username/email and password
     * @return a {@link ResponseEntity} containing a JWT authentication response if successful,
     * or an error message if authentication fails
     */
    @PostMapping("/public/login")
    public ResponseEntity<?> loginUser(@Validated @RequestBody LoginRequest loginRequest) {
        try {
            JwtAuthenticationResponse jwt = userService.loginUser(loginRequest);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

    /**
     * Registers a new user with the provided registration details.
     *
     * @param registerRequest the registration request containing username, email, and password
     * @return a {@link ResponseEntity} indicating success or failure of registration
     */
    @PostMapping("/public/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody RegisterRequest registerRequest) {
        User user = new User();
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setRole("ROLE_USER");

        userService.registerUser(user);
        return ResponseEntity.ok("User register Successfully");
    }
}