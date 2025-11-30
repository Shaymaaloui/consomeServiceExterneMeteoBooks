# 📦 Guide d'Installation Complet

## 🔧 Prérequis

### Obligatoire
- **Java 17 ou supérieur**
  - Vérifier: `java -version`
  - Télécharger: https://adoptium.net/

### Option 1: Maven
- **Maven 3.6+**
  - Vérifier: `mvn -version`
  - Télécharger: https://maven.apache.org/download.cgi

### Option 2: IDE
- **IntelliJ IDEA** (Community ou Ultimate)
- **Eclipse** avec Spring Tools
- **VS Code** avec extensions Java

## 📥 Installation

### Méthode 1: Ligne de commande avec Maven

```bash
# 1. Naviguer vers le dossier du projet
cd c:\Users\Dell\Desktop\projetSOA

# 2. Compiler le projet
mvn clean install

# 3. Démarrer l'application
mvn spring-boot:run

# L'application démarre sur http://localhost:8081
```

### Méthode 2: IntelliJ IDEA

1. **Ouvrir le projet**
   - File → Open
   - Sélectionner le dossier `projetSOA`
   - Attendre l'import Maven automatique

2. **Configurer le JDK**
   - File → Project Structure → Project
   - Sélectionner JDK 17+

3. **Exécuter l'application**
   - Naviguer vers `WebServicesApplication.java`
   - Clic droit → Run 'WebServicesApplication'
   - Ou cliquer sur le bouton vert ▶️

4. **Accéder à l'application**
   - Ouvrir http://localhost:8081/

### Méthode 3: Eclipse

1. **Importer le projet**
   - File → Import → Existing Maven Project
   - Sélectionner le dossier `projetSOA`
   - Finish

2. **Attendre la synchronisation Maven**
   - Laisser Eclipse télécharger les dépendances

3. **Exécuter l'application**
   - Clic droit sur le projet
   - Run As → Spring Boot App

4. **Accéder à l'application**
   - Ouvrir http://localhost:8081/

### Méthode 4: VS Code

1. **Installer les extensions nécessaires**
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - Maven for Java

2. **Ouvrir le projet**
   - File → Open Folder
   - Sélectionner `projetSOA`

3. **Exécuter l'application**
   - Ouvrir `WebServicesApplication.java`
   - F5 ou Run → Start Debugging
   - Ou utiliser le terminal: `mvn spring-boot:run`

4. **Accéder à l'application**
   - Ouvrir http://localhost:8081/

## ✅ Vérification de l'installation

### 1. Backend démarré
Vous devriez voir dans les logs:
```
Started WebServicesApplication in X.XXX seconds
```

### 2. Tester l'API
Ouvrir dans le navigateur:
- API Docs: http://localhost:8081/swagger-ui.html
- Console H2: http://localhost:8081/h2-console

### 3. Tester le Frontend
Ouvrir dans le navigateur:
- Accueil: http://localhost:8081/
- Livres: http://localhost:8081/books.html
- Météo: http://localhost:8081/weather.html

### 4. Test rapide
```bash
# Tester l'API Books
curl http://localhost:8081/api/books

# Devrait retourner un JSON avec 5 livres
```

## 🐛 Dépannage

### Problème 1: Port 8081 déjà utilisé

**Solution A**: Changer le port
```properties
# Dans src/main/resources/application.properties
server.port=8082
```

**Solution B**: Tuer le processus
```bash
# Windows
netstat -ano | findstr :8081
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8081 | xargs kill -9
```

### Problème 2: Maven introuvable

**Option 1**: Installer Maven
- Windows: Chocolatey → `choco install maven`
- Linux: `sudo apt install maven`
- Mac: `brew install maven`

**Option 2**: Utiliser l'IDE
- Tous les IDEs modernes incluent Maven

### Problème 3: Java version incorrecte

**Vérifier la version**:
```bash
java -version
```

**Si version < 17**:
- Télécharger Java 17: https://adoptium.net/
- Définir JAVA_HOME
- Windows: Panneau de configuration → Variables d'environnement
- Linux/Mac: Ajouter à `~/.bashrc`: `export JAVA_HOME=/path/to/java17`

### Problème 4: Erreurs de dépendances Maven

**Solution**:
```bash
# Nettoyer et réinstaller
mvn clean install -U

# Si ça persiste, supprimer le cache local
rm -rf ~/.m2/repository
mvn clean install
```

### Problème 5: Frontend ne charge pas les données

**Vérifications**:
1. Backend est démarré ✓
2. Port correct dans le code JavaScript (8081) ✓
3. Ouvrir la console navigateur (F12) pour voir les erreurs
4. Vérifier le CORS (déjà configuré dans Spring Boot)

**Solution**: Vider le cache du navigateur
- Chrome: Ctrl+Shift+Delete
- Firefox: Ctrl+Shift+Delete

### Problème 6: Base de données H2 inaccessible

**Vérification**:
```properties
# Dans application.properties
spring.h2.console.enabled=true
```

**Accès**:
- URL: http://localhost:8081/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (vide)

## 🔐 Configuration avancée

### Changer la base de données

Pour utiliser MySQL au lieu de H2:

1. **Ajouter la dépendance** dans `pom.xml`:
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

2. **Modifier** `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bibliotheque
spring.datasource.username=root
spring.datasource.password=votre_password
spring.jpa.hibernate.ddl-auto=update
```

### Activer le mode production

```properties
# Désactiver la console H2
spring.h2.console.enabled=false

# Désactiver les logs SQL
spring.jpa.show-sql=false

# Niveau de log
logging.level.root=WARN
```

### Configurer HTTPS

```properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=password
server.ssl.key-store-type=PKCS12
```

## 📊 Performances

### Mémoire JVM recommandée
```bash
# Démarrage avec plus de mémoire
java -Xmx512m -jar target/webservices-project-1.0.0.jar
```

### Compilation optimisée
```bash
# Compiler en mode production
mvn clean package -DskipTests
```

## 🚀 Déploiement

### Créer un JAR exécutable

```bash
# 1. Compiler
mvn clean package

# 2. Le JAR est créé dans target/
# webservices-project-1.0.0.jar

# 3. Exécuter
java -jar target/webservices-project-1.0.0.jar
```

### Exécuter en arrière-plan (Linux/Mac)

```bash
nohup java -jar target/webservices-project-1.0.0.jar > app.log 2>&1 &
```

### Service Windows

Créer un fichier `start.bat`:
```batch
@echo off
java -jar webservices-project-1.0.0.jar
pause
```

## ✅ Checklist post-installation

- [ ] Java 17+ installé
- [ ] Maven installé (ou IDE configuré)
- [ ] Projet compilé sans erreurs
- [ ] Application démarre sur port 8081
- [ ] Swagger accessible
- [ ] Frontend accessible
- [ ] API Books fonctionne
- [ ] API Weather fonctionne
- [ ] Données de test chargées

## 📞 Support

Si vous rencontrez des problèmes:

1. **Vérifier les logs** dans la console
2. **Consulter** `GUIDE_UTILISATION.md`
3. **Tester l'API** via Swagger
4. **Vérifier** que toutes les dépendances sont téléchargées

## 🎓 Pour la démonstration

### Démarrage rapide
```bash
cd projetSOA
mvn spring-boot:run
```

### Ouvrir les pages importantes
1. Frontend: http://localhost:8081/
2. Swagger: http://localhost:8081/swagger-ui.html
3. Livres: http://localhost:8081/books.html
4. Météo: http://localhost:8081/weather.html

### Prêt à présenter ! 🎉
