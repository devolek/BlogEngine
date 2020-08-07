package com.devolek.blogengine.main.dto.profile.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EditProfileWithoutPhotoRequest extends EditProfileRequest{
    private String photo;
}
