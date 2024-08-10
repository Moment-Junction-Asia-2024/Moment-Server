package com.example.momentService.cctv;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CctvJsonDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("agency")
    private String agency;

    @JsonProperty("road_addr")
    private String roadAddr;

    @JsonProperty("street_addr")
    private String streetAddr;

    @JsonProperty("objective")
    private String objective;

    @JsonProperty("cnt")
    private Integer cnt;

    @JsonProperty("shoot_direction")
    private String shootDirection;

    @JsonProperty("install_time")
    private String installTime;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;
}