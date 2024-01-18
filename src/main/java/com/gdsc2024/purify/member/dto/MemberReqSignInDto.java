package com.gdsc2024.purify.member.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberReqSignInDto {

    @Email(message = "Invalid email format")
    private String email;
    private String password;

    public MemberReqSignInDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
