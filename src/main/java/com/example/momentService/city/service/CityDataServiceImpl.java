package com.example.momentService.city.service;


import com.example.momentService.city.CityData;
import com.example.momentService.city.CrackJsonDto;
import com.example.momentService.city.repository.CityDataRepository;
import com.example.momentService.kafka.KafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Service
public class CityDataServiceImpl implements CityDataService {
    @Value("${city.instruction}")
    private String cityInstruction;
    @Value("${openai.api.key}")
    private String openApiKey;

    private final ObjectMapper mapper;
    private final CityDataRepository cityDataRepository;
    private final WebClient gptWebClient;
    private final WebClient fastWebClient;
    private final KafkaService kafkaService;

    public CityDataServiceImpl(ObjectMapper mapper, CityDataRepository cityDataRepository, @Qualifier("gptWebClient") WebClient gptWebClient, @Qualifier("fastWebClient") WebClient fastWebClient, KafkaService kafkaService) {
        this.mapper = mapper;
        this.cityDataRepository = cityDataRepository;
        this.gptWebClient = gptWebClient;
        this.fastWebClient = fastWebClient;
        this.kafkaService = kafkaService;
    }

    public CrackJsonDto getMobileData() throws IOException {
        StringBuilder content = new StringBuilder();
        cityDataRepository.findUniqueMobileDataJsonDtoByDateRange()
                .stream()
                .forEach(uniMobData -> {
                    try {
                        content.append(mapper.writeValueAsString(uniMobData)).append("\n");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });


        CityData cityData = CityData.builder()
                .cityData(content.isEmpty() ? null : content.toString())
                .description(content.isEmpty() ? "There is no data available for the specified time range." : cityInstruction)
                .build();
        String gptResponse = getChatGptResponse(mapper.writeValueAsString(cityData));
        JsonNode jsonNode = mapper.readTree(gptResponse);

        String crackInformation = jsonNode.path("choices").get(0).path("message").path("content").asText();


        return mapper.readValue(crackInformation, CrackJsonDto.class);

    }

    public String getChatGptResponse(String userMessage) {

        ChatGptRequest request = new ChatGptRequest("gpt-4o", userMessage);

        return gptWebClient.post()
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