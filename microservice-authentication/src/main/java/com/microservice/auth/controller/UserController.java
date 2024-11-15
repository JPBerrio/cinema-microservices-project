package com.microservice.auth.controller;

import com.microservice.auth.dto.RegisterUserDTO;
import com.microservice.auth.model.UserEntity;
import com.microservice.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all-users")
    public ResponseEntity<Page<UserEntity>> getAllMovies(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int elements) {
        Page<UserEntity> movies = userService.findAllUsers(page, elements);
        return ResponseEntity.ok(movies);
    }

    @PostMapping("/create")
    public ResponseEntity<RegisterUserDTO> addUser(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
        userService.saveUser(registerUserDTO);
        return ResponseEntity.ok(registerUserDTO);
    }
}
