package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.request.auth.LoginRequest;
import com.devolek.blogengine.main.dto.request.auth.SignupRequest;
import com.devolek.blogengine.main.dto.response.auth.LoginResponse;
import com.devolek.blogengine.main.dto.response.universal.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    Response register(SignupRequest signUpRequest);

    Response logout(HttpServletResponse response);

    Response checkAuth(HttpServletRequest httpServletRequest);

    LoginResponse login(LoginRequest request, HttpServletResponse response);
}
