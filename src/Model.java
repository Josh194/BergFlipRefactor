import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

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
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO userData (username, password) VALUES (?, ?);");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteUser(String username) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM userData WHERE username = ?");
            stmt.setString(1, username);
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addLeaderboardScore (String username, int score) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO leaderboard (username, score) VALUES (?, ?);");
            stmt.setString(1, username);
            stmt.setInt(2, score);
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateLeaderboardScore (String username, int score) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
            PreparedStatement stmt = conn.prepareStatement("UPDATE leaderboard SET score = ? WHERE username = ?;");
            stmt.setInt(1, score);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ArrayList getUsers() {
        ArrayList<String> users = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
            PreparedStatement stmt = conn.prepareStatement("SELECT username FROM userData");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}
