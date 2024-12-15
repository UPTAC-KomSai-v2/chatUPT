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
import uptackomsai.chatupt.model.UserChannel;
import uptackomsai.chatupt.network.ServerModule;

/**
 *
 * @author Lei
 */
public class JoinChannelProvider implements ServerModule {
    private int channelID;
    DbBaseProvider dbProvider = new DbBaseProvider();
    Connection dbConnection = dbProvider.getConnection();
    private boolean addUserToChannel(UserChannel userChannel) {
        String query = "SELECT channel_id FROM Channel WHERE chat_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, userChannel.getChatID());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                channelID = rs.getInt("channel_id");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching channel_id: " + e.getMessage());
            return false;
        }
        String insertSQL = "INSERT INTO User_Channel (user_id, channel_id, role) VALUES (?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE role = VALUES(role)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(insertSQL)) {
            stmt.setInt(1, userChannel.getUserID());
            stmt.setInt(2, channelID);
            stmt.setString(3, userChannel.getRole());
            System.out.println("Processing joinChannel: channel_id=" + channelID 
                    + ", user_id=" + userChannel.getUserID() + ", role=" + userChannel.getRole());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if insertion/update was successful
        } catch (SQLException e) {
            System.err.println("Error adding user to channel: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        if (type.equals("joinChannel")) {
            // Parse the JSON request
            Gson gson = new Gson();
            UserChannel userChannel = gson.fromJson(content, UserChannel.class);

            // Add user to the channel
            if (addUserToChannel(userChannel)) {
                out.println("success"); // Send success response
            } else {
                out.println("failure"); // Send failure response
            }
        }
    }
}
