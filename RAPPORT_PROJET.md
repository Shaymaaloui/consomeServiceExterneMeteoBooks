# Rapport de Projet - Services Web SOA

## 📌 Informations générales

**Titre** : Projet de développement et consommation de services web  
**Technologie principale** : Java / Spring Boot  
**Type de services** : REST  
**Date** : 2024

---

## 🎯 Objectifs du projet

Le projet répond aux trois exigences principales :

### 1. ✅ Développer un service web (SOAP ou REST)

**Service développé** : API REST de gestion de livres

**Technologies utilisées** :
- Spring Boot 3.1.5
- Spring Data JPA
- H2 Database (en mémoire)
- Hibernate

**Fonctionnalités implémentées** :
- ✅ Créer un livre (POST)
- ✅ Lire tous les livres (GET)
- ✅ Lire un livre par ID (GET)
- ✅ Lire un livre par ISBN (GET)
- ✅ Rechercher par auteur (GET)
- ✅ Rechercher par titre (GET)
- ✅ Mettre à jour un livre (PUT)
- ✅ Supprimer un livre (DELETE)

**Endpoints REST** :
```
GET    /api/books                      → Liste tous les livres
GET    /api/books/{id}                 → Récupère un livre par ID
GET    /api/books/isbn/{isbn}          → Récupère un livre par ISBN
GET    /api/books/search/author        → Recherche par auteur
GET    /api/books/search/title         → Recherche par titre
POST   /api/books                      → Crée un nouveau livre
PUT    /api/books/{id}                 → Met à jour un livre
DELETE /api/books/{id}                 → Supprime un livre
```

### 2. ✅ Consommer notre propre service

**Client développé** : `BookClient.java`

**Implémentation** :
```java
@Component
public class BookClient {
    private final WebClient webClient;
    
    public List<Book> getAllBooks() {
        return webClient.get()
                .uri("")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Book>>() {})
                .block();
    }
    
    public Book getBookById(Long id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Book.class)
                .block();
    }
    
    public Book createBook(Book book) {
        return webClient.post()
                .uri("")
                .bodyValue(book)
                .retrieve()
                .bodyToMono(Book.class)
                .block();
    }
}
```

**Démonstration** :
Le contrôleur `DemoController` utilise ce client pour consommer notre API :

```java
@GetMapping("/books-client")
public ResponseEntity<List<Book>> getBooksViaClient() {
    List<Book> books = bookClient.getAllBooks();
    return ResponseEntity.ok(books);
}
```

**Test** : `/api/demo/books-client` retourne les mêmes données que `/api/books`, mais en passant par une consommation HTTP réelle.

### 3. ✅ Consommer un service web externe

