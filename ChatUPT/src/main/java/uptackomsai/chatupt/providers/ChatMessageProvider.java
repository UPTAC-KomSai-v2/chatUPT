/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.providers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import uptackomsai.chatupt.model.ChatMessage;
import uptackomsai.chatupt.model.Message;
import uptackomsai.chatupt.model.User;
import uptackomsai.chatupt.network.ServerModule;
import uptackomsai.chatupt.utils.DatabaseUtils;

/**
 *
 * @author Lei
 */
public class ChatMessageProvider implements ServerModule {
    DatabaseUtils dbUtils = new DatabaseUtils();

    private boolean getListOfPrevChatMessages(ChatMessage chatMessage, PrintWriter out) {
        int chatId = chatMessage.getChatID(); // Retrieve chat_id from the chatMessage object
        ResultSet rs = null;

        try {
            // Fetch messages for the given chat_id
            rs = dbUtils.getChatMessagesById(chatId);

            // Convert ResultSet to JSON Array
            JsonArray messagesArray = new JsonArray();

            while (rs.next()) {
                JsonObject messageObject = new JsonObject();
                messageObject.addProperty("message_id", rs.getInt("message_id"));
                messageObject.addProperty("content", rs.getString("content"));
                messageObject.addProperty("time_sent", rs.getString("time_sent"));
                messageObject.addProperty("username", rs.getString("username"));
                messageObject.addProperty("profile_path", rs.getString("profile_path"));
                messageObject.addProperty("file_path", rs.getString("file_path"));
                messageObject.addProperty("is_read", rs.getBoolean("is_read"));
                messagesArray.add(messageObject);
            }

            // If no messages are found, return false
            if (messagesArray.size() == 0) {
                return false;
            }

            // Send JSON Array as response
            Gson gson = new Gson();
            out.println(gson.toJson(messagesArray));
            return true;

        } catch (SQLException e) {
            System.err.println("Error fetching previous chat messages: " + e.getMessage());
            e.printStackTrace();
            out.println("Error retrieving messages.");
            return false;
        } finally {
            // Always close the ResultSet
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        if (type.equals("prevMessages")) {
            Gson gson = new Gson();
            ChatMessage chatMessage = gson.fromJson(content, ChatMessage.class); // Deserialize JSON input

            // Get previous messages and send response
            if (!getListOfPrevChatMessages(chatMessage, out)) {
                out.println("No messages found!"); // Send failure response
            }
        }
    }
}
