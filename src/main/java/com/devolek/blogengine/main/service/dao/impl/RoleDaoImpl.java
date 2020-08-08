package com.devolek.blogengine.main.service.dao.impl;

import com.devolek.blogengine.main.enums.ERole;
import com.devolek.blogengine.main.model.Role;
import com.devolek.blogengine.main.repo.RoleRepository;
import com.devolek.blogengine.main.service.dao.RoleDao;
import org.springframework.stereotype.Service;

@Service
public class RoleDaoImpl implements RoleDao {
    private final RoleRepository roleRepository;

    public RoleDaoImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByRole(ERole role) {
        return roleRepository.findByRole(role);
    }
}