**Service externe** : Open-Meteo API (https://open-meteo.com/)

**Pourquoi Open-Meteo** :
- ✅ API gratuite
- ✅ Pas de clé API nécessaire
- ✅ Données météo en temps réel
- ✅ Documentation claire
- ✅ Fiable et rapide

**Client développé** : `WeatherClient.java`

**Implémentation** :
```java
@Component
public class WeatherClient {
    private final WebClient webClient;
    
    public WeatherResponse getWeather(Double latitude, Double longitude) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.open-meteo.com")
                        .path("/v1/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("current_weather", true)
                        .build())
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .block();
    }
}
```

**Endpoints disponibles** :
```
GET /api/weather?latitude={lat}&longitude={lon}  → Météo par coordonnées
GET /api/weather/paris                           → Météo à Paris
GET /api/weather/tunis                           → Météo à Tunis
```

---

## 🏗️ Architecture du projet

### Structure des packages

```
com.soa.webservices/
├── WebServicesApplication.java    # Point d'entrée
├── model/                          # Modèles de données
│   ├── Book.java
│   └── WeatherResponse.java
├── repository/                     # Couche d'accès aux données
│   └── BookRepository.java
├── service/                        # Logique métier
│   ├── BookService.java
│   └── WeatherService.java
├── controller/                     # Contrôleurs REST
│   ├── BookController.java
│   ├── WeatherController.java
│   └── DemoController.java
├── client/                         # Clients REST
│   ├── BookClient.java            # Consomme notre API ✓
│   └── WeatherClient.java         # Consomme API externe ✓
└── config/                         # Configuration
    ├── DataInitializer.java
    └── OpenApiConfig.java
```

### Architecture en couches

```
┌─────────────────────────────────────────┐
│         Controllers (REST API)          │
│  BookController, WeatherController      │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│            Services                      │
│  BookService, WeatherService            │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│    Repositories / Clients               │
│  BookRepository, WeatherClient          │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│    Database / External APIs             │
│      H2 Database, Open-Meteo            │
└─────────────────────────────────────────┘
```

### Flux de données

#### 1. Appel direct à notre API
```
Client HTTP → BookController → BookService → BookRepository → H2 Database
```

#### 2. Consommation de notre API via BookClient
```
DemoController → BookClient → HTTP Request → BookController → BookService → BookRepository
```

#### 3. Consommation de l'API externe
```
WeatherController → WeatherService → WeatherClient → HTTP Request → Open-Meteo API
```

---

## 🛠️ Technologies et outils

### Backend
- **Java 17** : Langage de programmation
- **Spring Boot 3.1.5** : Framework principal
- **Spring Web** : Pour créer les API REST
- **Spring Data JPA** : Pour la persistance
- **Hibernate** : ORM
- **H2 Database** : Base de données en mémoire
- **Lombok** : Réduction du boilerplate

### Client REST
- **WebClient** : Client HTTP réactif de Spring

### Documentation
- **SpringDoc OpenAPI** : Documentation automatique
- **Swagger UI** : Interface interactive

### Build
- **Maven** : Gestion des dépendances et build

---

## 📊 Démonstration des fonctionnalités

### Scénario 1 : CRUD sur les livres

```bash
# Créer un livre
POST /api/books
{
  "title": "Clean Code",
  "author": "Robert Martin",
  "isbn": "978-0132350884",
  "price": 35.99,
  "publicationYear": 2008
}

# Réponse : 201 Created
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert Martin",
  ...
}

# Lire tous les livres
GET /api/books
→ Retourne la liste complète

# Mettre à jour
PUT /api/books/1
→ 200 OK

# Supprimer
DELETE /api/books/1
→ 204 No Content
```

### Scénario 2 : Consommation de notre API

```bash
# Appel direct
GET /api/books
→ [livre1, livre2, ...]

# Appel via le client (même résultat, mais via HTTP)
GET /api/demo/books-client
→ [livre1, livre2, ...]  # Passé par BookClient
```

**Preuve** : Les logs montrent deux requêtes HTTP distinctes.

### Scénario 3 : Consommation API externe

```bash
# Météo à Tunis
GET /api/weather/tunis

# Réponse en temps réel
{
  "city": "Tunis",
  "latitude": 36.8065,
  "longitude": 10.1815,
  "currentWeather": {
    "temperature": "18.5°C",
    "windSpeed": "12.3 km/h",
    "description": "Ciel dégagé",
    "time": "2024-01-15T14:00"
  }
}
```

### Scénario 4 : Combinaison des services

```bash
GET /api/demo/combined

# Réponse combinée
{
  "totalBooks": 5,
  "books": [...],
  "weather": {
    "location": "Tunis",
    "temperature": "18.5°C",
    "windSpeed": "12.3 km/h"
  },
  "message": "Démonstration de la consommation de notre API + API externe"
}
```

---

## 🧪 Tests

### Tests unitaires

Fichier : `BookServiceTest.java`

```java
@Test
void testGetAllBooks() {
    List<Book> books = Arrays.asList(testBook);
    when(bookRepository.findAll()).thenReturn(books);
    
    List<Book> result = bookService.getAllBooks();
    
    assertEquals(1, result.size());
    verify(bookRepository, times(1)).findAll();
}
```

### Tests manuels via Swagger

1. Ouvrir http://localhost:8080/swagger-ui.html
2. Tester chaque endpoint
3. Vérifier les codes de statut
4. Valider les réponses JSON

---

## 📈 Points forts du projet

### ✅ Respect des exigences
- Service web REST complet
- Consommation de notre service via client
- Consommation de service externe

### ✅ Architecture propre
- Séparation en couches (Controller, Service, Repository)
- Injection de dépendances
- Code modulaire et réutilisable

### ✅ Documentation complète
- README détaillé
- Guide d'utilisation
- Swagger UI intégré
- Exemples de requêtes HTTP

### ✅ Bonnes pratiques
- Validation des données (`@Valid`)
- Gestion des erreurs
- Codes HTTP appropriés
- Nommage clair

### ✅ Facilité d'utilisation
- Base de données en mémoire (pas de setup)
- Données de test pré-chargées
- Démarrage simple (`mvn spring-boot:run`)
- Pas de configuration complexe

---

## 🚀 Comment exécuter le projet

### Prérequis
- Java 17+
- Maven 3.6+

### Étapes
```bash
# 1. Compiler
mvn clean install

# 2. Démarrer
mvn spring-boot:run

# 3. Accéder
http://localhost:8080/swagger-ui.html
```

### URLs importantes
- Application : http://localhost:8080
- Swagger UI : http://localhost:8080/swagger-ui.html
- Console H2 : http://localhost:8080/h2-console

---

## 📝 Conclusion

Ce projet démontre complètement les compétences suivantes :

1. **Développement de services web** : API REST complète avec CRUD
2. **Consommation de services** : Client pour notre propre API
3. **Intégration externe** : Consommation d'une API tierce
4. **Architecture** : Séparation en couches, injection de dépendances
5. **Documentation** : Swagger, README, guides
6. **Tests** : Tests unitaires avec Mockito

**Tous les objectifs du projet sont atteints** ✅

---

## 📚 Références

- Spring Boot : https://spring.io/projects/spring-boot
- Spring Data JPA : https://spring.io/projects/spring-data-jpa
- Open-Meteo API : https://open-meteo.com/
- SpringDoc OpenAPI : https://springdoc.org/
- REST Best Practices : https://restfulapi.net/

---

**Projet réalisé dans le cadre du cours de Services Web - SOA**
