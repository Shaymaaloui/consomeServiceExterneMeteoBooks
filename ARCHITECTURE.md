# 🏗️ Architecture du Projet

## 📊 Vue d'ensemble

```
┌─────────────────────────────────────────────────────────────────┐
│                      NAVIGATEUR WEB (Client)                     │
│                                                                   │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐                │
│  │ index.html │  │ books.html │  │weather.html│                │
│  │   (Home)   │  │   (CRUD)   │  │  (Météo)   │                │
│  └─────┬──────┘  └─────┬──────┘  └─────┬──────┘                │
│        │                │                │                        │
│        └────────────────┴────────────────┘                        │
│                         │                                         │
│                    JavaScript                                     │
│                    (Fetch API)                                    │
└─────────────────────────┬───────────────────────────────────────┘
                          │
                    HTTP REST
                          │
┌─────────────────────────▼───────────────────────────────────────┐
│                    SPRING BOOT (Backend)                         │
│                   http://localhost:8081                          │
│                                                                   │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                    CONTROLLERS                            │  │
│  │  ┌────────────┐  ┌────────────┐  ┌──────────────┐       │  │
│  │  │   Book     │  │  Weather   │  │    Demo      │       │  │
│  │  │ Controller │  │ Controller │  │  Controller  │       │  │
│  │  └─────┬──────┘  └─────┬──────┘  └──────┬───────┘       │  │
│  └────────┼────────────────┼─────────────────┼──────────────┘  │
│           │                │                 │                   │
│  ┌────────▼────────────────▼─────────────────▼──────────────┐  │
│  │                      SERVICES                             │  │
│  │  ┌────────────┐  ┌────────────┐                          │  │
│  │  │   Book     │  │  Weather   │                          │  │
│  │  │  Service   │  │  Service   │                          │  │
│  │  └─────┬──────┘  └─────┬──────┘                          │  │
│  └────────┼────────────────┼─────────────────────────────────┘  │
│           │                │                                     │
│  ┌────────▼────────┐  ┌────▼─────────────────────────────────┐ │
│  │   REPOSITORY    │  │          CLIENTS                      │ │
│  │  ┌──────────┐   │  │  ┌──────────┐  ┌──────────────┐     │ │
│  │  │   Book   │   │  │  │   Book   │  │   Weather    │     │ │
│  │  │Repository│   │  │  │  Client  │  │   Client     │     │ │
│  │  └────┬─────┘   │  │  └────┬─────┘  └──────┬───────┘     │ │
│  └───────┼─────────┘  └───────┼────────────────┼─────────────┘ │
│          │                     │                │                │
└──────────┼─────────────────────┼────────────────┼────────────────┘
           │                     │                │
           │            Consomme notre API        │
           │                     │                │
           │                HTTP │                │ HTTP
           │                     │                │
    ┌──────▼──────┐      ┌───────▼────────┐     │
    │ H2 Database │      │  localhost:8081 │     │
    │  (In-Memory)│      │   /api/books    │     │
    └─────────────┘      └─────────────────┘     │
                                                  │
                                         ┌────────▼────────────┐
                                         │   API Open-Meteo    │
                                         │  (Service Externe)  │
                                         │ api.open-meteo.com  │
                                         └─────────────────────┘
```

---

## 🔄 Flux de Données

### Flux 1: Afficher les livres

```
1. Navigateur
   ↓ [GET request]
2. books.html (JavaScript)
   ↓ [fetch('http://localhost:8081/api/books')]
3. BookController
   ↓ [getAllBooks()]
4. BookService
   ↓ [findAll()]
5. BookRepository
   ↓ [SQL query]
6. H2 Database
   ↓ [return List<Book>]
7. JSON Response → Navigateur
```

### Flux 2: Consommer notre propre API

```
1. Navigateur
   ↓ [GET /api/demo/books-client]
2. DemoController
   ↓ [bookClient.getAllBooks()]
3. BookClient (WebClient)
   ↓ [HTTP GET http://localhost:8081/api/books]
4. BookController
   ↓ [getAllBooks()]
5. BookService → BookRepository → Database
   ↓ [return List<Book>]
6. JSON Response → BookClient → DemoController → Navigateur
```

### Flux 3: Consommer l'API externe

```
1. Navigateur
   ↓ [GET /api/weather/tunis]
2. WeatherController
   ↓ [weatherService.getWeatherForTunis()]
3. WeatherService
   ↓ [weatherClient.getWeatherForTunis()]
4. WeatherClient (WebClient)
   ↓ [HTTP GET https://api.open-meteo.com/...]
5. API Open-Meteo
   ↓ [return WeatherResponse]
6. JSON Response → WeatherClient → WeatherService → WeatherController → Navigateur
```

