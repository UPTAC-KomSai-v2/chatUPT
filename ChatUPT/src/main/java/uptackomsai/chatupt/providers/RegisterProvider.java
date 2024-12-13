package uptackomsai.chatupt.providers;

import com.google.gson.Gson;
import uptackomsai.chatupt.model.User;
import uptackomsai.chatupt.network.ServerModule;
import org.mindrot.jbcrypt.BCrypt; // Import BCrypt library

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterProvider implements ServerModule {
    
    // Hash the user's password before storing it
    private boolean registerUser(User user) {
        // Hash the password using BCrypt
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword); // Update the password field with the hashed value
        
        // Setup database connection
        DbBaseProvider dbProvider = new DbBaseProvider();
        Connection dbConnection = dbProvider.getConnection();

        String insertUserSQL = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(insertUserSQL)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword()); // Store hashed password
            stmt.setString(3, user.getEmail());

            stmt.executeUpdate();
            System.out.println("User registered: " + user.getUsername());
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to register user: " + e.getMessage());
            return false; // Likely due to duplicate username/email
        }
    }

    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        if (type.equals("register")) {
            Gson gson = new Gson();
            User user = gson.fromJson(content, User.class);

            if (registerUser(user)) {
                out.println("Registration successful.");
            } else {
                out.println("Registration failed: Username or email already exists.");
            }
        }
    }
}