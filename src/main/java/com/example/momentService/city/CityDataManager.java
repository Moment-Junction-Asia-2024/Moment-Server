package com.example.momentService.city;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CityDataManager {
    @Value("${city.instruction}")
    private String cityInstruction;

    @Value("${city.uniqueMobileDataLoc}")
    private String uniqueMobileDataLoc;

    private final ObjectMapper mapper;

    public CityData findMobileDataByTime(String dateTime1, String dateTime2) throws IOException {
        Resource resource = new ClassPathResource(uniqueMobileDataLoc);
        List<UniqueMobileDataJsonDto> uniqueMobileDataJsonDtoList = mapper.readValue(
                resource.getInputStream(),
                new TypeReference<List<UniqueMobileDataJsonDto>>() {
                }
        );
        StringBuilder content = new StringBuilder();

        uniqueMobileDataJsonDtoList.stream()
                .filter(uniqueMobileDataJsonDto -> uniqueMobileDataJsonDto.getTime().compareTo(dateTime1) >= 0 && uniqueMobileDataJsonDto.getTime().compareTo(dateTime2) <= 0)
                .forEach(uniMobData -> {
                    try {
                        content.append(mapper.writeValueAsString(uniMobData)).append("\n");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });


        return CityData.builder()
                .cityData(content.isEmpty() ? null : content.toString())
                .description(content.isEmpty() ? "There is no data available for the specified time range." : cityInstruction)
                .build();
    }


}