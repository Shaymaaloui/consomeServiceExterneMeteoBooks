package com.soa.webservices.config;

import com.soa.webservices.model.Book;
import com.soa.webservices.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initialise la base de données avec des données de test
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Crée quelques livres de test
        Book book1 = new Book();
        book1.setTitle("Clean Code");
        book1.setAuthor("Robert C. Martin");
        book1.setIsbn("978-0132350884");
        book1.setPrice(35.99);
        book1.setDescription("Un guide pour écrire du code propre et maintenable");
        book1.setPublicationYear(2008);
        
        Book book2 = new Book();
        book2.setTitle("Design Patterns");
        book2.setAuthor("Gang of Four");
        book2.setIsbn("978-0201633610");
        book2.setPrice(54.99);
        book2.setDescription("Les patterns de conception fondamentaux");
        book2.setPublicationYear(1994);
        
        Book book3 = new Book();
        book3.setTitle("Java Concurrency in Practice");
        book3.setAuthor("Brian Goetz");
        book3.setIsbn("978-0321349606");
        book3.setPrice(45.00);
        book3.setDescription("Guide complet sur la concurrence en Java");
        book3.setPublicationYear(2006);
        
        Book book4 = new Book();
        book4.setTitle("Effective Java");
        book4.setAuthor("Joshua Bloch");
        book4.setIsbn("978-0134685991");
        book4.setPrice(49.99);
        book4.setDescription("Meilleures pratiques en Java");
        book4.setPublicationYear(2017);
        
        Book book5 = new Book();
        book5.setTitle("Spring in Action");
        book5.setAuthor("Craig Walls");
        book5.setIsbn("978-1617294945");
        book5.setPrice(44.99);
        book5.setDescription("Guide pratique du framework Spring");
        book5.setPublicationYear(2018);
        
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);
        bookRepository.save(book5);
        
        System.out.println("✓ Base de données initialisée avec " + bookRepository.count() + " livres");
    }
}
