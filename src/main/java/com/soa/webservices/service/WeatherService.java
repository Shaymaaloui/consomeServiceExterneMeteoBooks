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
        try {
            return weatherClient.getWeather(latitude, longitude);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à l'API météo pour coordonnées " + latitude + "," + longitude + ": " + e.getMessage());
            System.err.println("Utilisation du mode offline avec données simulées");
            // Mode offline : retourner des données simulées
            return createMockWeatherResponse(latitude, longitude, 15.0, 10.0, 2);
        }
    }
    
    
    public WeatherResponse getWeatherForTunis() {
        try {
            return weatherClient.getWeatherForTunis();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à l'API météo pour Tunis: " + e.getMessage());
            System.err.println("Utilisation du mode offline avec données simulées");
            // Mode offline : retourner des données simulées
            return createMockWeatherResponse(36.8065, 10.1815, 18.5, 12.3, 0);
        }
    }
    
    public WeatherResponse getWeatherForParis() {
        try {
            return weatherClient.getWeatherForParis();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à l'API météo pour Paris: " + e.getMessage());
            System.err.println("Utilisation du mode offline avec données simulées");
            // Mode offline : retourner des données simulées
            return createMockWeatherResponse(48.8566, 2.3522, 12.0, 15.5, 1);
        }
    }
    
    private WeatherResponse createMockWeatherResponse(Double lat, Double lon, Double temp, Double wind, Integer weatherCode) {
        WeatherResponse response = new WeatherResponse();
        response.setLatitude(lat);
        response.setLongitude(lon);
        response.setTimezone("CET");
        response.setTimezoneAbbreviation("CET");
        response.setElevation(0.0);
        
        WeatherResponse.CurrentWeather current = new WeatherResponse.CurrentWeather();
        current.setTemperature(temp);
        current.setWindspeed(wind);
        current.setWinddirection(45);
        current.setWeathercode(weatherCode);
        current.setTime(java.time.LocalDateTime.now().toString());
        current.setIsDay(1);
        
        response.setCurrentWeather(current);
        return response;
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
