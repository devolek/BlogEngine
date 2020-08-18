package com.devolek.blogengine.main.controller;

import com.devolek.blogengine.main.dto.request.auth.ChangePasswordRequest;
import com.devolek.blogengine.main.dto.request.auth.LoginRequest;
import com.devolek.blogengine.main.dto.request.auth.RestorePasswordRequest;
import com.devolek.blogengine.main.dto.request.auth.SignupRequest;
import com.devolek.blogengine.main.dto.response.View;
import com.devolek.blogengine.main.service.AuthService;
import com.devolek.blogengine.main.service.CaptchaService;
import com.devolek.blogengine.main.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final UserService userService;
    private final CaptchaService captchaService;
    private final AuthService authService;

    @JsonView(View.USER_FULL_INFO.class)
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(HttpServletResponse response,
                                              @Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest, response));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        return ResponseEntity.ok(authService.register(signUpRequest));
    }

    @GetMapping("/captcha")
    public ResponseEntity<?> getCaptcha() throws IOException {
        return ResponseEntity.ok(captchaService.getCaptcha());
    }

    @JsonView(View.USER_FULL_INFO.class)
    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(authService.checkAuth(httpServletRequest));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        return ResponseEntity.ok(authService.logout(response));
    }

    @PostMapping("/restore")
    public ResponseEntity<?> restorePassword(HttpServletRequest httpServletRequest, @RequestBody RestorePasswordRequest request){
        return ResponseEntity.ok(userService.passwordRecovery(httpServletRequest, request.getEmail()));
    }

    @PostMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request){
        return ResponseEntity.ok(userService.changePassword(request));
    }
}
