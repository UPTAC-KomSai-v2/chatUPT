/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import uptackomsai.chatupt.providers.DbBaseProvider;

/**
 *
 * @author Lei
 */
public class DatabaseUtils {
    private final Connection dbConnection;
    public DatabaseUtils(){
        DbBaseProvider dbProvider = new DbBaseProvider();
        this.dbConnection = dbProvider.getConnection();
    }
    public ResultSet getUserById(int userId) throws SQLException {
        String query = "SELECT * FROM User WHERE user_id = ?";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setInt(1, userId);
        return stmt.executeQuery(); // Caller must close ResultSet and Statement
    }
    public ResultSet getOnlineUsers(int userId) throws SQLException {
        String query = "SELECT * FROM User WHERE is_online = TRUE AND user_id != ?"; // Exclude the specified user_id
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setInt(1, userId); // Set the user_id to exclude
        return stmt.executeQuery(); // Execute the query and return the ResultSet
    }
    public ResultSet getAllUsers(int userId) throws SQLException {
        String query = "SELECT * FROM User WHERE user_id != ?"; // Exclude the specified user_id
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setInt(1, userId); // Set the user_id to exclude
        return stmt.executeQuery(); // Execute the query and return the ResultSet
    }
    public boolean setUserOnline(int userId) throws SQLException {
        String query = "UPDATE User SET is_online = TRUE WHERE user_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, userId); // Set the user_id
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Return true if the update was successful
        }
    }
    public boolean setUserOffline(int userId) throws SQLException {
        String query = "UPDATE User SET is_online = FALSE WHERE user_id = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, userId); // Set the user_id
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Return true if the update was successful
        }
    }
    /**
     * Fetch a record from the Channel table by channel_id.
     * @param channelId
     * @return 
     * @throws java.sql.SQLException
     */
    public ResultSet getChannelById(int channelId) throws SQLException {
        String query = "SELECT * FROM Channel WHERE channel_id = ?";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setInt(1, channelId);
        return stmt.executeQuery(); // Caller must close ResultSet and Statement
    }
    public ResultSet getAllChannels() throws SQLException {
        String query = "SELECT * FROM Channel"; // Query to get all channels
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        return stmt.executeQuery(); // Execute the query and return the ResultSet
    }
    /**
     * Fetch a record from the Chat table by chat_id.
     * @param chatId
     * @return 
     * @throws java.sql.SQLException
     */
    public ResultSet getChatById(int chatId) throws SQLException {
        String query = "SELECT * FROM Chat WHERE chat_id = ?";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setInt(1, chatId);
        return stmt.executeQuery(); // Caller must close ResultSet and Statement
    }
    public ResultSet getChatMessagesById(int chatId) throws SQLException {
        String query = """
            SELECT 
                c.chat_id,
                c.is_channel,
                m.message_id,
                m.content,
                m.time_sent,
                m.is_read,
                m.user_id,
                u.username,
                u.profile_path,
                a.file_path
            FROM 
                Chat c
            JOIN 
                Chat_Message cm ON c.chat_id = cm.chat_id
            JOIN 
                Message m ON cm.message_id = m.message_id
            JOIN 
                User u ON m.user_id = u.user_id
            LEFT JOIN 
                Attachment a ON m.attachment_id = a.attachment_id
            WHERE 
                c.chat_id = ?
            ORDER BY
                m.time_sent DESC;
        """;
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setInt(1, chatId); // Set the chat_id to filter messages
        return stmt.executeQuery(); // Caller must close ResultSet and Statement
    }
    /**
     * Fetch a record from the Message table by message_id.
     * @param messageId
     * @return 
     * @throws java.sql.SQLException
     */
    public ResultSet getMessageById(int messageId) throws SQLException {
        String query = "SELECT * FROM Message WHERE message_id = ?";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setInt(1, messageId);
        return stmt.executeQuery(); // Caller must close ResultSet and Statement
    }

    /**
     * Fetch a record from the Attachment table by attachment_id.
     * @param attachmentId
     * @return 
     * @throws java.sql.SQLException
     */
    public ResultSet getAttachmentById(int attachmentId) throws SQLException {
        String query = "SELECT * FROM Attachment WHERE attachment_id = ?";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setInt(1, attachmentId);
        return stmt.executeQuery(); // Caller must close ResultSet and Statement
    }
}
