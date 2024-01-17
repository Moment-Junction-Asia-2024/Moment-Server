package com.gdsc2024.purify.security;

import io.jsonwebtoken.*;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtValidator { // 토큰 유효성 검사
    @Value("${jwt.access.key}")
    private String accessKey;

    public boolean validateToken(ServletRequest request, String jwtToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(accessKey.getBytes()).parseClaimsJws(jwtToken);
            if (claimsJws.getBody().getExpiration() == null) { return true; }
        } catch (SignatureException e) {
            request.setAttribute("exception", "ForbiddenException");
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            request.setAttribute("exception", e.getClass().getSimpleName());
        }
        return false; // invalid
    }
}