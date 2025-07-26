package com.andreibel.shortlink.dtos;


import lombok.Data;
/**
 * Data Transfer Object for user login requests.
 * <p>
 * Contains the necessary fields for authenticating a user, including username and password.
 */
@Data
public class LoginRequestDTO {
    private String username;
    private String password;
}
