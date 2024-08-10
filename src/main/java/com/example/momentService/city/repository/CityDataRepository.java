package com.example.momentService.city.repository;

import com.example.momentService.city.UniqueMobileDataJsonDto;

import java.io.IOException;
import java.util.List;

public interface CityDataRepository {
    List<UniqueMobileDataJsonDto> findUniqueMobileDataJsonDtoByDateRange(String dateTime1, String dateTime2) throws IOException;
}
