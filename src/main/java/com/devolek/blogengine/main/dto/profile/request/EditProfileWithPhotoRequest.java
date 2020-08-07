package com.devolek.blogengine.main.dto.profile.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@EqualsAndHashCode(callSuper = true)
@Data
public class EditProfileWithPhotoRequest extends EditProfileRequest{
    private MultipartFile photo;
}
