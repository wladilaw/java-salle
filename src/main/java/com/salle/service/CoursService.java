package com.salle.service;

import com.salle.interfaces.ICoursRepository;
import com.salle.model.Cours;
import com.salle.model.Entraineur;
import com.salle.repository.CoursRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer la logique métier des Cours
 */
public class CoursService {
    
    private final ICoursRepository coursRepository;
    
    public CoursService() {
        this.coursRepository = new CoursRepository();
    }
    
    /**
     * Crée un nouveau cours
     */
    public Cours creerCours(String nom, String description, double prix, 
                           LocalDateTime dateHeure, int duree, int capaciteMax, 
                           Entraineur entraineur) {
        // Validation des données
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du cours ne peut pas être vide");
        }
        if (prix < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        if (dateHeure == null) {
            throw new IllegalArgumentException("La date et heure ne peuvent pas être nulles");
        }
        if (dateHeure.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date du cours ne peut pas être dans le passé");
        }
        if (duree <= 0) {
            throw new IllegalArgumentException("La durée doit être positive");
        }
        if (capaciteMax <= 0) {
            throw new IllegalArgumentException("La capacité maximale doit être positive");
        }
        if (entraineur == null) {
            throw new IllegalArgumentException("Un entraîneur doit être assigné au cours");
        }
        
        // Création du cours
        Cours nouveauCours = new Cours(
            0, nom.trim(), description != null ? description.trim() : "",
            prix, dateHeure, duree, capaciteMax, entraineur
        );
        
        return coursRepository.create(nouveauCours);
    }
    
    /**
     * Récupère tous les cours
     */
    public List<Cours> obtenirTousLesCours() {
        return coursRepository.findAll();
    }
    
    /**
     * Trouve un cours par son ID
     */
    public Optional<Cours> trouverCoursParId(int id) {
        return coursRepository.findById(id);
    }
    
    /**
     * Récupère les cours par entraîneur
     */
    public List<Cours> obtenirCoursParEntraineur(int entraineurId) {
        return coursRepository.findByEntraineurId(entraineurId);
    }
    
    /**
     * Récupère les cours dans une période donnée
     */
    public List<Cours> obtenirCoursParPeriode(LocalDateTime debut, LocalDateTime fin) {
        if (debut == null || fin == null) {
            throw new IllegalArgumentException("Les dates de début et fin ne peuvent pas être nulles");
        }
        if (debut.isAfter(fin)) {
            throw new IllegalArgumentException("La date de début doit être antérieure à la date de fin");
        }
        
        return coursRepository.findByDateHeureBetween(debut, fin);
    }
    
    /**
     * Récupère les cours disponibles (avec des places libres)
     */
    public List<Cours> obtenirCoursDisponibles() {
        return coursRepository.findCoursDisponibles();
    }
    
    /**
     * Récupère les cours d'aujourd'hui
     */
    public List<Cours> obtenirCoursAujourdhui() {
        return coursRepository.findCoursAujourdhui();
    }
    
    /**
     * Récupère les cours de cette semaine
     */
    public List<Cours> obtenirCoursSemaine() {
        LocalDateTime debutSemaine = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finSemaine = debutSemaine.plusDays(7);
        return obtenirCoursParPeriode(debutSemaine, finSemaine);
    }
    
    /**
     * Met à jour un cours
     */
    public Cours mettreAJourCours(Cours cours) {
        if (cours.getNom() == null || cours.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du cours ne peut pas être vide");
        }
        if (cours.getPrix() < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        if (cours.getDuree() <= 0) {
            throw new IllegalArgumentException("La durée doit être positive");
        }
        if (cours.getCapaciteMax() <= 0) {
            throw new IllegalArgumentException("La capacité maximale doit être positive");
        }
        
        return coursRepository.update(cours);
    }
    
    /**
     * Annule un cours
     */
    public boolean annulerCours(int coursId) {
        Optional<Cours> coursOpt = coursRepository.findById(coursId);
        if (coursOpt.isPresent()) {
            Cours cours = coursOpt.get();
            if (cours.getDateHeure().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Impossible d'annuler un cours passé");
            }
        }
        
        return coursRepository.delete(coursId);
    }
    
    /**
     * Obtient le nombre total de cours
     */
    public long obtenirNombreTotalCours() {
        return coursRepository.count();
    }
    
    /**
     * Vérifie si un cours est complet (capacité maximale atteinte)
     */
    public boolean estCoursComplet(Cours cours) {
        if (cours == null) return false;
        // Cette logique devrait être complétée avec le nombre de participants actuels
        // Pour l'instant, on retourne false
        return false;
    }
    
    /**
     * Vérifie si un cours est valide
     */
    public boolean estCoursValide(Cours cours) {
        return cours != null && 
               cours.getNom() != null && !cours.getNom().trim().isEmpty() &&
               cours.getDateHeure() != null &&
               cours.getDuree() > 0 &&
               cours.getCapaciteMax() > 0 &&
               cours.getPrix() >= 0 &&
               cours.getEntraineur() != null;
    }
    
    /**
     * Planifie un cours récurrent (utilitaire pour créer plusieurs cours)
     */
    public List<Cours> planifierCoursRecurrent(String nom, String description, double prix,
                                             LocalDateTime premierCours, int duree, int capaciteMax,
                                             Entraineur entraineur, int nombreSemaines) {
        List<Cours> coursPlannifies = new java.util.ArrayList<>();
        
        for (int i = 0; i < nombreSemaines; i++) {
            LocalDateTime dateCours = premierCours.plusWeeks(i);
            try {
                Cours cours = creerCours(nom, description, prix, dateCours, 
                                       duree, capaciteMax, entraineur);
                coursPlannifies.add(cours);
            } catch (Exception e) {
                System.err.println("❌ Erreur lors de la création du cours pour la semaine " + (i + 1) + 
                                 " : " + e.getMessage());
            }
        }
        
        return coursPlannifies;
    }
} 