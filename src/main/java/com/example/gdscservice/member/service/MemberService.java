package com.example.gdscservice.member.service;

import com.example.gdscservice.member.dto.ReqSignInDto;
import com.example.gdscservice.member.dto.ReqSignUpDto;
import com.example.gdscservice.security.dto.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface MemberService {
    Token getToken(ReqSignInDto reqSignInDto, String userAgent, PasswordEncoder passwordEncoder);
    void saveMemberInfo(ReqSignUpDto reqSignUpDto);
}
