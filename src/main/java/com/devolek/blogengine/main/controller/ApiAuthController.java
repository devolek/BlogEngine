package com.devolek.blogengine.main.controller;

import com.devolek.blogengine.main.dto.auth.request.ChangePasswordRequest;
import com.devolek.blogengine.main.dto.auth.request.LoginRequest;
import com.devolek.blogengine.main.dto.auth.request.RestorePasswordRequest;
import com.devolek.blogengine.main.dto.auth.request.SignupRequest;
import com.devolek.blogengine.main.dto.auth.response.LoginResponse;
import com.devolek.blogengine.main.dto.universal.ErrorResponse;
import com.devolek.blogengine.main.dto.universal.Response;
import com.devolek.blogengine.main.service.AuthService;
import com.devolek.blogengine.main.service.CaptchaService;
import com.devolek.blogengine.main.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final UserService userService;
    private final CaptchaService captchaService;
    private final AuthService authService;

    public ApiAuthController(UserService userService, CaptchaService captchaService, AuthService authService) {
        this.userService = userService;
        this.captchaService = captchaService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(HttpServletResponse response,
                                              @Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(new LoginResponse(true, authService.login(loginRequest, response)));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        Response result = authService.register(signUpRequest);
        return result instanceof ErrorResponse ?
                ResponseEntity.badRequest().body(result) : ResponseEntity.ok(result);
    }

    @GetMapping("/captcha")
    public ResponseEntity<?> getCaptcha() throws IOException {
        return ResponseEntity.ok(captchaService.getCaptcha());
    }

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
