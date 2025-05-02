package com.salle.model;

import java.util.ArrayList;
import java.util.List;

public class Entraineur extends Personne {
    private String specialite;
    private List<String> certifications;
    private List<Cours> coursEnseignes;

    public Entraineur(int id, String nom, String prenom, String email, String telephone, String specialite) {
        super(id, nom, prenom, email, telephone);
        this.specialite = specialite;
        this.certifications = new ArrayList<>();
        this.coursEnseignes = new ArrayList<>();
    }

    @Override
    public String getType() {
        return "Entraineur";
    }

    // Getters et Setters spécifiques
    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public List<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<String> certifications) {
        this.certifications = certifications;
    }

    public List<Cours> getCoursEnseignes() {
        return coursEnseignes;
    }

    public void setCoursEnseignes(List<Cours> coursEnseignes) {
        this.coursEnseignes = coursEnseignes;
    }

    public void ajouterCertification(String certification) {
        certifications.add(certification);
    }

    public void ajouterCours(Cours cours) {
        coursEnseignes.add(cours);
    }

    public void retirerCours(Cours cours) {
        coursEnseignes.remove(cours);
    }

    @Override
    public String toString() {
        return "Entraineur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", specialite='" + specialite + '\'' +
                ", certifications=" + certifications +
                ", nombreCours=" + coursEnseignes.size() +
                '}';
    }
} 