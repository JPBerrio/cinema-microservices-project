package com.microservice.auth.controller;

import com.microservice.auth.dto.LoginDTO;
import com.microservice.auth.dto.UserDTO;
import com.microservice.auth.model.UserEntity;
import com.microservice.auth.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDTO loginDTO) {
            String jwt = userService.authenticateAndGenerateToken(loginDTO);
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO registerUserDTO) {
        userService.registerUser(registerUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable String email, @RequestBody UserDTO userDTO) {
        UserEntity updatedUser = userService.updateUser(email, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/disable-account/{email}")
    public ResponseEntity<String> disableUserAccount(@PathVariable String email) {
        userService.disableUserAccount(email);
        return ResponseEntity.status(HttpStatus.OK).body("User account disabled successfully.");
    }
}
