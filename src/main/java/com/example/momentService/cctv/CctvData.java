package com.example.momentService.cctv;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CctvData {
    private String cctvData;
    private String description;
}
