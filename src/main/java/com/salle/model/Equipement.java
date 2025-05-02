package com.salle.model;

public class Equipement {
    protected int id;
    protected String nom;
    protected String description;
    protected String etat; // DISPONIBLE, EN_MAINTENANCE, RESERVE
    protected double prix;

    public Equipement(int id, String nom, String description, String etat, double prix) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.etat = etat;
        this.prix = prix;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Equipement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", etat='" + etat + '\'' +
                ", prix=" + prix +
                '}';
    }
} 