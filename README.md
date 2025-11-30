# Projet Services Web - SOA

## 📋 Description

Ce projet Java démontre le développement et la consommation de services web dans le cadre d'un cours de SOA (Service-Oriented Architecture).

### Objectifs réalisés

✅ **1. Développer un service web REST** : API complète de gestion de livres avec CRUD  
✅ **2. Consommer notre propre service** : Client REST (`BookClient`) qui consomme l'API Books  
✅ **3. Consommer un service externe** : Client REST (`WeatherClient`) qui consomme l'API météo Open-Meteo

## 🛠️ Technologies utilisées

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data JPA** (persistance)
- **H2 Database** (base de données en mémoire)
- **WebClient** (consommation d'APIs REST)
- **Lombok** (réduction du code boilerplate)
- **OpenAPI/Swagger** (documentation API)
- **Maven** (gestion des dépendances)

## 🚀 Installation et Démarrage

### Prérequis

- Java 17 ou supérieur
- Maven 3.6+ (ou utiliser votre IDE)

### Étapes

1. **Cloner le projet** (ou extraire l'archive)

```bash
cd projetSOA
```

2. **Compiler le projet**

```bash
mvn clean install
```

3. **Démarrer l'application**

```bash
mvn spring-boot:run
```

**Ou via votre IDE** (IntelliJ IDEA, Eclipse, VS Code):
- Ouvrir le projet
- Exécuter `WebServicesApplication.java`

L'application démarre sur **http://localhost:8081**

### Accès au Frontend

Une fois l'application démarrée, ouvrir dans le navigateur:
- **Interface web**: http://localhost:8081/
- **Page Livres**: http://localhost:8081/books.html
- **Page Météo**: http://localhost:8081/weather.html

## 📚 Documentation API

Une fois l'application démarrée, accédez à la documentation interactive Swagger :

**http://localhost:8081/swagger-ui.html**

## 🔌 Endpoints disponibles

### 1. API Books (Notre service REST)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/books` | Liste tous les livres |
| GET | `/api/books/{id}` | Récupère un livre par ID |
| GET | `/api/books/isbn/{isbn}` | Récupère un livre par ISBN |
| GET | `/api/books/search/author?author=...` | Recherche par auteur |
| GET | `/api/books/search/title?title=...` | Recherche par titre |
| POST | `/api/books` | Crée un nouveau livre |
| PUT | `/api/books/{id}` | Met à jour un livre |
| DELETE | `/api/books/{id}` | Supprime un livre |

### 2. API Weather (Service externe)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/weather?latitude=...&longitude=...` | Météo par coordonnées |
| GET | `/api/weather/paris` | Météo à Paris |
| GET | `/api/weather/tunis` | Météo à Tunis |

### 3. API Demo (Consommation de services)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/demo/books-client` | Liste des livres via BookClient |
| GET | `/api/demo/book-client/{id}` | Livre par ID via BookClient |
| GET | `/api/demo/combined` | Combine notre API + API externe |
| POST | `/api/demo/create-and-verify` | Crée et vérifie un livre |

## 📖 Exemples d'utilisation

### Exemple 1 : Créer un livre

```bash
curl -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Refactoring",
    "author": "Martin Fowler",
    "isbn": "978-0134757599",
    "price": 39.99,
    "description": "Améliorer la conception du code existant",
    "publicationYear": 2018
  }'
```

### Exemple 2 : Obtenir tous les livres via le client

```bash
curl http://localhost:8081/api/demo/books-client
```

### Exemple 3 : Obtenir la météo à Tunis

```bash
curl http://localhost:8081/api/weather/tunis
```

### Exemple 4 : Démonstration combinée

```bash
curl http://localhost:8081/api/demo/combined
```

## 🗄️ Base de données H2

La console H2 est accessible à : **http://localhost:8081/h2-console**

- **JDBC URL** : `jdbc:h2:mem:testdb`
- **Username** : `sa`
- **Password** : _(vide)_

## 📂 Structure du projet

```
src/main/java/com/soa/webservices/
├── WebServicesApplication.java      # Point d'entrée
├── model/
│   ├── Book.java                    # Entité Livre
│   └── WeatherResponse.java         # Modèle réponse météo
├── repository/
│   └── BookRepository.java          # Repository JPA
├── service/
│   ├── BookService.java             # Service métier livres
│   └── WeatherService.java          # Service météo
├── controller/
│   ├── BookController.java          # Contrôleur REST Books
│   ├── WeatherController.java       # Contrôleur REST Weather
│   └── DemoController.java          # Contrôleur démonstration
├── client/
│   ├── BookClient.java              # Client pour notre API ✓
│   └── WeatherClient.java           # Client API externe ✓
└── config/
    ├── DataInitializer.java         # Données de test
    └── OpenApiConfig.java           # Config Swagger
```

## 🎯 Points clés du projet

### 1. Service REST développé
- API complète de gestion de livres (CRUD)
- Validation des données
- Recherche par auteur et titre
- Persistance avec JPA/H2

### 2. Consommation de notre propre service
La classe `BookClient` utilise `WebClient` pour consommer notre API Books :
```java
public List<Book> getAllBooks() {
    return webClient.get()
            .uri("")
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<Book>>() {})
            .block();
}
```

### 3. Consommation d'un service externe
La classe `WeatherClient` consomme l'API gratuite Open-Meteo :
```java
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
```

## 🧪 Tests manuels

1. **Démarrer l'application**
2. **Tester l'API Books** via Swagger ou curl
3. **Tester la consommation via BookClient** : `/api/demo/books-client`
4. **Tester l'API externe** : `/api/weather/tunis`
5. **Tester la combinaison** : `/api/demo/combined`

## 📝 Notes importantes

- L'application utilise H2 en mémoire : les données sont perdues à l'arrêt
- Des données de test sont créées automatiquement au démarrage
- L'API Open-Meteo est gratuite et ne nécessite pas de clé API
- Le port par défaut est 8080 (configurable dans `application.properties`)

## 👨‍🎓 Auteur

Projet réalisé dans le cadre du cours de Services Web - SOA

## 📄 Licence

Projet éducatif
