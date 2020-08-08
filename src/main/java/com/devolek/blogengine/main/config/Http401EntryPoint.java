package com.devolek.blogengine.main.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class Http401EntryPoint implements AuthenticationEntryPoint {

    public static Http401EntryPoint unauthorizedHandler(){
        return new Http401EntryPoint();
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.warn("Auth error");
        ObjectMapper mapper = new ObjectMapper();

        response.setContentType("application/json");
        response.setStatus(401);
        response
                .getOutputStream()
                .println(mapper.writeValueAsString(""));
    }

}
