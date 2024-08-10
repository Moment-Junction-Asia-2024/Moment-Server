package com.example.momentService.action;

import com.example.momentService.cctv.CctvJsonDto;
import com.example.momentService.cctv.service.CctvService;
import com.example.momentService.kafka.KafkaService;
import com.example.momentService.kafka.dto.Answer;
import com.example.momentService.kafka.dto.Event;
import com.example.momentService.kafka.dto.EventTitle;
import com.example.momentService.kafka.dto.Status;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ActionManager {
    private final ActionInstructor actionInstructor;
    private final WebClient webClient;
    private final ObjectMapper mapper;
    private final CctvService cctvService;
    private final KafkaService kafkaService;

    @Value("${openai.api.key}")
    private String openApiKey;

    public List<?> apiExtractor(String prompt) throws Exception {
        List<CctvJsonDto> cctvJsonDtoList = new ArrayList<>();
        JsonNode jsonNode = mapper.readTree(getChatGptResponse(actionInstructor.promptBuilder(prompt)));
        String result = jsonNode.path("choices").get(0).path("message").path("content").asText();
        ActionResultDto actionResultDto = mapper.readValue(result, ActionResultDto.class);

        kafkaService.sendToKafka(
                Answer.builder()
                        .id(1L)
                        .event(
                                Event.builder()
                                    .userId(1L)
                                    .eventTitle(EventTitle.MISSING)
                                    .status(Status.RUNNING)
                                    .content("Your complaint has been received.")
                                    .build()
                        ).build()
        );

        if (actionResultDto.getApi().equals("/action/cctv/find")) {
            cctvService.analyzeCctvData(actionResultDto.getParameter().get(0), actionResultDto.getParameter().get(1));
            return cctvJsonDtoList;
        }

        if (actionResultDto.getApi().equals("/action/mobile-data/find")) {
            // 여기서 crack 시나리오
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
