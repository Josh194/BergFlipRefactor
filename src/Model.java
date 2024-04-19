import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Model {
    void createInitialUserTable() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
            String cmd = "CREATE TABLE IF NOT EXISTS userData (" +
                    "userID INTEGER PRIMARY KEY," +
                    "username STRING," +
                    "password STRING);";
            conn.createStatement().executeUpdate(cmd);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to create table for user data!");
        }
    }
    void createInitialLeaderboard() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
            String cmd = "CREATE TABLE IF NOT EXISTS leaderboard (" +
                    "userRank INTEGER PRIMARY KEY," +
                    "score INTEGER," +
                    "username STRING);";
            conn.createStatement().executeUpdate(cmd);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addUser(String username, String password) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO userData (username, password) VALUES ('?', '?');");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteUser(String username) {

    }

    void addLeaderboardScore() {

    }
}
