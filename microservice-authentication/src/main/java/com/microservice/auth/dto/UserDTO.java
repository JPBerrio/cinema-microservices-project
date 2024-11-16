package com.microservice.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String username;
    private String lastName;
    private String email;
    private String phone;
    private String password;
}
