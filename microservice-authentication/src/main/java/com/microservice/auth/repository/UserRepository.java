package com.microservice.auth.repository;

import com.microservice.auth.dto.RegisterUserDTO;
import com.microservice.auth.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Page<UserEntity> findAll(Pageable pageable);
    Page<UserEntity> findAllUserEntitiesByRoleContainingIgnoreCase(String role, Pageable pageable);
    Page<UserEntity> findAllUserEntitiesByEnabled(boolean enabled, Pageable pageable);
    boolean existsByIdUser(UUID idUser);
    UserEntity findByIdUser(UUID idUser);
    UserEntity save(RegisterUserDTO registerUserDTO);
}
