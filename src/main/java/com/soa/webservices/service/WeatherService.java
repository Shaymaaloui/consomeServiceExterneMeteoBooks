package com.soa.webservices.service;

import com.soa.webservices.client.WeatherClient;
import com.soa.webservices.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    
    @Autowired
    private WeatherClient weatherClient;
    
    public WeatherResponse getWeatherByCoordinates(Double latitude, Double longitude) {
        return weatherClient.getWeather(latitude, longitude);
    }
    
    public WeatherResponse getWeatherForTunis() {
        return weatherClient.getWeatherForTunis();
    }
    
    public WeatherResponse getWeatherForParis() {
        return weatherClient.getWeatherForParis();
    }
    
    public String getWeatherDescription(Integer weatherCode) {
        if (weatherCode == null) {
            return "Inconnu";
        }
        
        return switch (weatherCode) {
            case 0 -> "Ciel dégagé";
            case 1, 2, 3 -> "Partiellement nuageux";
            case 45, 48 -> "Brouillard";
            case 51, 53, 55 -> "Bruine";
            case 61, 63, 65 -> "Pluie";
            case 71, 73, 75 -> "Neige";
            case 80, 81, 82 -> "Averses";
            case 95, 96, 99 -> "Orage";
            default -> "Conditions météo variées";
        };
    }
}
