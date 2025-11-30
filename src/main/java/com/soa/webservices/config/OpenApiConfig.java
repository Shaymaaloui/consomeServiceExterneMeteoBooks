package com.soa.webservices.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Services Web - Projet SOA")
                        .version("1.0.0")
                        .description("""
                                # Projet de Services Web
                                
                                Ce projet démontre :
                                
                                1. **Développement d'un service web REST** : API de gestion de livres
                                2. **Consommation de notre propre service** : BookClient consomme l'API Books
                                3. **Consommation d'un service externe** : WeatherClient consomme l'API Open-Meteo
                                
                                ## Endpoints principaux
                                
                                - **/api/books** : CRUD complet pour la gestion des livres
                                - **/api/weather** : Consultation de la météo via API externe
                                - **/api/demo** : Démonstrations de consommation de services
                                
                                ## Technologies utilisées
                                
                                - Spring Boot 3
                                - Spring Data JPA
                                - H2 Database
                                - WebClient (pour consommer les APIs)
                                - OpenAPI/Swagger
                                """)
                        .contact(new Contact()
                                .name("Projet SOA")
                                .email("student@university.tn")));
    }
}
