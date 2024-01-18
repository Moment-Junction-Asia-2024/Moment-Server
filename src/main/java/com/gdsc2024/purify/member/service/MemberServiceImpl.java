package com.gdsc2024.purify.member.service;

import com.gdsc2024.purify.handler.CustomException;
import com.gdsc2024.purify.handler.StatusCode;
import com.gdsc2024.purify.security.JwtCreator;
import com.gdsc2024.purify.security.dto.Token;
import com.gdsc2024.purify.member.dto.MemberReqSignInDto;
import com.gdsc2024.purify.member.dto.MemberReqSignUpDto;
import com.gdsc2024.purify.member.domain.Member;
import com.gdsc2024.purify.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final JwtCreator jwtCreator;

    @Override
    public Token getToken(MemberReqSignInDto memberReqSignInDto, String userAgent, PasswordEncoder passwordEncoder) {
        Member member = memberRepository.findByEmail(memberReqSignInDto.getEmail()).orElseThrow(() -> new CustomException(StatusCode.USEREMAIL_NOT_FOUND));
        if (!passwordEncoder.matches(memberReqSignInDto.getPassword(), member.getPassword())) {
            throw new CustomException(StatusCode.INVALID_PASSWORD);
        }
        Token token = jwtCreator.createToken(member);
        return token;
    }

    @Override
    public void saveMemberInfo(MemberReqSignUpDto memberReqSignUpDto) {
        memberRepository.findByEmail(memberReqSignUpDto.getEmail()).ifPresent(
                memberInfo -> { throw new CustomException(StatusCode.REGISTERED_EMAIL); });
        Member member = memberReqSignUpDto.toEntity();
        memberRepository.save(member);
    }
}
