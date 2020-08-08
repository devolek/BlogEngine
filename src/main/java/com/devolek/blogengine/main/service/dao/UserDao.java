package com.devolek.blogengine.main.service.dao;

import com.devolek.blogengine.main.model.User;
import java.util.List;

public interface UserDao {
    List<User> getAll();

    User findByEmail(String email);

    User findById(int id);

    User findByCode(String code);

    boolean existsByName (String name);

    boolean existsByEmail (String email);

    void delete(int id);

    void save(User user);
}
