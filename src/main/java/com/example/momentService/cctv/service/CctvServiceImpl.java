package com.example.momentService.cctv.service;

import com.example.momentService.cctv.CctvData;
import com.example.momentService.cctv.CctvJsonDto;
import com.example.momentService.cctv.repository.CctvRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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
@RequiredArgsConstructor
public class CctvServiceImpl implements CctvService {
    private final CctvRepository cctvRepository;
    private final ObjectMapper mapper;
    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String openApiKey;
    @Value("${cctv.instruction}")
    private String cctvInstruction;
    @Value("${cctv.cctvFileBasePath}")
    private String cctvFileBasePath;

    @Override
    public List<CctvJsonDto> getCctvJsonDtoList(String location, String characteristic) throws IOException {
        StringBuilder content = new StringBuilder();
        List<CctvJsonDto> targetCctvIdList;
        List<Mono<String>> asyncReqQueue = new ArrayList<>();

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
            System.out.println(gptResponse);
            JsonNode jsonNode = mapper.readTree(gptResponse);

            String targetCctvIdListString = jsonNode.path("choices").get(0).path("message").path("content").asText();
            targetCctvIdList = mapper.readValue(targetCctvIdListString, new TypeReference<List<CctvJsonDto>>() {});

//            System.out.println(targetCctvIdList);
//            for (int i = 1; i < 15; i++) {
//                String folderName = "cctv" + i;
//                Path folderPath = Paths.get(cctvFileBasePath, folderName);
//                List<File> imageFiles = Files.list(folderPath)
//                        .filter(Files::isRegularFile)
//                        .map(Path::toFile)
//                        .collect(Collectors.toList());
//
//                MultipartBodyBuilder builder = new MultipartBodyBuilder();
//                builder.part("name", folderName);
//                for (File imageFile : imageFiles) {
//                    builder.part("file", new FileSystemResource(imageFile));
//                }
//
//                Mono<String> response = webClient.post()
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                        .bodyValue(builder.build())
//                        .retrieve()
//                        .bodyToMono(String.class);
//
//                asyncReqQueue.add(response);
//            }
//            asyncReqQueue.stream()
//                    .forEach(arq->{
//                        arq.subscribe(
//                                result -> System.out.println(" upload successful: " + result),
//                                error -> System.err.println(" upload failed: " + error.getMessage())
//                        );
//                    });
            // 여기에서

            return targetCctvIdList;
        }
        return cctvCandidates;
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
