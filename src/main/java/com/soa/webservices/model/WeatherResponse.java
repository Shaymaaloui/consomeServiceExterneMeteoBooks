package com.soa.webservices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Modèle pour la réponse de l'API météo Open-Meteo
 */
@Data
public class WeatherResponse {
    
    private Double latitude;
    private Double longitude;
    
    @JsonProperty("generationtime_ms")
    private Double generationtimeMs;
    
    @JsonProperty("utc_offset_seconds")
    private Integer utcOffsetSeconds;
    
    private String timezone;
    
    @JsonProperty("timezone_abbreviation")
    private String timezoneAbbreviation;
    
    private Double elevation;
    
    @JsonProperty("current_weather")
    private CurrentWeather currentWeather;
    
    @Data
    public static class CurrentWeather {
        private String time;
        private Integer interval;
        private Double temperature;
        private Double windspeed;
        private Integer winddirection;
        
        @JsonProperty("is_day")
        private Integer isDay;
        
        private Integer weathercode;
    }
}
