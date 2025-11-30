# 🚀 Quick Start - 5 Minutes

## Démarrage rapide

### 1️⃣ Démarrer l'application (1 minute)

```bash
cd projetSOA
mvn spring-boot:run
```

Attendez voir : `Started WebServicesApplication in X seconds`

### 2️⃣ Ouvrir Swagger UI (30 secondes)

Ouvrir dans le navigateur : **http://localhost:8080/swagger-ui.html**

### 3️⃣ Tester les 3 objectifs (3 minutes)

#### ✅ Objectif 1 : Notre service REST

1. Dans Swagger, section **Books**
2. Cliquer sur `GET /api/books`
3. Cliquer sur **Try it out** → **Execute**
4. ✅ Vous voyez 5 livres → **Service REST fonctionne !**

#### ✅ Objectif 2 : Consommer notre service

1. Section **Demo**
2. Cliquer sur `GET /api/demo/books-client`
3. Cliquer sur **Try it out** → **Execute**
4. ✅ Mêmes livres, mais via le client → **Consommation de notre API !**

#### ✅ Objectif 3 : Service externe

1. Section **Weather**
2. Cliquer sur `GET /api/weather/tunis`
3. Cliquer sur **Try it out** → **Execute**
4. ✅ Météo actuelle à Tunis → **API externe fonctionne !**

---

## 🎯 Preuve des 3 objectifs en 1 appel

**URL** : http://localhost:8080/api/demo/combined

Ouvrir dans le navigateur ou :

```bash
curl http://localhost:8080/api/demo/combined
```

**Résultat** :
```json
{
  "totalBooks": 5,
  "books": [...],           ← Notre API
  "weather": {              ← API externe
    "location": "Tunis",
    "temperature": "18°C"
  },
  "message": "Démonstration de la consommation de notre API + API externe"
}
```

---

## 📋 Checklist pour la présentation

- [ ] Application démarrée
- [ ] Swagger ouvert
- [ ] Testé `/api/books` (notre service)
- [ ] Testé `/api/demo/books-client` (consommation)
- [ ] Testé `/api/weather/tunis` (externe)
- [ ] Montré le code de `BookClient.java`
- [ ] Montré le code de `WeatherClient.java`

---

## 🎬 Démonstration complète (Option)

### Créer un livre
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Mon Livre",
    "author": "Mon Auteur",
    "isbn": "123-456",
    "price": 29.99,
    "publicationYear": 2024
  }'
```

### Le récupérer via le client
```bash
curl http://localhost:8080/api/demo/books-client
```

→ Votre livre apparaît ! ✅

---

## 📚 Fichiers importants à montrer

1. **README.md** : Documentation complète
2. **GUIDE_UTILISATION.md** : Guide d'utilisation détaillé
3. **RAPPORT_PROJET.md** : Rapport technique
4. **Code** :
   - `BookController.java` : Notre API REST
   - `BookClient.java` : Consommation de notre API
   - `WeatherClient.java` : Consommation API externe
   - `DemoController.java` : Intégration tout-en-un

---

## ❓ Questions fréquentes

**Q : Ça ne démarre pas ?**
→ Vérifier que Java 17+ est installé : `java -version`

**Q : Port 8080 occupé ?**
→ Changer le port dans `application.properties` : `server.port=8081`

**Q : Où sont les données ?**
→ Base H2 en mémoire, initialisée automatiquement

**Q : Comment prouver la consommation ?**
→ Montrer que `/api/demo/books-client` passe par `BookClient` qui fait un appel HTTP à `/api/books`

---

## 🎓 Pour la présentation

### Script de 2 minutes

1. **"J'ai développé une API REST de gestion de livres"**
   → Montrer `/api/books` dans Swagger

2. **"J'ai créé un client pour consommer ma propre API"**
   → Montrer `BookClient.java` (lignes 20-30)
   → Tester `/api/demo/books-client`

3. **"J'ai intégré une API externe de météo"**
   → Montrer `WeatherClient.java` (lignes 25-40)
   → Tester `/api/weather/tunis`

4. **"Voici une démonstration combinée"**
   → Tester `/api/demo/combined`

**Total : 3 objectifs en 2 minutes !** ✅

---

## 🎉 Félicitations !

Vous avez :
- ✅ Un service web REST complet
- ✅ Un client qui consomme votre service
- ✅ Une intégration avec un service externe
- ✅ Une documentation complète
- ✅ Un projet prêt à présenter

**Bonne présentation ! 🚀**
