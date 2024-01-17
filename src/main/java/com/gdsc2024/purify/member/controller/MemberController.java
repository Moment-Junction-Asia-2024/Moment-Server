package com.gdsc2024.purify.member.controller;

import com.gdsc2024.purify.common.dto.Message;
import com.gdsc2024.purify.handler.CustomException;
import com.gdsc2024.purify.handler.StatusCode;
import com.gdsc2024.purify.member.dto.MemberReqSignInDto;
import com.gdsc2024.purify.member.dto.MemberReqSignUpDto;
import com.gdsc2024.purify.member.service.MemberService;
import com.gdsc2024.purify.security.dto.Token;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RestController
@RequestMapping("/member")
@Log4j2
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/signin/")
    public ResponseEntity<Message> login(@Valid @RequestBody MemberReqSignInDto reqSignInDto, BindingResult bindingResult, @RequestHeader("User-Agent") String userAgent) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        }
        Token token = memberService.getToken(reqSignInDto, userAgent, passwordEncoder);
        return ResponseEntity.ok(new Message(StatusCode.OK, token));
    }

    @PostMapping("/api/signup/")
    public ResponseEntity<Message> signup(@Valid @RequestBody MemberReqSignUpDto reqSignUpDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        }
        reqSignUpDto.encodePassword(passwordEncoder);
        memberService.saveMemberInfo(reqSignUpDto);
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }


    @GetMapping("/admin")
    public ResponseEntity<Message> admin() {
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    @GetMapping("/simulator")
    public ResponseEntity<Message> simulator() {
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    @GetMapping("/viewer")
    public ResponseEntity<Message> viewer() {
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    @GetMapping("/logout")
    public ResponseEntity<Message> logout() {
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

}
