package com.example.momentService.city;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniqueMobileDataJsonDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("classname")
    private String className;

    @JsonProperty("regionname_1")
    private String regionName1;

    @JsonProperty("regionname_2")
    private String regionName2;

    @JsonProperty("regionname_3")
    private String regionName3;

    @JsonProperty("time")
    private String time;
}