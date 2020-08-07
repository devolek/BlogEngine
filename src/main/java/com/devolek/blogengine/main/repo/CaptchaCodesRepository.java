package com.devolek.blogengine.main.repo;

import com.devolek.blogengine.main.model.CaptchaCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Calendar;
import java.util.List;

public interface CaptchaCodesRepository extends CrudRepository<CaptchaCode, Integer> {
    @Query(value = "select c from CaptchaCode c where " +
            "c.time <= ?1")
    List<CaptchaCode> findExpiredCaptcha(Calendar date);

    CaptchaCode findCaptchaCodeBySecretCode(String secretCode);
}
