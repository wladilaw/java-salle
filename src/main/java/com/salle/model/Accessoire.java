package com.salle.model;

public class Accessoire extends Equipement {
    private String categorie; // YOGA, MUSCULATION, etc.
    private String taille;
    private String couleur;
    private int quantite;

    public Accessoire(int id, String nom, String description, String etat, double prix,
                     String categorie, String taille, String couleur, int quantite) {
        super(id, nom, description, etat, prix);
        this.categorie = categorie;
        this.taille = taille;
        this.couleur = couleur;
        this.quantite = quantite;
    }

    // Getters et Setters spécifiques
    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        if (quantite < 0) {
            throw new IllegalArgumentException("La quantité ne peut pas être négative");
        }
        this.quantite = quantite;
    }

    public void incrementerQuantite(int quantite) {
        if (quantite < 0) {
            throw new IllegalArgumentException("La quantité à ajouter ne peut pas être négative");
        }
        this.quantite += quantite;
    }

    public boolean decrementerQuantite(int quantite) {
        if (quantite < 0) {
            throw new IllegalArgumentException("La quantité à retirer ne peut pas être négative");
        }
        if (this.quantite >= quantite) {
            this.quantite -= quantite;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Accessoire{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", etat='" + etat + '\'' +
                ", prix=" + prix +
                ", categorie='" + categorie + '\'' +
                ", taille='" + taille + '\'' +
                ", couleur='" + couleur + '\'' +
                ", quantite=" + quantite +
                '}';
    }
} 