package com.salle;

import com.salle.config.DatabaseConfig;
import com.salle.service.MembreService;
import com.salle.service.EntraineurService;
import com.salle.service.CoursService;
import com.salle.model.Membre;
import com.salle.model.Entraineur;
import com.salle.model.Cours;
import com.salle.model.Participation;
import com.salle.repository.ParticipationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.List;

/**
 * Application interactive pour la gestion de salle de sport
 * Permet à l'utilisateur de saisir ses propres données
 */
public class InteractiveGymApp {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final MembreService membreService = new MembreService();
    private static final EntraineurService entraineurService = new EntraineurService();
    private static final CoursService coursService = new CoursService();
    private static final ParticipationRepository participationRepo = new ParticipationRepository();
    
    public static void main(String[] args) {
        System.out.println("=== GESTION SALLE DE SPORT INTERACTIVE ===");
        System.out.println("Connexion à la base de données H2...");
        
        // Test de connexion
        DatabaseConfig dbConfig = DatabaseConfig.getInstance();
        if (dbConfig.testConnection()) {
            System.out.println("Base de données prête !");
            System.out.println();
            
            // Menu principal
            afficherMenuPrincipal();
        } else {
            System.err.println("Impossible de se connecter à la base de données");
        }
        
        // Fermeture propre
        DatabaseConfig.getInstance().closeConnection();
        scanner.close();
    }
    
