package com.salle.model;

import java.time.LocalDateTime;

public class Notification {
    private int id;
    private String message;
    private LocalDateTime dateCreation;
    private boolean lue;
    private String type; // INFO, ALERTE, RAPPEL
    private Membre destinataire;

    public Notification(int id, String message, String type, Membre destinataire) {
        this.id = id;
        this.message = message;
        this.dateCreation = LocalDateTime.now();
        this.lue = false;
        this.type = type;
        this.destinataire = destinataire;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public boolean isLue() {
        return lue;
    }

    public void setLue(boolean lue) {
        this.lue = lue;
    }

    public String getType() {
        return type;
    }

    public Membre getDestinataire() {
        return destinataire;
    }

    @Override
    public String toString() {
        String prefix = "";
        switch (type) {
            case "ALERTE":
                prefix = "⚠️ ";
                break;
            case "RAPPEL":
                prefix = "🔔 ";
                break;
            case "INFO":
                prefix = "ℹ️ ";
                break;
        }
        return String.format("%s[%s] %s - %s", 
            prefix,
            type,
            dateCreation.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            message
        );
    }
} 