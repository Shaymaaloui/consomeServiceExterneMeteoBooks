package com.soa.webservices.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le titre est obligatoire")
    private String title;
    
    @NotBlank(message = "L'auteur est obligatoire")
    private String author;
    
    @NotBlank(message = "L'ISBN est obligatoire")
    @Column(unique = true)
    private String isbn;
    
    @NotNull(message = "Le prix est obligatoire")
    private Double price;
    
    private String description;
    
    @Column(name = "publication_year")
    private Integer publicationYear;
    
    // Constructeurs
    public Book() {}
    
    public Book(Long id, String title, String author, String isbn, Double price, String description, Integer publicationYear) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.description = description;
        this.publicationYear = publicationYear;
    }
    
    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public Double getPrice() { return price; }
    public String getDescription() { return description; }
    public Integer getPublicationYear() { return publicationYear; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setPrice(Double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }
    
    // toString, equals, hashCode
    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', author='" + author + "', isbn='" + isbn + "'}";
    }
}
