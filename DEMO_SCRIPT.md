# 🎬 Script de Démonstration

## ⏱️ Présentation 5 Minutes

### 📋 Checklist avant démonstration
- [ ] Application démarrée (mvn spring-boot:run)
- [ ] Message "Started WebServicesApplication" visible
- [ ] Navigateur prêt (fermer les onglets inutiles)
- [ ] Code source ouvert (BookClient.java, WeatherClient.java)
- [ ] Documentation prête (README.md)

---

## 🎯 Introduction (30 secondes)

**À dire**:
> "Bonjour, je vais vous présenter mon projet de services web.
> J'ai développé une application complète de gestion de bibliothèque
> qui démontre les 3 objectifs du cours :
> 1. Développement d'un service web REST
> 2. Consommation de notre propre service
> 3. Intégration d'un service web externe"

**Montrer**:
- Projet ouvert dans l'IDE
- Terminal avec l'application démarrée

---

## 🌐 Partie 1: Service Web REST (1 min 30)

### Étape 1.1: Documentation Swagger
**URL**: http://localhost:8081/swagger-ui.html

**À dire**:
> "Voici la documentation interactive de mon API REST.
> J'ai développé une API complète de gestion de livres avec CRUD."

**Montrer**:
- Section "Books" dans Swagger
- Les 8 endpoints disponibles

### Étape 1.2: Test GET /api/books
**Action**: Cliquer sur GET /api/books → Try it out → Execute

**À dire**:
> "Cette requête retourne tous les livres de la base de données.
> J'ai 5 livres de test pré-chargés automatiquement."

**Montrer**:
- Le JSON retourné
- Les champs (id, title, author, isbn, price, etc.)

### Étape 1.3: Test POST /api/books
**Action**: Cliquer sur POST /api/books → Try it out

**JSON à utiliser**:
```json
{
  "title": "Microservices Patterns",
  "author": "Chris Richardson",
  "isbn": "978-1617294549",
  "price": 44.99,
  "description": "Patterns pour architectures microservices",
  "publicationYear": 2018
}
```

**À dire**:
> "Je peux créer un nouveau livre en envoyant du JSON.
> Le service valide les données et retourne le livre créé avec son ID."

**Montrer**:
- Code 201 Created
- Le livre retourné avec un ID généré

---

## 🔌 Partie 2: Consommation Interne (1 min 30)

### Étape 2.1: Démonstration API
**URL**: http://localhost:8081/api/demo/books-client

**Action**: Tester dans Swagger (section Demo)

**À dire**:
> "Maintenant je vais démontrer la consommation de mon propre service.
> Cet endpoint utilise un client REST pour appeler /api/books."

**Montrer**:
- Le même résultat que /api/books
- Mais passé par BookClient

### Étape 2.2: Code Source BookClient
**Fichier**: `src/.../client/BookClient.java`

**Lignes à montrer** (23-33):
```java
public List<Book> getAllBooks() {
    return webClient.get()
            .uri("")
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<Book>>() {})
            .block();
}
```

**À dire**:
> "Voici le code du client. J'utilise WebClient de Spring
> pour faire un vrai appel HTTP à mon API REST."

**Montrer**:
- L'annotation @Component
- L'utilisation de WebClient
- La méthode qui appelle /api/books

### Étape 2.3: Code DemoController
**Fichier**: `src/.../controller/DemoController.java`

**Lignes à montrer**:
```java
@GetMapping("/books-client")
public ResponseEntity<List<Book>> getBooksViaClient() {
    List<Book> books = bookClient.getAllBooks();
    return ResponseEntity.ok(books);
}
```

**À dire**:
> "Le contrôleur injecte BookClient et l'utilise pour récupérer les livres."

---

## 🌤️ Partie 3: Service Externe (1 min 30)

### Étape 3.1: Démonstration API Weather
**URL**: http://localhost:8081/api/weather/tunis

**Action**: Tester dans Swagger (section Weather)

**À dire**:
> "Pour le service externe, j'utilise l'API gratuite Open-Meteo
> qui fournit des données météo en temps réel."

**Montrer**:
- La température actuelle
- La vitesse du vent
- L'heure de la dernière mise à jour

### Étape 3.2: Code Source WeatherClient
**Fichier**: `src/.../client/WeatherClient.java`

**Lignes à montrer** (29-45):
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

**À dire**:
> "Mon client fait un appel HTTP à l'API externe Open-Meteo
> et désérialise la réponse JSON en objet Java."

**Montrer**:
- L'URL de l'API externe (api.open-meteo.com)
- Les paramètres (latitude, longitude)
- Le retour WeatherResponse

---

## 🖥️ Partie 4: Frontend (30 secondes)

### Étape 4.1: Page d'accueil
**URL**: http://localhost:8081/

**À dire**:
> "J'ai également développé une interface web complète."

**Montrer**:
- Dashboard avec statistiques
- Design moderne
- Navigation

### Étape 4.2: Page Livres
**URL**: http://localhost:8081/books.html

**Action**: Faire une recherche, ajouter un livre

**À dire**:
> "L'interface permet de gérer les livres :
> recherche, ajout, modification, suppression."

**Montrer**:
- La recherche en temps réel
- Le formulaire d'ajout
- La liste des livres

### Étape 4.3: Page Météo
**URL**: http://localhost:8081/weather.html

**À dire**:
> "Et voici la météo en temps réel via le service externe."

**Montrer**:
- Météo Tunis
- Météo Paris
- Possibilité de chercher par coordonnées

---

## 🎓 Conclusion (30 secondes)

