package com.example.momentService.cctv.repository;

import com.example.momentService.cctv.CctvJsonDto;
import com.example.momentService.city.UniqueMobileDataJsonDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JsonCctvRepository implements CctvRepository{
    @Value("${cctv.cctvLoc}")
    private String cctvLoc;

    private final ObjectMapper mapper;
    @Override
    public List<CctvJsonDto> findCctvJsonDtoByLocation(String location) throws IOException {
        Resource resource = new ClassPathResource(cctvLoc);
        return mapper.readValue(
                resource.getInputStream(),
                new TypeReference<List<CctvJsonDto>>() {
                }
        );
    }
}
