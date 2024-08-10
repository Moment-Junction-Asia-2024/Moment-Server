package com.example.momentService.city.service;

import com.example.momentService.city.CityData;

import java.io.IOException;

public interface CityDataService {
    CityData findMobileDataByTime(String dateTime1, String dateTime2) throws IOException;
}
