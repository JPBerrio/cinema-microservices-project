package com.microservice.auth.service;

import com.microservice.auth.model.UserEntity;
import com.microservice.auth.repository.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        UserEntity user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User" + email + "not found");
        }

        String roles = user.getRole().toString();

        System.out.println("User role: " + user.getRole());

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(AuthorityUtils.createAuthorityList(roles))
                .build();
    }
}
