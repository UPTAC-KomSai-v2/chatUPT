package uptackomsai.chatupt.providers;

import uptackomsai.chatupt.network.ServerModule;
import com.google.gson.Gson;
import uptackomsai.chatupt.utils.SessionUtil;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataProvider implements ServerModule {

    // Retrieve the username based on the user ID
    private String getUsernameById(int userId) {
        // Setup database connection
        DbBaseProvider dbProvider = new DbBaseProvider();
        Connection dbConnection = dbProvider.getConnection();

        String selectUsernameSQL = "SELECT username FROM user WHERE user_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(selectUsernameSQL)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("username"); // Return the username
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching username: " + e.getMessage());
        }
        return null; // Return null if the username is not found
    }

    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        try {
            // Get the username from the database
            String username = getUsernameById(SessionUtil.sessionUserId);

            // Send the response to the client
            if (username != null) {
                out.println("{\"status\":\"success\",\"username\":\"" + username + "\"}");
            } else {
                out.println("{\"status\":\"error\",\"message\":\"User ID not found\"}");
            }
        } catch (Exception e) {
            System.err.println("Error in handleRequest: " + e.getMessage());
            out.println("{\"status\":\"error\",\"message\":\"Invalid input format\"}");
        }
    }
}
