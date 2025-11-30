# Guide d'utilisation - Projet Services Web

## 🎯 Objectif

Ce guide vous aide à comprendre et tester le projet de services web.

## 📋 Checklist de démonstration

### ✅ 1. Développement d'un service web REST

**Notre service** : API de gestion de livres

**Fichiers concernés** :
- `BookController.java` : Endpoints REST
- `BookService.java` : Logique métier
- `Book.java` : Modèle de données
- `BookRepository.java` : Accès aux données

**Test** :
1. Démarrer l'application
2. Aller sur http://localhost:8080/swagger-ui.html
3. Tester les endpoints `/api/books` (GET, POST, PUT, DELETE)

### ✅ 2. Consommation de notre propre service

**Notre client** : `BookClient.java`

**Fichiers concernés** :
- `BookClient.java` : Client REST qui consomme notre API
- `DemoController.java` : Utilise le BookClient

**Test** :
1. Ouvrir Swagger : http://localhost:8080/swagger-ui.html
2. Section "Demo"
3. Tester `/api/demo/books-client` : récupère les livres via le client
4. Tester `/api/demo/create-and-verify` : crée un livre via le client

**Preuve de consommation** :
```java
// Dans DemoController.java
@GetMapping("/books-client")
public ResponseEntity<List<Book>> getBooksViaClient() {
    // Consomme notre propre API via BookClient
    List<Book> books = bookClient.getAllBooks();
    return ResponseEntity.ok(books);
}
```

### ✅ 3. Consommation d'un service externe

**Service externe** : Open-Meteo API (météo gratuite)

**Fichiers concernés** :
- `WeatherClient.java` : Client qui consomme l'API externe
- `WeatherService.java` : Service météo
- `WeatherController.java` : Expose les données météo

**Test** :
1. Ouvrir Swagger : http://localhost:8080/swagger-ui.html
2. Section "Weather"
3. Tester `/api/weather/tunis` ou `/api/weather/paris`
4. Observer les données en temps réel

**API externe utilisée** :
- URL : https://api.open-meteo.com
- Documentation : https://open-meteo.com/
- Gratuite, pas de clé API nécessaire

## 🧪 Scénarios de test complets

### Scénario 1 : CRUD complet sur les livres

```bash
# 1. Lister tous les livres
curl http://localhost:8080/api/books

# 2. Créer un nouveau livre
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Book",
    "author": "Test Author",
    "isbn": "123-456-789",
    "price": 29.99,
    "publicationYear": 2024
  }'

# 3. Récupérer le livre créé (remplacer {id})
curl http://localhost:8080/api/books/{id}

# 4. Mettre à jour le livre
curl -X PUT http://localhost:8080/api/books/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated Book",
    "author": "Test Author",
    "isbn": "123-456-789",
    "price": 34.99,
    "publicationYear": 2024
  }'

# 5. Supprimer le livre
curl -X DELETE http://localhost:8080/api/books/{id}
```

### Scénario 2 : Démonstration de la consommation

```bash
# 1. Accès direct à l'API
curl http://localhost:8080/api/books

# 2. Accès via le client REST (consommation de notre API)
curl http://localhost:8080/api/demo/books-client

# Les deux retournent les mêmes données, mais :
# - Le premier appelle directement le contrôleur
# - Le second passe par BookClient (simulation d'une app cliente)
```

### Scénario 3 : Combinaison des services

```bash
# Appel qui combine notre API + API externe
curl http://localhost:8080/api/demo/combined

# Retourne :
# - Liste des livres (notre API)
# - Météo à Tunis (API externe)
```

## 🔍 Points d'attention pour la présentation

### 1. Montrer le code du service REST

Ouvrir `BookController.java` et montrer :
- Les annotations `@RestController`, `@GetMapping`, `@PostMapping`
- Les méthodes CRUD
- La validation avec `@Valid`

### 2. Montrer la consommation interne

Ouvrir `BookClient.java` et montrer :
- Utilisation de `WebClient`
- Méthodes qui appellent notre propre API
- Comment on construit les requêtes HTTP

### 3. Montrer la consommation externe

Ouvrir `WeatherClient.java` et montrer :
- Appel à l'API Open-Meteo
- Construction de l'URL avec paramètres
- Désérialisation de la réponse

### 4. Montrer l'intégration

Ouvrir `DemoController.java` et montrer :
- Injection de `BookClient` et `WeatherService`
- Méthode `getCombinedData()` qui combine les deux

## 📊 Démonstration visuelle

### Option 1 : Via Swagger UI

1. Ouvrir http://localhost:8080/swagger-ui.html
2. Interface graphique pour tester tous les endpoints
3. Documentation automatique de l'API

### Option 2 : Via curl/Postman

Utiliser les commandes curl ci-dessus pour montrer :
- Les requêtes HTTP brutes
- Les réponses JSON
- Le cycle complet de consommation

### Option 3 : Via navigateur

```
http://localhost:8080/api/books
http://localhost:8080/api/demo/books-client
http://localhost:8080/api/weather/tunis
http://localhost:8080/api/demo/combined
```

## 🎓 Points à expliquer

1. **Architecture REST** :
   - Ressources (Books)
   - Verbes HTTP (GET, POST, PUT, DELETE)
   - Codes de statut (200, 201, 404)

2. **Client REST** :
   - WebClient vs RestTemplate
   - Requêtes asynchrones/synchrones
   - Gestion des erreurs

3. **Service externe** :
   - Choix d'une API gratuite
   - Pas besoin de clé API
   - Données en temps réel

4. **Technologies** :
   - Spring Boot pour simplifier le développement
   - JPA/H2 pour la persistance
   - Lombok pour réduire le boilerplate
   - Swagger pour la documentation

## ❓ Questions possibles et réponses

**Q : Pourquoi REST plutôt que SOAP ?**
R : REST est plus simple, léger, et utilise HTTP standard. Idéal pour ce type de projet.

**Q : Pourquoi Open-Meteo ?**
R : API gratuite, sans clé, simple à utiliser, données en temps réel.

**Q : Comment prouver la consommation de votre API ?**
R : Le `BookClient` fait des appels HTTP réels à notre API. Montrer les logs, le code, et les résultats.

**Q : Peut-on ajouter SOAP ?**
R : Oui, avec Spring Web Services. Mais REST suffit pour les exigences du projet.

## 📈 Améliorations possibles

Si vous avez du temps :

1. Ajouter des tests unitaires
2. Ajouter un service SOAP en plus de REST
3. Utiliser une vraie base de données (PostgreSQL, MySQL)
4. Ajouter de l'authentification JWT
5. Créer une interface web (frontend)
6. Dockeriser l'application

## 🎬 Conclusion

Ce projet démontre :
- ✅ Développement d'un service web (REST API Books)
- ✅ Consommation de notre propre service (BookClient)
- ✅ Consommation d'un service externe (WeatherClient)

Tous les objectifs sont remplis ! 🎉
