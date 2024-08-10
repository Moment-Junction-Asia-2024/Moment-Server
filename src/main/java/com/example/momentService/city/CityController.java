package com.example.momentService.city;

import com.example.momentService.city.service.CityDataServiceImpl;
import com.example.momentService.common.dto.Message;
import com.example.momentService.handler.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/city")
public class CityController {
    private final CityDataServiceImpl cityDataServiceImpl;

    @GetMapping("/")
    public ResponseEntity<Message> searchCityDataInDateRange() throws Exception {
        String dateTime1 = "2024-06-2";
        String dateTime2 = "2024-06-4";
        return ResponseEntity.ok(new Message(StatusCode.OK, cityDataServiceImpl.getMobileDataByTime(dateTime1,dateTime2)));
    }
}
