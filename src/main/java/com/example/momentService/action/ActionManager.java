package com.example.momentService.action;

import com.example.momentService.cctv.CctvJsonDto;
import com.example.momentService.cctv.service.CctvService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ActionManager {
    private final ActionInstructor actionInstructor;
    private final WebClient webClient;
    private final ObjectMapper mapper;
    private final CctvService cctvService;

    @Value("${openai.api.key}")
    private String openApiKey;

    public List<?> apiExtractor(String prompt) throws IOException {
        List<CctvJsonDto> cctvJsonDtoList = new ArrayList<>();
        JsonNode jsonNode = mapper.readTree(getChatGptResponse(actionInstructor.promptBuilder(prompt)));
        String result = jsonNode.path("choices").get(0).path("message").path("content").asText();
        ActionResultDto actionResultDto = mapper.readValue(result, ActionResultDto.class);

        if (actionResultDto.getApi().equals("/action/cctv/find")) {
            cctvJsonDtoList = cctvService.getCctvJsonDtoList(actionResultDto.getParameter().get(0), actionResultDto.getParameter().get(1));
            return cctvJsonDtoList;
        }
        return null;

    }

    public String getChatGptResponse(String userMessage) {

        ChatGptRequest request = new ChatGptRequest("gpt-4o", userMessage);

        return webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + openApiKey)
                .body(Mono.just(request), ChatGptRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Data
    private static class ChatGptRequest {
        private String model;
        private List<ChatGptRequest.Message> messages;

        public ChatGptRequest(String model, String prompt) {
            this.model = model;
            this.messages = List.of(
                    new ChatGptRequest.Message("user", prompt)
            );
        }

        @Data
        @AllArgsConstructor
        private static class Message {
            private String role;
            private String content;
        }
    }
}
