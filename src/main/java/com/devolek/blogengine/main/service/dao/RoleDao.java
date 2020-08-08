package com.devolek.blogengine.main.service.dao;

import com.devolek.blogengine.main.enums.ERole;
import com.devolek.blogengine.main.model.Role;

public interface RoleDao {
    Role findByRole(ERole role);
}
