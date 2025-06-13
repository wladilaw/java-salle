package com.salle.repository;

import com.salle.config.DatabaseConfig;
import com.salle.interfaces.ICoursRepository;
import com.salle.model.Cours;
import com.salle.model.Entraineur;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du repository pour les Cours
 */
public class CoursRepository implements ICoursRepository {
    
    private final DatabaseConfig dbConfig;
    
    public CoursRepository() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    @Override
    public Cours create(Cours cours) {
        String sql = "INSERT INTO Cours (nom, description, prix, date_heure, duree, capacite_max, entraineur_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cours.getNom());
            stmt.setString(2, cours.getDescription());
            stmt.setDouble(3, cours.getPrix());
            stmt.setTimestamp(4, Timestamp.valueOf(cours.getDateHeure()));
            stmt.setInt(5, cours.getDuree());
            stmt.setInt(6, cours.getCapaciteMax());
            stmt.setInt(7, cours.getEntraineur().getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Échec de la création du cours");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cours.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Échec de la récupération de l'ID du cours");
                }
            }
            
            System.out.println("Cours créé avec succès : " + cours.getNom());
            return cours;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du cours : " + e.getMessage());
            throw new RuntimeException("Erreur de création du cours", e);
        }
    }
    
    @Override
    public Optional<Cours> findById(int id) {
        String sql = "SELECT c.id, c.nom, c.description, c.prix, c.date_heure, c.duree, c.capacite_max, " +
                     "c.entraineur_id, p.nom as entraineur_nom, p.prenom as entraineur_prenom, " +
                     "p.email as entraineur_email, p.telephone as entraineur_telephone, e.specialite " +
                     "FROM Cours c " +
                     "JOIN Entraineur e ON c.entraineur_id = e.id " +
                     "JOIN Personne p ON e.personne_id = p.id " +
                     "WHERE c.id = ?";
            
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCours(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du cours par ID : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche du cours", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Cours> findAll() {
        String sql = "SELECT c.id, c.nom, c.description, c.prix, c.date_heure, c.duree, c.capacite_max, " +
                     "c.entraineur_id, p.nom as entraineur_nom, p.prenom as entraineur_prenom, " +
                     "p.email as entraineur_email, p.telephone as entraineur_telephone, e.specialite " +
                     "FROM Cours c " +
                     "JOIN Entraineur e ON c.entraineur_id = e.id " +
                     "JOIN Personne p ON e.personne_id = p.id " +
                     "ORDER BY c.date_heure";
            
        List<Cours> cours = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                cours.add(mapResultSetToCours(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les cours : " + e.getMessage());
            throw new RuntimeException("Erreur de récupération des cours", e);
        }
        
        return cours;
    }
    
    @Override
    public Cours update(Cours cours) {
        String sql = "UPDATE Cours SET nom = ?, description = ?, prix = ?, date_heure = ?, " +
                     "duree = ?, capacite_max = ?, entraineur_id = ? WHERE id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cours.getNom());
            stmt.setString(2, cours.getDescription());
            stmt.setDouble(3, cours.getPrix());
            stmt.setTimestamp(4, Timestamp.valueOf(cours.getDateHeure()));
            stmt.setInt(5, cours.getDuree());
            stmt.setInt(6, cours.getCapaciteMax());
            stmt.setInt(7, cours.getEntraineur().getId());
            stmt.setInt(8, cours.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cours mis à jour : " + cours.getNom());
                return cours;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du cours : " + e.getMessage());
            throw new RuntimeException("Erreur de mise à jour du cours", e);
        }
        
        return cours;
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Cours WHERE id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Cours supprimé avec succès (ID: " + id + ")");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du cours : " + e.getMessage());
            throw new RuntimeException("Erreur de suppression du cours", e);
        }
        
        return false;
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM Cours";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des cours : " + e.getMessage());
            throw new RuntimeException("Erreur de comptage des cours", e);
        }
        
        return 0;
    }
    
    @Override
    public List<Cours> findByEntraineurId(int entraineurId) {
        String sql = "SELECT c.id, c.nom, c.description, c.prix, c.date_heure, c.duree, c.capacite_max, " +
                     "c.entraineur_id, p.nom as entraineur_nom, p.prenom as entraineur_prenom, " +
                     "p.email as entraineur_email, p.telephone as entraineur_telephone, e.specialite " +
                     "FROM Cours c " +
                     "JOIN Entraineur e ON c.entraineur_id = e.id " +
                     "JOIN Personne p ON e.personne_id = p.id " +
                     "WHERE c.entraineur_id = ? " +
                     "ORDER BY c.date_heure";
            
        List<Cours> cours = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, entraineurId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cours.add(mapResultSetToCours(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par entraîneur : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche des cours", e);
        }
        
        return cours;
    }
    
    @Override
    public List<Cours> findByDateHeureBetween(LocalDateTime debut, LocalDateTime fin) {
        String sql = "SELECT c.id, c.nom, c.description, c.prix, c.date_heure, c.duree, c.capacite_max, " +
                     "c.entraineur_id, p.nom as entraineur_nom, p.prenom as entraineur_prenom, " +
                     "p.email as entraineur_email, p.telephone as entraineur_telephone, e.specialite " +
                     "FROM Cours c " +
                     "JOIN Entraineur e ON c.entraineur_id = e.id " +
                     "JOIN Personne p ON e.personne_id = p.id " +
                     "WHERE c.date_heure BETWEEN ? AND ? " +
                     "ORDER BY c.date_heure";
            
        List<Cours> cours = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(debut));
            stmt.setTimestamp(2, Timestamp.valueOf(fin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cours.add(mapResultSetToCours(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par période : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche des cours", e);
        }
        
        return cours;
    }
    
    @Override
    public List<Cours> findCoursDisponibles() {
        String sql = "SELECT c.id, c.nom, c.description, c.prix, c.date_heure, c.duree, c.capacite_max, " +
                     "c.entraineur_id, p.nom as entraineur_nom, p.prenom as entraineur_prenom, " +
                     "p.email as entraineur_email, p.telephone as entraineur_telephone, e.specialite, " +
                     "COALESCE(participants.nb_participants, 0) as nb_participants " +
                     "FROM Cours c " +
                     "JOIN Entraineur e ON c.entraineur_id = e.id " +
                     "JOIN Personne p ON e.personne_id = p.id " +
                     "LEFT JOIN ( " +
                     "  SELECT cours_id, COUNT(*) as nb_participants " +
                     "  FROM Participation " +
                     "  GROUP BY cours_id " +
                     ") participants ON c.id = participants.cours_id " +
                     "WHERE COALESCE(participants.nb_participants, 0) < c.capacite_max " +
                     "AND c.date_heure > NOW() " +
                     "ORDER BY c.date_heure";
            
        List<Cours> cours = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                cours.add(mapResultSetToCours(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des cours disponibles : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche des cours disponibles", e);
        }
        
        return cours;
    }
    
    @Override
    public List<Cours> findCoursAujourdhui() {
        String sql = "SELECT c.id, c.nom, c.description, c.prix, c.date_heure, c.duree, c.capacite_max, " +
                     "c.entraineur_id, p.nom as entraineur_nom, p.prenom as entraineur_prenom, " +
                     "p.email as entraineur_email, p.telephone as entraineur_telephone, e.specialite " +
                     "FROM Cours c " +
                     "JOIN Entraineur e ON c.entraineur_id = e.id " +
                     "JOIN Personne p ON e.personne_id = p.id " +
                                           "WHERE CAST(c.date_heure AS DATE) = CURRENT_DATE " +
                     "ORDER BY c.date_heure";
            
        List<Cours> cours = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                cours.add(mapResultSetToCours(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des cours d'aujourd'hui : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche des cours", e);
        }
        
        return cours;
    }
    
    private Cours mapResultSetToCours(ResultSet rs) throws SQLException {
        // Créer l'entraîneur
        Entraineur entraineur = new Entraineur(
            rs.getInt("entraineur_id"),
            rs.getString("entraineur_nom"),
            rs.getString("entraineur_prenom"),
            rs.getString("entraineur_email"),
            rs.getString("entraineur_telephone"),
            rs.getString("specialite")
        );
        
        // Créer le cours
        return new Cours(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("description"),
            rs.getDouble("prix"),
            rs.getTimestamp("date_heure").toLocalDateTime(),
            rs.getInt("duree"),
            rs.getInt("capacite_max"),
            entraineur
        );
    }
} 