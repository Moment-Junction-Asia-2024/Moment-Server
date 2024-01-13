package com.example.gdscservice.member.service;

import com.example.gdscservice.handler.CustomException;
import com.example.gdscservice.handler.StatusCode;
import com.example.gdscservice.member.dto.ReqSignInDto;
import com.example.gdscservice.member.dto.ReqSignUpDto;
import com.example.gdscservice.member.entity.Member;
import com.example.gdscservice.member.repository.MemberRepository;
import com.example.gdscservice.security.JwtCreator;
import com.example.gdscservice.security.dto.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final JwtCreator jwtCreator;


    @Override
    public Token getToken(ReqSignInDto reqSignInDto, String userAgent, PasswordEncoder passwordEncoder) {
        Member member = memberRepository.findByEmail(reqSignInDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.USERNAME_NOT_FOUND));
        if(!passwordEncoder.matches(reqSignInDto.getPassword(), member.getPassword()))
            throw new CustomException(StatusCode.INVALID_PASSWORD);
        Token token = jwtCreator.createToken(member);
        return token;
    }
    @Override
    public void saveMemberInfo(ReqSignUpDto reqSignUpDto) {
        memberRepository.findByEmail(reqSignUpDto.getEmail()).ifPresent(
                memberInfo -> { throw new CustomException(StatusCode.REGISTERED_EMAIL); });
        memberRepository.findByPhoneNum(reqSignUpDto.getPhoneNum()).ifPresent(
                memberInfo -> { throw new CustomException(StatusCode.REGISTERED_EMAIL); });
        Member member = reqSignUpDto.toEntity();

        memberRepository.save(member);
    }
}
