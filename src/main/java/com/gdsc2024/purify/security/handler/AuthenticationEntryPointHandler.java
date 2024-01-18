package com.gdsc2024.purify.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc2024.purify.handler.StatusCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequestMapping
// Spring security setting 에서 해당 AuthenticationEntryPoint 등록 필요
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint { // 인증되지 않고 보호된 리소스에 접근 시도 시 호출

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");

        if (exception == null || exception.equals("NullPointerException")) { // 사용자 미인증
            setResponse(response, StatusCode.UNAUTHORIZED); // 401
            return;
        }

        if (exception.equals("MalformedJwtException")) { // jwt token 형식 잘못된 경우
            setResponse(response, StatusCode.MALFORMED); // 400
            return;
        }

        if (exception.equals("PasswordNotFoundException")) {
            setResponse(response, StatusCode.PASSWORD_NOT_FOUND); // 401
            return;
        }

        if (exception.equals("ForbiddenException")) { // 접근 금지
            setResponse(response, StatusCode.FORBIDDEN); // 403
            return;
        }

        if (exception.equals("ExpiredJwtException")) {
            setResponse(response, StatusCode.EXPIRED_JWT); // 401
            return;
        }
    }

    private void setResponse(HttpServletResponse response, StatusCode statusCode) throws IOException {
        Map<String , Object> json = new HashMap<>();
        json.put("statusCode", statusCode.getStatusCode());
        json.put("message", statusCode.getMessage());

        response.setContentType("application/json");
        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);

        response.getWriter().print(new ObjectMapper().writeValueAsString(json)); // json 응답 생성 후 클라이언트에게 전송
    }
}
