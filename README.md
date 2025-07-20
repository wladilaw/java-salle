# Présentation

Ce projet est une application de gestion de salle de sport écrite en Java. Elle permet de gérer les membres, les abonnements, les cours, les entraîneurs, les équipements, etc.

# Lancer le projet

## Prérequis
- Java (JDK 17 ou supérieur recommandé)
- (Optionnel) Un SGBD compatible (H2 ou MySQL, les drivers sont fournis dans le dossier `lib/`)

## Compilation et exécution

1. Ouvrir un terminal à la racine du projet.
2. Pour compiler et exécuter avec la base H2 embarquée :
   - Sous Windows :
     - Double-cliquer sur `compile_and_run_h2.bat` ou exécuter :
       ```
       ./compile_and_run_h2.bat
       ```
   - Sous PowerShell :
       ```
       ./compile_and_run.ps1
       ```
   - Pour la version corrigée H2 :
       ```
       ./compile_and_run_h2_fixed.bat
       ```
3. Pour utiliser une autre base ou personnaliser, adapter les scripts ou compiler manuellement les fichiers Java dans `src/`.

# Architecture des dossiers

- `src/main/java/com/salle/` : Code source principal
  - `config/` : Configuration de la base de données (ex : DatabaseConfig.java)
  - `demo/` : Applications de démonstration (ex : CompleteDemoApp.java, GymApp.java)
  - `interfaces/` : Interfaces des repositories et services (ex : IMembreRepository.java)
  - `model/` : Modèles métiers (ex : Membre.java, Abonnement.java, Equipement.java...)
  - `repository/` : Implémentations des accès aux données (ex : MembreRepository.java)
  - `service/` : Logique métier (ex : MembreService.java)
  - Fichiers principaux :
    - `GestionSalleSport.java` : Point d'entrée principal de l'application
    - `InteractiveGymApp.java` : Application interactive
    - `Database.java` : Gestion de la connexion à la base

- `db/` : Scripts ou fichiers liés à la base de données (ex : bdd.txt)
- `docs/` : Diagrammes UML et documentation (diagramme de classes, séquence, activité, etc.)
- `lib/` : Librairies externes nécessaires (drivers H2 et MySQL)
- `prisma/` : Fichier de schéma Prisma (optionnel, pour la génération de schémas ou documentation)

# Auteurs
- Jules
- Jeremie
- dervax
- Alexis
