package com.salle.interfaces;

import com.salle.model.Equipement;
import java.util.List;

/**
 * Interface spécialisée pour les opérations sur les Équipements
 */
public interface IEquipementRepository extends IRepository<Equipement> {
    
    /**
     * Trouve les équipements par état
     * @param etat L'état recherché
     * @return Liste des équipements dans cet état
     */
    List<Equipement> findByEtat(String etat);
    
    /**
     * Trouve les équipements par type
     * @param type Le type d'équipement
     * @return Liste des équipements de ce type
     */
    List<Equipement> findByType(String type);
    
    /**
     * Trouve les équipements en panne ou à réparer
     * @return Liste des équipements défaillants
     */
    List<Equipement> findEquipementsDefaillants();
    
    /**
     * Trouve les équipements dans une fourchette de prix
     * @param prixMin Prix minimum
     * @param prixMax Prix maximum
     * @return Liste des équipements dans la fourchette
     */
    List<Equipement> findByPrixBetween(double prixMin, double prixMax);
    
    /**
     * Compte les équipements par état
     * @param etat L'état
     * @return Le nombre d'équipements
     */
    long countByEtat(String etat);
} 