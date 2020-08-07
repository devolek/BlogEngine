package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.auth.request.ChangePasswordRequest;
import com.devolek.blogengine.main.dto.auth.request.LoginRequest;
import com.devolek.blogengine.main.dto.auth.request.SignupRequest;
import com.devolek.blogengine.main.dto.profile.request.EditProfileRequest;
import com.devolek.blogengine.main.dto.universal.Response;
import com.devolek.blogengine.main.model.User;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UserService {

    Response register(SignupRequest signUpRequest);

    User findByEmail(String email);

    User findById(int id);

    void delete(int id);

    Response logout(String sessionId);

    Response checkAuth(HttpServletRequest httpServletRequest, String sessionId);

    Response login(LoginRequest request, String sessionId);

    Response passwordRecovery(HttpServletRequest request, String email);

    Response changePassword(ChangePasswordRequest request);

    Response editProfile(int userId, EditProfileRequest request) throws IOException;

    Response getMyStatistic(int userId);
}
