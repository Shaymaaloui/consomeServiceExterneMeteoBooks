package com.soa.webservices.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
