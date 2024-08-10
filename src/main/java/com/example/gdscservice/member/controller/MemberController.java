package com.example.gdscservice.member.controller;

import com.example.gdscservice.common.dto.Message;
import com.example.gdscservice.handler.CustomException;
import com.example.gdscservice.handler.StatusCode;
import com.example.gdscservice.member.dto.ReqSignInDto;
import com.example.gdscservice.member.dto.ReqSignUpDto;
import com.example.gdscservice.member.service.MemberService;
import com.example.gdscservice.security.dto.AuthorizerDto;
import com.example.gdscservice.security.dto.Token;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.example.gdscservice.security.JwtInfoExtractor.getAuthorizer;


@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    @PostMapping("/signin")
    public ResponseEntity<Message> login(@Valid @RequestBody ReqSignInDto reqSignInDto, BindingResult bindingResult, @RequestHeader("User-Agent") String userAgent) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        Token token = memberService.getToken(reqSignInDto, userAgent, passwordEncoder);
        return ResponseEntity.ok(new Message(StatusCode.OK, token));
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<Message> signup(@Valid @RequestBody ReqSignUpDto reqSignUpDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new CustomException(StatusCode.INVALID_DATA_FORMAT);
        reqSignUpDto.encodePassword(passwordEncoder);

        memberService.saveMemberInfo(reqSignUpDto);
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }
}
