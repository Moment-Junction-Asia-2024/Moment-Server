package com.example.momentService.city.service;

import com.example.momentService.city.CityData;

import java.io.IOException;

public interface CityDataService {
    CityData getMobileDataByTime(String dateTime1, String dateTime2) throws Exception;
}
