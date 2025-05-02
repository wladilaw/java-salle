package com.salle.model;

import java.time.LocalDate;

public class Membre extends Personne {
    private LocalDate dateInscription;
    private String typeAbonnement;
    private boolean actif;

    public Membre(int id, String nom, String prenom, String email, String telephone, 
                 LocalDate dateInscription, String typeAbonnement) {
        super(id, nom, prenom, email, telephone);
        this.dateInscription = dateInscription;
        this.typeAbonnement = typeAbonnement;
        this.actif = true;
    }

    @Override
    public String getType() {
        return "Membre";
    }

    // Getters et Setters spécifiques
    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getTypeAbonnement() {
        return typeAbonnement;
    }

    public void setTypeAbonnement(String typeAbonnement) {
        this.typeAbonnement = typeAbonnement;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "Membre{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", dateInscription=" + dateInscription +
                ", typeAbonnement='" + typeAbonnement + '\'' +
                ", actif=" + actif +
                '}';
    }
} 