package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.request.auth.ChangePasswordRequest;
import com.devolek.blogengine.main.dto.request.profile.EditProfileRequest;
import com.devolek.blogengine.main.dto.request.profile.EditProfileWithPhotoRequest;
import com.devolek.blogengine.main.dto.response.profile.StatisticResponse;
import com.devolek.blogengine.main.dto.response.universal.ErrorResponse;
import com.devolek.blogengine.main.dto.response.universal.Response;
import com.devolek.blogengine.main.dto.response.universal.UniversalResponseFactory;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.User;
import com.devolek.blogengine.main.security.jwt.JwtTokenProvider;
import com.devolek.blogengine.main.service.EmailService;
import com.devolek.blogengine.main.service.ImageService;
import com.devolek.blogengine.main.service.UserService;
import com.devolek.blogengine.main.service.dao.UserDao;
import com.devolek.blogengine.main.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final CaptchaServiceImpl captchaService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ImageService imageService;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${user.photo.width}")
    private int userPhotoWidth;
    @Value("${user.password.minLength}")
    private int passwordMinLength;
    @Value("${project.link.changePassword}")
    private String changePasswordLink;
    @Value("${project.link.expiredMessage}")
    private String expiredMessage;
    @Value("${jwt.token.cookieName}")
    private String jwtTokenCookieName;

    public static StatisticResponse getStatisticResponse(List<Post> posts) {
        if (posts == null || posts.size() == 0) {
            return new StatisticResponse(0,
                    0,
                    0,
                    0,
                    Calendar.getInstance());
        }
        int postsCount = 0;
        int likesCount = 0;
        int dislikesCount = 0;
        int viewsCount = 0;
        Calendar firstPublication = Calendar.getInstance();
        for (Post post : posts) {
            postsCount += 1;
            likesCount += (int) post.getPostVotes().stream().filter(postVote -> postVote.getValue() == 1).count();
            dislikesCount += (int) post.getPostVotes().stream().filter(postVote -> postVote.getValue() == 0).count();
            viewsCount += post.getViewCount();
            firstPublication = post.getTime().before(firstPublication) ? post.getTime() : firstPublication;
        }
        return new StatisticResponse(postsCount,
                likesCount,
                dislikesCount,
                viewsCount,
                firstPublication);
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
                            changePasswordLink + code);
            log.info("User {} requested password recovery, confirmation email was sent", email);
            return UniversalResponseFactory.getTrueResponse();
        } catch (UsernameNotFoundException e) {
            return UniversalResponseFactory.getFalseResponse();
        }
    }

    @Override
    public Response changePassword(ChangePasswordRequest request) {
        Map<String, String> errors = new HashMap<>();
        User user = userDao.findByCode(request.getCode());
        if (user == null) {
            errors.put("code", expiredMessage);
        }
        if (!captchaService.validateCaptcha(request.getCaptcha(), request.getCaptchaSecret())) {
            errors.put("captcha", "Код с картинки введён неверно");
        }

        if (request.getPassword().length() < passwordMinLength) {
            errors.put("password", "Пароль короче " + passwordMinLength + "-ти символов");
        }
        if (errors.size() != 0) {
            return new ErrorResponse(errors);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userDao.save(user);
        return UniversalResponseFactory.getTrueResponse();
    }

    @Override
    public Response editProfile(int userId, EditProfileRequest request, HttpServletResponse response) throws IOException {
        User user = userDao.findById(userId);
        Map<String, String> errors = new HashMap<>();
        if (!user.getName().equals(request.getName()) && userDao.existsByName(request.getName())) {
            errors.put("name", "Имя уже существует, попробуйте другое");
        }

        if (!user.getEmail().equals(request.getEmail()) && userDao.existsByEmail(request.getEmail())) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }

        if (request.getPassword() != null && request.getPassword().length() < passwordMinLength) {
            errors.put("password", "Пароль короче " + passwordMinLength + "-ти символов");
        }

        if (errors.size() != 0) {
            return new ErrorResponse(errors);
        }


        if (!request.getEmail().equals(user.getEmail())) {
            user.setEmail(request.getEmail());
            String token = jwtTokenProvider.createToken(user.getEmail());
            Cookie cookie = new Cookie(jwtTokenCookieName, token);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        user.setName(request.getName());
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRemovePhoto() != null && request.getRemovePhoto() == 1) {
            user.setPhoto("");
        }
        if (request instanceof EditProfileWithPhotoRequest) {
            user.setPhoto(imageService.saveImage(((EditProfileWithPhotoRequest) request).getPhoto(), userPhotoWidth));
        }

        userDao.save(user);
        return UniversalResponseFactory.getTrueResponse();
    }

    @Override
    public StatisticResponse getMyStatistic(int userId) {
        User user = userDao.findById(userId);
        List<Post> posts = user.getPosts();
        return getStatisticResponse(posts);
    }
}
