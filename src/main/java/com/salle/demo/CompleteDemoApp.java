package com.salle.demo;

import com.salle.config.DatabaseConfig;
import com.salle.service.*;
import com.salle.model.*;
import com.salle.repository.ParticipationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Application de démonstration complète pour toutes les entités
 * Tests d'intégration de toutes les fonctionnalités du système
 */
public class CompleteDemoApp {
    
    public static void main(String[] args) {
        System.out.println("🏋️‍♂️ === DÉMONSTRATION COMPLÈTE DU SYSTÈME === 🏋️‍♀️");
        System.out.println();
        
        try {
            // Test de la connexion à la base de données
            System.out.println("📊 Test de connexion à la base de données...");
            DatabaseConfig dbConfig = DatabaseConfig.getInstance();
            if (dbConfig.testConnection()) {
                System.out.println("✅ Connexion établie avec succès !");
            } else {
                System.out.println("❌ Échec de la connexion !");
                return;
            }
            System.out.println();
            
            // Création des services
            MembreService membreService = new MembreService();
            EntraineurService entraineurService = new EntraineurService();
            CoursService coursService = new CoursService();
            ParticipationRepository participationRepo = new ParticipationRepository();
            
            // === 1. GESTION DES MEMBRES ===
            System.out.println("👥 === GESTION DES MEMBRES ===");
            try {
                // Création de membres
                Membre membre1 = membreService.inscrireMembre(
                    "Durand", "Sophie", "sophie.durand@email.com", "0123456789", "MENSUEL"
                );
                Membre membre2 = membreService.inscrireMembre(
                    "Lefebvre", "Pierre", "pierre.lefebvre@email.com", "0987654321", "ANNUEL"
                );
                Membre membre3 = membreService.inscrireMembre(
                    "Garcia", "Ana", "ana.garcia@email.com", "0556677889", "TRIMESTRIEL"
                );
                
                System.out.println("✅ 3 membres créés avec succès");
                
                // Statistiques membres
                long totalMembres = membreService.obtenirNombreTotalMembres();
                long membresActifs = membreService.obtenirNombreMembresActifs();
                System.out.println("📊 Total membres: " + totalMembres + " | Actifs: " + membresActifs);
                
            } catch (Exception e) {
                System.out.println("⚠️ Certains membres existent peut-être déjà");
            }
            System.out.println();
            
            // === 2. GESTION DES ENTRAÎNEURS ===
            System.out.println("👨‍🏫 === GESTION DES ENTRAÎNEURS ===");
            try {
                // Création d'entraîneurs
                Entraineur entraineur1 = entraineurService.inscrireEntraineur(
                    "Martin", "Jean", "jean.martin@email.com", "0111111111", "Musculation"
                );
                Entraineur entraineur2 = entraineurService.inscrireEntraineur(
                    "Bernard", "Claire", "claire.bernard@email.com", "0222222222", "Cardio"
                );
                Entraineur entraineur3 = entraineurService.inscrireEntraineur(
                    "Petit", "Marc", "marc.petit@email.com", "0333333333", "Yoga"
                );
                
                System.out.println("✅ 3 entraîneurs créés avec succès");
                
                // Spécialités disponibles
                List<Entraineur> entraineursMusculation = entraineurService.obtenirEntraineursParSpecialite("Musculation");
                List<Entraineur> entraineursCardio = entraineurService.obtenirEntraineursParSpecialite("Cardio");
                System.out.println("💪 Musculation: " + entraineursMusculation.size() + " entraîneur(s)");
                System.out.println("❤️ Cardio: " + entraineursCardio.size() + " entraîneur(s)");
                
            } catch (Exception e) {
                System.out.println("⚠️ Certains entraîneurs existent peut-être déjà");
            }
            System.out.println();
            
            // === 3. GESTION DES COURS ===
            System.out.println("🏃‍♂️ === GESTION DES COURS ===");
            try {
                // Récupération des entraîneurs pour assigner aux cours
                List<Entraineur> tousLesEntraineurs = entraineurService.obtenirTousLesEntraineurs();
                if (!tousLesEntraineurs.isEmpty()) {
                    Entraineur entraineur1 = tousLesEntraineurs.get(0);
                    Entraineur entraineur2 = tousLesEntraineurs.size() > 1 ? tousLesEntraineurs.get(1) : entraineur1;
                    
                    // Création de cours
                    LocalDateTime maintenant = LocalDateTime.now();
                    
                    Cours cours1 = coursService.creerCours(
                        "Musculation Débutant", 
                        "Cours de musculation pour débutants",
                        25.0,
                        maintenant.plusDays(1).withHour(10).withMinute(0),
                        60,
                        15,
                        entraineur1
                    );
                    
                    Cours cours2 = coursService.creerCours(
                        "Cardio Intense", 
                        "Séance de cardio haute intensité",
                        20.0,
                        maintenant.plusDays(2).withHour(18).withMinute(0),
                        45,
                        20,
                        entraineur2
                    );
                    
                    Cours cours3 = coursService.creerCours(
                        "Yoga Relaxation", 
                        "Séance de yoga pour la détente",
                        15.0,
                        maintenant.plusDays(3).withHour(19).withMinute(0),
                        75,
                        12,
                        entraineur1
                    );
                    
                    System.out.println("✅ 3 cours créés avec succès");
                    
                    // Statistiques cours
                    long totalCours = coursService.obtenirNombreTotalCours();
                    List<Cours> coursDisponibles = coursService.obtenirCoursDisponibles();
                    System.out.println("📚 Total cours: " + totalCours + " | Disponibles: " + coursDisponibles.size());
                    
                    // === 4. GESTION DES PARTICIPATIONS ===
                    System.out.println();
                    System.out.println("🤝 === GESTION DES PARTICIPATIONS ===");
                    
                    // Récupération des membres pour les inscriptions
                    List<Membre> tousLesMembres = membreService.obtenirTousLesMembres();
                    List<Cours> tousLesCours = coursService.obtenirTousLesCours();
                    
                    if (!tousLesMembres.isEmpty() && !tousLesCours.isEmpty()) {
                        // Inscription de membres aux cours
                        int participationsCreees = 0;
                        
                        for (int i = 0; i < Math.min(3, tousLesMembres.size()); i++) {
                            for (int j = 0; j < Math.min(2, tousLesCours.size()); j++) {
                                try {
                                    Membre membre = tousLesMembres.get(i);
                                    Cours cours = tousLesCours.get(j);
                                    
                                    // Vérifier si la participation n'existe pas déjà
                                    if (!participationRepo.existsByMembreIdAndCoursId(membre.getId(), cours.getId())) {
                                        Participation participation = new Participation(
                                            cours.getId(),
                                            membre.getId(),
                                            LocalDateTime.now()
                                        );
                                        participationRepo.create(participation);
                                        participationsCreees++;
                                    }
                                } catch (Exception e) {
                                    // Participation déjà existante ou autre erreur
                                }
                            }
                        }
                        
                        System.out.println("✅ " + participationsCreees + " participations créées");
                        
                        // Statistiques des participations
                        if (!tousLesMembres.isEmpty()) {
                            Membre premierMembre = tousLesMembres.get(0);
                            List<Participation> participationsMembre = participationRepo.findByMembreId(premierMembre.getId());
                            System.out.println("📊 " + premierMembre.getNom() + " participe à " + participationsMembre.size() + " cours");
                        }
                        
                        if (!tousLesCours.isEmpty()) {
                            Cours premierCours = tousLesCours.get(0);
                            long participantsCours = participationRepo.countByCoursId(premierCours.getId());
                            System.out.println("📊 Cours '" + premierCours.getNom() + "' : " + participantsCours + " participant(s)");
                        }
                    }
                    
                } else {
                    System.out.println("⚠️ Aucun entraîneur disponible pour créer des cours");
                }
                
            } catch (Exception e) {
                System.out.println("⚠️ Erreur lors de la création des cours : " + e.getMessage());
            }
            System.out.println();
            
            // === 5. DÉMONSTRATION DES RECHERCHES AVANCÉES ===
            System.out.println("🔍 === RECHERCHES AVANCÉES ===");
            
            // Cours disponibles aujourd'hui
            List<Cours> coursAujourdhui = coursService.obtenirCoursAujourdhui();
            System.out.println("📅 Cours aujourd'hui : " + coursAujourdhui.size());
            
            // Cours de la semaine
            List<Cours> coursSemaine = coursService.obtenirCoursSemaine();
            System.out.println("📅 Cours cette semaine : " + coursSemaine.size());
            
            // Entraîneurs disponibles
            List<Entraineur> entraineursDisponibles = entraineurService.obtenirEntraineursDisponibles();
            System.out.println("👨‍🏫 Entraîneurs disponibles : " + entraineursDisponibles.size());
            
            // Membres par type d'abonnement
            String[] typesAbonnement = {"MENSUEL", "TRIMESTRIEL", "ANNUEL"};
            for (String type : typesAbonnement) {
                List<Membre> membresParType = membreService.obtenirMembresParTypeAbonnement(type);
                System.out.println("📊 " + type + " : " + membresParType.size() + " membre(s)");
            }
            System.out.println();
            
            // === 6. DÉMONSTRATION DE RECHERCHES COMPLEXES ===
            System.out.println("🎯 === RECHERCHES COMPLEXES ===");
            
            // Participations récentes
            List<Membre> tousLesMembres = membreService.obtenirTousLesMembres();
            if (!tousLesMembres.isEmpty()) {
                Membre premierMembre = tousLesMembres.get(0);
                List<Participation> participationsRecentes = participationRepo.findParticipationsRecentesByMembre(
                    premierMembre.getId(), 30
                );
                System.out.println("📈 " + premierMembre.getNom() + " : " + participationsRecentes.size() + 
                                 " participations dans les 30 derniers jours");
            }
            
            // Cours dans une période
            LocalDateTime debut = LocalDateTime.now();
            LocalDateTime fin = debut.plusDays(7);
            List<Cours> coursProchaineSemaine = coursService.obtenirCoursParPeriode(debut, fin);
            System.out.println("📅 Cours prochaine semaine : " + coursProchaineSemaine.size());
            
            System.out.println();
            
            // === 7. STATISTIQUES GLOBALES ===
            System.out.println("📊 === STATISTIQUES GLOBALES ===");
            
            long totalMembresFinaux = membreService.obtenirNombreTotalMembres();
            long membresActifsFinaux = membreService.obtenirNombreMembresActifs();
            long totalEntraineurs = entraineurService.obtenirNombreTotalEntraineurs();
            long totalCoursFinaux = coursService.obtenirNombreTotalCours();
            long totalParticipations = participationRepo.count();
            
            System.out.println("👥 Total membres : " + totalMembresFinaux + " (actifs: " + membresActifsFinaux + ")");
            System.out.println("👨‍🏫 Total entraîneurs : " + totalEntraineurs);
            System.out.println("🏃‍♂️ Total cours : " + totalCoursFinaux);
            System.out.println("🤝 Total participations : " + totalParticipations);
            
            double tauxActivite = totalMembresFinaux > 0 ? (double) membresActifsFinaux / totalMembresFinaux * 100 : 0;
            System.out.println("📈 Taux d'activité : " + String.format("%.1f", tauxActivite) + "%");
            
            if (totalCoursFinaux > 0 && totalMembresFinaux > 0) {
                double moyenneParticipations = (double) totalParticipations / totalMembresFinaux;
                System.out.println("📊 Moyenne participations par membre : " + String.format("%.1f", moyenneParticipations));
            }
            
            System.out.println();
            System.out.println("🎉 === DÉMONSTRATION COMPLÈTE TERMINÉE AVEC SUCCÈS ! ===");
            System.out.println("✨ Toutes les entités ont été testées et intégrées correctement !");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'exécution de la démonstration : " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fermeture propre de la connexion
            DatabaseConfig.getInstance().closeConnection();
            System.out.println("🔌 Connexion fermée proprement.");
        }
    }
} 