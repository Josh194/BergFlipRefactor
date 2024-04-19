import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Model {
    Connection conn;
    void createInitialUserTable() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
            String cmd = "CREATE TABLE IF NOT EXISTS userData (" +
                    "userID INTEGER PRIMARY KEY," +
                    "username STRING," +
                    "password STRING);";
            conn.createStatement().executeUpdate(cmd);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to create table for user data!");
        }
    }

    void createInitialLeaderboard() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
            String cmd = "CREATE TABLE IF NOT EXISTS leaderboard (" +
                    "userRank INTEGER PRIMARY KEY," +
                    "score INTEGER," +
                    "username STRING);";
            conn.createStatement().executeUpdate(cmd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addUser(String username, String password) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO userData (username, password) VALUES (?, ?);");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteUser(String username) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM userData WHERE username = ?");
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addLeaderboardScore(String username, int score) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO leaderboard (username, score) VALUES (?, ?);");
            stmt.setString(1, username);
            stmt.setInt(2, score);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
