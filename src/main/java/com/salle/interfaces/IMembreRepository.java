package com.salle.interfaces;

import com.salle.model.Membre;
import java.util.List;

/**
 * Interface spécialisée pour les opérations sur les Membres
 */
public interface IMembreRepository extends IRepository<Membre> {
    
    /**
     * Trouve tous les membres actifs
     * @return Liste des membres actifs
     */
    List<Membre> findByActif(boolean actif);
    
    /**
     * Trouve les membres par type d'abonnement
     * @param typeAbonnement Le type d'abonnement
     * @return Liste des membres avec ce type d'abonnement
     */
    List<Membre> findByTypeAbonnement(String typeAbonnement);
    
    /**
     * Trouve un membre par son email
     * @param email L'email du membre
     * @return Le membre correspondant ou null
     */
    Membre findByEmail(String email);
    
    /**
     * Compte le nombre de membres actifs
     * @return Le nombre de membres actifs
     */
    long countMembresActifs();
} 