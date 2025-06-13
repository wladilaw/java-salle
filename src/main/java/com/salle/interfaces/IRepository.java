package com.salle.interfaces;

import java.util.List;
import java.util.Optional;

/**
 * Interface générique pour les opérations CRUD sur les entités
 * @param <T> Type de l'entité
 */
public interface IRepository<T> {
    
    /**
     * Crée une nouvelle entité en base
     * @param entity L'entité à créer
     * @return L'entité créée avec son ID généré
     */
    T create(T entity);
    
    /**
     * Trouve une entité par son ID
     * @param id L'identifiant de l'entité
     * @return Un Optional contenant l'entité si trouvée
     */
    Optional<T> findById(int id);
    
    /**
     * Récupère toutes les entités
     * @return Liste de toutes les entités
     */
    List<T> findAll();
    
    /**
     * Met à jour une entité existante
     * @param entity L'entité à mettre à jour
     * @return L'entité mise à jour
     */
    T update(T entity);
    
    /**
     * Supprime une entité par son ID
     * @param id L'identifiant de l'entité à supprimer
     * @return true si la suppression a réussi
     */
    boolean delete(int id);
    
    /**
     * Compte le nombre total d'entités
     * @return Le nombre d'entités
     */
    long count();
} 