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
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        String jwt = userService.authenticateAndGenerateToken(loginDTO);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO registerUserDTO) {
        userService.registerUser(registerUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserDTO userDTO) {
        UserEntity updatedUser = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/disable-account")
    public ResponseEntity<String> disableUserAccount() {
        userService.disableUserAccount();
        return ResponseEntity.status(HttpStatus.OK).body("User account disabled successfully.");
    }
}
