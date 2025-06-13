package com.salle.repository;

import com.salle.config.DatabaseConfig;
import com.salle.interfaces.IEntraineurRepository;
import com.salle.model.Entraineur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du repository pour les Entraîneurs
 */
public class EntraineurRepository implements IEntraineurRepository {
    
    private final DatabaseConfig dbConfig;
    
    public EntraineurRepository() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    @Override
    public Entraineur create(Entraineur entraineur) {
        String sqlPersonne = "INSERT INTO Personne (nom, prenom, email, telephone, type) VALUES (?, ?, ?, ?, 'entraineur')";
        String sqlEntraineur = "INSERT INTO Entraineur (specialite, personne_id) VALUES (?, ?)";
        
        try (Connection conn = dbConfig.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmtPersonne = conn.prepareStatement(sqlPersonne, Statement.RETURN_GENERATED_KEYS)) {
                stmtPersonne.setString(1, entraineur.getNom());
                stmtPersonne.setString(2, entraineur.getPrenom());
                stmtPersonne.setString(3, entraineur.getEmail());
                stmtPersonne.setString(4, entraineur.getTelephone());
                
                int rowsAffected = stmtPersonne.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Échec de la création de la personne");
                }
                
                int personneId;
                try (ResultSet generatedKeys = stmtPersonne.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        personneId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Échec de la récupération de l'ID de la personne");
                    }
                }
                
                try (PreparedStatement stmtEntraineur = conn.prepareStatement(sqlEntraineur, Statement.RETURN_GENERATED_KEYS)) {
                    stmtEntraineur.setString(1, entraineur.getSpecialite());
                    stmtEntraineur.setInt(2, personneId);
                    stmtEntraineur.executeUpdate();
                    
                    // Récupérer l'ID de l'entraîneur généré
                    try (ResultSet entraineurKeys = stmtEntraineur.getGeneratedKeys()) {
                        if (entraineurKeys.next()) {
                            entraineur.setId(entraineurKeys.getInt(1));
                        }
                    }
                }
                
                conn.commit();
                System.out.println("Entraîneur créé avec succès : " + entraineur.getNom() + " " + entraineur.getPrenom());
                return entraineur;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'entraîneur : " + e.getMessage());
            throw new RuntimeException("Erreur de création de l'entraîneur", e);
        }
    }
    
    @Override
    public Optional<Entraineur> findById(int id) {
        String sql = "SELECT e.id, p.nom, p.prenom, p.email, p.telephone, " +
                     "e.specialite " +
                     "FROM Personne p " +
                     "JOIN Entraineur e ON p.id = e.personne_id " +
                     "WHERE e.id = ?";
            
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntraineur(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'entraîneur par ID : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche de l'entraîneur", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Entraineur> findAll() {
        String sql = "SELECT e.id, p.nom, p.prenom, p.email, p.telephone, " +
                     "e.specialite " +
                     "FROM Personne p " +
                     "JOIN Entraineur e ON p.id = e.personne_id " +
                     "ORDER BY p.nom, p.prenom";
            
        List<Entraineur> entraineurs = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                entraineurs.add(mapResultSetToEntraineur(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les entraîneurs : " + e.getMessage());
            throw new RuntimeException("Erreur de récupération des entraîneurs", e);
        }
        
        return entraineurs;
    }
    
    @Override
    public Entraineur update(Entraineur entraineur) {
        String sqlPersonne = "UPDATE Personne SET nom = ?, prenom = ?, email = ?, telephone = ? WHERE id = ?";
        String sqlEntraineur = "UPDATE Entraineur SET specialite = ? WHERE personne_id = ?";
        
        try (Connection conn = dbConfig.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                try (PreparedStatement stmtPersonne = conn.prepareStatement(sqlPersonne)) {
                    stmtPersonne.setString(1, entraineur.getNom());
                    stmtPersonne.setString(2, entraineur.getPrenom());
                    stmtPersonne.setString(3, entraineur.getEmail());
                    stmtPersonne.setString(4, entraineur.getTelephone());
                    stmtPersonne.setInt(5, entraineur.getId());
                    stmtPersonne.executeUpdate();
                }
                
                try (PreparedStatement stmtEntraineur = conn.prepareStatement(sqlEntraineur)) {
                    stmtEntraineur.setString(1, entraineur.getSpecialite());
                    stmtEntraineur.setInt(2, entraineur.getId());
                    stmtEntraineur.executeUpdate();
                }
                
                conn.commit();
                System.out.println("Entraîneur mis à jour : " + entraineur.getNom() + " " + entraineur.getPrenom());
                return entraineur;
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'entraîneur : " + e.getMessage());
            throw new RuntimeException("Erreur de mise à jour de l'entraîneur", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Personne WHERE id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Entraîneur supprimé avec succès (ID: " + id + ")");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'entraîneur : " + e.getMessage());
            throw new RuntimeException("Erreur de suppression de l'entraîneur", e);
        }
        
        return false;
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM Entraineur";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des entraîneurs : " + e.getMessage());
            throw new RuntimeException("Erreur de comptage des entraîneurs", e);
        }
        
        return 0;
    }
    
    @Override
    public List<Entraineur> findBySpecialite(String specialite) {
        String sql = "SELECT e.id, p.nom, p.prenom, p.email, p.telephone, " +
                     "e.specialite " +
                     "FROM Personne p " +
                     "JOIN Entraineur e ON p.id = e.personne_id " +
                     "WHERE e.specialite = ? " +
                     "ORDER BY p.nom, p.prenom";
            
        List<Entraineur> entraineurs = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, specialite);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    entraineurs.add(mapResultSetToEntraineur(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par spécialité : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche des entraîneurs", e);
        }
        
        return entraineurs;
    }
    
    @Override
    public Entraineur findByEmail(String email) {
        String sql = "SELECT e.id, p.nom, p.prenom, p.email, p.telephone, " +
                     "e.specialite " +
                     "FROM Personne p " +
                     "JOIN Entraineur e ON p.id = e.personne_id " +
                     "WHERE p.email = ?";
            
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntraineur(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par email : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche de l'entraîneur", e);
        }
        
        return null;
    }
    
    @Override
    public List<Entraineur> findEntraineursDisponibles() {
        // Entraîneurs sans cours aujourd'hui
        String sql = "SELECT e.id, p.nom, p.prenom, p.email, p.telephone, " +
                     "e.specialite " +
                     "FROM Personne p " +
                     "JOIN Entraineur e ON p.id = e.personne_id " +
                     "WHERE e.id NOT IN ( " +
                     "  SELECT DISTINCT entraineur_id FROM Cours " +
                     "  WHERE CAST(date_heure AS DATE) = CURRENT_DATE " +
                     ") " +
                     "ORDER BY p.nom, p.prenom";
            
        List<Entraineur> entraineurs = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                entraineurs.add(mapResultSetToEntraineur(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des entraîneurs disponibles : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche des entraîneurs disponibles", e);
        }
        
        return entraineurs;
    }
    
    @Override
    public long countBySpecialite(String specialite) {
        String sql = "SELECT COUNT(*) FROM Entraineur WHERE specialite = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, specialite);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage par spécialité : " + e.getMessage());
            throw new RuntimeException("Erreur de comptage des entraîneurs", e);
        }
        
        return 0;
    }
    
    private Entraineur mapResultSetToEntraineur(ResultSet rs) throws SQLException {
        return new Entraineur(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("prenom"),
            rs.getString("email"),
            rs.getString("telephone"),
            rs.getString("specialite")
        );
    }
} 