**À dire**:
> "En conclusion, mon projet démontre complètement les 3 objectifs :
> 
> 1. ✅ J'ai développé un service web REST complet avec CRUD
> 2. ✅ J'ai créé un client qui consomme mon propre service
> 3. ✅ J'ai intégré un service externe gratuit pour la météo
> 
> En bonus, j'ai ajouté une interface web moderne et une documentation complète.
> 
> Le code est propre, l'architecture est en couches, et tout est fonctionnel.
> 
> Merci de votre attention, avez-vous des questions ?"

---

## ❓ Questions Probables et Réponses

### Q1: Pourquoi Open-Meteo ?
**Réponse**:
> "J'ai choisi Open-Meteo car c'est une API gratuite, sans clé API,
> avec des données en temps réel. C'est parfait pour une démonstration
> et ça fonctionne de manière fiable."

### Q2: Comment prouvez-vous la consommation de votre API ?
**Réponse**:
> "Le BookClient fait un vrai appel HTTP à http://localhost:8081/api/books.
> C'est visible dans le code et testable via /api/demo/books-client.
> Le frontend JavaScript fait aussi des appels fetch() à l'API."

### Q3: Pourquoi pas SOAP ?
**Réponse**:
> "J'ai choisi REST car c'est plus moderne, plus simple, et mieux adapté
> aux applications web. REST utilise HTTP standard et JSON, ce qui facilite
> l'intégration avec le frontend."

### Q4: La base de données est persistante ?
**Réponse**:
> "Non, j'utilise H2 en mode in-memory pour simplifier le déploiement.
> Les données sont réinitialisées à chaque démarrage avec 5 livres de test.
> En production, on pourrait facilement basculer vers MySQL ou PostgreSQL."

### Q5: Le frontend consomme-t-il directement l'API ?
**Réponse**:
> "Oui ! Le JavaScript dans books.js fait des appels fetch() à l'API REST.
> C'est une autre forme de consommation de service, mais côté client."

### Q6: Combien de temps avez-vous mis ?
**Réponse**:
> "Environ [X heures] pour le backend, [Y heures] pour le frontend,
> et [Z heures] pour la documentation. Le projet est complet et bien structuré."

---

## 📝 Notes pour la Présentation

### ✅ À faire
- Parler clairement et pas trop vite
- Montrer le code source (BookClient, WeatherClient)
- Tester en direct (Swagger, Frontend)
- Souligner les 3 objectifs accomplis
- Mentionner l'architecture en couches

### ❌ À éviter
- Ne pas montrer le terminal avec des erreurs
- Ne pas s'attarder sur des détails techniques
- Ne pas dépasser le temps imparti
- Ne pas oublier de montrer le frontend

### 💡 Points à souligner
1. **Complet**: Backend + Frontend + Documentation
2. **Propre**: Code organisé, commenté, testé
3. **Moderne**: Technologies récentes (Spring Boot 3, ES6+)
4. **Fonctionnel**: Tout marche, rien de cassé
5. **Bonus**: Interface web professionnelle

---

## 🎯 URLs à avoir ouvertes

### Backend
- http://localhost:8081/swagger-ui.html
- http://localhost:8081/h2-console

### Frontend
- http://localhost:8081/
- http://localhost:8081/books.html
- http://localhost:8081/weather.html

### Documentation
- README.md
- ARCHITECTURE.md

### Code Source
- `BookClient.java` (lignes 23-50)
- `WeatherClient.java` (lignes 29-45)
- `DemoController.java` (lignes 25-35)

---

## ⏱️ Timing Détaillé

| Partie | Temps | Contenu |
|--------|-------|---------|
| Introduction | 0:00 - 0:30 | Présentation générale |
| Service REST | 0:30 - 2:00 | Swagger + tests API |
| Consommation interne | 2:00 - 3:30 | BookClient + code |
| Service externe | 3:30 - 5:00 | WeatherClient + code |
| Frontend | 5:00 - 5:30 | Interface web |
| Conclusion | 5:30 - 6:00 | Récapitulatif |
| Questions | 6:00 - ... | Réponses |

---

## 🎬 Action !

### Dernière vérification avant de commencer
```bash
# Vérifier que l'application tourne
curl http://localhost:8081/api/books

# Si ça retourne du JSON, vous êtes prêt !
```

**Bonne présentation ! 🚀**

---

## 📸 Screenshots Suggérés

Si vous devez faire un rapport avec captures d'écran:

1. **Swagger UI** - Documentation complète
2. **GET /api/books** - Liste des livres
3. **POST /api/books** - Création d'un livre
4. **Code BookClient** - Consommation interne
5. **GET /api/demo/books-client** - Résultat client
6. **Code WeatherClient** - Consommation externe
7. **GET /api/weather/tunis** - Météo temps réel
8. **Page d'accueil** - Dashboard
9. **Page livres** - Interface CRUD
10. **Page météo** - Affichage temps réel

---

## 🏆 Points Forts à Mentionner

1. **Architecture propre** - MVC, séparation des couches
2. **Code documenté** - Commentaires JavaDoc
3. **Tests fonctionnels** - Tout marche !
4. **Frontend moderne** - Design professionnel
5. **Documentation complète** - 10 fichiers MD
6. **Bonus** - Interface web en plus du backend
7. **Technologies actuelles** - Spring Boot 3, Java 17
8. **API documentée** - Swagger intégré
9. **Facile à démarrer** - Une seule commande
10. **Prêt pour production** - Architecture scalable

---

**Confiance et préparation = Succès garanti ! 🎓✨**
