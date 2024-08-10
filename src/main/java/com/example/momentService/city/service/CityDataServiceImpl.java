package com.example.momentService.city.service;

import com.example.momentService.city.CityData;
import com.example.momentService.city.repository.CityDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CityDataServiceImpl implements CityDataService {
    @Value("${city.instruction}")
    private String cityInstruction;

    private final ObjectMapper mapper;
    private final CityDataRepository cityDataRepository;

    public CityData findMobileDataByTime(String dateTime1, String dateTime2) throws IOException {
        StringBuilder content = new StringBuilder();
        cityDataRepository.findUniqueMobileDataJsonDtoByDateRange(dateTime1, dateTime2)
                .stream()
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