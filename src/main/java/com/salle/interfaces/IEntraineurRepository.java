package com.salle.interfaces;

import com.salle.model.Entraineur;
import java.util.List;

/**
 * Interface spécialisée pour les opérations sur les Entraîneurs
 */
public interface IEntraineurRepository extends IRepository<Entraineur> {
    
    /**
     * Trouve les entraîneurs par spécialité
     * @param specialite La spécialité recherchée
     * @return Liste des entraîneurs avec cette spécialité
     */
    List<Entraineur> findBySpecialite(String specialite);
    
    /**
     * Trouve un entraîneur par son email
     * @param email L'email de l'entraîneur
     * @return L'entraîneur correspondant ou null
     */
    Entraineur findByEmail(String email);
    
    /**
     * Trouve les entraîneurs disponibles (sans cours à une heure donnée)
     * @return Liste des entraîneurs disponibles
     */
    List<Entraineur> findEntraineursDisponibles();
    
    /**
     * Compte le nombre d'entraîneurs par spécialité
     * @param specialite La spécialité
     * @return Le nombre d'entraîneurs
     */
    long countBySpecialite(String specialite);
} 