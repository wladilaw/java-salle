package com.salle.demo;

import com.salle.config.DatabaseConfig;
import com.salle.service.MembreService;
import com.salle.model.Membre;
import java.util.List;
import java.util.Optional;

/**
 * Application de démonstration pour tester l'intégration de la base de données
 * avec le système de gestion de salle de sport
 */
public class GymApp {
    
    public static void main(String[] args) {
        System.out.println("🏋️‍♂️ === SYSTÈME DE GESTION SALLE DE SPORT === 🏋️‍♀️");
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
            
            // Création du service des membres
            MembreService membreService = new MembreService();
            
            // === DÉMONSTRATION DES FONCTIONNALITÉS ===
            
            // 1. Inscription de nouveaux membres
            System.out.println("👥 === INSCRIPTION DE NOUVEAUX MEMBRES ===");
            try {
                Membre membre1 = membreService.inscrireMembre(
                    "Dupont", "Jean", "jean.dupont@email.com", "0123456789", "MENSUEL"
                );
                System.out.println("✅ Membre 1 inscrit : " + membre1);
                
                Membre membre2 = membreService.inscrireMembre(
                    "Martin", "Marie", "marie.martin@email.com", "0987654321", "ANNUEL"
                );
                System.out.println("✅ Membre 2 inscrit : " + membre2);
                
                Membre membre3 = membreService.inscrireMembre(
                    "Bernard", "Paul", "paul.bernard@email.com", "0556677889", "TRIMESTRIEL"
                );
                System.out.println("✅ Membre 3 inscrit : " + membre3);
                
            } catch (Exception e) {
                System.out.println("⚠️ Certains membres existent peut-être déjà : " + e.getMessage());
            }
            System.out.println();
            
            // 2. Affichage de tous les membres
            System.out.println("📋 === LISTE DE TOUS LES MEMBRES ===");
            List<Membre> tousLesMembres = membreService.obtenirTousLesMembres();
            if (tousLesMembres.isEmpty()) {
                System.out.println("Aucun membre trouvé.");
            } else {
                for (Membre membre : tousLesMembres) {
                    System.out.println("👤 " + membre);
                }
            }
            System.out.println();
            
            // 3. Recherche par email
            System.out.println("🔍 === RECHERCHE PAR EMAIL ===");
            String emailRecherche = "jean.dupont@email.com";
            Membre membreTrouve = membreService.trouverMembreParEmail(emailRecherche);
            if (membreTrouve != null) {
                System.out.println("✅ Membre trouvé pour " + emailRecherche + " : " + membreTrouve);
            } else {
                System.out.println("❌ Aucun membre trouvé pour l'email : " + emailRecherche);
            }
            System.out.println();
            
            // 4. Filtrage par type d'abonnement
            System.out.println("📊 === MEMBRES PAR TYPE D'ABONNEMENT ===");
            String[] typesAbonnement = {"MENSUEL", "TRIMESTRIEL", "ANNUEL"};
            for (String type : typesAbonnement) {
                List<Membre> membresParType = membreService.obtenirMembresParTypeAbonnement(type);
                System.out.println("📈 " + type + " : " + membresParType.size() + " membre(s)");
                for (Membre membre : membresParType) {
                    System.out.println("   → " + membre.getNom() + " " + membre.getPrenom());
                }
            }
            System.out.println();
            
            // 5. Statistiques
            System.out.println("📈 === STATISTIQUES ===");
            long totalMembres = membreService.obtenirNombreTotalMembres();
            long membresActifs = membreService.obtenirNombreMembresActifs();
            System.out.println("👥 Total des membres : " + totalMembres);
            System.out.println("✅ Membres actifs : " + membresActifs);
            System.out.println("❌ Membres inactifs : " + (totalMembres - membresActifs));
            System.out.println();
            
            // 6. Test de désactivation/réactivation
            System.out.println("🔄 === TEST DÉSACTIVATION/RÉACTIVATION ===");
            if (!tousLesMembres.isEmpty()) {
                Membre premierMembre = tousLesMembres.get(0);
                int membreId = premierMembre.getId();
                
                System.out.println("📴 Désactivation du membre : " + premierMembre.getNom());
                boolean desactiveSuccess = membreService.desactiverMembre(membreId);
                System.out.println("Résultat désactivation : " + (desactiveSuccess ? "✅" : "❌"));
                
                System.out.println("📱 Réactivation du membre : " + premierMembre.getNom());
                boolean reactiveSuccess = membreService.reactiverMembre(membreId);
                System.out.println("Résultat réactivation : " + (reactiveSuccess ? "✅" : "❌"));
            }
            System.out.println();
            
            // 7. Test de recherche par ID
            System.out.println("🔍 === RECHERCHE PAR ID ===");
            if (!tousLesMembres.isEmpty()) {
                int idRecherche = tousLesMembres.get(0).getId();
                Optional<Membre> membreParId = membreService.trouverMembreParId(idRecherche);
                if (membreParId.isPresent()) {
                    System.out.println("✅ Membre trouvé par ID " + idRecherche + " : " + membreParId.get());
                } else {
                    System.out.println("❌ Aucun membre trouvé pour l'ID : " + idRecherche);
                }
            }
            System.out.println();
            
            System.out.println("🎉 === DÉMONSTRATION TERMINÉE AVEC SUCCÈS ! ===");
            
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