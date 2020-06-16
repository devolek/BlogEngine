package com.devolek.blogengine.main.controller;

import com.devolek.blogengine.main.dto.auth.request.LoginRequest;
import com.devolek.blogengine.main.dto.auth.request.SignupRequest;
import com.devolek.blogengine.main.dto.auth.response.LoginResponse;
import com.devolek.blogengine.main.dto.captha.response.CaptchaResponse;
import com.devolek.blogengine.main.dto.universal.ErrorResponse;
import com.devolek.blogengine.main.dto.universal.Response;
import com.devolek.blogengine.main.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final UserService userService;

    public ApiAuthController(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<?> getCaptcha() {
        return ResponseEntity.ok(new CaptchaResponse("0000", "0000"));
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(@RequestHeader(name = "Cookie") String sessionId, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(userService.checkAuth(httpServletRequest, sessionId));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Cookie") String sessionId) {
        return ResponseEntity.ok(userService.logout(sessionId));
    }
}
