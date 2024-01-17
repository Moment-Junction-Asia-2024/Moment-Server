package com.gdsc2024.purify.member.service;

import com.gdsc2024.purify.security.dto.Token;
import com.gdsc2024.purify.member.dto.MemberReqSignInDto;
import com.gdsc2024.purify.member.dto.MemberReqSignUpDto;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface MemberService {
    Token getToken(MemberReqSignInDto memberReqSignInDto, String userAgent, PasswordEncoder passwordEncoder);
    void saveMemberInfo(MemberReqSignUpDto memberReqSignUpDto);
}
