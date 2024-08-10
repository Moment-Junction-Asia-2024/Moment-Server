package com.example.momentService.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ActionInstructor {

    @Value("${action.role}")
    private String role;
    @Value("${action.context}")
    private String context;
    @Value("${action.openAiSpecLoc}")
    private String openAiSpecLoc;
    @Value("${action.instruction}")
    private String instruction;

    private final ObjectMapper mapper;

    public String promptBuilder(String userContext) throws IOException {
        String openAiSpec = loadOpenAiSpec();
        Prompt prompt = new Prompt(role, context, openAiSpec, instruction, userContext);
        return mapper.writeValueAsString(prompt);
    }

    private String loadOpenAiSpec() throws IOException {
        var resource = new ClassPathResource(openAiSpecLoc);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    @AllArgsConstructor
    @Getter
    private static class Prompt {
        private final String role;
        private final String context;
        private final String openAiSpec;
        private final String instruction;
        private final String user;
    }
}
