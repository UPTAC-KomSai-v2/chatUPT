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

public class GetChatWindowDetailsProvider implements ServerModule {
    private final Connection dbConnection;

    public GetChatWindowDetailsProvider() {
        DbBaseProvider dbProvider = new DbBaseProvider();
        this.dbConnection = dbProvider.getConnection();
    }

    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        if (type.equals("getChatWindowDetails")) {
            System.out.println("Processing getChatWindowDetails request...");

            // Parse the request JSON
            Gson gson = new Gson();
            JsonObject requestData = gson.fromJson(content, JsonObject.class);
            int chatID = requestData.get("chat_id").getAsInt();
            int currentUserID = requestData.get("user_id").getAsInt();
            boolean isChannel = requestData.get("is_channel").getAsBoolean();

            // Query to find the other user in the chat
            String query = """
                SELECT u.username, u.is_online, c.channel_name
                FROM User u
                JOIN User_Chat uc ON u.user_id = uc.user_id
                LEFT JOIN Channel c ON c.chat_id = uc.chat_id
                WHERE uc.chat_id = ? AND u.user_id != ?
            """;

            try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
                stmt.setInt(1, chatID);
                stmt.setInt(2, currentUserID);

                ResultSet rs = stmt.executeQuery();
                JsonObject response = new JsonObject();

                if (rs.next()) {
                    if(isChannel) response.addProperty("chatname", rs.getString("channel_name"));
                    else response.addProperty("chatname", rs.getString("username"));
                    response.addProperty("is_online", rs.getBoolean("is_online"));
                } else {
                    response.addProperty("chatname", "Unknown");
                    response.addProperty("is_online", false);
                }

                // Send the response back to the client
                out.println(response.toString());
                System.out.println("Sent chat details: " + response);

            } catch (SQLException e) {
                System.err.println("Error fetching chat details: " + e.getMessage());
                out.println("failure");
            }
        }
    }
}

