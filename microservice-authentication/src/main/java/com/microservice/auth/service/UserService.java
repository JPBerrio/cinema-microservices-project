package com.microservice.auth.service;

import com.microservice.auth.dto.RegisterUserDTO;
import com.microservice.auth.exception.InvalidUserException;
import com.microservice.auth.exception.UserNotFoundException;
import com.microservice.auth.model.UserEntity;
import com.microservice.auth.repository.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private void validatePageAndSize(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new ValidationException("Page number cannot be negative and size must be greater than zero.");
        }
    }

    private void validateUserExists(UUID idUser) {
        if (idUser == null) {
            throw new ValidationException("User ID must be different of null.");
        }
        if (!userRepository.existsByIdUser(idUser)) {
            throw new UserNotFoundException("The user with ID " + idUser + " does not exist.");
        }
    }

    private void validateUserRegister(RegisterUserDTO registerUserDTO) {
        if (registerUserDTO == null) {
            throw new InvalidUserException("User must be different of null.");
        }
        if (registerUserDTO.getUsername() == null || registerUserDTO.getUsername().isEmpty()) {
            throw new InvalidUserException("Username must be different of null or empty.");
        }
        if (registerUserDTO.getPassword() == null || registerUserDTO.getPassword().isEmpty()) {
            throw new InvalidUserException("Password must be different of null or empty.");
        }
    }

    public Page<UserEntity> findAllUsers(int page, int size) {
        validatePageAndSize(page, size);
        return userRepository.findAll(PageRequest.of(page, size));
    }

    public UserEntity findUserById(UUID idUser) {
        validateUserExists(idUser);
        return userRepository.findByIdUser(idUser);
    }

    public Page<UserEntity> findUsersByRole(String role, int page, int size) {
        validatePageAndSize(page, size);
        return userRepository.findAllUserEntitiesByRoleContainingIgnoreCase(role, PageRequest.of(page, size));
    }

    public Page<UserEntity> findUsersByEnabled(boolean enabled, int page, int size) {
        validatePageAndSize(page, size);
        return userRepository.findAllUserEntitiesByEnabled(enabled, PageRequest.of(page, size));
    }

    @Transactional
    public UserEntity saveUser(RegisterUserDTO registerUserDTO) {
        validateUserRegister(registerUserDTO);
        UserEntity userEntity = convertToEntity(registerUserDTO);
        return userRepository.save(userEntity);
    }

    private UserEntity convertToEntity(RegisterUserDTO registerUserDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerUserDTO.getUsername());
        userEntity.setLastName(registerUserDTO.getLastName());
        userEntity.setEmail(registerUserDTO.getEmail());
        userEntity.setPhone(registerUserDTO.getPhone());
        userEntity.setPassword(registerUserDTO.getPassword());

        userEntity.setRole(UserEntity.Role.USER);
        userEntity.setEnabled(true);
        return userEntity;
    }
}
