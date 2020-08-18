package com.devolek.blogengine.main.security;

import com.devolek.blogengine.main.enums.ERole;
import com.devolek.blogengine.main.model.Role;
import com.devolek.blogengine.main.model.User;
import com.devolek.blogengine.main.repo.RoleRepository;
import com.devolek.blogengine.main.security.jwt.UserDetailsFactory;
import com.devolek.blogengine.main.service.dao.UserDao;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userDao.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User with ID " + email + " not found");
        }
        if (user.getIsModerator() == 1 && !user.getRoles().contains(roleRepository.findByRole(ERole.ROLE_MODERATOR))) {
            Set<Role> roles = user.getRoles();
            roles.add(roleRepository.findByRole(ERole.ROLE_MODERATOR));
            user.setRoles(roles);
            userDao.save(user);
        } else if (user.getIsModerator() == 0 && user.getRoles().contains(roleRepository.findByRole(ERole.ROLE_MODERATOR))) {
            Set<Role> roles = user.getRoles();
            roles.clear();
            roles.add(roleRepository.findByRole(ERole.ROLE_USER));
            user.setRoles(roles);
            userDao.save(user);
        }
        return UserDetailsFactory.build(user);
    }
}
