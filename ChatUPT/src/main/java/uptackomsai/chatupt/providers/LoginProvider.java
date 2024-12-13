package uptackomsai.chatupt.providers;

import com.google.gson.Gson;
import uptackomsai.chatupt.model.User;
import uptackomsai.chatupt.network.ServerModule;
import org.mindrot.jbcrypt.BCrypt; // Import BCrypt library

import java.io.PrintWriter;
import java.sql.*;

public class LoginProvider implements ServerModule {

    // Authenticate the user based on username and password
    private boolean authenticateUser(User user) {
        // Setup database connection
        DbBaseProvider dbProvider = new DbBaseProvider();
        Connection dbConnection = dbProvider.getConnection();

        String selectUserSQL = "SELECT password FROM user WHERE username = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(selectUserSQL)) {
            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                // Compare the stored hashed password with the entered password
                if (BCrypt.checkpw(user.getPassword(), storedPassword)) {
                    return true; // Authentication successful
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while authenticating user: " + e.getMessage());
        }
        return false; // Authentication failed
    }

    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        if (type.equals("login")) {
            Gson gson = new Gson();
            User user = gson.fromJson(content, User.class); // Deserialize the JSON content to a User object

            if (authenticateUser(user)) {
                out.println("Login successful"); // Send success response
            } else {
                out.println("Invalid username or password"); // Send failure response
            }
        }
    }
}
