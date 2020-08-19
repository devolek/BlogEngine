package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.request.auth.ChangePasswordRequest;
import com.devolek.blogengine.main.dto.request.profile.EditProfileRequest;
import com.devolek.blogengine.main.dto.response.profile.StatisticResponse;
import com.devolek.blogengine.main.dto.response.universal.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserService {

    Response passwordRecovery(HttpServletRequest request, String email);

    Response changePassword(ChangePasswordRequest request);

    Response editProfile(int userId, EditProfileRequest request, HttpServletResponse response) throws IOException;

    StatisticResponse getMyStatistic(int userId);
}
