package com.devolek.blogengine.main.controller;

import com.devolek.blogengine.main.dto.auth.request.ChangePasswordRequest;
import com.devolek.blogengine.main.dto.auth.request.LoginRequest;
import com.devolek.blogengine.main.dto.auth.request.RestorePasswordRequest;
import com.devolek.blogengine.main.dto.auth.request.SignupRequest;
import com.devolek.blogengine.main.dto.auth.response.LoginResponse;
import com.devolek.blogengine.main.dto.universal.ErrorResponse;
import com.devolek.blogengine.main.dto.universal.Response;
import com.devolek.blogengine.main.service.CaptchaService;
import com.devolek.blogengine.main.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final UserService userService;
    private final CaptchaService captchaService;

    public ApiAuthController(UserService userService, CaptchaService captchaService) {
        this.userService = userService;
        this.captchaService = captchaService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestHeader(name = "Cookie") String sessionId,
                                              @Valid @RequestBody LoginRequest loginRequest) {

        Response userResponse = userService.login(loginRequest, sessionId);

        return ResponseEntity.ok(new LoginResponse(true, userResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        Response result = userService.register(signUpRequest);
        return result instanceof ErrorResponse ?
                ResponseEntity.badRequest().body(result) : ResponseEntity.ok(result);
    }

    @GetMapping("/captcha")
    public ResponseEntity<?> getCaptcha() throws IOException {
        return ResponseEntity.ok(captchaService.getCaptcha());
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(@RequestHeader(name = "Cookie", required = false) String sessionId, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(userService.checkAuth(httpServletRequest, sessionId));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Cookie") String sessionId) {
        return ResponseEntity.ok(userService.logout(sessionId));
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
