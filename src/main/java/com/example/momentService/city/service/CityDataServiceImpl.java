package com.example.momentService.city.service;

import com.example.momentService.city.CityData;
import com.example.momentService.city.repository.CityDataRepository;
import com.example.momentService.kafka.KafkaService;
import com.example.momentService.kafka.dto.Answer;
import com.example.momentService.kafka.dto.Event;
import com.example.momentService.kafka.dto.EventTitle;
import com.example.momentService.kafka.dto.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityDataServiceImpl implements CityDataService {
    @Value("${city.instruction}")
    private String cityInstruction;

    private final ObjectMapper mapper;
    private final CityDataRepository cityDataRepository;
    private final KafkaService kafkaService;

    public CityData getMobileDataByTime(String dateTime1, String dateTime2) throws Exception {
        kafkaService.sendToKafka(
            Answer.builder()
                .id(1L)
                .event(
                    Event.builder()
                        .userId(1L)
                        .eventTitle(EventTitle.CITYSCAPE)
                        .status(Status.RUNNING)
                        .content("Your complaint has been received.")
                        .build()
                ).build()
        );
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