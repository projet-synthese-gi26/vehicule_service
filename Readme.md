# Template Hexagonal Reactive - com.yowyob

Ce projet est le **socle standard** pour le développement de microservices backend chez **com.yowyob**. Il fournit une structure clé en main respectant les principes de l'**Architecture Hexagonale** sur une stack technologique entièrement **Réactive** (Non-bloquante).

## 📘 À propos de ce Template

### Objectif
L'objectif de ce repository n'est pas d'être un simple "Hello World", mais de fournir un **exemple complet et réaliste** d'un microservice de production. Il a été réalisé pour standardiser nos développements, assurer la cohérence du code entre les équipes et faciliter la maintenance.

### Scénario d'implémentation (Comment il a été réalisé)
Pour démontrer les capacités du template, nous avons implémenté un cas d'usage fonctionnel complet : **"La création d'un produit"**.

Ce scénario a été choisi car il traverse toutes les couches techniques nécessaires dans un vrai système distribué :

1.  **Entrée API** : Réception d'une requête POST (Layer REST).
2.  **Validation Métier** : Appel à un service externe (Stock Service) pour vérifier la capacité via HTTP.
3.  **Persistance** : Sauvegarde des données dans PostgreSQL (R2DBC).
4.  **Performance** : Mise en cache du résultat dans Redis.
5.  **Communication Asynchrone** : Publication d'un événement "ProductCreated" dans Kafka.
6.  **Résilience** : Gestion des pannes du service externe via un Circuit Breaker.

Ce flux permet aux développeurs de voir concrètement comment enchaîner ces opérations de manière réactive (Reactor/Mono/Flux) tout en gardant le cœur du métier pur.

---

## 📋 Prérequis

- **Java 21**
- **Maven 3.8+**
- **Docker** (Optionel)

## 🏗 Architecture du Projet

L'architecture est divisée en trois couches distinctes pour isoler la logique métier des technologies.

### 1. Domain (`src/main/java/com/yowyob/template/domain`)
C'est le cœur du métier. **Aucune dépendance Spring** ne doit se trouver ici (sauf exception rare).
- **`model/`** : Les objets métiers (Records Java).
- **`ports/in/`** : Les interfaces (Use Cases) définissant ce que l'application *peut faire* (ex: `CreateProductUseCase`).
- **`ports/out/`** : Les interfaces définissant ce dont l'application *a besoin* (ex: Repository, Client API, Bus d'événements).
- **`exception/`** : Exceptions métier.

### 2. Application (`src/main/java/com/yowyob/template/application`)
L'orchestrateur.
- Implémente les interfaces `ports/in`.
- Utilise les interfaces `ports/out`.
- Contient la logique des flux (Flows reactor).

### 3. Infrastructure (`src/main/java/com/yowyob/template/infrastructure`)
Tout ce qui est technique. C'est ici qu'on implémente Spring, les bases de données, etc.
- **`adapters/inbound/`** : Ce qui *entre* dans l'app (Controlleurs REST, Listeners Kafka).
- **`adapters/outbound/`** : Ce qui *sort* ou stocke (Implémentation R2DBC, Client WebClient, Redis, Kafka Producer).
- **`config/`** : Configuration Spring (Beans, Security, Serializers).
- **`mappers/`** : Conversion entre les DTOs, les Entités et le Domaine (MapStruct).


## ⚙️ Configuration et URLs Externes

La configuration est centralisée dans `src/main/resources/application.yml`.

### Comment définir l'URL d'un Microservice externe ?

Pour appeler un autre service (ex: Stock Service), nous utilisons `WebClient` configuré via une interface déclarative (Http Interface Client).

1. **Définir l'URL dans le YAML** :
   Dans `application.yml` (ou `prod.application.yml`), localisez la section `application.external` :
   ```yaml
   application:
     external:
       stock-service-url: http://168.119.122.86:8081
   ```

2. **Injection dans le Client** :
   Dans `WebClientConfig.java`, cette valeur est injectée via `@Value("${application.external.stock-service-url}")` et passée au builder du client HTTP.

3. **Utilisation** :
   L'adaptateur (`StockAdapter`) utilise l'interface `StockApiClient` sans se soucier de l'URL.

### Configuration Infrastructure (DB, Redis, Kafka)

Les accès aux services de données sont définis dans les fichiers YAML.
- **Profil par défaut (`application.yml`)** : Configuré pour le développement (Redis standalone).
- **Profil Prod (`prod.application.yml`)** : Configuré pour la production (Redis Cluster, IPs statiques).

---

## 🚀 Comment Lancer l'Application

### 1. En local (Développement)

Assurez-vous que vos services (Postgres, Kafka, Redis) sont accessibles aux adresses définies dans `application.yml`.

```bash
mvn clean install
mvn spring-boot:run
```

### 2. Via Docker

Pour construire l'image et lancer un conteneur :

```bash
# Construire l'image
docker build -t reactive-hexagonal .

# Lancer (en pointant vers le profil prod par exemple)
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod reactive-hexagonal
```

---

## 🛠 Guide du Développeur : Comment ajouter une fonctionnalité ?

Si vous devez ajouter une fonctionnalité (ex: "Supprimer un produit"), suivez cet ordre strict :

1.  **Domain (Port In)** : Créez l'interface `DeleteProductUseCase` dans `domain/ports/in`.
2.  **Domain (Port Out)** : Si besoin d'accès DB, créez/maj `ProductRepositoryPort` dans `domain/ports/out`.
3.  **Application** : Implémentez le UseCase dans `ProductService` (ou un nouveau service `DeleteProductService`).
4.  **Infrastructure (Out)** : Implémentez la méthode de suppression dans `PostgresR2dbcAdapter`.
5.  **Infrastructure (In)** : Ajoutez l'endpoint `DELETE` dans `ProductController`.

### Résilience (Circuit Breaker)

Les appels externes sont protégés par Resilience4j.
Pour modifier les seuils (ex: passer à 10s d'attente), modifiez la section `resilience4j` dans le fichier YAML.

```yaml
resilience4j:
  circuitbreaker:
    instances:
      stock-service:
        failureRateThreshold: 50 # Ouvre le circuit si 50% d'échecs
```

---

## ✅ Vérifications avant commit

- [ ] Les DTOs (Infrastructure) sont séparés des Objets du Domaine (Domain).
- [ ] Aucune annotation Spring (`@Service`, `@Component`) dans le package `domain`.
- [ ] Les tests unitaires couvrent la couche Application.

## 📚 Documentation de l'API (Swagger UI)

Le projet intègre **OpenAPI 3** (via Springdoc) pour documenter et tester les endpoints automatiquement.
test

Une fois l'application lancée, la documentation est accessible ici :

- **Interface Visuelle (Swagger UI)** : [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Définition JSON (OpenAPI)** : [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
