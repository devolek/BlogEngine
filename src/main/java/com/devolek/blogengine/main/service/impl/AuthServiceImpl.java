package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.request.auth.LoginRequest;
import com.devolek.blogengine.main.dto.request.auth.SignupRequest;
import com.devolek.blogengine.main.dto.response.auth.LoginResponse;
import com.devolek.blogengine.main.dto.response.universal.ErrorResponse;
import com.devolek.blogengine.main.dto.response.universal.Response;
import com.devolek.blogengine.main.dto.response.universal.UniversalResponseFactory;
import com.devolek.blogengine.main.dto.response.user.UserResponseFactory;
import com.devolek.blogengine.main.enums.ERole;
import com.devolek.blogengine.main.enums.ModerationStatus;
import com.devolek.blogengine.main.model.Role;
import com.devolek.blogengine.main.model.User;
import com.devolek.blogengine.main.repo.PostRepository;
import com.devolek.blogengine.main.repo.RoleRepository;
import com.devolek.blogengine.main.security.jwt.JwtAuthenticationException;
import com.devolek.blogengine.main.security.jwt.JwtTokenProvider;
import com.devolek.blogengine.main.service.AuthService;
import com.devolek.blogengine.main.service.CaptchaService;
import com.devolek.blogengine.main.service.dao.GlobalSettingsDao;
import com.devolek.blogengine.main.service.dao.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final CaptchaService captchaService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserDao userDao;
    private final PostRepository postRepository;
    private final GlobalSettingsDao globalSettingsDao;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${user.password.minLength}")
    private int passwordMinLength;
    @Value("${jwt.token.cookieName}")
    private String jwtTokenCookieName;

    @Override
    public Response register(SignupRequest signUpRequest) {
        Map<String, String> errors = new HashMap<>();
        if (!globalSettingsDao.getSettings().get("MULTIUSER_MODE")) {
            errors.put("multiuser mode", "В данный момент регистрация на сайте не доступна");
            return new ErrorResponse(errors);
        }
        if (userDao.existsByName(signUpRequest.getName())) {
            errors.put("name", "Имя уже существует, попробуйте другое");
        }

        if (userDao.existsByEmail(signUpRequest.getEmail())) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        if (!captchaService.validateCaptcha(signUpRequest.getCaptcha(), signUpRequest.getCaptchaSecret())) {
            errors.put("captcha", "Код с картинки введён неверно");
        }

        if (signUpRequest.getPassword().length() < passwordMinLength) {
            errors.put("password", "Пароль короче " + passwordMinLength + "-ти символов");
        }

        if (errors.size() != 0) {
            return new ErrorResponse(errors);
        }

        // Create new user's account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        Role roleUser = roleRepository.findByRole(ERole.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(roleUser));
        user.setIsModerator(0);
        userDao.save(user);
        log.info("IN register - user: {} successfully registered", user.getEmail());
        return UniversalResponseFactory.getTrueResponse();
    }

    @Override
    public Response logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(jwtTokenCookieName, null);
        cookie.setPath("/");
        response.addCookie(cookie);
        return UniversalResponseFactory.getTrueResponse();
    }

    @Override
    public Response checkAuth(HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                User user = userDao.findByEmail(jwtTokenProvider.getEmail(token));
                int moderationCount = user.getIsModerator() == 1 ?
                        postRepository.getCountModeration(ModerationStatus.NEW, null) : 0;
                return UserResponseFactory.getAuthenticateUserResponse(user, moderationCount);
            }
            return UniversalResponseFactory.getFalseResponse();
        } catch (JwtAuthenticationException e) {
            return UniversalResponseFactory.getFalseResponse();
        }
    }

    @Override
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        String email = request.getEmail();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));

            User user = userDao.findByEmail(email);
            String token = jwtTokenProvider.createToken(email);
            Cookie cookie = new Cookie(jwtTokenCookieName, token);
            cookie.setPath("/");
            response.addCookie(cookie);

            int moderationCount = user.getIsModerator() == 1 ?
                    postRepository.getCountModeration(ModerationStatus.NEW, null) : 0;

            return UserResponseFactory.getAuthenticateUserResponse(user, moderationCount);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password for user " + email);
        }
    }
}
