package com.example.momentService.city;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CrackJsonDto {
    @JsonProperty("count")
    private int count;

    @JsonProperty("data")
    private List<CrackDataDto> data;

    @Data
    public static class CrackDataDto {

        @JsonProperty("id")
        private int id;

        @JsonProperty("longitude")
        private double longitude;

        @JsonProperty("latitude")
        private double latitude;

        @JsonProperty("classname")
        private String classname;

        @JsonProperty("regionname_1")
        private String regionname1;

        @JsonProperty("regionname_2")
        private String regionname2;

        @JsonProperty("regionname_3")
        private String regionname3;

        @JsonProperty("time")
        private String time;
    }
}
