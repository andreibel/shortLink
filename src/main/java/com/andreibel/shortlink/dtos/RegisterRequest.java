package com.andreibel.shortlink.dtos;

import lombok.Data;

import java.util.Set;

/**
 * Data Transfer Object for user registration requests.
 * <p>
 * Encapsulates the necessary information for registering a new user, including username, email, roles, and password.
 */
@Data
public class RegisterRequest {

    private String username;

    private String email;

    private Set<String> roles;

    private String password;
}