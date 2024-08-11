package com.example.momentService.cctv.service;

import com.example.momentService.cctv.CctvData;
import com.example.momentService.cctv.CctvJsonDto;
import com.example.momentService.cctv.repository.CctvRepository;
import com.example.momentService.kafka.KafkaService;
import com.example.momentService.kafka.dto.Answer;
import com.example.momentService.kafka.dto.Event;
import com.example.momentService.kafka.dto.EventTitle;
import com.example.momentService.kafka.dto.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CctvServiceImpl implements CctvService {
    private final CctvRepository cctvRepository;
    private final ObjectMapper mapper;
    private final KafkaService kafkaService;
    private final WebClient fastWebClient;
    private final WebClient gptWebClient;

    @Value("${openai.api.key}")
    private String openApiKey;
    @Value("${cctv.instruction}")
    private String cctvInstruction;
    @Value("${cctv.cctvFileBasePath}")
    private String cctvFileBasePath;

    @Override
    public void analyzeCctvData(String location, String characteristic) throws Exception {
        List<CctvJsonDto> cctvCandidates = getCctvJsonDtoList(location, characteristic);
        kafkaService.sendToKafka(
            Answer.builder()
                .id(1L)
                .event(
                    Event.builder()
                        .userId(1L)
                        .eventTitle(EventTitle.MISSING)
                        .status(Status.RUNNING)
                        .content("CCTV information has been found.")
                        .next("Analyzing the CCTV data.")
                        .build()
                ).build()
        );
        // 여기에서 AI server에 분석 요청
        String analysisResult = "";
        kafkaService.sendToKafka(
            Answer.builder()
                .id(1L)
                .event(
                    Event.builder()
                        .userId(1L)
                        .eventTitle(EventTitle.MISSING)
                        .status(Status.RUNNING)
                        .content("CCTV data has been analyzed.")
                        .next("Providing the analysis result.")
                        .build()
                ).build()
        );
        // 분석 결과 정보로 GPT를 이용해 응답 생성
        String gptResponse = getChatGptResponse(analysisResult);
        kafkaService.sendToKafka(
            Answer.builder()
                .id(1L)
                .event(
                    Event.builder()
                        .userId(1L)
                        .eventTitle(EventTitle.MISSING)
                        .status(Status.FINISHED)
                        .content("Here is the report of the CCTV data analysis.\n" + gptResponse)
                        .build()
                ).build()
        );
    }

    public CctvServiceImpl(CctvRepository cctvRepository, ObjectMapper mapper, KafkaService kafkaService, @Qualifier("gptWebClient") WebClient gptWebClient, @Qualifier("fastWebClient") WebClient fastWebClient) {
        this.cctvRepository = cctvRepository;
        this.mapper = mapper;
        this.kafkaService = kafkaService;
        this.fastWebClient = fastWebClient;
        this.gptWebClient = gptWebClient;
    }


    @Override
    public List<CctvJsonDto> getCctvJsonDtoList(String location, String characteristic) throws IOException {
        StringBuilder content = new StringBuilder();
        List<CctvJsonDto> targetCctvList;
        List<CctvJsonDto> findCctvList = new ArrayList<>();

        List<CctvJsonDto> cctvCandidates = cctvRepository.findCctvJsonDtoByLocation(location)
                .stream()
                .filter(cctvJsonDto -> {
                    String roadAddr = cctvJsonDto.getRoadAddr();
                    String streetAddr = cctvJsonDto.getStreetAddr();
                    return (roadAddr != null && roadAddr.contains(location)) || (streetAddr != null && streetAddr.contains(location));
                })
                .map(cctvJsonDto -> {
                    try {
                        content.append(mapper.writeValueAsString(cctvJsonDto));
                        return cctvJsonDto;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
        if (!content.isEmpty()) {
            CctvData cctvData = CctvData.builder()
                    .cctvData(content.toString())
                    .description(cctvInstruction)
                    .build();
            String gptResponse = getChatGptResponse(mapper.writeValueAsString(cctvData));
            JsonNode jsonNode = mapper.readTree(gptResponse);

            String targetCctvIdListString = jsonNode.path("choices").get(0).path("message").path("content").asText();
            targetCctvList = mapper.readValue(targetCctvIdListString, new TypeReference<List<CctvJsonDto>>() {
            });


            for (int i = 1; i < 4; i++) {
                String folderName = "cctv" + i;
                Path folderPath = Paths.get(cctvFileBasePath, folderName);
                List<File> imageFiles = Files.list(folderPath)
                        .filter(Files::isRegularFile)
                        .map(Path::toFile)
                        .collect(Collectors.toList());

                MultipartBodyBuilder builder = new MultipartBodyBuilder();
                builder.part("name", folderName);
                builder.part("object_list", List.of(characteristic));

                for (File imageFile : imageFiles) {
                    builder.part("files", new FileSystemResource(imageFile));
                }

                String response = fastWebClient.post()
                        .uri("/video/recognition")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .bodyValue(builder.build())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                JsonNode rootNode = mapper.readTree(response);

                JsonNode objectProbsNode = rootNode.path("object_probs");
                List<Double> objectProbs = new ArrayList<>();
                if (objectProbsNode.isArray()) {
                    for (JsonNode probNode : objectProbsNode) {
                        if (probNode.isArray() && probNode.size() > 0) {
                            objectProbs.add(probNode.get(0).asDouble());
                        }
                    }
                }

                List<Double> confidences = objectProbs.stream()
                        .filter(a -> a > 0.85)
                        .toList();
                if (confidences.size() > 3) {
                    findCctvList.add(targetCctvList.get(i-1));
                }
            }

            return findCctvList;
        }
        return null;
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
        private List<Message> messages;

        public ChatGptRequest(String model, String prompt) {
            this.model = model;
            this.messages = List.of(
                    new Message("user", prompt)
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
