package codekart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import codekart.DBConnection;

public class UserManager {

	public static int loginUser(String username, String password) {
		String sql = "SELECT user_id FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
		return -1;
	}

	public static boolean registerUser(String username, String password) {
		 String sql = "INSERT INTO users(username, password) VALUES (?, ?)";
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setString(1, username);
	            stmt.setString(2, password);
	            int rows = stmt.executeUpdate();
	            return rows > 0;
	        } catch (SQLException e) {
	            System.out.println("Error registering user: " + e.getMessage());
	            return false;
	        }
	}

}
