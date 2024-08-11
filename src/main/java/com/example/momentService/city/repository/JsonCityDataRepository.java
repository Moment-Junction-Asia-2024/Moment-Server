package com.example.momentService.city.repository;

import com.example.momentService.city.UniqueMobileDataJsonDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JsonCityDataRepository implements CityDataRepository{
    @Value("${city.uniqueMobileDataLoc}")
    private String uniqueMobileDataLoc;

    private final ObjectMapper mapper;
    @Override
    public List<UniqueMobileDataJsonDto> findUniqueMobileDataJsonDtoByDateRange() throws IOException {
        Resource resource = new ClassPathResource(uniqueMobileDataLoc);
        return mapper.readValue(
                resource.getInputStream(),
                new TypeReference<List<UniqueMobileDataJsonDto>>() {
                }
        );
    }
}
