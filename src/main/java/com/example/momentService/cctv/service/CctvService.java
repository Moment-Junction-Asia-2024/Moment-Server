package com.example.momentService.cctv.service;

import com.example.momentService.cctv.CctvJsonDto;
import com.example.momentService.city.CityData;

import java.io.IOException;
import java.util.List;

public interface CctvService {
    List<CctvJsonDto> getCctvJsonDtoList(String location) throws IOException;
}
