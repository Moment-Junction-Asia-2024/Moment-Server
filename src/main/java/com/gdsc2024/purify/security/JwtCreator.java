package com.gdsc2024.purify.security;

import com.gdsc2024.purify.security.dto.AuthorizerDto;
import com.gdsc2024.purify.security.dto.Token;
import com.gdsc2024.purify.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtCreator {
    @Value("${jwt.access.key}")
    private String accessSecretKey;

    @Value("${jwt.access.validTime}")
    private long accessTokenValidTime;

    @PostConstruct
    protected void init() { // Base64로 인코딩
        accessSecretKey = Base64.getEncoder().encodeToString(accessSecretKey.getBytes());
    }

    public Token createToken(Member member) { // userPk = email
        Claims claims = Jwts.claims().setSubject(member.getEmail()); // jwt payload 저장 단위
        Date currentDate = Date.from(Instant.now()); // jwt 생성 시 동적으로 시간 설정
        String accessToken = getToken(member, claims, currentDate, accessTokenValidTime, accessSecretKey);
        return Token.builder()
                .accessToken(accessToken)
                .key(member.getEmail())
                .build();
    }

    private String getToken(Member member, Claims claims, Date currentDate, long tokenValidTime, String secretKey) {
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .claim(AuthorizerDto.ClaimName.ID.getValue(), String.valueOf(member.getMemberId()))
                .claim(AuthorizerDto.ClaimName.NAME.getValue(), member.getName())
                .claim(AuthorizerDto.ClaimName.ROLE.getValue(), member.getRole())
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘
                .compact();
    }
}
