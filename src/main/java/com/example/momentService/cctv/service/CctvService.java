package com.example.momentService.cctv.service;

import com.example.momentService.cctv.CctvJsonDto;
import com.example.momentService.city.CityData;

import java.io.IOException;
import java.util.List;

public interface CctvService {
    void analyzeCctvData(String location, String characteristic) throws Exception;
    List<CctvJsonDto> getCctvJsonDtoList(String location, String characteristic) throws IOException;
}
