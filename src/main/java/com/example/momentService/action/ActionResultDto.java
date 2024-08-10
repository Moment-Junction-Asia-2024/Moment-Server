package com.example.momentService.action;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ActionResultDto {
    private String api;
    private List<String> parameter;
}
