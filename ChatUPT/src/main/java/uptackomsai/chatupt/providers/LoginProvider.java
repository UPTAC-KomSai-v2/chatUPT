package uptackomsai.chatupt.providers;

import com.google.gson.Gson;
import uptackomsai.chatupt.model.User;
import uptackomsai.chatupt.network.ServerModule;
import org.mindrot.jbcrypt.BCrypt; // Import BCrypt library
import uptackomsai.chatupt.utils.SessionUtil;

import java.io.PrintWriter;
import java.sql.*;

public class LoginProvider implements ServerModule {
    private int userId;
    private boolean isAlreadyLoggedIn = false;
    // Authenticate the user based on username and password
    private boolean authenticateUser(User user) {
        // Setup database connection
        DbBaseProvider dbProvider = new DbBaseProvider();
        Connection dbConnection = dbProvider.getConnection();

        String selectUserSQL = "SELECT user_id, password, is_online FROM user WHERE username = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(selectUserSQL)) {
            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("user_id"); // Retrieve user_id
                String storedPassword = rs.getString("password"); // Retrieve password

                // Compare the stored hashed password with the entered password
                if (BCrypt.checkpw(user.getPassword(), storedPassword)) {
                    if(rs.getBoolean("is_online")) {
                        System.err.println("User already logged in. ");
                        isAlreadyLoggedIn = true;
                        return false;
                    }
                    SessionUtil.sessionUsername = user.getUsername();
                    SessionUtil.sessionUserId = userId;
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
                out.println(userId); // Send success response
            } else {
                if(isAlreadyLoggedIn) out.println("Account already logged in");
                else out.println("Invalid username or password"); // Send failure response
            }
        }
    }
}
