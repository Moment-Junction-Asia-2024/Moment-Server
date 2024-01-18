package com.gdsc2024.purify.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthorizerDto {
    private Long memberId;
    private String name;
    private String role;

    @Builder
    public AuthorizerDto(Long memberId, String name, String role) {
        this.memberId = memberId;
        this.name = name;
        this.role = role;
    }

    @Getter
    @AllArgsConstructor
    public enum ClaimName {
        ID("ID"),
        NAME("NAME"),
        ROLE("ROLE");

        private String value;
    }
}
