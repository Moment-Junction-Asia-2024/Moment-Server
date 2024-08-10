package com.example.momentService.kafka.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Answer {
    private Long id;
    private Event event;
}
