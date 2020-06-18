package com.devolek.blogengine.main.dto.user.response;

import lombok.Data;

@Data
public class UserWithPhotoDto extends UserDto {
    private String photo;

    public UserWithPhotoDto(int id, String name, String photo) {
        super(id, name);
        this.photo = photo;
    }
}
