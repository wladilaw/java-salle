package com.salle.model;

import java.time.LocalDate;
import java.util.*;

public class Statistiques {
    private int nombreTotalMembres;
    private int nombreMembresActifs;
    private int nombreCoursTotal;
    private int nombreCoursAujourdhui;
    private Map<String, Integer> equipementsParType;
    private Map<String, Integer> coursParType;
    private double revenuMensuel;
    private List<String> evenementsRecents;

    public Statistiques() {
        this.equipementsParType = new HashMap<>();
        this.coursParType = new HashMap<>();
        this.evenementsRecents = new ArrayList<>();
    }

    public void mettreAJourStatistiques(List<Membre> membres, List<Cours> cours, List<Equipement> equipements) {
        // Statistiques des membres
        this.nombreTotalMembres = membres.size();
        this.nombreMembresActifs = (int) membres.stream()
                .filter(Membre::isActif)
                .count();

        // Statistiques des cours
        this.nombreCoursTotal = cours.size();
        this.nombreCoursAujourdhui = (int) cours.stream()
                .filter(c -> c.getDateHeure().toLocalDate().equals(LocalDate.now()))
                .count();

        // Statistiques des équipements
        equipementsParType.clear();
        equipements.forEach(e -> {
            if (e instanceof Machine) {
                String type = ((Machine) e).getType();
                equipementsParType.merge(type, 1, Integer::sum);
            } else if (e instanceof Accessoire) {
                String categorie = ((Accessoire) e).getCategorie();
                equipementsParType.merge(categorie, 1, Integer::sum);
            }
        });

        // Calcul du revenu mensuel
        this.revenuMensuel = membres.stream()
                .map(m -> {
                    if (m.getTypeAbonnement().equals("MENSUEL")) {
                        return 50.0; // Prix mensuel
                    } else if (m.getTypeAbonnement().equals("ANNUEL")) {
                        return 500.0 / 12; // Prix annuel divisé par 12
                    }
                    return 0.0;
                })
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public void ajouterEvenement(String evenement) {
        evenementsRecents.add(0, evenement); // Ajouter au début
        if (evenementsRecents.size() > 10) {
            evenementsRecents.remove(evenementsRecents.size() - 1);
        }
    }

    // Getters
    public int getNombreTotalMembres() {
        return nombreTotalMembres;
    }

    public int getNombreMembresActifs() {
        return nombreMembresActifs;
    }

    public int getNombreCoursTotal() {
        return nombreCoursTotal;
    }

    public int getNombreCoursAujourdhui() {
        return nombreCoursAujourdhui;
    }

    public Map<String, Integer> getEquipementsParType() {
        return equipementsParType;
    }

    public Map<String, Integer> getCoursParType() {
        return coursParType;
    }

    public double getRevenuMensuel() {
        return revenuMensuel;
    }

    public List<String> getEvenementsRecents() {
        return evenementsRecents;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== STATISTIQUES DE LA SALLE ===\n");
        sb.append("Membres : ").append(nombreMembresActifs).append("/").append(nombreTotalMembres).append(" actifs\n");
        sb.append("Cours aujourd'hui : ").append(nombreCoursAujourdhui).append("/").append(nombreCoursTotal).append(" total\n");
        sb.append("Revenu mensuel estimé : ").append(String.format("%.2f", revenuMensuel)).append(" €\n");
        
        sb.append("\nÉquipements par type :\n");
        equipementsParType.forEach((type, count) -> 
            sb.append("- ").append(type).append(" : ").append(count).append("\n")
        );

        sb.append("\nÉvénements récents :\n");
        evenementsRecents.forEach(event -> 
            sb.append("- ").append(event).append("\n")
        );

        return sb.toString();
    }
} 