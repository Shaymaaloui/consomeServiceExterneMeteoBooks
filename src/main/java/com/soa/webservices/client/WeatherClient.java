package com.soa.webservices.client;

import com.soa.webservices.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Client pour consommer l'API météo gratuite Open-Meteo
 * Démontre la consommation d'un service web externe
 */
@Component
public class WeatherClient {
    
    private final WebClient webClient;
    
    @Value("${weather.api.url}")
    private String weatherApiUrl;
    
    @Autowired
    public WeatherClient(WebClient.Builder webClientBuilder) {
        // Créer un WebClient dédié pour l'API externe avec timeout augmenté
        this.webClient = WebClient.builder()
                .baseUrl("https://api.open-meteo.com")
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();
    }
    
    /**
     * Récupère les données météo pour une localisation donnée
     * @param latitude Latitude de la localisation
     * @param longitude Longitude de la localisation
     * @return Données météo actuelles
     */
    public WeatherResponse getWeather(Double latitude, Double longitude) {
        try {
            String url = String.format("https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true", 
                latitude, longitude);
            System.out.println("Appel API météo: " + url);
            
            WeatherResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/forecast")
                            .queryParam("latitude", latitude)
                            .queryParam("longitude", longitude)
                            .queryParam("current_weather", true)
                            .build())
                    .retrieve()
                    .bodyToMono(WeatherResponse.class)
                    .block();
            
            System.out.println("Réponse API météo reçue: " + (response != null ? "OK" : "NULL"));
            return response;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à l'API météo: " + e.getClass().getName() + " - " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'appel à l'API Open-Meteo", e);
        }
    }
    
    /**
     * Récupère la météo pour Paris (exemple)
     */
    public WeatherResponse getWeatherForParis() {
        return getWeather(48.8566, 2.3522); // Coordonnées de Paris
    }
    
    /**
     * Récupère la météo pour Tunis (exemple)
     */
    public WeatherResponse getWeatherForTunis() {
        return getWeather(36.8065, 10.1815); // Coordonnées de Tunis
    }
}
