package com.salle.repository;

import com.salle.config.DatabaseConfig;
import com.salle.interfaces.IParticipationRepository;
import com.salle.model.Participation;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du repository pour les Participations
 */
public class ParticipationRepository implements IParticipationRepository {
    
    private final DatabaseConfig dbConfig;
    
    public ParticipationRepository() {
        this.dbConfig = DatabaseConfig.getInstance();
    }
    
    @Override
    public Participation create(Participation participation) {
        String sql = "INSERT INTO Participation (cours_id, membre_id, date_participation) VALUES (?, ?, ?)";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, participation.getCoursId());
            stmt.setInt(2, participation.getMembreId());
            stmt.setTimestamp(3, Timestamp.valueOf(participation.getDateParticipation()));
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Échec de la création de la participation");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    participation.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Échec de la récupération de l'ID de la participation");
                }
            }
            
            System.out.println("Participation créée avec succès");
            return participation;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la participation : " + e.getMessage());
            throw new RuntimeException("Erreur de création de la participation", e);
        }
    }
    
    @Override
    public Optional<Participation> findById(int id) {
        String sql = "SELECT id, cours_id, membre_id, date_participation FROM Participation WHERE id = ?";
            
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToParticipation(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la participation par ID : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche de la participation", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Participation> findAll() {
        String sql = "SELECT id, cours_id, membre_id, date_participation FROM Participation ORDER BY date_participation DESC";
            
        List<Participation> participations = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                participations.add(mapResultSetToParticipation(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de toutes les participations : " + e.getMessage());
            throw new RuntimeException("Erreur de récupération des participations", e);
        }
        
        return participations;
    }
    
    @Override
    public Participation update(Participation participation) {
        String sql = "UPDATE Participation SET cours_id = ?, membre_id = ?, date_participation = ? WHERE id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, participation.getCoursId());
            stmt.setInt(2, participation.getMembreId());
            stmt.setTimestamp(3, Timestamp.valueOf(participation.getDateParticipation()));
            stmt.setInt(4, participation.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Participation mise à jour");
                return participation;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la participation : " + e.getMessage());
            throw new RuntimeException("Erreur de mise à jour de la participation", e);
        }
        
        return participation;
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM Participation WHERE id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Participation supprimée avec succès (ID: " + id + ")");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la participation : " + e.getMessage());
            throw new RuntimeException("Erreur de suppression de la participation", e);
        }
        
        return false;
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM Participation";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des participations : " + e.getMessage());
            throw new RuntimeException("Erreur de comptage des participations", e);
        }
        
        return 0;
    }
    
    @Override
    public List<Participation> findByMembreId(int membreId) {
        String sql = "SELECT id, cours_id, membre_id, date_participation FROM Participation " +
                     "WHERE membre_id = ? ORDER BY date_participation DESC";
            
        List<Participation> participations = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, membreId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    participations.add(mapResultSetToParticipation(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par membre : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche des participations", e);
        }
        
        return participations;
    }
    
    @Override
    public List<Participation> findByCoursId(int coursId) {
        String sql = "SELECT id, cours_id, membre_id, date_participation FROM Participation " +
                     "WHERE cours_id = ? ORDER BY date_participation";
            
        List<Participation> participations = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, coursId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    participations.add(mapResultSetToParticipation(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par cours : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche des participations", e);
        }
        
        return participations;
    }
    
    @Override
    public boolean existsByMembreIdAndCoursId(int membreId, int coursId) {
        String sql = "SELECT COUNT(*) FROM Participation WHERE membre_id = ? AND cours_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, membreId);
            stmt.setInt(2, coursId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification d'existence : " + e.getMessage());
            throw new RuntimeException("Erreur de vérification de la participation", e);
        }
        
        return false;
    }
    
    @Override
    public List<Participation> findByMembreIdAndDateBetween(int membreId, LocalDateTime debut, LocalDateTime fin) {
        String sql = "SELECT id, cours_id, membre_id, date_participation FROM Participation " +
                     "WHERE membre_id = ? AND date_participation BETWEEN ? AND ? " +
                     "ORDER BY date_participation";
            
        List<Participation> participations = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, membreId);
            stmt.setTimestamp(2, Timestamp.valueOf(debut));
            stmt.setTimestamp(3, Timestamp.valueOf(fin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    participations.add(mapResultSetToParticipation(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par période : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche des participations", e);
        }
        
        return participations;
    }
    
    @Override
    public long countByCoursId(int coursId) {
        String sql = "SELECT COUNT(*) FROM Participation WHERE cours_id = ?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, coursId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage par cours : " + e.getMessage());
            throw new RuntimeException("Erreur de comptage des participations", e);
        }
        
        return 0;
    }
    
    @Override
    public List<Participation> findParticipationsRecentesByMembre(int membreId, int nombreJours) {
        String sql = "SELECT id, cours_id, membre_id, date_participation FROM Participation " +
                     "WHERE membre_id = ? AND date_participation >= DATEADD('DAY', -?, CURRENT_TIMESTAMP) " +
                     "ORDER BY date_participation DESC";
            
        List<Participation> participations = new ArrayList<>();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, membreId);
            stmt.setInt(2, nombreJours);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    participations.add(mapResultSetToParticipation(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des participations récentes : " + e.getMessage());
            throw new RuntimeException("Erreur de recherche des participations", e);
        }
        
        return participations;
    }
    
    private Participation mapResultSetToParticipation(ResultSet rs) throws SQLException {
        return new Participation(
            rs.getInt("id"),
            rs.getInt("cours_id"),
            rs.getInt("membre_id"),
            rs.getTimestamp("date_participation").toLocalDateTime()
        );
    }
} 