package com.devolek.blogengine.main.dto.user;

import com.devolek.blogengine.main.dto.user.response.UserDto;
import com.devolek.blogengine.main.dto.user.response.UserResponse;
import com.devolek.blogengine.main.dto.user.response.UserWithPhotoDto;
import com.devolek.blogengine.main.model.User;

public class UserResponseFactory {
    public static UserWithPhotoDto getUserWithPhoto(User user){
        return new UserWithPhotoDto(user.getId(), user.getName(), user.getPhoto());
    }

    public static UserResponse getAuthenticateUserResponse(User user, int moderationCount){
        return new UserResponse(user.getId(), user.getName(), user.getPhoto(), user.getEmail(),
                user.getIsModerator() == 1, moderationCount, user.getIsModerator() == 1);
    }

    public static UserDto getUserDto(User user){
        return new UserDto(user.getId(), user.getName());
    }
}
