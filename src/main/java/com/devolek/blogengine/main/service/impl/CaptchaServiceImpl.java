package com.devolek.blogengine.main.service.impl;

import com.devolek.blogengine.main.dto.captha.response.CaptchaResponse;
import com.devolek.blogengine.main.dto.universal.Response;
import com.devolek.blogengine.main.model.CaptchaCode;
import com.devolek.blogengine.main.repo.CaptchaCodesRepository;
import com.devolek.blogengine.main.service.CaptchaService;
import com.devolek.blogengine.main.util.CaptchaGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class CaptchaServiceImpl implements CaptchaService {
    private final CaptchaCodesRepository captchaCodesRepository;
    @Value("${captcha.expirationMs}")
    private long captchaExpirationMs;

    public CaptchaServiceImpl(CaptchaCodesRepository captchaCodesRepository) {
        this.captchaCodesRepository = captchaCodesRepository;
    }

    @Override
    public Response getCaptcha() throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Calendar.getInstance().getTime().getTime() - captchaExpirationMs);
        List<CaptchaCode> captchaCodes = captchaCodesRepository.findExpiredCaptcha(calendar);
        if (captchaCodes.size() > 0) {
            captchaCodesRepository.deleteAll(captchaCodes);
        }

        Map<String, String> map = CaptchaGenerator.getCaptcha();
        String code = map.get("secret"); //отображаемый код
        String secret = UUID.randomUUID().toString() + Base64.getEncoder().encodeToString(code.getBytes()); //уникальный код
        String image = "data:image/png;base64," + map.get("image");
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setCode(code);
        captchaCode.setSecretCode(secret);
        captchaCodesRepository.save(captchaCode);
        return new CaptchaResponse(secret, image);
    }

    @Override
    public boolean validateCaptcha(String code, String secret) {
        CaptchaCode captchaCode = captchaCodesRepository.findCaptchaCodeBySecretCode(secret);
        return captchaCode != null && captchaCode.getCode().equals(code);
    }
}
