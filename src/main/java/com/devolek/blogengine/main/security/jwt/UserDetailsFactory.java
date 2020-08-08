package com.devolek.blogengine.main.security.jwt;

import com.devolek.blogengine.main.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsFactory {

    public UserDetailsFactory() {
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getRole().toString())
                ).collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}
