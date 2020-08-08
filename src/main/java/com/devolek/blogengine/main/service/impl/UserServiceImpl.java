package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.auth.request.ChangePasswordRequest;
import com.devolek.blogengine.main.dto.profile.request.EditProfileRequest;
import com.devolek.blogengine.main.dto.profile.request.EditProfileWithPhotoRequest;
import com.devolek.blogengine.main.dto.profile.response.MyStatisticResponse;
import com.devolek.blogengine.main.dto.universal.ErrorResponse;
import com.devolek.blogengine.main.dto.universal.FalseResponse;
import com.devolek.blogengine.main.dto.universal.OkResponse;
import com.devolek.blogengine.main.dto.universal.Response;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.User;
import com.devolek.blogengine.main.service.EmailService;
import com.devolek.blogengine.main.service.ImageService;
import com.devolek.blogengine.main.service.UserService;
import com.devolek.blogengine.main.service.dao.UserDao;
import com.devolek.blogengine.main.util.CodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final CaptchaServiceImpl captchaService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ImageService imageService;

    @Autowired
    public UserServiceImpl(UserDao userDao, CaptchaServiceImpl captchaService,
                           PasswordEncoder passwordEncoder, EmailService emailService, ImageService imageService) {
        this.userDao = userDao;
        this.captchaService = captchaService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.imageService = imageService;
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
        if (user == null) {
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
        if (!user.getName().equals(request.getName()) && userDao.existsByName(request.getName())) {
            errors.put("name", "Имя указано неверно");
        }

        if (!user.getEmail().equals(request.getEmail()) && userDao.existsByEmail(request.getEmail())) {
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
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRemovePhoto() != null && request.getRemovePhoto() == 1) {
            user.setPhoto("");
        }
        if (request instanceof EditProfileWithPhotoRequest) {
            user.setPhoto(imageService.saveImage(((EditProfileWithPhotoRequest) request).getPhoto(), 36));
        }
        userDao.save(user);

        return new OkResponse();
    }

    @Override
    public Response getMyStatistic(int userId) {
        User user = userDao.findById(userId);
        List<Post> posts = user.getPosts();
        return getStatisticResponse(posts);
    }

    public static Response getStatisticResponse(List<Post> posts) {
        if (posts == null || posts.size() == 0) {
            return new MyStatisticResponse(0, 0, 0, 0, Calendar.getInstance());
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
        return new MyStatisticResponse(postsCount, likesCount, dislikesCount, viewsCount, firstPublication);
    }
}
