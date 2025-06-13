package com.salle.service;

import com.salle.interfaces.IMembreRepository;
import com.salle.model.Membre;
import com.salle.repository.MembreRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer la logique métier des Membres
 * Utilise le repository pour les opérations de persistence
 */
public class MembreService {
    
    private final IMembreRepository membreRepository;
    
    public MembreService() {
        this.membreRepository = new MembreRepository();
    }
    
    /**
     * Inscrit un nouveau membre
     * @param nom Nom du membre
     * @param prenom Prénom du membre
     * @param email Email du membre (doit être unique)
     * @param telephone Numéro de téléphone
     * @param typeAbonnement Type d'abonnement choisi
     * @return Le membre créé
     * @throws IllegalArgumentException Si l'email existe déjà
     */
    public Membre inscrireMembre(String nom, String prenom, String email, String telephone, String typeAbonnement) {
        // Vérification que l'email n'existe pas déjà
        if (membreRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Un membre avec cet email existe déjà : " + email);
        }
        
        // Validation des données
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email invalide");
        }
        
        // Création du nouveau membre
        Membre nouveauMembre = new Membre(
            0, // L'ID sera généré par la base de données
            nom.trim(),
            prenom.trim(),
            email.toLowerCase().trim(),
            telephone,
            LocalDate.now(), // Date d'inscription = aujourd'hui
            typeAbonnement
        );
        
        return membreRepository.create(nouveauMembre);
    }
    
    /**
     * Récupère tous les membres actifs
     * @return Liste des membres actifs
     */
    public List<Membre> obtenirMembresActifs() {
        return membreRepository.findByActif(true);
    }
    
    /**
     * Récupère tous les membres
     * @return Liste de tous les membres
     */
    public List<Membre> obtenirTousLesMembres() {
        return membreRepository.findAll();
    }
    
    /**
     * Trouve un membre par son ID
     * @param id ID du membre
     * @return Optional contenant le membre si trouvé
     */
    public Optional<Membre> trouverMembreParId(int id) {
        return membreRepository.findById(id);
    }
    
    /**
     * Trouve un membre par son email
     * @param email Email du membre
     * @return Le membre correspondant ou null
     */
    public Membre trouverMembreParEmail(String email) {
        return membreRepository.findByEmail(email);
    }
    
    /**
     * Récupère les membres par type d'abonnement
     * @param typeAbonnement Type d'abonnement recherché
     * @return Liste des membres avec ce type d'abonnement
     */
    public List<Membre> obtenirMembresParTypeAbonnement(String typeAbonnement) {
        return membreRepository.findByTypeAbonnement(typeAbonnement);
    }
    
    /**
     * Désactive un membre
     * @param membreId ID du membre à désactiver
     * @return true si la désactivation a réussi
     */
    public boolean desactiverMembre(int membreId) {
        Optional<Membre> membreOpt = membreRepository.findById(membreId);
        if (membreOpt.isPresent()) {
            Membre membre = membreOpt.get();
            membre.setActif(false);
            membreRepository.update(membre);
            System.out.println("✅ Membre désactivé : " + membre.getNom() + " " + membre.getPrenom());
            return true;
        }
        return false;
    }
    
    /**
     * Réactive un membre
     * @param membreId ID du membre à réactiver
     * @return true si la réactivation a réussi
     */
    public boolean reactiverMembre(int membreId) {
        Optional<Membre> membreOpt = membreRepository.findById(membreId);
        if (membreOpt.isPresent()) {
            Membre membre = membreOpt.get();
            membre.setActif(true);
            membreRepository.update(membre);
            System.out.println("✅ Membre réactivé : " + membre.getNom() + " " + membre.getPrenom());
            return true;
        }
        return false;
    }
    
    /**
     * Met à jour les informations d'un membre
     * @param membre Le membre avec les nouvelles informations
     * @return Le membre mis à jour
     */
    public Membre mettreAJourMembre(Membre membre) {
        // Validation des données
        if (membre.getNom() == null || membre.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        if (membre.getPrenom() == null || membre.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }
        
        return membreRepository.update(membre);
    }
    
    /**
     * Supprime définitivement un membre
     * @param membreId ID du membre à supprimer
     * @return true si la suppression a réussi
     */
    public boolean supprimerMembre(int membreId) {
        return membreRepository.delete(membreId);
    }
    
    /**
     * Obtient le nombre total de membres
     * @return Nombre total de membres
     */
    public long obtenirNombreTotalMembres() {
        return membreRepository.count();
    }
    
    /**
     * Obtient le nombre de membres actifs
     * @return Nombre de membres actifs
     */
    public long obtenirNombreMembresActifs() {
        return membreRepository.countMembresActifs();
    }
    
    /**
     * Vérifie si un membre est valide (actif et informations complètes)
     * @param membre Le membre à vérifier
     * @return true si le membre est valide
     */
    public boolean estMembreValide(Membre membre) {
        return membre != null && 
               membre.isActif() && 
               membre.getNom() != null && !membre.getNom().trim().isEmpty() &&
               membre.getPrenom() != null && !membre.getPrenom().trim().isEmpty() &&
               membre.getEmail() != null && membre.getEmail().contains("@");
    }
} 