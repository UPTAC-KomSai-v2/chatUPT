package uptackomsai.chatupt.providers;

import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;
import uptackomsai.chatupt.model.User;
import uptackomsai.chatupt.network.ServerModule;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditProfileProvider implements ServerModule {
    // Hash the user's password before storing it
    private boolean editUser(User user) {

        // Setup database connection
        DbBaseProvider dbProvider = new DbBaseProvider();
        Connection dbConnection = dbProvider.getConnection();

        // Base SQL query for updating user details
        StringBuilder updateSQL = new StringBuilder("UPDATE user SET ");
        boolean updateRequired = false;

        // Dynamically build the SQL query based on provided fields
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            updateSQL.append("password = ?, ");
            updateRequired = true;
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            updateSQL.append("email = ?, ");
            updateRequired = true;
        }
        if (user.getNewUsername() != null && !user.getNewUsername().isEmpty()) {
            updateSQL.append("username = ?, ");
            updateRequired = true;
        }

        // If no fields to update, return early
        if (!updateRequired) {
            System.err.println("No fields provided to update for username: " + user.getUsername());
            return false;
        }

        // Remove trailing comma and space, and add WHERE clause
        updateSQL.setLength(updateSQL.length() - 2); // Remove last comma
        updateSQL.append(" WHERE username = ?");

        try (PreparedStatement stmt = dbConnection.prepareStatement(updateSQL.toString())) {

            int paramIndex = 1;

            // Set values dynamically
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                stmt.setString(paramIndex++, hashedPassword);
            }
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                stmt.setString(paramIndex++, user.getEmail());
            }
            if (user.getNewUsername() != null && !user.getNewUsername().isEmpty()) {
                stmt.setString(paramIndex++, user.getNewUsername());
            }

            // Set the condition for the current username
            stmt.setString(paramIndex, user.getUsername());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User profile updated: " + user.getUsername());
                return true;
            } else {
                System.err.println("No user found with username: " + user.getUsername());
                return false; // No matching user found
            }
        } catch (SQLException e) {
            System.err.println("Failed to update user profile: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void editUser(String type, String content, PrintWriter out) {
        Gson gson = new Gson();
        User user = gson.fromJson(content, User.class);

        if (registerUser(user)) {
            out.println("Profile edit successful.");
        } else {
            out.println("Edit profile failed");
        }
    }
}
