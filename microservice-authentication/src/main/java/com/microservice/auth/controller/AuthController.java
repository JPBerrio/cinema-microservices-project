package com.microservice.auth.controller;

import com.microservice.auth.dto.UserDTO;
import com.microservice.auth.model.UserEntity;
import com.microservice.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    //private final AuthenticationManager authenticationManager;
    //private final JwtConfig jwtConfig;

    public AuthController(UserService userService) {
        this.userService = userService;
        //this.authenticationManager = authenticationManager;
        //this.jwtConfig = jwtConfig;
    }

    /*@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        authenticationManager.authenticate(login);

        String jwt = jwtConfig.create(loginDTO.getEmail());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
    }*/

    @PostMapping("/register")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO registerUserDTO) {
        System.out.println("Registro de usuario recibido: " + registerUserDTO.getEmail());
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
