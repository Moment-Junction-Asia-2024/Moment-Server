package com.example.momentService.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum StatusCode {
    OK(200, "OK", HttpStatus.OK),
    CREATED(201, "회원가입이 필요합니다.", HttpStatus.CREATED),
    DISABLED_OAUTH_TOKEN(403, "기간이 만료됐거나 올바르지 않은 Google OAuth 토큰입니다.", HttpStatus.FORBIDDEN),
    DORMANT_ACCOUNT(423, "이 계정은 휴먼 계정입니다.", HttpStatus.LOCKED),
    MALFORMED(400, "형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    USERNAME_NOT_FOUND(404, "가입된 이메일이 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD (400, "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_FOUND (400, "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    FORBIDDEN(403, "해당 요청에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    FORBIDDEN_CREATING_TWICE(403, "하루 하나의 다이어리만 작성가능합니다.", HttpStatus.FORBIDDEN),
    FORBIDDEN_REQUEST_TWICE(403, "이미 친구 신청을 보낸 사용자입니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED (400, "로그인 후 이용가능합니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_JWT(400, "기존 토큰이 만료되었습니다. 해당 토큰을 가지고 /token/refresh 링크로 이동 후 토큰을 재발급 받으세요.", HttpStatus.UNAUTHORIZED),
    RE_LOGIN(400, "모든 토큰이 만료되었습니다. 다시 로그인해주세요.", HttpStatus.UNAUTHORIZED),
    FAILED_SIGNUP(400, "회원가입에 실패하였습니다.", HttpStatus.BAD_REQUEST),

    REGISTERED_EMAIL(400, "등록된 회원입니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND(404, "일치하는 정보가 없습니다.", HttpStatus.NOT_FOUND),
    FAILED_REQUEST(400, "요청에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    NOT_EXIST_FILE(400, "파일이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_DATA_FORMAT(400, "형식이 맞지 않습니다.", HttpStatus.BAD_REQUEST),
    ;
    @Getter
    private int statusCode;
    @Getter
    private String message;
    @Getter
    private HttpStatus status;

    StatusCode(int statusCode, String message, HttpStatus status) {
        this.statusCode = statusCode;
        this.message = message;
        this.status = status;
    }

    public String toString() {
        return "{" +
                "\"code\" : " + "\""+ statusCode +"\"" +
                "\"status\" : " + "\""+status+"\"" +
                "\"message\" : " + "\""+message+"\"" +
                "}";
    }
}
