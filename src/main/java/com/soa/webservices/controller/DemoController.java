package com.soa.webservices.controller;

import com.soa.webservices.client.BookClient;
import com.soa.webservices.model.Book;
import com.soa.webservices.model.WeatherResponse;
import com.soa.webservices.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur de démonstration qui combine :
 * 1. La consommation de notre propre API (BookClient)
 * 2. La consommation d'un service externe (WeatherService)
 */
@RestController
@RequestMapping("/api/demo")
@Tag(name = "Demo", description = "Démonstration de la consommation de services (propre + externe)")
public class DemoController {
    
    @Autowired
    private BookClient bookClient;
    
    @Autowired
    private WeatherService weatherService;
    
    @GetMapping("/books-client")
    @Operation(summary = "Obtenir les livres via le client REST (consommation de notre API)")
    public ResponseEntity<List<Book>> getBooksViaClient() {
        // Démontre la consommation de notre propre service REST
        List<Book> books = bookClient.getAllBooks();
        return ResponseEntity.ok(books);
    }
    
    @GetMapping("/book-client/{id}")
    @Operation(summary = "Obtenir un livre par ID via le client REST")
    public ResponseEntity<Book> getBookByIdViaClient(@PathVariable Long id) {
        // Démontre la consommation de notre propre service REST
        Book book = bookClient.getBookById(id);
        return ResponseEntity.ok(book);
    }
    
    @GetMapping("/combined")
    @Operation(summary = "Combinaison : livres (notre API) + météo (API externe)")
    public ResponseEntity<Map<String, Object>> getCombinedData() {
        // Consomme notre propre API
        List<Book> books = bookClient.getAllBooks();
        
        // Consomme un service externe
        WeatherResponse weather = weatherService.getWeatherForTunis();
        
        Map<String, Object> response = new HashMap<>();
        response.put("totalBooks", books.size());
        response.put("books", books);
        response.put("weather", Map.of(
            "location", "Tunis",
            "temperature", weather.getCurrentWeather() != null ? 
                weather.getCurrentWeather().getTemperature() + "°C" : "N/A",
            "windSpeed", weather.getCurrentWeather() != null ? 
                weather.getCurrentWeather().getWindspeed() + " km/h" : "N/A"
        ));
        response.put("message", "Démonstration de la consommation de notre API + API externe");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/create-and-verify")
    @Operation(summary = "Créer un livre et le vérifier (démo complète)")
    public ResponseEntity<Map<String, Object>> createAndVerifyBook(@RequestBody Book book) {
        // 1. Crée un livre via notre client (consomme notre API)
        Book createdBook = bookClient.createBook(book);
        
        // 2. Récupère le livre créé pour vérifier
        Book verifiedBook = bookClient.getBookById(createdBook.getId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("created", createdBook);
        response.put("verified", verifiedBook);
        response.put("message", "Livre créé et vérifié via le client REST");
        
        return ResponseEntity.ok(response);
    }
}
