package com.example.momentService.action;

import com.example.momentService.common.dto.Message;
import com.example.momentService.handler.StatusCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/action")
public class ActionController {
    private final ActionManager actionManager;

    @PostMapping("/")
    public ResponseEntity<Message> getPrompt(@RequestBody HashMap<String, String> userPrompt) throws Exception {
        return ResponseEntity.ok(new Message(StatusCode.OK, actionManager.apiExtractor(userPrompt.get("userPrompt"))));
    }
}
