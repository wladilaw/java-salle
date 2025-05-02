package com.salle.model;

import java.time.LocalDate;

public class Abonnement extends Service {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String type; // MENSUEL, ANNUEL, etc.
    private boolean renouvellementAuto;

    public Abonnement(int id, String nom, String description, double prix,
                     LocalDate dateDebut, LocalDate dateFin, String type, boolean renouvellementAuto) {
        super(id, nom, description, prix);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.type = type;
        this.renouvellementAuto = renouvellementAuto;
    }

    // Getters et Setters spécifiques
    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRenouvellementAuto() {
        return renouvellementAuto;
    }

    public void setRenouvellementAuto(boolean renouvellementAuto) {
        this.renouvellementAuto = renouvellementAuto;
    }

    public boolean estValide() {
        LocalDate aujourdhui = LocalDate.now();
        return aujourdhui.isAfter(dateDebut) && aujourdhui.isBefore(dateFin);
    }

    public boolean estExpire() {
        return LocalDate.now().isAfter(dateFin);
    }

    @Override
    public String toString() {
        return "Abonnement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", actif=" + actif +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", type='" + type + '\'' +
                ", renouvellementAuto=" + renouvellementAuto +
                '}';
    }
} 