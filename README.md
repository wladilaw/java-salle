# 🏋️‍♂️ Système de Gestion de Salle de Sport

Projet Java pour la gestion complète d'une salle de sport avec intégration base de données MySQL.

## 📋 Table des matières

- [Fonctionnalités](#-fonctionnalités)
- [Architecture](#-architecture)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Utilisation](#-utilisation)
- [Structure du projet](#-structure-du-projet)
- [Base de données](#-base-de-données)

## ✨ Fonctionnalités

### 👥 Gestion des Membres - COMPLET
- ✅ Inscription et CRUD complet
- ✅ Recherche avancée (ID, email, type abonnement)
- ✅ Gestion statut (actif/inactif)
- ✅ Statistiques en temps réel
- ✅ Validation et sécurité

### 👨‍🏫 Gestion des Entraîneurs - COMPLET
- ✅ CRUD complet avec spécialités
- ✅ Recherche par spécialité et disponibilité  
- ✅ Gestion des entraîneurs libres
- ✅ Statistiques par spécialité
- ✅ Validation des données

### 🏃‍♂️ Gestion des Cours - COMPLET
- ✅ Création et planification intelligente
- ✅ Attribution automatique d'entraîneurs
- ✅ Gestion capacité et disponibilité
- ✅ Recherche par période et entraîneur
- ✅ Cours récurrents et statistiques

### 🤝 Gestion des Participations - COMPLET
- ✅ Inscription membres aux cours
- ✅ Historique complet des participations
- ✅ Recherche par membre et période
- ✅ Statistiques de fréquentation
- ✅ Vérification des doublons

### 🏋️‍♀️ Gestion des Équipements - EN DÉVELOPPEMENT
- 🔄 Inventaire des machines et accessoires
- 🔄 Suivi de l'état des équipements  
- 🔄 Maintenance préventive

### 📋 Gestion des Abonnements - EN DÉVELOPPEMENT
- 🔄 Types d'abonnements multiples
- 🔄 Gestion des échéances
- 🔄 Renouvellements automatiques

## 🏗️ Architecture

Le projet suit une **architecture en couches** avec les principes SOLID :

```
├── Model (Entités métier)
├── Repository (Accès aux données)
├── Service (Logique métier)
├── Config (Configuration BDD)
└── Demo (Application de test)
```

**Patterns utilisés :**
- 🔄 **Repository Pattern** : Abstraction de la couche de données
- 🎯 **Singleton Pattern** : Gestion unique de la connexion BDD
- 🏗️ **Service Layer** : Séparation de la logique métier
- 🎭 **Facade Pattern** : Interface simplifiée

## 🔧 Prérequis

### Logiciels requis
- ☕ **Java 11** ou supérieur
- 🔧 **Maven 3.6+**
- 🗄️ **MySQL 8.0+** (ou MariaDB 10.5+)
- 💻 **IDE** (IntelliJ IDEA, Eclipse, VS Code)

### Vérification des prérequis
```bash
# Vérifier Java
java -version

# Vérifier Maven
mvn -version

# Vérifier MySQL
mysql --version
```

## 📦 Installation

### 1. Cloner le projet
```bash
git clone <url-du-projet>
cd java
```

### 2. Installer les dépendances
```bash
mvn clean install
```

### 3. Configurer MySQL
```bash
# Se connecter à MySQL
mysql -u root -p

# Créer la base de données
source db/bdd.txt
```

## ⚙️ Configuration

### 1. Configuration de la base de données

Modifiez les paramètres dans `src/main/java/com/salle/config/DatabaseConfig.java` :

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/gymdb";
private static final String DB_USER = "root";        // Votre utilisateur MySQL
private static final String DB_PASSWORD = "";        // Votre mot de passe MySQL
```

### 2. Variables d'environnement (optionnel)
```bash
export MYSQL_USER="root"
export MYSQL_PASSWORD="votre_mot_de_passe"
export MYSQL_URL="jdbc:mysql://localhost:3306/gymdb"
```

## 🚀 Utilisation

### 1. Exécution via Maven
```bash
# Compiler le projet
mvn compile

# Exécuter l'application de démonstration simple (membres uniquement)
mvn exec:java -Dexec.mainClass="com.salle.demo.GymApp"

# Exécuter la démonstration complète (toutes les entités)
mvn exec:java -Dexec.mainClass="com.salle.demo.CompleteDemoApp"
```

### 2. Exécution via JAR
```bash
# Créer le JAR exécutable
mvn package

# Exécuter le JAR
java -jar target/gestion-salle-sport-executable.jar
```

### 3. Exemple d'utilisation du code

```java
// === GESTION DES MEMBRES ===
MembreService membreService = new MembreService();

// Inscrire un nouveau membre
Membre nouveauMembre = membreService.inscrireMembre(
    "Dupont", "Jean", "jean.dupont@email.com", 
    "0123456789", "MENSUEL"
);

// Rechercher un membre par email
Membre membre = membreService.trouverMembreParEmail("jean.dupont@email.com");

// Obtenir tous les membres actifs
List<Membre> membresActifs = membreService.obtenirMembresActifs();

// === GESTION DES ENTRAÎNEURS ===
EntraineurService entraineurService = new EntraineurService();

// Inscrire un entraîneur
Entraineur entraineur = entraineurService.inscrireEntraineur(
    "Martin", "Paul", "paul.martin@email.com", 
    "0123456789", "Musculation"
);

// Trouver entraîneurs par spécialité
List<Entraineur> musculation = entraineurService.obtenirEntraineursParSpecialite("Musculation");

// === GESTION DES COURS ===
CoursService coursService = new CoursService();

// Créer un cours
Cours cours = coursService.creerCours(
    "Musculation Débutant", "Cours pour débutants", 25.0,
    LocalDateTime.now().plusDays(1), 60, 15, entraineur
);

// Cours disponibles
List<Cours> coursDisponibles = coursService.obtenirCoursDisponibles();

// === GESTION DES PARTICIPATIONS ===
ParticipationRepository participationRepo = new ParticipationRepository();

// Inscrire un membre à un cours
Participation participation = new Participation(cours.getId(), membre.getId(), LocalDateTime.now());
participationRepo.create(participation);

// Voir les participations d'un membre
List<Participation> participations = participationRepo.findByMembreId(membre.getId());

// === STATISTIQUES ===
long totalMembres = membreService.obtenirNombreTotalMembres();
long totalEntraineurs = entraineurService.obtenirNombreTotalEntraineurs();
long totalCours = coursService.obtenirNombreTotalCours();
```

## 📁 Structure du projet

```
src/main/java/com/salle/
├── config/
│   └── DatabaseConfig.java       # Configuration BDD
├── interfaces/
│   ├── IRepository.java          # Interface générique CRUD
│   ├── IMembreRepository.java    # Interface membres
│   ├── IEntraineurRepository.java # Interface entraîneurs
│   ├── ICoursRepository.java     # Interface cours
│   ├── IParticipationRepository.java # Interface participations
│   ├── IEquipementRepository.java # Interface équipements
│   ├── IAbonnementRepository.java # Interface abonnements
│   └── IGestionStock.java        # Interface équipements (legacy)
├── model/
│   ├── Personne.java            # Classe abstraite
│   ├── Membre.java              # Entité membre
│   ├── Entraineur.java          # Entité entraîneur
│   ├── Cours.java               # Entité cours
│   ├── Participation.java       # Entité participation (many-to-many)
│   ├── Equipement.java          # Entité équipement
│   ├── Machine.java             # Sous-classe machine
│   ├── Accessoire.java          # Sous-classe accessoire
│   └── Abonnement.java          # Entité abonnement
├── repository/
│   ├── MembreRepository.java    # CRUD membres
│   ├── EntraineurRepository.java # CRUD entraîneurs
│   ├── CoursRepository.java     # CRUD cours
│   └── ParticipationRepository.java # CRUD participations
├── service/
│   ├── MembreService.java       # Logique métier membres
│   ├── EntraineurService.java   # Logique métier entraîneurs
│   └── CoursService.java        # Logique métier cours
└── demo/
    ├── GymApp.java              # Demo simple (membres)
    └── CompleteDemoApp.java     # Demo complète (toutes entités)
```

## 🗄️ Base de données

### Schéma de la base

La base de données `gymdb` contient les tables suivantes :

- 👤 **Personne** : Informations de base
- 👥 **Membre** : Détails des membres
- 👨‍🏫 **Entraineur** : Profils des entraîneurs
- 🏃‍♂️ **Cours** : Planning des cours
- 🏋️‍♀️ **Equipement** : Inventaire des équipements
- 🤝 **Participation** : Inscriptions aux cours
- 📝 **Abonnement** : Gestion des abonnements
- 📢 **Notification** : Système de notifications

### Relations principales

```
Personne (1) ←→ (N) Membre
Personne (1) ←→ (N) Entraineur
Entraineur (1) ←→ (N) Cours
Membre (N) ←→ (N) Cours (via Participation)
Equipement (1) ←→ (1) Machine/Accessoire
```

## 🧪 Tests

### Exécuter les tests
```bash
# Tous les tests
mvn test

# Tests spécifiques
mvn test -Dtest=MembreServiceTest
```

### Tests disponibles
- ✅ Tests unitaires des services
- ✅ Tests d'intégration de la BDD
- ✅ Tests des repositories

## 📊 Fonctionnalités avancées

### 1. Gestion transactionnelle
- Rollback automatique en cas d'erreur
- Intégrité des données garantie

### 2. Pool de connexions (à implémenter)
- Optimisation des performances
- Gestion efficace des ressources

### 3. Logging
- Suivi des opérations
- Gestion des erreurs

## 🔒 Sécurité

- ✅ Protection contre l'injection SQL (PreparedStatement)
- ✅ Validation des données d'entrée
- ✅ Gestion propre des exceptions
- ⚠️ **À faire** : Chiffrement des mots de passe

## 🐛 Dépannage

### Erreur de connexion MySQL
```
❌ Erreur de connexion à la base de données
```
**Solutions :**
1. Vérifier que MySQL est démarré
2. Contrôler les identifiants dans `DatabaseConfig.java`
3. Vérifier que la base `gymdb` existe

### Erreur de driver JDBC
```
❌ Driver MySQL non trouvé
```
**Solution :**
```bash
mvn clean install
```

### Erreur de compilation
```
❌ Text Blocks feature not available
```
**Solution :** Utiliser Java 11+ ou mettre à jour la syntaxe

## 🤝 Contribution

1. Fork le projet
2. Créer une branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📝 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 👥 Auteurs

- **Votre équipe** - Développement initial

## 🎯 Prochaines étapes

- [ ] Interface graphique (JavaFX/Swing)
- [ ] API REST (Spring Boot)
- [ ] Authentification et autorisation
- [ ] Pool de connexions (HikariCP)
- [ ] Tests de performance
- [ ] Documentation API

---

💡 **Astuce :** Pour tester rapidement le système, exécutez `GymApp.java` qui contient une démonstration complète de toutes les fonctionnalités !

🆘 **Support :** En cas de problème, vérifiez la section [Dépannage](#-dépannage) ou ouvrez une issue.