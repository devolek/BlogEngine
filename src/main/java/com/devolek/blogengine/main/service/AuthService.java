package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.auth.request.LoginRequest;
import com.devolek.blogengine.main.dto.auth.request.SignupRequest;
import com.devolek.blogengine.main.dto.universal.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    Response register(SignupRequest signUpRequest);

    Response logout(HttpServletResponse response);

    Response checkAuth(HttpServletRequest httpServletRequest);

    Response login(LoginRequest request, HttpServletResponse response);
}