---

## 📦 Structure des Packages

```
com.soa.webservices/
│
├── 🚀 WebServicesApplication.java      # Point d'entrée Spring Boot
│
├── 📦 model/                            # Modèles de données
│   ├── Book.java                        # Entité JPA + Lombok
│   └── WeatherResponse.java            # DTO API météo
│
├── 🗄️ repository/                       # Couche d'accès aux données
│   └── BookRepository.java             # Interface JPA Repository
│
├── ⚙️ service/                          # Logique métier
│   ├── BookService.java                # CRUD + recherche livres
│   └── WeatherService.java             # Traitement données météo
│
├── 🌐 controller/                       # Contrôleurs REST
│   ├── BookController.java             # API REST Books
│   ├── WeatherController.java          # API REST Weather
│   └── DemoController.java             # Démonstrations
│
├── 🔌 client/                           # Clients REST
│   ├── BookClient.java                 # Consomme /api/books
│   └── WeatherClient.java              # Consomme Open-Meteo
│
└── 🔧 config/                           # Configuration
    ├── DataInitializer.java            # Données de test
    └── OpenApiConfig.java              # Config Swagger
```

---

## 🎯 Les 3 Objectifs en Détail

### ✅ Objectif 1: Développer un Service Web

**Fichiers principaux**:
- `BookController.java` → Expose les endpoints REST
- `BookService.java` → Logique métier
- `BookRepository.java` → Accès données
- `Book.java` → Modèle

**Endpoints**:
```
POST   /api/books           → Créer
GET    /api/books           → Lire tous
GET    /api/books/{id}      → Lire un
PUT    /api/books/{id}      → Mettre à jour
DELETE /api/books/{id}      → Supprimer
GET    /api/books/search/author?author=...
GET    /api/books/search/title?title=...
```

**Technologies**:
- Spring Boot REST
- Spring Data JPA
- H2 Database
- Bean Validation

### ✅ Objectif 2: Consommer Notre Service

**Fichier principal**: `BookClient.java`

**Code clé**:
```java
@Component
public class BookClient {
    private final WebClient webClient;
    
    public List<Book> getAllBooks() {
        return webClient.get()
                .uri("/api/books")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Book>>() {})
                .block();
    }
}
```

**Démonstration**: `DemoController.java`
```java
@GetMapping("/books-client")
public ResponseEntity<List<Book>> getBooksViaClient() {
    List<Book> books = bookClient.getAllBooks();
    return ResponseEntity.ok(books);
}
```

**URL de test**: http://localhost:8081/api/demo/books-client

### ✅ Objectif 3: Consommer un Service Externe

**Fichier principal**: `WeatherClient.java`

**Code clé**:
```java
@Component
public class WeatherClient {
    private final WebClient webClient;
    
    public WeatherResponse getWeather(Double lat, Double lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.open-meteo.com")
                        .path("/v1/forecast")
                        .queryParam("latitude", lat)
                        .queryParam("longitude", lon)
                        .queryParam("current_weather", true)
                        .build())
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .block();
    }
}
```

**API externe**: Open-Meteo
- URL: https://api.open-meteo.com
- Type: REST API gratuite
- Pas de clé API nécessaire

**URL de test**: http://localhost:8081/api/weather/tunis

---

## 🖥️ Architecture Frontend

```
static/
│
├── 📄 index.html               # Page d'accueil
│   ├── Dashboard statistiques
│   ├── Livres récents
│   └── Présentation projet
│
├── 📄 books.html               # Gestion livres
│   ├── Liste tous les livres
│   ├── Recherche temps réel
│   ├── Modal ajout/modification
│   └── Suppression avec confirmation
│
├── 📄 weather.html             # Météo temps réel
│   ├── Sélection ville (Tunis/Paris)
│   ├── Recherche par coordonnées
│   └── Affichage données météo
│
├── 🎨 css/
│   └── style.css               # Design complet
│       ├── Variables CSS
│       ├── Composants réutilisables
│       ├── Responsive design
│       └── Animations
│
└── 💻 js/
    ├── main.js                 # Page accueil
    │   ├── Charger statistiques
    │   ├── Charger livres récents
    │   └── Afficher météo
    │
    ├── books.js                # Page livres
    │   ├── CRUD complet
    │   ├── Recherche
    │   ├── Modals
    │   └── Notifications
    │
    └── weather.js              # Page météo
        ├── Charger météo ville
        ├── Coordonnées custom
        ├── Afficher données
        └── Icônes dynamiques
```

---

## 🔗 Communication Frontend ↔ Backend

