/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.providers;

import com.google.gson.Gson;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import uptackomsai.chatupt.model.UserRole;
import uptackomsai.chatupt.network.ServerModule;

/**
 *
 * @author Lei
 */
public class UserRoleProvider implements ServerModule {
    private final Connection dbConnection;
    private int channelID;
    public UserRoleProvider() {
        DbBaseProvider dbProvider = new DbBaseProvider();
        this.dbConnection = dbProvider.getConnection();
    }

    // Method to retrieve the user role in a channel
    private String getUserRole(int chatId, int userId) {
        String query = "SELECT channel_id FROM Channel WHERE chat_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, chatId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                channelID = rs.getInt("channel_id");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching channel_id: " + e.getMessage());
            return null;
        }
        
        query = "SELECT role FROM User_Channel WHERE channel_id = ? AND user_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, channelID);
            stmt.setInt(2, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role"); // Return the role (e.g., admin, member, pending)
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user role: " + e.getMessage());
        }
        return null; // Return null if no role is found or an error occurs
    }

    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        if (type.equals("getUserRole")) {
            Gson gson = new Gson();

            // Parse the JSON request
            UserRole request = gson.fromJson(content, UserRole.class);
            int chatId = request.getChatID();
            int userId = request.getUserID();

            System.out.println("Processing getUserRole: chat_id=" + chatId + ", user_id=" + userId);

            // Fetch the user role
            String role = getUserRole(chatId, userId);

            if (role != null) {
                out.println(role); // Send back the role (e.g., "admin", "member", "pending")
            } else {
                out.println("null"); // Send "null" if no role is found
            }
        }
    }
}
