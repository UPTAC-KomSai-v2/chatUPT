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

    /**
     * Fetch a record from the Channel table by channel_id.
     */
    public ResultSet getChannelById(int channelId) throws SQLException {
        String query = "SELECT * FROM Channel WHERE channel_id = ?";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setInt(1, channelId);
        return stmt.executeQuery(); // Caller must close ResultSet and Statement
    }

    /**
     * Fetch a record from the Chat table by chat_id.
     */
    public ResultSet getChatById(int chatId) throws SQLException {
        String query = "SELECT * FROM Chat WHERE chat_id = ?";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setInt(1, chatId);
        return stmt.executeQuery(); // Caller must close ResultSet and Statement
    }

    /**
     * Fetch a record from the Message table by message_id.
     */
    public ResultSet getMessageById(int messageId) throws SQLException {
        String query = "SELECT * FROM Message WHERE message_id = ?";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setInt(1, messageId);
        return stmt.executeQuery(); // Caller must close ResultSet and Statement
    }

    /**
     * Fetch a record from the Attachment table by attachment_id.
     */
    public ResultSet getAttachmentById(int attachmentId) throws SQLException {
        String query = "SELECT * FROM Attachment WHERE attachment_id = ?";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setInt(1, attachmentId);
        return stmt.executeQuery(); // Caller must close ResultSet and Statement
    }
}
