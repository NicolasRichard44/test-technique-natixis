# TaskManagementFront

Cette application web Angular constitue le frontend de notre système de gestion de tâches. Elle permet aux utilisateurs de créer, visualiser, modifier et supprimer des tâches dans une interface intuitive et responsive.

## À propos de l'application

Notre application de gestion de tâches est conçue selon une architecture moderne avec:
- Un frontend Angular (ce projet)
- Un backend Spring Boot RESTful API 

### Fonctionnalités principales

- Tableau de bord des tâches avec visualisation claire
- Création, modification et suppression de tâches
- Filtrage et tri des tâches par différents critères
- Changement de statut des tâches (À faire, En cours, Terminée)
- Interface responsive adaptée à tous les appareils

## Prérequis

- Node.js (version 16 ou supérieure)
- NPM (version 8 ou supérieure)
- Angular CLI (version 19.x)
- Le backend Spring Boot doit être en cours d'exécution pour que l'application fonctionne correctement

## Installation

1. Clonez ce dépôt
2. Naviguez vers le répertoire du projet
3. Installez les dépendances:

```bash
npm install
```

## Configuration

Le fichier `src/environments/environment.ts` contient les paramètres de configuration, notamment l'URL de l'API backend. Assurez-vous que cette URL correspond à l'adresse où votre backend Spring Boot est en cours d'exécution.

## Développement

Pour démarrer un serveur de développement local, exécutez:

```bash
ng serve
```

Une fois le serveur en cours d'exécution, ouvrez votre navigateur et accédez à `http://localhost:4200/`. L'application se rechargera automatiquement si vous modifiez l'un des fichiers source.

## Structure du projet

```
src/
├── app/
│   ├── components/     # Composants réutilisables
│   ├── models/         # Interfaces et classes de modèles
│   ├── services/       # Services pour la communication avec l'API
│   ├── shared/         # Ressources partagées (pipes, directives, etc.)
│   └── ...
├── assets/            # Images, fichiers statiques
├── environments/      # Configuration selon l'environnement
└── ...
```


## Build

Pour compiler le projet, exécutez:

```bash
ng build
```

Les artefacts de build seront stockés dans le répertoire `dist/`. Par défaut, la build de production optimise votre application pour la performance et la vitesse.

## Tests unitaires

Pour exécuter les tests unitaires avec [Karma](https://karma-runner.github.io), utilisez la commande suivante:

```bash
ng test
```

## Tests end-to-end

Pour les tests end-to-end (e2e), exécutez:

```bash
ng e2e
```

Angular CLI n'est pas livré avec un framework de test end-to-end par défaut. Vous pouvez choisir celui qui convient à vos besoins.

## Déploiement

Pour déployer l'application en production:

1. Créez une build de production:
```bash
ng build --configuration production
```

2. Déployez le contenu du dossier `dist/` sur votre serveur web

## Ressources supplémentaires

Pour plus d'informations sur l'utilisation d'Angular CLI, y compris des références détaillées de commandes, visitez la page [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli).

## Contribution

Pour contribuer à ce projet, veuillez:
1. Créer une branche pour votre fonctionnalité
2. Rédiger des tests pour vos changements
3. Assurer que tous les tests passent
4. Soumettre une pull request avec une description claire de vos modifications
