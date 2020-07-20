package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.auth.request.LoginRequest;
import com.devolek.blogengine.main.dto.auth.request.SignupRequest;
import com.devolek.blogengine.main.dto.universal.ErrorResponse;
import com.devolek.blogengine.main.dto.universal.OkResponse;
import com.devolek.blogengine.main.dto.universal.Response;
import com.devolek.blogengine.main.dto.universal.SingleResponse;
import com.devolek.blogengine.main.dto.user.response.UserResponse;
import com.devolek.blogengine.main.enums.ERole;
import com.devolek.blogengine.main.model.Role;
import com.devolek.blogengine.main.model.User;
import com.devolek.blogengine.main.repo.RoleRepository;
import com.devolek.blogengine.main.repo.UserRepository;
import com.devolek.blogengine.main.security.UserDetailsImpl;
import com.devolek.blogengine.main.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    Map<String, Integer> session = new HashMap<>();

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Response register(SignupRequest signUpRequest) {
        Map<String, String> errors = new HashMap<>();
        if (userRepository.existsByName(signUpRequest.getName())) {
            errors.put("name", "Имя указано неверно");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        if (!signUpRequest.getCaptcha().equals(signUpRequest.getCaptchaSecret())) {
            errors.put("captcha", "Код с картинки введён неверно");
        }

        if (signUpRequest.getPassword().length() < 6){
            errors.put("password", "Пароль короче 6-ти символов");
        }

        if (errors.size() != 0){
            return new ErrorResponse(errors);
        }

        // Create new user's account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        Role roleUser = roleRepository.findByRole(ERole.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPhoto("/default-1.png");
        user.setRoles(Collections.singleton(roleUser));
        user.setIsModerator(0);

        log.info("IN register - user: {} successfully registered", userRepository.save(user));
        return new OkResponse();
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
    public Response logout(String sessionId) {
        session.remove(sessionId);
        log.info("IN logout - user: {} logged out", sessionId);
        return new OkResponse();
    }


    @Override
    public Response checkAuth(HttpServletRequest httpServletRequest, String sessionId){
        if (sessionId == null || !session.containsKey(sessionId)){
            return new OkResponse(false);
        }
        Principal principal = httpServletRequest.getUserPrincipal();
        String email = null;
        if(principal != null){
            email = httpServletRequest.getUserPrincipal().getName();
        }
        Optional<User> optional = userRepository.findByEmail(email);
        User user = new User();
        if (optional.isPresent()) {
            user = optional.get();
        }
        return new SingleResponse(new UserResponse(user.getId(), user.getName(), user.getPhoto(), user.getEmail(),
                user.getIsModerator() == 1, 1, false));
    }

    @Override
    public Response login(LoginRequest loginRequest, String sessionId) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = findByEmail(userDetails.getEmail());

        session.put(sessionId, user.getId());
        return new UserResponse(user.getId(), user.getName(), user.getPhoto(), user.getEmail(),
                user.getIsModerator() == 1, 1, false);
    }
}
