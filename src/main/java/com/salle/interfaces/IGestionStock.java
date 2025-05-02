package com.salle.interfaces;

import com.salle.model.Equipement;
import java.util.List;

public interface IGestionStock {
    void ajouterEquipement(Equipement equipement);
    void retirerEquipement(int id);
    void mettreAJourEtat(int id, String nouvelEtat);
    Equipement rechercherEquipement(int id);
    List<Equipement> listerEquipements();
    List<Equipement> listerEquipementsParEtat(String etat);
} 