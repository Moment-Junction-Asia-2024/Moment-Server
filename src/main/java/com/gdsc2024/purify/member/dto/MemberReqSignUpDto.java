package com.gdsc2024.purify.member.dto;

import com.gdsc2024.purify.member.domain.Member;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
public class MemberReqSignUpDto {
    @Email(message = "Invalid email format")
    private String email;
    private String name;
    private String password;
    private String role;

    @Builder
    public MemberReqSignUpDto(String email, String name, String password, String role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
    public Member toEntity() {
        return Member.builder()
                .email(this.email)
                .name(this.name)
                .password(this.password)
                .role(this.role)
                .build();
    }

}
