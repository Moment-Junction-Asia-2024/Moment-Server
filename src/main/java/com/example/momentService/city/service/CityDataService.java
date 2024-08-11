package com.example.momentService.city.service;

import com.example.momentService.city.CityData;
import com.example.momentService.city.CrackJsonDto;

import java.io.IOException;

public interface CityDataService {
    CrackJsonDto getMobileData() throws IOException;
}
