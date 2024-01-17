package com.gdsc2024.purify.security;

import com.gdsc2024.purify.security.dto.AuthorizerDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtInfoExtractor { // jwt 에서 정보 추출 & 현재 사용자의 권한 정보 반환
    @Value("${jwt.access.key}")
    private String accessKey;

    public Claims extractToken(String token) { // 전달받은 토큰에서 클레임 정보 추출하여 반환
        // jwt token signature 확인
        return Jwts.parser().setSigningKey(accessKey.getBytes())
                .parseClaimsJws(token) // parseClaimsJwt -> parseClaimsJws 수정 (UnsupportedJwtException 방지)
                .getBody();
    }

    public static AuthorizerDto getAuthorizer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 사용자 인증 정보 가져옴
        if (authentication instanceof JwtAuthenticationToken) { // 사용자가 jwt token 으로 인증된 경우만 처리
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Claims claims = jwtToken.getClaims();

            return AuthorizerDto.builder()
                    .memberId(Long.parseLong(claims.get(AuthorizerDto.ClaimName.ID.getValue()).toString()))
                    .name(claims.get(AuthorizerDto.ClaimName.NAME.getValue()).toString())
                    .role((String) claims.get(AuthorizerDto.ClaimName.ROLE.getValue()))
                    .build();
        }

        return null;
    }
}
