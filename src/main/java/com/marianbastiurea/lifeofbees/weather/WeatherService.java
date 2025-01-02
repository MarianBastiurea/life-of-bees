package com.marianbastiurea.lifeofbees.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
public class WeatherService {
    @Autowired
    private RestTemplate restTemplate;

    public  WeatherData getWeatherData(LocalDate startDate) {
        String weatherApiUrl = "http://localhost:8081/api/weather/" + startDate;
        return restTemplate.getForObject(weatherApiUrl, WeatherData.class);
    }
}
