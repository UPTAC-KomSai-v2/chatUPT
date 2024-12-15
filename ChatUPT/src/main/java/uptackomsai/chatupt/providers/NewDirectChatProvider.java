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
import uptackomsai.chatupt.model.Channel;
import uptackomsai.chatupt.model.DirectChat;
import uptackomsai.chatupt.network.ServerModule;

/**
 *
 * @author Lei
 */
public class NewDirectChatProvider implements ServerModule {
    private int chatId;
    DbBaseProvider dbProvider = new DbBaseProvider();
    Connection dbConnection = dbProvider.getConnection();
        
    public boolean areUsersConnected(int user1ID, int user2ID) throws SQLException {
        String query = "SELECT chat_id FROM User_Chat WHERE user_id IN (?, ?) GROUP BY chat_id HAVING COUNT(user_id) = 2";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, user1ID); // Set the first user ID
            stmt.setInt(2, user2ID); // Set the second user ID

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                chatId = rs.getInt("chat_id");
                System.out.println("Users are connected in chat_id: " + chatId);
                return true; // Users are connected
            }
        }
        return false; // Users are not connected
    }
    public boolean createDirectChat(DirectChat directchat) throws SQLException{
        String insertUserSQL = "INSERT INTO chat (is_channel) VALUES (?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(insertUserSQL,PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setBoolean(1, false);

            stmt.executeUpdate();
            // Retrieve the generated key
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    chatId = generatedKeys.getInt(1); // Get the generated chat_id
                    System.out.println("Chat Interface Added with chat_id: " + chatId);
                } else {
                    System.err.println("Failed to retrieve chat_id.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to add chat interface: " + e.getMessage());
            return false; 
        }
        
        String insertUserChatSQL = "INSERT INTO user_chat (user_id, chat_id) VALUES (?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(insertUserChatSQL)) {
            // Insert the first user with the chat_id
            stmt.setInt(1, directchat.getUser1ID()); // First user_id
            stmt.setInt(2, chatId);  // Shared chat_id
            stmt.executeUpdate();
            stmt.clearParameters();
            // Insert the second user with the same chat_id
            stmt.setInt(1, directchat.getUser2ID()); // Second user_id
            stmt.setInt(2, chatId);  // Shared chat_id
            stmt.executeUpdate();

            System.out.println("Added users " + directchat.getUser1ID() + " and " + directchat.getUser2ID() + " to Chat ID " + chatId);
            return true; // Indicate success
        } catch (SQLException e) {
            System.err.println("Failed to add users to chat: " + e.getMessage());
            return false; // Indicate failure
        }
    }
    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        if (type.equals("newDirectChat")) {
            Gson gson = new Gson();
            DirectChat directchat = gson.fromJson(content, DirectChat.class); // Deserialize the JSON content to a DirectChat object

            try {
                if (areUsersConnected(directchat.getUser1ID(), directchat.getUser2ID())) {
                    System.out.println("The users are already connected.");
                    // update last_visited in User
                    out.println(chatId); // Send chat ID
                } 
                else if (createDirectChat(directchat)) {
                    // update last_visited in User
                    out.println(chatId); // Send chat ID
                } else {
                    out.println("Unable to set up direct chat"); // Send failure response
                }
            } catch (SQLException e) {
                System.err.println("Error creating direct chat: " + e.getMessage());
                e.printStackTrace();
                out.println("Server error while creating direct chat"); // Send error response
            }
        }
    }
}
