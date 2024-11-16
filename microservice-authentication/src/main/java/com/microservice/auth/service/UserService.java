package com.microservice.auth.service;

import com.microservice.auth.dto.UserDTO;
import com.microservice.auth.model.UserEntity;
import com.microservice.auth.repository.UserRepository;
import com.microservice.auth.validation.UserValidation;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserValidation userValidation;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, UserValidation userValidation) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.userValidation = userValidation;
    }

    public Page<UserEntity> findAllUsers(int page, int size) {
        userValidation.validatePageAndSize(page, size);
        return userRepository.findAll(PageRequest.of(page, size));
    }

    public UserEntity findUserById(UUID idUser) {
        userValidation.validateUserExists(idUser);
        return userRepository.findByIdUser(idUser);
    }

    public UserEntity getUserByEmail(String email) {
        userValidation.validateUserExistsByEmail(userRepository, email);
        return userRepository.findByEmail(email);
    }

    public Page<UserEntity> getUsersByRole(String role, int page, int size) {
        userValidation.validatePageAndSize(page, size);
        return userRepository.findAllUserEntitiesByRoleContainingIgnoreCase(role, PageRequest.of(page, size));
    }

    public Page<UserEntity> getUsersByEnabled(boolean enabled, int page, int size) {
        userValidation.validatePageAndSize(page, size);
        return userRepository.findAllUserEntitiesByEnabled(enabled, PageRequest.of(page, size));
    }

    @Transactional
    public void registerUser(UserDTO registerUserDTO) {
        userValidation.validateUserRegister(registerUserDTO);
        UserEntity userEntity = convertToEntity(registerUserDTO);
        userRepository.save(userEntity);
    }

    @Transactional
    public void changeRoleToAdmin(String email) {
        userValidation.validateUserExistsByEmail(userRepository, email);
        userValidation.validateIfUserIsAdmin(email);
        userRepository.changeRoleToAdmin(email);
    }

    @Transactional
    public UserEntity updateUser(String email, UserDTO userDTO) {
        userValidation.validateUserExistsByEmail(userRepository, email);
        UserEntity existingUser = userRepository.findByEmail(email);
        modelMapper.map(userDTO, existingUser);
        return userRepository.save(existingUser);
    }

    @Transactional
    public UserEntity disableUserAccount(String email) {
        userValidation.validateUserExistsByEmail(userRepository, email);
        UserEntity existingUser = userRepository.findByEmail(email);
        existingUser.setEnabled(false);
        return userRepository.save(existingUser);
    }

    private UserEntity convertToEntity(UserDTO registerUserDTO) {
        UserEntity userEntity = modelMapper.map(registerUserDTO, UserEntity.class);
        userEntity.setRole(UserEntity.Role.USER);
        userEntity.setEnabled(true);
        return userEntity;
    }
}