    private static void afficherMenuPrincipal() {
        while (true) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Gestion des Membres");
            System.out.println("2. Gestion des Entraîneurs");
            System.out.println("3. Gestion des Cours");
            System.out.println("4. Statistiques");
            System.out.println("0. Quitter");
            System.out.print("\nChoisissez une option : ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1" -> gestionMembres();
                case "2" -> gestionEntraineurs();
                case "3" -> gestionCours();
                case "4" -> afficherStatistiques();
                case "0" -> {
                    System.out.println("Au revoir !");
                    return;
                }
                default -> System.out.println("Option invalide !");
            }
        }
    }
    
    private static void gestionMembres() {
        while (true) {
            System.out.println("\n=== GESTION DES MEMBRES ===");
            System.out.println("1. Ajouter un membre");
            System.out.println("2. Lister tous les membres");
            System.out.println("3. Rechercher un membre");
            System.out.println("0. Retour au menu principal");
            System.out.print("\nChoisissez une option : ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1" -> ajouterMembre();
                case "2" -> listerMembres();
                case "3" -> rechercherMembre();
                case "0" -> { return; }
                default -> System.out.println("Option invalide !");
            }
        }
    }
    
    private static void ajouterMembre() {
        System.out.println("\n=== AJOUTER UN MEMBRE ===");
        
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        
        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();
        
        System.out.print("Email : ");
        String email = scanner.nextLine();
        
        System.out.print("Téléphone : ");
        String telephone = scanner.nextLine();
        
        System.out.println("\nTypes d'abonnement disponibles :");
        System.out.println("1. MENSUEL");
        System.out.println("2. TRIMESTRIEL");
        System.out.println("3. ANNUEL");
        System.out.print("Choisissez (1-3) : ");
        
        String choixAbonnement = scanner.nextLine();
        String typeAbonnement = switch (choixAbonnement) {
            case "1" -> "MENSUEL";
            case "2" -> "TRIMESTRIEL";
            case "3" -> "ANNUEL";
            default -> "MENSUEL";
        };
        
        try {
            Membre membre = membreService.inscrireMembre(nom, prenom, email, telephone, typeAbonnement);
            System.out.println("Membre créé avec succès ! ID: " + membre.getId());
        } catch (Exception e) {
            System.err.println("Erreur lors de la création : " + e.getMessage());
        }
    }
    
    private static void listerMembres() {
        System.out.println("\n=== LISTE DES MEMBRES ===");
        
        try {
            List<Membre> membres = membreService.obtenirTousLesMembres();
            
            if (membres.isEmpty()) {
                System.out.println("Aucun membre trouvé.");
                return;
            }
            
            System.out.printf("%-4s %-15s %-15s %-25s %-15s %-12s %-6s%n", 
                "ID", "Nom", "Prénom", "Email", "Téléphone", "Abonnement", "Actif");
            System.out.println("─".repeat(95));
            
            for (Membre membre : membres) {
                System.out.printf("%-4d %-15s %-15s %-25s %-15s %-12s %-6s%n",
                    membre.getId(),
                    membre.getNom(),
                    membre.getPrenom(),
                    membre.getEmail(),
                    membre.getTelephone(),
                    membre.getTypeAbonnement(),
                    membre.isActif() ? "Oui" : "Non");
            }
            
            System.out.println("\nTotal : " + membres.size() + " membre(s)");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération : " + e.getMessage());
        }
    }
    
    private static void rechercherMembre() {
        System.out.print("Email du membre à rechercher : ");
        String email = scanner.nextLine();
        
        try {
            Membre membre = membreService.trouverMembreParEmail(email);
            if (membre != null) {
                System.out.printf("Membre trouvé : %s %s (%s)%n", 
                    membre.getNom(), membre.getPrenom(), membre.getTypeAbonnement());
            } else {
                System.out.println("Aucun membre trouvé avec cet email.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche : " + e.getMessage());
        }
    }
    
    private static void gestionEntraineurs() {
        while (true) {
            System.out.println("\n=== GESTION DES ENTRAÎNEURS ===");
            System.out.println("1. Ajouter un entraîneur");
            System.out.println("2. Lister tous les entraîneurs");
            System.out.println("3. Rechercher par spécialité");
            System.out.println("0. Retour au menu principal");
            System.out.print("\nChoisissez une option : ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1" -> ajouterEntraineur();
                case "2" -> listerEntraineurs();
                case "3" -> rechercherParSpecialite();
                case "0" -> { return; }
                default -> System.out.println("Option invalide !");
            }
        }
    }
    
    private static void ajouterEntraineur() {
        System.out.println("\n=== AJOUTER UN ENTRAÎNEUR ===");
        
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        
        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();
        
        System.out.print("Email : ");
        String email = scanner.nextLine();
        
        System.out.print("Téléphone : ");
        String telephone = scanner.nextLine();
        
        System.out.print("Spécialité : ");
        String specialite = scanner.nextLine();
        
        try {
            Entraineur entraineur = entraineurService.inscrireEntraineur(nom, prenom, email, telephone, specialite);
            System.out.println("Entraîneur créé avec succès ! ID: " + entraineur.getId());
        } catch (Exception e) {
            System.err.println("Erreur lors de la création : " + e.getMessage());
        }
    }
    
    private static void listerEntraineurs() {
        System.out.println("\n=== LISTE DES ENTRAÎNEURS ===");
        
        try {
            List<Entraineur> entraineurs = entraineurService.obtenirTousLesEntraineurs();
            
            if (entraineurs.isEmpty()) {
                System.out.println("Aucun entraîneur trouvé.");
                return;
            }
            
            System.out.printf("%-4s %-15s %-15s %-25s %-15s %-20s%n", 
                "ID", "Nom", "Prénom", "Email", "Téléphone", "Spécialité");
            System.out.println("─".repeat(100));
            
            for (Entraineur entraineur : entraineurs) {
                System.out.printf("%-4d %-15s %-15s %-25s %-15s %-20s%n",
                    entraineur.getId(),
                    entraineur.getNom(),
                    entraineur.getPrenom(),
                    entraineur.getEmail(),
                    entraineur.getTelephone(),
                    entraineur.getSpecialite());
            }
            
            System.out.println("\nTotal : " + entraineurs.size() + " entraîneur(s)");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération : " + e.getMessage());
        }
    }
    
    private static void rechercherParSpecialite() {
        System.out.print("Spécialité recherchée : ");
        String specialite = scanner.nextLine();
        
        try {
            List<Entraineur> entraineurs = entraineurService.obtenirEntraineursParSpecialite(specialite);
            System.out.printf("%d entraîneur(s) trouvé(s) pour '%s'%n", entraineurs.size(), specialite);
            
            for (Entraineur e : entraineurs) {
                System.out.printf("   - %s %s%n", e.getNom(), e.getPrenom());
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche : " + e.getMessage());
        }
    }
    
    private static void gestionCours() {
        while (true) {
            System.out.println("\n=== GESTION DES COURS ===");
            System.out.println("1. Ajouter un cours");
            System.out.println("2. Lister tous les cours");
            System.out.println("3. Cours disponibles");
            System.out.println("0. Retour au menu principal");
            System.out.print("\nChoisissez une option : ");
            
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1" -> ajouterCours();
                case "2" -> listerCours();
                case "3" -> listerCoursDisponibles();
                case "0" -> { return; }
                default -> System.out.println("Option invalide !");
            }
        }
    }
    
    private static void ajouterCours() {
        System.out.println("\n=== AJOUTER UN COURS ===");
        
        // Vérifier qu'il y a des entraîneurs
        List<Entraineur> entraineurs = entraineurService.obtenirTousLesEntraineurs();
        if (entraineurs.isEmpty()) {
            System.out.println("Aucun entraîneur disponible. Créez d'abord un entraîneur.");
            return;
        }
        
        System.out.print("Nom du cours : ");
        String nom = scanner.nextLine();
        
        System.out.print("Description : ");
        String description = scanner.nextLine();
        
        System.out.print("Prix (€) : ");
        double prix = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Durée (minutes) : ");
        int duree = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Capacité maximum : ");
        int capaciteMax = Integer.parseInt(scanner.nextLine());
        
        // Sélection de l'entraîneur
        System.out.println("\nEntraîneurs disponibles :");
        for (int i = 0; i < entraineurs.size(); i++) {
            Entraineur e = entraineurs.get(i);
            System.out.printf("%d. %s %s (%s)%n", i + 1, e.getNom(), e.getPrenom(), e.getSpecialite());
        }
        
        System.out.print("Choisissez un entraîneur (1-" + entraineurs.size() + ") : ");
        int choixEntraineur = Integer.parseInt(scanner.nextLine()) - 1;
        
        if (choixEntraineur < 0 || choixEntraineur >= entraineurs.size()) {
            System.out.println("Choix invalide !");
            return;
        }
        
        Entraineur entraineur = entraineurs.get(choixEntraineur);
        
        // Date et heure
        System.out.print("Date et heure (YYYY-MM-DD HH:mm) : ");
        String dateStr = scanner.nextLine();
        
        try {
            LocalDateTime dateHeure = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            
            Cours cours = coursService.creerCours(nom, description, prix, dateHeure, duree, capaciteMax, entraineur);
            System.out.println("Cours créé avec succès ! ID: " + cours.getId());
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la création : " + e.getMessage());
        }
    }
    
    private static void listerCours() {
        System.out.println("\n=== LISTE DES COURS ===");
        
        try {
            List<Cours> cours = coursService.obtenirTousLesCours();
            
            if (cours.isEmpty()) {
                System.out.println("Aucun cours trouvé.");
                return;
            }
            
            for (Cours c : cours) {
                System.out.println("─".repeat(80));
                System.out.printf("ID: %d | %s%n", c.getId(), c.getNom());
                System.out.printf("Description: %s%n", c.getDescription());
                System.out.printf("Prix: %.2f€ | Durée: %d min | Capacité: %d%n", 
                    c.getPrix(), c.getDuree(), c.getCapaciteMax());
                System.out.printf("Date: %s%n", c.getDateHeure().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                System.out.printf("Entraîneur: %s %s (%s)%n", 
                    c.getEntraineur().getNom(), c.getEntraineur().getPrenom(), c.getEntraineur().getSpecialite());
            }
            
            System.out.println("─".repeat(80));
            System.out.println("Total : " + cours.size() + " cours");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération : " + e.getMessage());
        }
    }
    
    private static void listerCoursDisponibles() {
        System.out.println("\n=== COURS DISPONIBLES ===");
        
        try {
            List<Cours> cours = coursService.obtenirCoursDisponibles();
            
            if (cours.isEmpty()) {
                System.out.println("Aucun cours disponible.");
                return;
            }
            
            for (Cours c : cours) {
                long participantsInscrits = participationRepo.countByCoursId(c.getId());
                
                System.out.printf("%d | %s | %s | %d/%d places | %.2f€%n",
                    c.getId(),
                    c.getNom(),
                    c.getDateHeure().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    participantsInscrits,
                    c.getCapaciteMax(),
                    c.getPrix());
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération : " + e.getMessage());
        }
    }
    
    private static void afficherStatistiques() {
        System.out.println("\n=== STATISTIQUES ===");
        
        try {
            long totalMembres = membreService.obtenirNombreTotalMembres();
            long membresActifs = membreService.obtenirNombreMembresActifs();
            long totalEntraineurs = entraineurService.obtenirNombreTotalEntraineurs();
            long totalCours = coursService.obtenirNombreTotalCours();
            long totalParticipations = participationRepo.count();
            
            System.out.println("Membres :");
            System.out.printf("   Total : %d%n", totalMembres);
            System.out.printf("   Actifs : %d%n", membresActifs);
            System.out.printf("   Taux d'activité : %.1f%%%n", 
                totalMembres > 0 ? (double) membresActifs / totalMembres * 100 : 0);
            
            System.out.println("\nEntraîneurs : " + totalEntraineurs);
            System.out.println("Cours : " + totalCours);
            System.out.println("Participations : " + totalParticipations);
            
            if (totalMembres > 0 && totalCours > 0) {
                double moyenneParticipations = (double) totalParticipations / totalMembres;
                System.out.printf("Moyenne participations par membre : %.1f%n", moyenneParticipations);
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des statistiques : " + e.getMessage());
        }
    }
} 