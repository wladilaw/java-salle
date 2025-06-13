package com.salle.service;

import com.salle.interfaces.IEntraineurRepository;
import com.salle.model.Entraineur;
import com.salle.repository.EntraineurRepository;
import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer la logique métier des Entraîneurs
 */
public class EntraineurService {
    
    private final IEntraineurRepository entraineurRepository;
    
    public EntraineurService() {
        this.entraineurRepository = new EntraineurRepository();
    }
    
    /**
     * Inscrit un nouvel entraîneur
     */
    public Entraineur inscrireEntraineur(String nom, String prenom, String email, 
                                       String telephone, String specialite) {
        // Vérification que l'email n'existe pas déjà
        if (entraineurRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Un entraîneur avec cet email existe déjà : " + email);
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
        if (specialite == null || specialite.trim().isEmpty()) {
            throw new IllegalArgumentException("La spécialité ne peut pas être vide");
        }
        
        // Création du nouvel entraîneur
        Entraineur nouvelEntraineur = new Entraineur(
            0, nom.trim(), prenom.trim(), email.toLowerCase().trim(),
            telephone, specialite.trim()
        );
        
        return entraineurRepository.create(nouvelEntraineur);
    }
    
    /**
     * Récupère tous les entraîneurs
     */
    public List<Entraineur> obtenirTousLesEntraineurs() {
        return entraineurRepository.findAll();
    }
    
    /**
     * Trouve un entraîneur par son ID
     */
    public Optional<Entraineur> trouverEntraineurParId(int id) {
        return entraineurRepository.findById(id);
    }
    
    /**
     * Trouve un entraîneur par son email
     */
    public Entraineur trouverEntraineurParEmail(String email) {
        return entraineurRepository.findByEmail(email);
    }
    
    /**
     * Récupère les entraîneurs par spécialité
     */
    public List<Entraineur> obtenirEntraineursParSpecialite(String specialite) {
        return entraineurRepository.findBySpecialite(specialite);
    }
    
    /**
     * Récupère les entraîneurs disponibles
     */
    public List<Entraineur> obtenirEntraineursDisponibles() {
        return entraineurRepository.findEntraineursDisponibles();
    }
    
    /**
     * Met à jour les informations d'un entraîneur
     */
    public Entraineur mettreAJourEntraineur(Entraineur entraineur) {
        if (entraineur.getNom() == null || entraineur.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        if (entraineur.getPrenom() == null || entraineur.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }
        if (entraineur.getSpecialite() == null || entraineur.getSpecialite().trim().isEmpty()) {
            throw new IllegalArgumentException("La spécialité ne peut pas être vide");
        }
        
        return entraineurRepository.update(entraineur);
    }
    
    /**
     * Supprime un entraîneur
     */
    public boolean supprimerEntraineur(int entraineurId) {
        return entraineurRepository.delete(entraineurId);
    }
    
    /**
     * Obtient le nombre total d'entraîneurs
     */
    public long obtenirNombreTotalEntraineurs() {
        return entraineurRepository.count();
    }
    
    /**
     * Obtient le nombre d'entraîneurs par spécialité
     */
    public long obtenirNombreEntraineursParSpecialite(String specialite) {
        return entraineurRepository.countBySpecialite(specialite);
    }
    
    /**
     * Vérifie si un entraîneur est valide
     */
    public boolean estEntraineurValide(Entraineur entraineur) {
        return entraineur != null && 
               entraineur.getNom() != null && !entraineur.getNom().trim().isEmpty() &&
               entraineur.getPrenom() != null && !entraineur.getPrenom().trim().isEmpty() &&
               entraineur.getEmail() != null && entraineur.getEmail().contains("@") &&
               entraineur.getSpecialite() != null && !entraineur.getSpecialite().trim().isEmpty();
    }
} 