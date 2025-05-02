package com.salle.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cours extends Service {
    private LocalDateTime dateHeure;
    private int duree; // en minutes
    private int capaciteMax;
    private List<Membre> participants;
    private Entraineur entraineur;

    public Cours(int id, String nom, String description, double prix,
                LocalDateTime dateHeure, int duree, int capaciteMax, Entraineur entraineur) {
        super(id, nom, description, prix);
        this.dateHeure = dateHeure;
        this.duree = duree;
        this.capaciteMax = capaciteMax;
        this.entraineur = entraineur;
        this.participants = new ArrayList<>();
    }

    // Getters et Setters spécifiques
    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public List<Membre> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Membre> participants) {
        this.participants = participants;
    }

    public Entraineur getEntraineur() {
        return entraineur;
    }

    public void setEntraineur(Entraineur entraineur) {
        this.entraineur = entraineur;
    }

    public boolean ajouterParticipant(Membre membre) {
        if (participants.size() < capaciteMax) {
            return participants.add(membre);
        }
        return false;
    }

    public boolean retirerParticipant(Membre membre) {
        return participants.remove(membre);
    }

    public boolean estComplet() {
        return participants.size() >= capaciteMax;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", actif=" + actif +
                ", dateHeure=" + dateHeure +
                ", duree=" + duree +
                ", capaciteMax=" + capaciteMax +
                ", nombreParticipants=" + participants.size() +
                ", entraineur=" + entraineur.getNom() + " " + entraineur.getPrenom() +
                '}';
    }
} 