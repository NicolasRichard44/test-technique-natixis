# Task Management System

Ce projet est une application complète de gestion de tâches, construite avec une architecture moderne full-stack :
- **Frontend** : Angular (version 19)
- **Backend** : Spring Boot (version 3.2.3)

L'application permet de créer, lister, et gérer des tâches via une interface utilisateur locale accessible via un navigateur.

## Structure du projet

Le projet est divisé en deux parties principales:

```
task-management-back/     # Backend Spring Boot
task-management-front/    # Frontend Angular
```

## Backend (Spring Boot)

### Technologies utilisées

- **Spring Boot** : 3.2.3
- **Java** : 21
- **Spring Security** : Pour la configuration de sécurité
- **Lombok** : Pour réduire le code boilerplate
- **SpringDoc OpenAPI** : Pour la documentation API
- **JaCoCo** : Pour la couverture de tests

### Architecture

Le backend est structuré selon les principes d'architecture hexagonale (ports et adaptateurs):

```
fr.natixis
├── api/                  # Contrôleurs REST et DTOs
├── config/               # Configuration Spring
├── core/                 # Logique métier
│   ├── application/      # Services d'application
│   ├── domain/           # Entités métier
│   └── ports/            # Interfaces pour l'infrastructure
└── infrastructure/       # Implémentations des ports
    └── persistence/      # Stockage des données
```

### API REST

Le backend expose les endpoints suivants:

- `GET /api/tasks` - Récupérer toutes les tâches (avec filtres et pagination)
- `GET /api/tasks/{id}` - Récupérer une tâche par son ID
- `POST /api/tasks` - Créer une nouvelle tâche
- `PUT /api/tasks/{id}/status` - Mettre à jour le statut d'une tâche

La documentation complète de l'API est disponible via Swagger UI: `http://localhost:8080/swagger-ui.html`

### Démarrer le backend

1. Assurez-vous d'avoir Java 21 installé
2. Naviguez vers le répertoire du backend:
   ```bash
   cd task-management-back
   ```
3. Lancez l'application avec Maven:
   ```bash
   mvn spring-boot:run
   ```

Le serveur démarre sur `http://localhost:8080`

### Exécuter les tests

```bash
mvn test
```

Le rapport de couverture JaCoCo sera généré dans `target/site/jacoco/index.html`

## Frontend (Angular)

### Technologies utilisées

- **Angular**: version 19
- **Angular Material**: Pour les composants UI
- **RxJS**: Pour la programmation réactive

### Fonctionnalités

- Tableau de bord des tâches avec pagination
- Filtrage des tâches (toutes, en cours, terminées)
- Création de nouvelles tâches
- Affichage des détails d'une tâche
- Changement du statut d'une tâche (terminée/non terminée)

### Prérequis

- Node.js (version 18 ou supérieure)
- NPM (version 8 ou supérieure)
- Angular CLI (version 19.x)

### Démarrer le frontend

1. Naviguez vers le répertoire du frontend:
   ```bash
   cd task-management-front
   ```
2. Installez les dépendances:
   ```bash
   npm install
   ```
3. Lancez l'application:
   ```bash
   ng serve
   ```

L'application sera accessible à `http://localhost:4200`

### Exécuter les tests

```bash
ng test
```

## Fonctionnement de l'application complète

1. Démarrez le backend Spring Boot (port 8080)
2. Démarrez le frontend Angular (port 4200)
3. Accédez à l'application via `http://localhost:4200`

## Développeur

Ce projet a été développé par Nicolas RICHARD dans le cadre d'un test technique pour Natixis.

## Licence

Propriétaire - Tous droits réservés
