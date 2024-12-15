/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.providers;

/**
 *
 * @author Lei
 */
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import uptackomsai.chatupt.network.ServerModule;

public class GetUserByIdProvider implements ServerModule {
    private final Connection dbConnection;

    public GetUserByIdProvider() {
        DbBaseProvider dbProvider = new DbBaseProvider();
        this.dbConnection = dbProvider.getConnection();
    }

    // Fetch user details by user_id
    private JsonObject getUserById(int userId) {
        String query = "SELECT username, profile_path FROM User WHERE user_id = ?";
        JsonObject userDetails = new JsonObject();
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                userDetails.addProperty("username", rs.getString("username"));
                userDetails.addProperty("profile_path", rs.getString("profile_path"));
            } else {
                System.out.println("No user found with user_id: " + userId);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user details: " + e.getMessage());
        }
        return userDetails;
    }

    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        if (type.equals("getUserById")) {
            Gson gson = new Gson();
            JsonObject request = gson.fromJson(content, JsonObject.class);

            // Extract user_id from the JSON request
            int userId = request.get("user_id").getAsInt();
            System.out.println("Processing getUserById for user_id: " + userId);

            // Fetch user details
            JsonObject userDetails = getUserById(userId);

            // Send response
            if (userDetails.has("username")) {
                out.println(userDetails.toString()); // Send user details as JSON
            } else {
                out.println("null"); // Send null if user not found
            }
        }
    }
}

