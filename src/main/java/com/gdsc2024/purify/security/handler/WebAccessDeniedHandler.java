package com.gdsc2024.purify.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc2024.purify.handler.StatusCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WebAccessDeniedHandler implements AccessDeniedHandler{ // 접근 거부 시 실행될 로직
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 현재 요청에 관한 정보, 응답에 대한 조작 가능한 객체, 발생한 접근 거부 예외
        StatusCode statusCode = StatusCode.FORBIDDEN;

        Map<String , Object> json = new HashMap<>();
        json.put("statusCode", statusCode.getStatusCode());
        json.put("message", statusCode.getMessage());

        response.setContentType("application/json");
        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);

        new ObjectMapper().writeValue(response.getWriter(), responseEntity.getBody());
    }
}
