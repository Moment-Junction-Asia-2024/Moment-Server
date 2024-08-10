package com.example.momentService.kafka.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Event {
    private Long userId;
    private EventTitle eventTitle;
    private Status status;
    private String content;
    private String next;
}