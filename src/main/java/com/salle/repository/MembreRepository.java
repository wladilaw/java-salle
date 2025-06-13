package com.salle.repository;

import com.salle.config.DatabaseConfig;
import com.salle.interfaces.IMembreRepository;
import com.salle.model.Membre;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du repository pour les Membres
 * Gère toutes les opérations CRUD et les requêtes spécialisées
 */
public class MembreRepository implements IMembreRepository {
    
    private final DatabaseConfig dbConfig;
    
    public MembreRepository() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    @Override
    public Membre create(Membre membre) {
        String sqlPersonne = "INSERT INTO Personne (nom, prenom, email, telephone, type) VALUES (?, ?, ?, ?, 'membre')";
        String sqlMembre = "INSERT INTO Membre (type_abonnement, actif, personne_id) VALUES (?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection()) {
            conn.setAutoCommit(false); // Transaction
            
            try (PreparedStatement stmtPersonne = conn.prepareStatement(sqlPersonne, Statement.RETURN_GENERATED_KEYS)) {
                // Insertion dans la table Personne
                stmtPersonne.setString(1, membre.getNom());
                stmtPersonne.setString(2, membre.getPrenom());
                stmtPersonne.setString(3, membre.getEmail());
                stmtPersonne.setString(4, membre.getTelephone());
                
                int rowsAffected = stmtPersonne.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Échec de la création de la personne");
                }
                
                // Récupération de l'ID généré pour Personne
                int personneId;
                try (ResultSet generatedKeys = stmtPersonne.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        personneId = generatedKeys.getInt(1);
                        membre.setId(personneId);
                    } else {
                        throw new SQLException("Échec de la récupération de l'ID de la personne");
                    }
                }
                
                // Insertion dans la table Membre
                try (PreparedStatement stmtMembre = conn.prepareStatement(sqlMembre, Statement.RETURN_GENERATED_KEYS)) {
                    stmtMembre.setString(1, membre.getTypeAbonnement());
                    stmtMembre.setBoolean(2, membre.isActif());
                    stmtMembre.setInt(3, personneId);
                    
                    stmtMembre.executeUpdate();
                }
                
