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

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private UserService userService;

    @PostMapping("/public/login")
    public ResponseEntity<?> loginUser(@Validated @RequestBody LoginRequest loginRequest) {
        try {

            JwtAuthenticationResponse jwt = userService.loginUser(loginRequest);
            return ResponseEntity.ok(jwt);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }

    }


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
