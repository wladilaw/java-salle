package com.salle.interfaces;

import com.salle.model.Cours;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface spécialisée pour les opérations sur les Cours
 */
public interface ICoursRepository extends IRepository<Cours> {
    
    /**
     * Trouve les cours par entraîneur
     * @param entraineurId L'ID de l'entraîneur
     * @return Liste des cours de cet entraîneur
     */
    List<Cours> findByEntraineurId(int entraineurId);
    
    /**
     * Trouve les cours dans une période donnée
     * @param debut Date/heure de début
     * @param fin Date/heure de fin
     * @return Liste des cours dans cette période
     */
    List<Cours> findByDateHeureBetween(LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Trouve les cours disponibles (non complets)
     * @return Liste des cours avec des places disponibles
     */
    List<Cours> findCoursDisponibles();
    
    /**
     * Trouve les cours d'aujourd'hui
     * @return Liste des cours du jour
     */
    List<Cours> findCoursAujourdhui();
} 