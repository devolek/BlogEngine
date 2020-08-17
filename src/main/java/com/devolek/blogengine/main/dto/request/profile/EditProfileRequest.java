package com.devolek.blogengine.main.dto.request.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EditProfileRequest {
    private Integer removePhoto;
    private String name;
    private String email;
    private String password;
}
