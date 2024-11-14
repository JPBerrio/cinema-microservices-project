package com.microservice.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_role", nullable = false)
        private Integer idRole;

        @Column(nullable = false)
        private String name;

        @OneToMany(mappedBy = "role")
        @JsonIgnore
        private List<UserEntity> userEntities;
}
