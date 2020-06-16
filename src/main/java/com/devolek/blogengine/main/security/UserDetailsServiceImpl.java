package com.devolek.blogengine.main.security;

import com.devolek.blogengine.main.model.User;
import com.devolek.blogengine.main.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User person = this.userService.findByEmail(email);
        if (person == null) {
            throw new UsernameNotFoundException("User with ID " + email + " not found");
        }

        return UserDetailsImpl.build(person);
    }
}
