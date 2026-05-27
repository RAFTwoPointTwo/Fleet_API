# Fleet API

> API REST backend pour la gestion de flotte informatique — Développée avec Spring Boot

---

## 📋 À propos

**Fleet API** est une API REST complète permettant de gérer une flotte d'équipements informatiques au sein d'une organisation. Elle couvre l'ensemble du cycle de vie des assets : de leur enregistrement jusqu'à leur maintenance, en passant par les assignations, les demandes et le suivi des pannes.

---

## ⚙️ Stack technique

| Technologie | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.6 |
| Spring Security | Session-based |
| Spring Data JPA | Hibernate 6 |
| MySQL / MariaDB | 10.4+ |
| Lombok | Latest |
| Springdoc OpenAPI | Swagger UI |
| Maven | Build tool |

---

## 🏗️ Architecture

```
src/main/java/com/fleet/fleet_api/
├── configs/          # Configurations métier globales
├── controllers/      # Endpoints REST
├── dtos/             # Records request/response par entité
├── exceptions/       # Exceptions custom + GlobalExceptionHandler
├── mappers/          # Conversion entité ↔ DTO
├── models/           # Entités JPA
├── repositories/     # Spring Data JPA repositories
├── services/         # Logique métier
└── utilities/        # Enums métier
```

### Principes appliqués
- **Séparation stricte** Entités / DTOs — Les Entités sont converties en objets sécurisés et optimals pour chaque réponse
- **Transactions** appliquées pour l'optimisation des requêtes
- **Logs métier** — Chaque évènement majeur est tracé dans les `fleet_logs`

---

## 📦 Entités principales

| Entité | Description |
|---|---|
| `User` | Utilisateurs |
| `Department` | Départements de l'organisation |
| `Category` | Catégories d'assets |
| `Asset` | Équipements |
| `Pack` | Regroupement d'assets assignables ensemble |
| `Assignment` | Affectation d'un asset ou pack à un utilisateur |
| `Request` | Demande d'asset ou pack par un utilisateur |
| `Breakdown` | Déclaration de panne sur un asset |
| `Maintenance` | Suivi de maintenance corrective ou préventive |
| `FleetLog` | Journal des événements métier |

---

## 🔐 Authentification

L'authentification est gérée par **session HTTP** via Spring Security.

```
POST /api/users/register   → Créer un compte
POST /api/users/login      → Authentification + création de session
POST /api/users/logout     → Invalidation de session
```

Un **admin initial** est automatiquement créé au premier démarrage via `AdminInitializer`, configurable par variables d'environnement.

---

## 📡 Endpoints

### Users `/api/users`
| Méthode | Route | Description |
|---|---|---|
| GET | `/` | Liste tous les utilisateurs |
| GET | `/{id}` | Détail d'un utilisateur |
| GET | `/search?name=` | Recherche par nom |
| GET | `/department/{id}` | Users d'un département |
| GET | `/role/{role}` | Users par rôle |
| POST | `/register` | Inscription |
| POST | `/login` | Connexion |
| POST | `/logout` | Déconnexion |
| PATCH | `/{id}/profile` | Modifier son profil |
| PATCH | `/{id}/update/role` | Modifier le rôle (Admin) |

### Assets `/api/assets`
| Méthode | Route | Description |
|---|---|---|
| GET | `/` | Liste tous les assets |
| GET | `/{id}` | Détail d'un asset |
| GET | `/available` | Assets disponibles |
| GET | `/status/{status}` | Assets par statut |
| GET | `/category/{id}` | Assets d'une catégorie |
| GET | `/pack/{id}` | Assets d'un pack |
| POST | `/` | Créer un asset |
| PATCH | `/{id}` | Modifier un asset |
| DELETE | `/{id}` | Supprimer un asset |

### Assignments `/api/assignments`
| Méthode | Route | Description |
|---|---|---|
| POST | `/asset` | Assigner un asset |
| POST | `/pack` | Assigner un pack |
| PATCH | `/{id}/end` | Terminer une assignation |

### Requests `/api/requests`
| Méthode | Route | Description |
|---|---|---|
| POST | `/asset` | Demande d'asset |
| POST | `/pack` | Demande de pack |
| PATCH | `/{id}/validate` | Valider / Rejeter |
| PATCH | `/{id}/cancel` | Annuler |

### Maintenances `/api/maintenances`
| Méthode | Route | Description |
|---|---|---|
| POST | `/` | Créer une maintenance |
| PATCH | `/{id}/end` | Terminer une maintenance |

> Les routes complètes avec tous les filtres sont disponibles dans la documentation Swagger.

---

## Lancer le projet

### Prérequis
- Java 21 ou plus
- MySQL / MariaDB
- Maven

### Configuration

Créer les variables d'environnement ou modifier `application.properties` :

```properties
DB_HOST=localhost
DB_PORT=3307
DB_NAME=YOUR_DATABASE_NAME
DB_USER=YOUR_USERNAME
DB_PASS=YOUR_DATABASE_PASSWORD
SERVER_PORT=8081
INITIAL_ADMIN_EMAIL=YOUR_ADMIN_EMAIL
INITIAL_ADMIN_PASSWORD=YOUR_ADMIN_PASSWORD
```

### Démarrage

```bash
mvn spring-boot:run
```

La base de données est créée automatiquement au premier lancement via `schema.sql`.

---

## 📖 Documentation API

Swagger UI disponible après démarrage :

```
http://localhost:8081/swagger-ui/index.html (Ajustez les paramètres de l'url selon votre configuration)
```

---

## 🛡️ Gestion des erreurs

Toutes les exceptions sont centralisées dans `GlobalExceptionsHandler` et renvoient une réponse JSON générique, pour une analyse efficiente des erreurs.

| Exception | HTTP Status |
|---|---|
| `ResourceNotFoundException` | 404 |
| `DuplicateResourceException` | 409 |
| `InvalidAssetStateException` | 422 |
| `InvalidRequestParamException` | 400 |
| `AuthException` | 401 |
| `MethodArgumentNotValidException` | 400 |

---

## 📝 Logs métier

Chaque action significative génère une entrée dans `fleet_logs` :

```
[ Enregistrement d'asset ] Nom : MacBook Pro
[ Enregistrement d'assignation ] MacBook Pro assigné à John Doe
[ Fin d'assignation ] Affectation à John Doe terminée
```

---

## 👤 Auteur

Développé par **HOUINSOU Raphaël** — Étudiant en Génie Logiciel, IFRI — Université d'Abomey-Calavi, Bénin.
