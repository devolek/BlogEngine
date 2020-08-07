package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.auth.request.ChangePasswordRequest;
import com.devolek.blogengine.main.dto.auth.request.LoginRequest;
import com.devolek.blogengine.main.dto.auth.request.SignupRequest;
import com.devolek.blogengine.main.dto.profile.request.EditProfileRequest;
import com.devolek.blogengine.main.dto.profile.request.EditProfileWithPhotoRequest;
import com.devolek.blogengine.main.dto.profile.response.MyStatisticResponse;
import com.devolek.blogengine.main.dto.universal.*;
import com.devolek.blogengine.main.dto.user.response.UserResponse;
import com.devolek.blogengine.main.enums.ERole;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.Role;
import com.devolek.blogengine.main.model.User;
import com.devolek.blogengine.main.repo.RoleRepository;
import com.devolek.blogengine.main.repo.UserRepository;
import com.devolek.blogengine.main.security.UserDetailsImpl;
import com.devolek.blogengine.main.service.EmailService;
import com.devolek.blogengine.main.service.ImageService;
import com.devolek.blogengine.main.service.UserService;
import com.devolek.blogengine.main.service.dao.UserDao;
import com.devolek.blogengine.main.util.CodeGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final CaptchaServiceImpl captchaService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final ImageService imageService;
    Map<String, Integer> session = new HashMap<>();

    @Autowired
    public UserServiceImpl(UserDao userDao, CaptchaServiceImpl captchaService, UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService, ImageService imageService) {
        this.userDao = userDao;
        this.captchaService = captchaService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.imageService = imageService;
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
        if (!captchaService.validateCaptcha(signUpRequest.getCaptcha(), signUpRequest.getCaptchaSecret())) {
            errors.put("captcha", "Код с картинки введён неверно");
        }

        if (signUpRequest.getPassword().length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
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
        user.setPhoto("/default-1.png");
        user.setRoles(Collections.singleton(roleUser));
        user.setIsModerator(0);

        log.info("IN register - user: {} successfully registered", userRepository.save(user));
        return new OkResponse();
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
        userDao.delete(id);
        log.info("IN delete - user with id: {} successfully deleted", id);
    }

    @Override
    public Response logout(String sessionId) {
        session.remove(sessionId);
        log.info("IN logout - user: {} logged out", sessionId);
        return new OkResponse();
    }


    @Override
    public Response checkAuth(HttpServletRequest httpServletRequest, String sessionId) {
        if (sessionId == null || !session.containsKey(sessionId)) {
            return new OkResponse(false);
        }
        Principal principal = httpServletRequest.getUserPrincipal();
        String email = null;
        if (principal != null) {
            email = httpServletRequest.getUserPrincipal().getName();
        }

        User user = userDao.findByEmail(email);
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

    @Override
    public Response passwordRecovery(HttpServletRequest request, String email) {
        try {
            User user = userDao.findByEmail(email);
            String code = CodeGenerator.codeGenerator();
            user.setCode(code);
            userDao.save(user);
            emailService.sendPasswordRecovery(email, user.getName(),
                    request.getScheme() + "://" + request.getServerName() +
                            ":" + request.getServerPort() +
                            "/login/change-password/" + code);
            log.info("User {} requested password recovery, confirmation email was sent", email);
            return new OkResponse();
        } catch (UsernameNotFoundException e) {
            return new FalseResponse();
        }
    }

    @Override
    public Response changePassword(ChangePasswordRequest request) {
        Map<String, String> errors = new HashMap<>();
        User user = userDao.findByCode(request.getCode());
        if(user == null){
            errors.put("code", "Ссылка для восстановления пароля устарела.\n" +
                    "\t<a href=\"/auth/restore\">Запросить ссылку снова</a>");
        }
        if (!captchaService.validateCaptcha(request.getCaptcha(), request.getCaptchaSecret())) {
            errors.put("captcha", "Код с картинки введён неверно");
        }

        if (request.getPassword().length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if (errors.size() != 0) {
            return new ErrorResponse(errors);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userDao.save(user);
        return new OkResponse();
    }

    @Override
    public Response editProfile(int userId, EditProfileRequest request) throws IOException {
        User user = userDao.findById(userId);
        Map<String, String> errors = new HashMap<>();
        if (!user.getName().equals(request.getName()) && userRepository.existsByName(request.getName())) {
            errors.put("name", "Имя указано неверно");
        }

        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }

        if (request.getPassword() != null && request.getPassword().length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }

        if (request instanceof EditProfileWithPhotoRequest &&
                ((EditProfileWithPhotoRequest) request).getPhoto().getSize() > 5242880) {
            errors.put("photo", "Фото слишком большое, нужно не более 5 Мб");
        }

        if (errors.size() != 0) {
            return new ErrorResponse(errors);
        }


        user.setEmail(request.getEmail());
        user.setName(request.getName());
        if (request.getPassword() != null){
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRemovePhoto() != null && request.getRemovePhoto() == 1){
            user.setPhoto("");
        }
        if (request instanceof EditProfileWithPhotoRequest){
            user.setPhoto(imageService.saveImage(((EditProfileWithPhotoRequest) request).getPhoto(), 36));
        }
        userDao.save(user);

        return new OkResponse();
    }

    @Override
    public Response getMyStatistic(int userId) {
        User user = userDao.findById(userId);
        List<Post> posts = user.getPosts();
        if(posts == null || posts.size() == 0){
            return new MyStatisticResponse(0,0,0,0, Calendar.getInstance());
        }
        int postsCount = 0;
        int likesCount = 0;
        int dislikesCount = 0;
        int viewsCount = 0;
        Calendar firstPublication = Calendar.getInstance();
        for (Post post : posts){
            postsCount += 1;
            likesCount += (int) post.getPostVotes().stream().filter(postVote -> postVote.getValue() == 1).count();
            dislikesCount += (int) post.getPostVotes().stream().filter(postVote -> postVote.getValue() == 0).count();
            viewsCount += post.getViewCount();
            firstPublication = post.getTime().before(firstPublication) ? post.getTime() : firstPublication;
        }
        return new MyStatisticResponse(postsCount, likesCount, dislikesCount, viewsCount, firstPublication);
    }
}
