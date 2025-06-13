package com.salle.interfaces;

import com.salle.model.Abonnement;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface spécialisée pour les opérations sur les Abonnements
 */
public interface IAbonnementRepository extends IRepository<Abonnement> {
    
    /**
     * Trouve les abonnements par membre
     * @param membreId L'ID du membre
     * @return Liste des abonnements du membre
     */
    List<Abonnement> findByMembreId(int membreId);
    
    /**
     * Trouve les abonnements par type
     * @param type Le type d'abonnement
     * @return Liste des abonnements de ce type
     */
    List<Abonnement> findByType(String type);
    
    /**
     * Trouve les abonnements actifs (non expirés)
     * @return Liste des abonnements actifs
     */
    List<Abonnement> findAbonnementsActifs();
    
    /**
     * Trouve les abonnements qui expirent bientôt
     * @param nombreJours Nombre de jours avant expiration
     * @return Liste des abonnements qui expirent
     */
    List<Abonnement> findAbonnementsExpirantDans(int nombreJours);
    
    /**
     * Trouve l'abonnement actuel d'un membre
     * @param membreId L'ID du membre
     * @return L'abonnement actuel ou null
     */
    Abonnement findAbonnementActuelByMembreId(int membreId);
    
    /**
     * Compte les abonnements par type
     * @param type Le type d'abonnement
     * @return Le nombre d'abonnements
     */
    long countByType(String type);
} 