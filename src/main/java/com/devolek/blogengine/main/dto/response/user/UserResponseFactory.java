package com.devolek.blogengine.main.dto.response.user;

import com.devolek.blogengine.main.dto.response.auth.LoginResponse;
import com.devolek.blogengine.main.model.User;

public class UserResponseFactory {

    public static LoginResponse getAuthenticateUserResponse(User user, int moderationCount){

        return new LoginResponse(true, getUserDto(user, moderationCount));
    }

    public static UserDto getUserDto(User user, int moderationCount){
        return new UserDto(user.getId(), user.getName(), user.getPhoto(), user.getEmail(),
                user.getIsModerator() == 1, moderationCount, user.getIsModerator() == 1);
    }
}
