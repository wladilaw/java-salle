package com.salle.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Configuration et gestion de la connexion à la base de données
 * Utilise H2 Database en mémoire pour la démonstration
 */
public class DatabaseConfig {
    
    // Singleton instance
    private static DatabaseConfig instance;
    
    // Configuration de la base de données H2 (en mémoire)
    private static final String DB_URL = "jdbc:h2:mem:gymdb;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS PUBLIC";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private static final String DB_DRIVER = "org.h2.Driver";
    
    // Connexion actuelle
    private Connection connection;
    
    /**
     * Constructeur privé pour pattern Singleton
     */
    private DatabaseConfig() {
        try {
            // Charger le driver H2
            Class.forName(DB_DRIVER);
            
            // Établir la connexion
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Créer les tables automatiquement
            createTables();
            
            System.out.println("✅ Connexion H2 établie avec succès !");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver H2 non trouvé : " + e.getMessage());
            throw new RuntimeException("Driver H2 non disponible", e);
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la base de données : " + e.getMessage());
            throw new RuntimeException("Impossible de se connecter à la base de données", e);
        }
    }
    
    /**
     * Obtient l'instance unique (Singleton)
     */
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }
    
    /**
     * Retourne la connexion active
     */
    public Connection getConnection() {
        try {
            // Vérifier si la connexion est toujours valide
            if (connection == null || connection.isClosed()) {
                // Recréer la connexion si nécessaire
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération de la connexion : " + e.getMessage());
            throw new RuntimeException("Impossible d'obtenir la connexion", e);
        }
    }
    
    /**
     * Teste la connexion à la base de données
     */
    public boolean testConnection() {
        try (Connection testConn = getConnection();
             Statement stmt = testConn.createStatement()) {
            
            stmt.execute("SELECT 1");
            System.out.println("✅ Test de connexion réussi !");
            return true;
            
        } catch (SQLException e) {
            System.err.println("❌ Test de connexion échoué : " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Crée les tables nécessaires
     */
    private void createTables() {
        try (Statement stmt = connection.createStatement()) {
            
            // Table Personne
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Personne (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "nom VARCHAR(100) NOT NULL, " +
                "prenom VARCHAR(100) NOT NULL, " +
                "email VARCHAR(255) UNIQUE NOT NULL, " +
                "telephone VARCHAR(20), " +
                "type VARCHAR(20) NOT NULL" +
                ")"
            );
            
            // Table Membre
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Membre (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "type_abonnement VARCHAR(50) NOT NULL, " +
                "date_inscription DATE DEFAULT CURRENT_DATE, " +
                "actif BOOLEAN DEFAULT TRUE, " +
                "personne_id INT, " +
                "FOREIGN KEY (personne_id) REFERENCES Personne(id) ON DELETE CASCADE" +
                ")"
            );
            
            // Table Entraineur
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Entraineur (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "specialite VARCHAR(100) NOT NULL, " +
                "personne_id INT, " +
                "FOREIGN KEY (personne_id) REFERENCES Personne(id) ON DELETE CASCADE" +
                ")"
            );
            
            // Table Cours
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Cours (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "nom VARCHAR(200) NOT NULL, " +
                "description TEXT, " +
                "prix DECIMAL(10,2) NOT NULL, " +
                "date_heure TIMESTAMP NOT NULL, " +
                "duree INT NOT NULL, " +
                "capacite_max INT NOT NULL, " +
                "entraineur_id INT, " +
                "FOREIGN KEY (entraineur_id) REFERENCES Entraineur(id)" +
                ")"
            );
            
            // Table Participation
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Participation (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "cours_id INT NOT NULL, " +
                "membre_id INT NOT NULL, " +
                "date_participation TIMESTAMP NOT NULL, " +
                "FOREIGN KEY (cours_id) REFERENCES Cours(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (membre_id) REFERENCES Membre(id) ON DELETE CASCADE, " +
                "UNIQUE(cours_id, membre_id)" +
                ")"
            );
            
            System.out.println("✅ Tables créées avec succès !");
            
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la création des tables : " + e.getMessage());
            throw new RuntimeException("Impossible de créer les tables", e);
        }
    }
    
    /**
     * Ferme la connexion proprement
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔌 Connexion fermée proprement.");
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Erreur lors de la fermeture : " + e.getMessage());
        }
    }
    
    /**
     * Affiche les informations de configuration
     */
    public void printConfig() {
        System.out.println("📊 Configuration Base de Données :");
        System.out.println("   - Type: H2 Database (en mémoire)");
        System.out.println("   - URL: " + DB_URL);
        System.out.println("   - Utilisateur: " + DB_USER);
        System.out.println("   - Statut: " + (testConnection() ? "Connecté" : "Déconnecté"));
    }
} 