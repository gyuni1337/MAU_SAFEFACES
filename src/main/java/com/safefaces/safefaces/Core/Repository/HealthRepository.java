package com.safefaces.safefaces.Core.Repository;

import com.safefaces.safefaces.Core.DatabaseConnection;
import com.safefaces.safefaces.Core.Model.HealthEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HealthRepository {

    // ── Queries ──────────────────────────────────────────────────────────────

    public List<HealthEntry> findByUserAndCategory(int userId, String category) {
        List<HealthEntry> list = new ArrayList<>();
        String sql = """
                SELECT id, user_id, category, title
                FROM safefaces.health_entries
                WHERE user_id = ? AND category = ?
                ORDER BY id
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HealthEntry e = new HealthEntry();
                    e.id       = rs.getInt("id");
                    e.userId   = rs.getInt("user_id");
                    e.category = rs.getString("category");
                    e.title    = rs.getString("title");
                    list.add(e);
                }
            }
        } catch (SQLException e) {
            System.out.println("HealthRepository.findByUserAndCategory error: " + e.getMessage());
        }
        return list;
    }

    public void save(int userId, String category, String title) {
        String sql = """
                INSERT INTO safefaces.health_entries (user_id, category, title)
                VALUES (?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, category);
            stmt.setString(3, title);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("HealthRepository.save error: " + e.getMessage());
        }
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM safefaces.health_entries WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("HealthRepository.deleteById error: " + e.getMessage());
        }
    }
}
