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
import java.sql.SQLException;
import uptackomsai.chatupt.network.ServerModule;

public class SetUserOnlineProvider implements ServerModule {
    private final Connection dbConnection;

    public SetUserOnlineProvider() {
        DbBaseProvider dbProvider = new DbBaseProvider();
        this.dbConnection = dbProvider.getConnection();
    }

    // Method to set a user online
    private boolean setUserOnline(int userId) {
        String query = "UPDATE User SET is_online = TRUE WHERE user_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, userId); // Bind the user ID
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // True if the update was successful
        } catch (SQLException e) {
            System.err.println("Error setting user online: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        if (type.equals("setUserOnline")) {
            System.out.println("Processing setUserOnline request...");

            // Parse the JSON content
            Gson gson = new Gson();
            JsonObject request = gson.fromJson(content, JsonObject.class);
            int userId = request.get("user_id").getAsInt();

            // Perform the update operation
            if (setUserOnline(userId)) {
                out.println("success"); // Send success response
                System.out.println("User with ID " + userId + " set to online.");
            } else {
                out.println("failure"); // Send failure response
                System.err.println("Failed to set user with ID " + userId + " as online.");
            }
        }
    }
}

