package com.example.gdscservice.cityData;

import com.example.gdscservice.cityData.dto.CityData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.Reader;

@Repository
public class CityDataRepository {
    public CityData findMobileDataByTime(String dateTime1, String dateTime2) {
        try {
            JSONParser parser = new JSONParser();
            Reader reader = new FileReader("src/main/java/com/example/gdscservice/cityData/data/unique_mobile_data.json");
            JSONArray dataArray = (JSONArray) parser.parse(reader);
            StringBuilder content = new StringBuilder();
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject element = (JSONObject) dataArray.get(i);
                Long id = (Long) element.get("id");
                Double longitude = (Double) element.get("longitude");
                Double latitude = (Double) element.get("latitude");
                String className = (String) element.get("classname");
                String regionName1 = (String) element.get("regionname_1");
                String regionName2 = (String) element.get("regionname_2");
                String regionName3 = (String) element.get("regionname_3");
                String time = (String) element.get("time");
                if (time.compareTo(dateTime1) >= 0 && time.compareTo(dateTime2) <= 0) {
                    content.append(element.toJSONString()).append("\n");
                }
            }
            String description = "Here is the data of Pohang City. The data is collected from the mobile devices." +
                    "The data includes the longitude, latitude, class name, region name, and etc.\n" +
                    "The classname is the name of the problems.:\n" +
                    "- crack: The road is cracked.\n" +
                    "- banner: The illegal banner is placed.\n" +
                    "- trash: The trash is thrown away illegally.\n" +
                    "- pothole: The pothole is on the road.\n";
            return CityData.builder()
                    .cityData(content.toString())
                    .description(description)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CityData.builder()
                .cityData(null)
                .description("There is no data.")
                .build();
    }

    public static void main(String[] args) {
        CityDataRepository repository = new CityDataRepository();
        String dateTime1 = "2024-07-20";
        String dateTime2 = "2024-07-2";

        CityData cityData = repository.findMobileDataByTime(dateTime1, dateTime2);

        if (cityData != null && cityData.getCityData() != null) {
            System.out.println("City Data:");
            System.out.println(cityData.getCityData());
            System.out.println("Description:");
            System.out.println(cityData.getDescription());
        } else {
            System.out.println("No data found.");
        }
    }
}