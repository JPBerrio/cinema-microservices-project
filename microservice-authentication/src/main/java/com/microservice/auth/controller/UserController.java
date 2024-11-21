package com.microservice.auth.controller;

import com.microservice.auth.dto.ChangeRoleDTO;
import com.microservice.auth.model.UserEntity;
import com.microservice.auth.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all-users")
    public ResponseEntity<Page<UserEntity>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int elements) {
        Page<UserEntity> users = userService.findAllUsers(page, elements);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search-by-email")
    public ResponseEntity<UserEntity> getUserByEmail(@RequestParam String email) {
        UserEntity user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search-by-role")
    public ResponseEntity<Page<UserEntity>> getUsersByRole(@RequestParam String role,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int elements) {
        Page<UserEntity> users = userService.getUsersByRole(role, page, elements);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search-by-enabled")
    public ResponseEntity<Page<UserEntity>> getUsersByEnabled(@RequestParam boolean enabled,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int elements) {
        Page<UserEntity> users = userService.getUsersByEnabled(enabled, page, elements);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/change-role")
    public ResponseEntity<String> changeRoleToAdmin(@RequestBody ChangeRoleDTO changeRoleDTO) {
        userService.changeRoleToAdmin(changeRoleDTO.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("Role changed to ADMIN successfully.");
    }
}
