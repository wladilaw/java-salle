package com.salle.model;

public class Machine extends Equipement {
    private String type; // CARDIO, MUSCULATION, etc.
    private String marque;
    private String modele;
    private int anneeFabrication;

    public Machine(int id, String nom, String description, String etat, double prix,
                  String type, String marque, String modele, int anneeFabrication) {
        super(id, nom, description, etat, prix);
        this.type = type;
        this.marque = marque;
        this.modele = modele;
        this.anneeFabrication = anneeFabrication;
    }

    // Getters et Setters spécifiques
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getAnneeFabrication() {
        return anneeFabrication;
    }

    public void setAnneeFabrication(int anneeFabrication) {
        this.anneeFabrication = anneeFabrication;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", etat='" + etat + '\'' +
                ", prix=" + prix +
                ", type='" + type + '\'' +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", anneeFabrication=" + anneeFabrication +
                '}';
    }
} 