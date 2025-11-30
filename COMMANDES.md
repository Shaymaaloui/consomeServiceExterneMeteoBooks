# Commandes utiles

## 🚀 Compilation et exécution

### Compiler le projet
```bash
mvn clean install
```

### Démarrer l'application
```bash
mvn spring-boot:run
```

### Compiler sans tests
```bash
mvn clean install -DskipTests
```

### Créer un JAR exécutable
```bash
mvn clean package
java -jar target/webservices-project-1.0.0.jar
```

## 🧪 Tests avec curl

### API Books

```bash
# Liste tous les livres
curl http://localhost:8080/api/books

# Récupère un livre par ID
curl http://localhost:8080/api/books/1

# Crée un nouveau livre
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Book",
    "author": "Test Author",
    "isbn": "123-456-789",
    "price": 29.99,
    "description": "Un livre de test",
    "publicationYear": 2024
  }'

# Recherche par auteur
curl "http://localhost:8080/api/books/search/author?author=Martin"

# Recherche par titre
curl "http://localhost:8080/api/books/search/title?title=Clean"

# Met à jour un livre (remplacer 1 par l'ID réel)
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated Book",
    "author": "Updated Author",
    "isbn": "123-456-789",
    "price": 34.99,
    "description": "Livre mis à jour",
    "publicationYear": 2024
  }'

# Supprime un livre (remplacer 6 par l'ID réel)
curl -X DELETE http://localhost:8080/api/books/6
```

### API Weather

```bash
# Météo à Paris
curl http://localhost:8080/api/weather/paris

# Météo à Tunis
curl http://localhost:8080/api/weather/tunis

# Météo par coordonnées (Londres)
curl "http://localhost:8080/api/weather?latitude=51.5074&longitude=-0.1278"
```

### API Demo

```bash
# Livres via le client REST
curl http://localhost:8080/api/demo/books-client

# Livre par ID via le client
curl http://localhost:8080/api/demo/book-client/1

# Démonstration combinée
curl http://localhost:8080/api/demo/combined

# Créer et vérifier un livre
curl -X POST http://localhost:8080/api/demo/create-and-verify \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Demo Book",
    "author": "Demo Author",
    "isbn": "999-999-999",
    "price": 19.99,
    "publicationYear": 2024
  }'
```

## 🧹 Nettoyage

```bash
# Nettoie le projet
mvn clean

# Supprime le répertoire target
rm -rf target/   # Linux/Mac
rmdir /s target  # Windows
```

## 📊 Autres commandes Maven

```bash
# Affiche l'arbre des dépendances
mvn dependency:tree

# Vérifie les mises à jour
mvn versions:display-dependency-updates

# Exécute les tests
mvn test

# Génère un rapport de tests
mvn surefire-report:report
```

## 🌐 URLs importantes

- Application : http://localhost:8080
- Swagger UI : http://localhost:8080/swagger-ui.html
- API Docs : http://localhost:8080/api-docs
- Console H2 : http://localhost:8080/h2-console

## 🔧 Configuration

### Changer le port

Dans `src/main/resources/application.properties` :
```properties
server.port=8081
```

Puis redémarrer l'application.

## 🐛 Dépannage

### Port déjà utilisé

Si le port 8080 est occupé :
1. Changer le port dans `application.properties`
2. Ou tuer le processus :

Windows :
```bash
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

Linux/Mac :
```bash
lsof -ti:8080 | xargs kill -9
```

### Erreur de compilation

```bash
# Nettoyer et recompiler
mvn clean install -U
```

### Base de données corrompue

Redémarrer l'application (H2 en mémoire se réinitialise).

## 📝 Logs

Les logs s'affichent dans la console lors de l'exécution.

Pour ajuster le niveau de log, dans `application.properties` :
```properties
logging.level.com.soa.webservices=DEBUG
```
