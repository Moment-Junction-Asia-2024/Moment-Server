package com.example.momentService.util;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SpringConfig {

    @Value("${openai.api.base-url}")
    private String openAiBaseUrl;
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @Qualifier("gptWebClient")
    public WebClient gptWebClient(WebClient.Builder builder) {
        return builder.baseUrl(openAiBaseUrl)
                .build();
    }
    @Bean
    @Qualifier("fastWebClient")
    public WebClient fastWebClient(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8000")
                .build();
    }
}
