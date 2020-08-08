package com.devolek.blogengine.main.service;

import com.devolek.blogengine.main.dto.auth.request.ChangePasswordRequest;
import com.devolek.blogengine.main.dto.profile.request.EditProfileRequest;
import com.devolek.blogengine.main.dto.universal.Response;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UserService {

    Response passwordRecovery(HttpServletRequest request, String email);

    Response changePassword(ChangePasswordRequest request);

    Response editProfile(int userId, EditProfileRequest request) throws IOException;

    Response getMyStatistic(int userId);
}
