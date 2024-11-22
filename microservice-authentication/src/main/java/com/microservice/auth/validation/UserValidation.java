package com.microservice.auth.validation;

import com.microservice.auth.dto.LoginDTO;
import com.microservice.auth.dto.UserDTO;
import com.microservice.auth.exception.InvalidEmailException;
import com.microservice.auth.exception.InvalidUserException;
import com.microservice.auth.exception.UserNotFoundException;
import com.microservice.auth.model.UserEntity;
import com.microservice.auth.repository.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Component
public class UserValidation {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserValidation(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void validatePageAndSize(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new ValidationException("Page number cannot be negative and size must be greater than zero.");
        }
    }

    public void validateUserExists(UUID idUser) {
        if (idUser == null) {
            throw new ValidationException("User ID must be different of null.");
        }
        if (!userRepository.existsByIdUser(idUser)) {
            throw new UserNotFoundException("The user with ID " + idUser + " does not exist.");
        }
    }

    public void validateUserExistsByEmail(UserRepository userRepository, String email) {
        if (userRepository.findByEmail(email) == null) {
            throw new UserNotFoundException("The user with email " + email + " does not exist.");
        }
    }

    public void validateEnabledUser(LoginDTO loginDTO) {
        UserEntity userEntity = userRepository.findByEmail(loginDTO.getEmail());
        if (!userEntity.isEnabled()) {
            throw new InvalidUserException("The user with email " + loginDTO.getEmail() + " is disabled.");
        }
    }

    public void validateLoginDTO(LoginDTO loginDTO) {
        if (loginDTO.getEmail() == null || loginDTO.getEmail().isEmpty()) {
            throw new InvalidUserException("Email must be different of null or empty.");
        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            throw new InvalidUserException("Password must be different of null or empty.");
        }
        validateUserExistsByEmail(userRepository, loginDTO.getEmail());
        validateEnabledUser(loginDTO);
    }

    public void validateIfUserIsAdmin(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity.getRole() == UserEntity.Role.ADMIN) {
            throw new InvalidUserException("The user with email " + email + " is already an admin.");
        }
    }

    public void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new InvalidEmailException("Email must be different of null or empty");
        }
        if (email.length() > 100) {
            throw new InvalidEmailException("Email must have less than 100 characters");
        }
        if (!email.contains("@")) {
            throw new InvalidEmailException("The email must contain the character '@'");
        }
        if (userRepository.existsByEmail(email)) {
            throw new InvalidEmailException("The email " + email + " is already in use.");
        }
    }

    public void validateUserRegister(UserDTO registerUserDTO) {
        if (registerUserDTO == null) {
            throw new InvalidUserException("User must be different of null.");
        }
        if (registerUserDTO.getUsername() == null || registerUserDTO.getUsername().isEmpty()) {
            throw new InvalidUserException("Username must be different of null or empty.");
        }
        if (registerUserDTO.getPassword() == null || registerUserDTO.getPassword().isEmpty()) {
            throw new InvalidUserException("Password must be different of null or empty.");
        }
        if (registerUserDTO.getPhone() == null || registerUserDTO.getPhone().isEmpty()) {
            throw new InvalidUserException("Phone must be different of null or empty.");
        }
        if (registerUserDTO.getLastName() == null || registerUserDTO.getLastName().isEmpty()) {
            throw new InvalidUserException("Last name must be different of null or empty.");
        }
        if (registerUserDTO.getUsername().length() < 3 || registerUserDTO.getUsername().length() > 50) {
            throw new InvalidUserException("Username must have between 3 and 50 characters.");
        }
        if (registerUserDTO.getPassword().length() < 8 || registerUserDTO.getPassword().length() > 100) {
            throw new InvalidUserException("Password must have between 8 and 100 characters.");
        }
        if (registerUserDTO.getPhone().length() < 10 || registerUserDTO.getPhone().length() > 15) {
            throw new InvalidUserException("Phone must have between 10 and 15 characters.");
        }
        validateEmail(registerUserDTO.getEmail());
    }

    public void validateUpdateUser(UserDTO userDTO, UserEntity existingUser) {
        if (userDTO == null){
            throw new InvalidUserException("User must be different of null.");
        }
        if (userDTO.getUsername() != null && !userDTO.getUsername().isEmpty()) {
            existingUser.setUsername(userDTO.getUsername());
        }
        if (userDTO.getLastName() != null && !userDTO.getLastName().isEmpty()) {
            existingUser.setLastName(userDTO.getLastName());
        }
        if (userDTO.getPhone() != null && !userDTO.getPhone().isEmpty()) {
            existingUser.setPhone(userDTO.getPhone());
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            String encodePassword = passwordEncoder.encode(userDTO.getPassword());
            existingUser.setPassword(encodePassword);
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty() && !userDTO.getEmail().equals(existingUser.getEmail())) {
            validateEmail(userDTO.getEmail());
            existingUser.setEmail(userDTO.getEmail());
        }
    }
}
