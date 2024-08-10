package com.example.momentService.cctv;

import com.example.momentService.cctv.service.CctvService;
import com.example.momentService.common.dto.Message;
import com.example.momentService.handler.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/cctv")
@RequiredArgsConstructor
public class CctvController {
    private final CctvService cctvService;

    @GetMapping("/")
    public ResponseEntity<Message> searchCctvInformation() throws IOException {
        cctvService.getCctvJsonDtoList("남구 지곡동");
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }
}
