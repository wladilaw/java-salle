package com.salle.interfaces;

import com.salle.model.Participation;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface spécialisée pour les opérations sur les Participations
 */
public interface IParticipationRepository extends IRepository<Participation> {
    
    /**
     * Trouve les participations par membre
     * @param membreId L'ID du membre
     * @return Liste des participations du membre
     */
    List<Participation> findByMembreId(int membreId);
    
    /**
     * Trouve les participations par cours
     * @param coursId L'ID du cours
     * @return Liste des participations au cours
     */
    List<Participation> findByCoursId(int coursId);
    
    /**
     * Vérifie si un membre participe à un cours
     * @param membreId L'ID du membre
     * @param coursId L'ID du cours
     * @return true si le membre participe au cours
     */
    boolean existsByMembreIdAndCoursId(int membreId, int coursId);
    
    /**
     * Trouve les participations d'un membre dans une période
     * @param membreId L'ID du membre
     * @param debut Date de début
     * @param fin Date de fin
     * @return Liste des participations dans la période
     */
    List<Participation> findByMembreIdAndDateBetween(int membreId, LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Compte le nombre de participants à un cours
     * @param coursId L'ID du cours
     * @return Le nombre de participants
     */
    long countByCoursId(int coursId);
    
    /**
     * Trouve les participations récentes d'un membre
     * @param membreId L'ID du membre
     * @param nombreJours Nombre de jours en arrière
     * @return Liste des participations récentes
     */
    List<Participation> findParticipationsRecentesByMembre(int membreId, int nombreJours);
} 