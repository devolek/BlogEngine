package com.devolek.blogengine.main.repo;

import com.devolek.blogengine.main.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

    User findByCode(String code);

    Boolean existsByName(String name);

    Boolean existsByEmail(String email);
}
