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

import uptackomsai.chatupt.model.Message;
import uptackomsai.chatupt.network.ServerModule;

public class MessageHandlerProvider implements ServerModule {
    private final Connection dbConnection;

    public MessageHandlerProvider() {
        DbBaseProvider dbProvider = new DbBaseProvider();
        this.dbConnection = dbProvider.getConnection();
    }

    // Insert into the Attachment table and return generated attachment_id
    private int insertAttachment(String fileName, String filePath) throws SQLException {
        String query = "INSERT INTO Attachment (file_name, file_path, file_type, file_size) VALUES (?, ?, NULL, NULL)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, fileName);
            stmt.setString(2, filePath);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return generated attachment_id
            }
        }
        return 0; // Return 0 if not inserted
    }

    // Insert into Message table and return generated message_id
    private int insertMessage(Message message, int attachmentId) throws SQLException {
        String query = "INSERT INTO Message (content, time_sent, user_id, attachment_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, message.getContent());
            stmt.setString(2, message.getTimeSent());
            stmt.setInt(3, message.getUserID());
            stmt.setObject(4, attachmentId > 0 ? attachmentId : null);

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return generated message_id
            }
        }
        return 0; // Return 0 if not inserted
    }

    // Insert into Chat_Message table
    private boolean insertChatMessage(int chatId, int messageId) throws SQLException {
        String query = "INSERT INTO Chat_Message (chat_id, message_id) VALUES (?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, chatId);
            stmt.setInt(2, messageId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Fetch user details (profile_path and username)
    private JsonObject getUserDetails(int userId) {
        String query = "SELECT profile_path, username FROM User WHERE user_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JsonObject userDetails = new JsonObject();
                userDetails.addProperty("profile_path", rs.getString("profile_path"));
                userDetails.addProperty("username", rs.getString("username"));
                return userDetails;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user details: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        if ("sendMessage".equals(type)) {
            System.out.println("Processing sendMessage request...");
            Gson gson = new Gson();
            Message message = gson.fromJson(content, Message.class);

            try {
                int attachmentId = 0;

                // Insert into Attachment table if file_path exists
                if (message.getFilePath() != null && !message.getFilePath().isEmpty()) {
                    attachmentId = insertAttachment(message.getFilePath(), message.getFilePath());
                }

                // Insert into Message table
                int messageId = insertMessage(message, attachmentId);
                if (messageId > 0) {
                    // Link to Chat_Message table
                    if (insertChatMessage(message.chat_id(), messageId)) {
                        JsonObject userDetails = getUserDetails(message.getUserID());
                        if (userDetails != null) {
                            // Prepare response including user and message details
                            JsonObject response = new JsonObject();
                            response.addProperty("profile_path", userDetails.get("profile_path").getAsString());
                            response.addProperty("username", userDetails.get("username").getAsString());
                            response.addProperty("time_sent", message.getTimeSent());
                            response.addProperty("content", message.getContent());
                            response.addProperty("file_path", message.getFilePath());
                            response.addProperty("is_read", false);
                            response.addProperty("chat_id", message.chat_id());
                            response.addProperty("user_id", message.getUserID());

                            out.println(response.toString()); // Send response back
                            System.out.println("Message sent and stored successfully.");
                        }
                    }
                } else {
                    out.println("failure");
                    System.err.println("Failed to store message.");
                }
            } catch (SQLException e) {
                out.println("failure");
                System.err.println("Error handling message: " + e.getMessage());
            }
        }
    }
}
