package com.soa.webservices.controller;

import com.soa.webservices.model.WeatherResponse;
import com.soa.webservices.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
@Tag(name = "Weather", description = "API météo (consomme un service externe)")
public class WeatherController {
    
    @Autowired
    private WeatherService weatherService;
    
    @GetMapping("/test")
    @Operation(summary = "Test endpoint météo (données simulées)")
    public ResponseEntity<Map<String, Object>> testWeather() {
        Map<String, Object> response = new HashMap<>();
        response.put("city", "Tunis (Test)");
        response.put("latitude", 36.8065);
        response.put("longitude", 10.1815);
        
        Map<String, Object> currentWeather = new HashMap<>();
        currentWeather.put("temperature", "18.5°C");
        currentWeather.put("windSpeed", "12.3 km/h");
        currentWeather.put("time", "2025-11-30T00:20:00");
        currentWeather.put("description", "Ciel dégagé");
        
        response.put("currentWeather", currentWeather);
        response.put("status", "OK - Endpoint de test fonctionnel");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Obtenir la météo par coordonnées")
    public ResponseEntity<WeatherResponse> getWeather(
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        WeatherResponse weather = weatherService.getWeatherByCoordinates(latitude, longitude);
        return ResponseEntity.ok(weather);
    }
    
    @GetMapping("/paris")
    @Operation(summary = "Obtenir la météo à Paris")
    public ResponseEntity<Map<String, Object>> getWeatherForParis() {
        WeatherResponse weather = weatherService.getWeatherForParis();
        return ResponseEntity.ok(formatWeatherResponse(weather, "Paris"));
    }
    
    @GetMapping("/tunis")
    @Operation(summary = "Obtenir la météo à Tunis")
    public ResponseEntity<Map<String, Object>> getWeatherForTunis() {
        WeatherResponse weather = weatherService.getWeatherForTunis();
        return ResponseEntity.ok(formatWeatherResponse(weather, "Tunis"));
    }
    
    private Map<String, Object> formatWeatherResponse(WeatherResponse weather, String city) {
        Map<String, Object> response = new HashMap<>();
        response.put("city", city);
        response.put("latitude", weather.getLatitude());
        response.put("longitude", weather.getLongitude());
        
        if (weather.getCurrentWeather() != null) {
            Map<String, Object> currentWeather = new HashMap<>();
            currentWeather.put("temperature", weather.getCurrentWeather().getTemperature() + "°C");
            currentWeather.put("windSpeed", weather.getCurrentWeather().getWindspeed() + " km/h");
            currentWeather.put("time", weather.getCurrentWeather().getTime());
            currentWeather.put("description", 
                weatherService.getWeatherDescription(weather.getCurrentWeather().getWeathercode()));
            
            response.put("currentWeather", currentWeather);
        }
        
        return response;
    }
}
