package com.microservice.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    private String email;
    private String password;
}
