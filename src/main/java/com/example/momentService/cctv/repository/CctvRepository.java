package com.example.momentService.cctv.repository;

import com.example.momentService.cctv.CctvJsonDto;

import java.io.IOException;
import java.util.List;

public interface CctvRepository {
    List<CctvJsonDto> findCctvJsonDtoByLocation(String location) throws IOException;
}
