package com.example.gdscservice.cityData;

import com.example.gdscservice.cityData.dto.CityData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CityDataController {
    private final CityDataRepository cityDataRepository;

    public CityData getMobileData(String dateTime1, String dateTime2) {
        return cityDataRepository.findMobileDataByTime(dateTime1, dateTime2);
    }
}