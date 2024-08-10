package com.example.momentService.city;

import com.example.momentService.common.dto.Message;
import com.example.momentService.handler.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/city")
public class CityController {
    private final CityDataManager cityDataManager;

    @GetMapping("/")
    public ResponseEntity<Message> searchCityDataInDateRange() throws IOException {
        String dateTime1 = "2024-06-2";
        String dateTime2 = "2024-06-4";
        return ResponseEntity.ok(new Message(StatusCode.OK, cityDataManager.findMobileDataByTime(dateTime1,dateTime2)));
    }
}
