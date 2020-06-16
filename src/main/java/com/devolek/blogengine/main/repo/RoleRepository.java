package com.devolek.blogengine.main.repo;


import com.devolek.blogengine.main.enums.ERole;
import com.devolek.blogengine.main.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(ERole role);
}
