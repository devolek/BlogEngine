package com.devolek.blogengine.main.security;

import com.devolek.blogengine.main.model.User;
import com.devolek.blogengine.main.service.dao.UserDao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;

    public UserDetailsServiceImpl(UserDao userDao) {

        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User person = this.userDao.findByEmail(email);
        if (person == null) {
            throw new UsernameNotFoundException("User with ID " + email + " not found");
        }

        return UserDetailsImpl.build(person);
    }
}