### API Books
```javascript
// JavaScript (Frontend)
const API_BASE_URL = 'http://localhost:8081/api';

// Récupérer tous les livres
fetch(`${API_BASE_URL}/books`)
  .then(res => res.json())
  .then(books => displayBooks(books));

// Créer un livre
fetch(`${API_BASE_URL}/books`, {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(bookData)
});
```

### API Weather
```javascript
// JavaScript (Frontend)
fetch(`${API_BASE_URL}/weather/tunis`)
  .then(res => res.json())
  .then(weather => displayWeather(weather));
```

---

## 🔐 Sécurité et Bonnes Pratiques

### Backend
- ✅ Validation des données (`@Valid`, `@NotBlank`)
- ✅ Gestion des erreurs (try-catch)
- ✅ Codes HTTP appropriés (200, 201, 404, 500)
- ✅ CORS configuré pour le frontend
- ✅ Séparation des couches

### Frontend
- ✅ Échappement HTML (prévention XSS)
- ✅ Validation formulaires
- ✅ Feedback utilisateur (toasts)
- ✅ Gestion des erreurs réseau
- ✅ Loading states

---

## 📊 Diagramme de Séquence

### Créer un livre via l'interface

```
Utilisateur → books.html → BookController → BookService → BookRepository → H2
     │            │              │              │              │           │
     │ Remplir    │              │              │              │           │
     │ formulaire │              │              │              │           │
     │────────────→              │              │              │           │
     │            │ POST /api/   │              │              │           │
     │            │ books        │              │              │           │
     │            │──────────────→ createBook() │              │           │
     │            │              │──────────────→ save()       │           │
     │            │              │              │──────────────→ INSERT    │
     │            │              │              │              │───────────→
     │            │              │              │              │←───────────
     │            │              │              │←──────────────           │
     │            │              │←──────────────              │           │
     │            │←──────────────              │              │           │
     │←───────────│ 201 Created  │              │              │           │
     │ Notification               │              │              │           │
     │ "Livre ajouté"             │              │              │           │
```

---

## 🎓 Points d'Apprentissage

### Architecture
- ✅ Architecture en couches (MVC)
- ✅ Injection de dépendances
- ✅ Inversion de contrôle
- ✅ Séparation des responsabilités

### REST API
- ✅ Endpoints RESTful
- ✅ Verbes HTTP (GET, POST, PUT, DELETE)
- ✅ Codes de statut HTTP
- ✅ Format JSON
- ✅ Documentation OpenAPI

### Consommation de Services
- ✅ WebClient (HTTP client réactif)
- ✅ Appels REST synchrones/asynchrones
- ✅ Désérialisation JSON
- ✅ Gestion des erreurs réseau

### Persistance
- ✅ Spring Data JPA
- ✅ Entités JPA
- ✅ Repository pattern
- ✅ Requêtes dérivées
- ✅ Base H2 in-memory

### Frontend
- ✅ HTML5 sémantique
- ✅ CSS3 moderne (Grid, Flexbox)
- ✅ JavaScript ES6+ (async/await, fetch)
- ✅ DOM manipulation
- ✅ Responsive design

---

## 🚀 Déploiement

### Structure de déploiement
```
webservices-project-1.0.0.jar
├── Spring Boot Embedded Tomcat (port 8081)
├── API REST endpoints
├── Static files (HTML, CSS, JS)
├── H2 Database (in-memory)
└── Configuration (application.properties)
```

### Commande de déploiement
```bash
java -jar webservices-project-1.0.0.jar
```

---

## 📈 Évolutions Possibles

### Court terme
- [ ] Ajouter plus de tests unitaires
- [ ] Pagination pour les livres
- [ ] Tri des résultats
- [ ] Filtres avancés

### Moyen terme
- [ ] Authentification JWT
- [ ] Base de données PostgreSQL
- [ ] Service SOAP en plus de REST
- [ ] Upload de couvertures de livres
- [ ] Cache Redis

### Long terme
- [ ] Microservices architecture
- [ ] Docker containerization
- [ ] CI/CD Pipeline
- [ ] Monitoring (Prometheus, Grafana)
- [ ] API Gateway

---

## ✅ Validation du Projet

### Critères remplis
- ✅ Service web développé (REST API Books)
- ✅ Consommation interne (BookClient)
- ✅ Consommation externe (WeatherClient)
- ✅ Code propre et documenté
- ✅ Frontend fonctionnel
- ✅ Documentation complète

### Prêt pour
- ✅ Présentation
- ✅ Démonstration
- ✅ Validation
- ✅ Note finale

---

**Architecture solide, code propre, projet complet ! 🎉**
