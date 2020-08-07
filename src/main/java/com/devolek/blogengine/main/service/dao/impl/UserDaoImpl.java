package com.devolek.blogengine.main.service.dao.impl;

import com.devolek.blogengine.main.model.User;
import com.devolek.blogengine.main.repo.UserRepository;
import com.devolek.blogengine.main.service.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserDaoImpl implements UserDao {
    private final UserRepository userRepository;

    public UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        List<User> result = (List<User>) userRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    public User findByEmail(String email) {
        User result = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        log.info("IN findByUsername - user: {} found by email: {}", result, email);
        return result;
    }

    @Override
    public User findById(int id) {
        User result = userRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }

        log.info("IN findById - user: {} found by id: {}", result, id);
        return result;
    }

    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted", id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
        log.info("IN save - user with id: {} successfully saved", user.getId());
    }

    @Override
    public User findByCode(String code) {
        User user = userRepository.findByCode(code);
        if (user == null){
            log.warn("IN findByCode - no user found by code: {}", code);
            return null;
        }
        return user;
    }
}
