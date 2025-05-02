package com.salle;

import com.salle.model.*;
import com.salle.interfaces.IGestionStock;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestionSalleSport {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Membre> membres = new ArrayList<>();
    private static List<Entraineur> entraineurs = new ArrayList<>();
    private static List<Cours> cours = new ArrayList<>();
    private static List<Equipement> equipements = new ArrayList<>();
    private static List<Abonnement> abonnements = new ArrayList<>();
    private static int dernierIdMembre = 0;
    private static int dernierIdCours = 0;
    private static int dernierIdEquipement = 0;

    public static void main(String[] args) {
        boolean continuer = true;
        while (continuer) {
            afficherMenuPrincipal();
            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour à la ligne

            switch (choix) {
                case 1:
                    gererMembres();
                    break;
                case 2:
                    gererCours();
                    break;
                case 3:
                    gererEquipements();
                    break;
                case 0:
                    continuer = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
        scanner.close();
    }

    private static void afficherMenuPrincipal() {
        System.out.println("\n=== GESTION DE SALLE DE SPORT ===");
        System.out.println("1. Gestion des membres");
        System.out.println("2. Gestion des cours");
        System.out.println("3. Gestion des équipements");
        System.out.println("0. Quitter");
        System.out.print("Votre choix : ");
    }

    private static void gererMembres() {
        System.out.println("\n=== GESTION DES MEMBRES ===");
        System.out.println("1. Ajouter un membre");
        System.out.println("2. Modifier un membre");
        System.out.println("3. Supprimer un membre");
        System.out.println("4. Afficher tous les membres");
        System.out.println("0. Retour");
        System.out.print("Votre choix : ");
        
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        switch (choix) {
            case 1:
                ajouterMembre();
                break;
            case 2:
                modifierMembre();
                break;
            case 3:
                supprimerMembre();
                break;
            case 4:
                afficherMembres();
                break;
            case 0:
                return;
            default:
                System.out.println("Choix invalide.");
        }
    }

    private static void gererCours() {
        System.out.println("\n=== GESTION DES COURS ===");
        System.out.println("1. Créer un cours");
        System.out.println("2. Modifier un cours");
        System.out.println("3. Supprimer un cours");
        System.out.println("4. Afficher tous les cours");
        System.out.println("0. Retour");
        System.out.print("Votre choix : ");
        
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        switch (choix) {
            case 1:
                creerCours();
                break;
            case 2:
                modifierCours();
                break;
            case 3:
                supprimerCours();
                break;
            case 4:
                afficherCours();
                break;
            case 0:
                return;
            default:
                System.out.println("Choix invalide.");
        }
    }

    private static void gererEquipements() {
        System.out.println("\n=== GESTION DES ÉQUIPEMENTS ===");
        System.out.println("1. Ajouter un équipement");
        System.out.println("2. Modifier un équipement");
        System.out.println("3. Supprimer un équipement");
        System.out.println("4. Afficher tous les équipements");
        System.out.println("0. Retour");
        System.out.print("Votre choix : ");
        
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        switch (choix) {
            case 1:
                ajouterEquipement();
                break;
            case 2:
                modifierEquipement();
                break;
            case 3:
                supprimerEquipement();
                break;
            case 4:
                afficherEquipements();
                break;
            case 0:
                return;
            default:
                System.out.println("Choix invalide.");
        }
    }

    // Méthodes pour la gestion des membres
    private static void ajouterMembre() {
        System.out.println("\n=== AJOUT D'UN MEMBRE ===");
        
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        
        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();
        
        System.out.print("Email : ");
        String email = scanner.nextLine();
        
        System.out.print("Téléphone : ");
        String telephone = scanner.nextLine();
        
        System.out.print("Type d'abonnement (MENSUEL/ANNUEL) : ");
        String typeAbonnement = scanner.nextLine().toUpperCase();
        
        Membre membre = new Membre(
            ++dernierIdMembre,
            nom,
            prenom,
            email,
            telephone,
            LocalDate.now(),
            typeAbonnement
        );
        
        membres.add(membre);
        System.out.println("Membre ajouté avec succès !");
    }

    private static void modifierMembre() {
        System.out.println("\n=== MODIFICATION D'UN MEMBRE ===");
        System.out.print("ID du membre à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        Membre membre = trouverMembre(id);
        if (membre != null) {
            System.out.print("Nouveau nom [" + membre.getNom() + "] : ");
            String nom = scanner.nextLine();
            if (!nom.isEmpty()) membre.setNom(nom);

            System.out.print("Nouveau prénom [" + membre.getPrenom() + "] : ");
            String prenom = scanner.nextLine();
            if (!prenom.isEmpty()) membre.setPrenom(prenom);

            System.out.print("Nouvel email [" + membre.getEmail() + "] : ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) membre.setEmail(email);

            System.out.print("Nouveau téléphone [" + membre.getTelephone() + "] : ");
            String telephone = scanner.nextLine();
            if (!telephone.isEmpty()) membre.setTelephone(telephone);

            System.out.println("Membre modifié avec succès !");
        } else {
            System.out.println("Membre non trouvé !");
        }
    }

    private static void supprimerMembre() {
        System.out.println("\n=== SUPPRESSION D'UN MEMBRE ===");
        System.out.print("ID du membre à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        Membre membre = trouverMembre(id);
        if (membre != null) {
            membres.remove(membre);
            System.out.println("Membre supprimé avec succès !");
        } else {
            System.out.println("Membre non trouvé !");
        }
    }

    private static void afficherMembres() {
        System.out.println("\n=== LISTE DES MEMBRES ===");
        if (membres.isEmpty()) {
            System.out.println("Aucun membre enregistré.");
        } else {
            for (Membre membre : membres) {
                System.out.println(membre);
            }
        }
    }

    private static Membre trouverMembre(int id) {
        return membres.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Méthodes pour la gestion des cours
    private static void creerCours() {
        System.out.println("\n=== CRÉATION D'UN COURS ===");
        
        System.out.print("Nom du cours : ");
        String nom = scanner.nextLine();
        
        System.out.print("Description : ");
        String description = scanner.nextLine();
        
        System.out.print("Prix : ");
        double prix = scanner.nextDouble();
        scanner.nextLine(); // Consommer le retour à la ligne
        
        System.out.print("Date et heure (format: dd/MM/yyyy HH:mm) : ");
        String dateStr = scanner.nextLine();
        LocalDateTime dateHeure = LocalDateTime.parse(dateStr, 
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        
        System.out.print("Durée (en minutes) : ");
        int duree = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne
        
        System.out.print("Capacité maximale : ");
        int capaciteMax = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne
        
        System.out.print("ID de l'entraîneur : ");
        int idEntraineur = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne
        
        Entraineur entraineur = trouverEntraineur(idEntraineur);
        if (entraineur != null) {
            Cours cours = new Cours(
                ++dernierIdCours,
                nom,
                description,
                prix,
                dateHeure,
                duree,
                capaciteMax,
                entraineur
            );
            
            this.cours.add(cours);
            entraineur.ajouterCours(cours);
            System.out.println("Cours créé avec succès !");
        } else {
            System.out.println("Entraîneur non trouvé !");
        }
    }

    private static Entraineur trouverEntraineur(int id) {
        return entraineurs.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Méthodes pour la gestion des équipements
    private static void ajouterEquipement() {
        System.out.println("\n=== AJOUT D'UN ÉQUIPEMENT ===");
        System.out.println("1. Machine");
        System.out.println("2. Accessoire");
        System.out.print("Votre choix : ");
        
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        switch (choix) {
            case 1:
                ajouterMachine();
                break;
            case 2:
                ajouterAccessoire();
                break;
            default:
                System.out.println("Choix invalide !");
        }
    }

    private static void ajouterMachine() {
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        
        System.out.print("Description : ");
        String description = scanner.nextLine();
        
        System.out.print("Prix : ");
        double prix = scanner.nextDouble();
        scanner.nextLine(); // Consommer le retour à la ligne
        
        System.out.print("Type (CARDIO/MUSCULATION) : ");
        String type = scanner.nextLine().toUpperCase();
        
        System.out.print("Marque : ");
        String marque = scanner.nextLine();
        
        System.out.print("Modèle : ");
        String modele = scanner.nextLine();
        
        System.out.print("Année de fabrication : ");
        int anneeFabrication = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        Machine machine = new Machine(
            ++dernierIdEquipement,
            nom,
            description,
            "DISPONIBLE",
            prix,
            type,
            marque,
            modele,
            anneeFabrication
        );
        
        equipements.add(machine);
        System.out.println("Machine ajoutée avec succès !");
    }

    private static void ajouterAccessoire() {
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        
        System.out.print("Description : ");
        String description = scanner.nextLine();
        
        System.out.print("Prix : ");
        double prix = scanner.nextDouble();
        scanner.nextLine(); // Consommer le retour à la ligne
        
        System.out.print("Catégorie (YOGA/MUSCULATION) : ");
        String categorie = scanner.nextLine().toUpperCase();
        
        System.out.print("Taille : ");
        String taille = scanner.nextLine();
        
        System.out.print("Couleur : ");
        String couleur = scanner.nextLine();
        
        System.out.print("Quantité : ");
        int quantite = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne

        Accessoire accessoire = new Accessoire(
            ++dernierIdEquipement,
            nom,
            description,
            "DISPONIBLE",
            prix,
            categorie,
            taille,
            couleur,
            quantite
        );
        
        equipements.add(accessoire);
        System.out.println("Accessoire ajouté avec succès !");
    }

    private static void afficherEquipements() {
        System.out.println("\n=== LISTE DES ÉQUIPEMENTS ===");
        if (equipements.isEmpty()) {
            System.out.println("Aucun équipement enregistré.");
        } else {
            for (Equipement equipement : equipements) {
                System.out.println(equipement);
            }
        }
    }
} 