                conn.commit(); // Validation de la transaction
                System.out.println("Membre créé avec succès : " + membre.getNom() + " " + membre.getPrenom());
                return membre;
                
            } catch (SQLException e) {
                conn.rollback(); // Annulation en cas d'erreur
                throw e;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du membre : " + e.getMessage());
            throw new RuntimeException("Erreur de création du membre", e);
        }
    }
    
    @Override
    public Optional<Membre> findById(int id) {
        String sql = "SELECT p.id, p.nom, p.prenom, p.email, p.telephone, " +
                     "m.date_inscription, m.type_abonnement, m.actif " +
                     "FROM Personne p " +
                     "JOIN Membre m ON p.id = m.personne_id " +
                     "WHERE p.id = ?";
            
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMembre(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du membre par ID : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche du membre", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Membre> findAll() {
        String sql = "SELECT p.id, p.nom, p.prenom, p.email, p.telephone, " +
                     "m.date_inscription, m.type_abonnement, m.actif " +
                     "FROM Personne p " +
                     "JOIN Membre m ON p.id = m.personne_id " +
                     "ORDER BY p.nom, p.prenom";
            
        List<Membre> membres = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                membres.add(mapResultSetToMembre(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les membres : " + e.getMessage());
            throw new RuntimeException("Erreur de récupération des membres", e);
        }
        
        return membres;
    }
    
    @Override
    public Membre update(Membre membre) {
        String sqlPersonne = "UPDATE Personne SET nom = ?, prenom = ?, email = ?, telephone = ? WHERE id = ?";
        String sqlMembre = "UPDATE Membre SET type_abonnement = ?, actif = ? WHERE personne_id = ?";
        
        try (Connection conn = dbConfig.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Mise à jour de la table Personne
                try (PreparedStatement stmtPersonne = conn.prepareStatement(sqlPersonne)) {
                    stmtPersonne.setString(1, membre.getNom());
                    stmtPersonne.setString(2, membre.getPrenom());
                    stmtPersonne.setString(3, membre.getEmail());
                    stmtPersonne.setString(4, membre.getTelephone());
                    stmtPersonne.setInt(5, membre.getId());
                    stmtPersonne.executeUpdate();
                }
                
                // Mise à jour de la table Membre
                try (PreparedStatement stmtMembre = conn.prepareStatement(sqlMembre)) {
                    stmtMembre.setString(1, membre.getTypeAbonnement());
                    stmtMembre.setBoolean(2, membre.isActif());
                    stmtMembre.setInt(3, membre.getId());
                    stmtMembre.executeUpdate();
                }
                
                conn.commit();
                System.out.println("Membre mis à jour avec succès : " + membre.getNom() + " " + membre.getPrenom());
                return membre;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du membre : " + e.getMessage());
            throw new RuntimeException("Erreur de mise à jour du membre", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Personne WHERE id = ?"; // CASCADE supprimera automatiquement le membre
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Membre supprimé avec succès (ID: " + id + ")");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du membre : " + e.getMessage());
            throw new RuntimeException("Erreur de suppression du membre", e);
        }
        
        return false;
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM Membre";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des membres : " + e.getMessage());
            throw new RuntimeException("Erreur de comptage des membres", e);
        }
        
        return 0;
    }
    
    @Override
    public List<Membre> findByActif(boolean actif) {
        String sql = "SELECT p.id, p.nom, p.prenom, p.email, p.telephone, " +
                     "m.date_inscription, m.type_abonnement, m.actif " +
                     "FROM Personne p " +
                     "JOIN Membre m ON p.id = m.personne_id " +
                     "WHERE m.actif = ? " +
                     "ORDER BY p.nom, p.prenom";
            
        List<Membre> membres = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, actif);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    membres.add(mapResultSetToMembre(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des membres par statut actif : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche des membres", e);
        }
        
        return membres;
    }
    
    @Override
    public List<Membre> findByTypeAbonnement(String typeAbonnement) {
        String sql = "SELECT p.id, p.nom, p.prenom, p.email, p.telephone, " +
                     "m.date_inscription, m.type_abonnement, m.actif " +
                     "FROM Personne p " +
                     "JOIN Membre m ON p.id = m.personne_id " +
                     "WHERE m.type_abonnement = ? " +
                     "ORDER BY p.nom, p.prenom";
            
        List<Membre> membres = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, typeAbonnement);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    membres.add(mapResultSetToMembre(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des membres par type d'abonnement : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche des membres", e);
        }
        
        return membres;
    }
    
    @Override
    public Membre findByEmail(String email) {
        String sql = "SELECT p.id, p.nom, p.prenom, p.email, p.telephone, " +
                     "m.date_inscription, m.type_abonnement, m.actif " +
                     "FROM Personne p " +
                     "JOIN Membre m ON p.id = m.personne_id " +
                     "WHERE p.email = ?";
            
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMembre(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du membre par email : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche du membre", e);
        }
        
        return null;
    }
    
    @Override
    public long countMembresActifs() {
        String sql = "SELECT COUNT(*) FROM Membre WHERE actif = true";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des membres actifs : " + e.getMessage());
            throw new RuntimeException("Erreur de comptage des membres actifs", e);
        }
        
        return 0;
    }
    
    /**
     * Convertit un ResultSet en objet Membre
     * @param rs Le ResultSet de la requête
     * @return L'objet Membre correspondant
     * @throws SQLException Si erreur de lecture du ResultSet
     */
    private Membre mapResultSetToMembre(ResultSet rs) throws SQLException {
        // Date par défaut si null (pour compatibilité H2)
        LocalDate dateInscription = LocalDate.now();
        try {
            if (rs.getDate("date_inscription") != null) {
                dateInscription = rs.getDate("date_inscription").toLocalDate();
            }
        } catch (SQLException ignored) {
            // La colonne n'existe pas ou est null, utiliser la date actuelle
        }
        
        return new Membre(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("prenom"),
            rs.getString("email"),
            rs.getString("telephone"),
            dateInscription,
            rs.getString("type_abonnement")
        );
    }
} 