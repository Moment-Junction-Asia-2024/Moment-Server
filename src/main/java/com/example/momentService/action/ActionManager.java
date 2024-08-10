package com.example.momentService.action;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActionManager {
    private final ActionInstructor actionInstructor;
}
