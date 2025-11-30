package com.soa.webservices.client;

import com.soa.webservices.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Client pour consommer notre propre API REST de livres
 * Démontre la consommation de notre service
 */
@Component
public class BookClient {
    
    private final WebClient webClient;
    private static final String BASE_URL = "http://localhost:8080/api/books";
    
    @Autowired
    public BookClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }
    
    /**
     * Récupère tous les livres via l'API REST
     */
    public List<Book> getAllBooks() {
        return webClient.get()
                .uri("")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Book>>() {})
                .block();
    }
    
    /**
     * Récupère un livre par son ID via l'API REST
     */
    public Book getBookById(Long id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Book.class)
                .block();
    }
    
    /**
     * Crée un nouveau livre via l'API REST
     */
    public Book createBook(Book book) {
        return webClient.post()
                .uri("")
                .bodyValue(book)
                .retrieve()
                .bodyToMono(Book.class)
                .block();
    }
    
    /**
     * Recherche des livres par auteur via l'API REST
     */
    public List<Book> searchByAuthor(String author) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/author")
                        .queryParam("author", author)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Book>>() {})
                .block();
    }
}
