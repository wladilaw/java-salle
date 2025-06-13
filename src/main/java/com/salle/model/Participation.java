package com.salle.model;

import java.time.LocalDateTime;

/**
 * Classe représentant une participation d'un membre à un cours
 * Gère la relation many-to-many entre Cours et Membre
 */
public class Participation {
    private int id;
    private int coursId;
    private int membreId;
    private LocalDateTime dateParticipation;
    
    // Références optionnelles pour faciliter l'utilisation
    private Cours cours;
    private Membre membre;

    // Constructeurs
    public Participation() {}

    public Participation(int id, int coursId, int membreId, LocalDateTime dateParticipation) {
        this.id = id;
        this.coursId = coursId;
        this.membreId = membreId;
        this.dateParticipation = dateParticipation;
    }

    public Participation(int coursId, int membreId, LocalDateTime dateParticipation) {
        this(0, coursId, membreId, dateParticipation);
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoursId() {
        return coursId;
    }

    public void setCoursId(int coursId) {
        this.coursId = coursId;
    }

    public int getMembreId() {
        return membreId;
    }

    public void setMembreId(int membreId) {
        this.membreId = membreId;
    }

    public LocalDateTime getDateParticipation() {
        return dateParticipation;
    }

    public void setDateParticipation(LocalDateTime dateParticipation) {
        this.dateParticipation = dateParticipation;
    }

    public Cours getCours() {
        return cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
        if (cours != null) {
            this.coursId = cours.getId();
        }
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
        if (membre != null) {
            this.membreId = membre.getId();
        }
    }

    // Méthodes utilitaires
    public boolean estValide() {
        return coursId > 0 && membreId > 0 && dateParticipation != null;
    }

    public boolean estAujourdhui() {
        if (dateParticipation == null) return false;
        LocalDateTime maintenant = LocalDateTime.now();
        return dateParticipation.toLocalDate().equals(maintenant.toLocalDate());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Participation that = (Participation) obj;
        return coursId == that.coursId && membreId == that.membreId;
    }

    @Override
    public int hashCode() {
        return coursId * 31 + membreId;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "id=" + id +
                ", coursId=" + coursId +
                ", membreId=" + membreId +
                ", dateParticipation=" + dateParticipation +
                (cours != null ? ", cours='" + cours.getNom() + "'" : "") +
                (membre != null ? ", membre='" + membre.getNom() + " " + membre.getPrenom() + "'" : "") +
                '}';
    }
} 