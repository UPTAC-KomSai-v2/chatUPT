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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import uptackomsai.chatupt.network.ServerModule;

public class GetAllChannelsProvider implements ServerModule {
    private final Connection dbConnection;

    public GetAllChannelsProvider() {
        DbBaseProvider dbProvider = new DbBaseProvider();
        this.dbConnection = dbProvider.getConnection();
    }

    // Fetch all channels from the Channel table
    private JsonArray getAllChannels() {
        String query = "SELECT channel_name, chat_id, is_private FROM Channel";
        JsonArray channelsArray = new JsonArray();

        try (PreparedStatement stmt = dbConnection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                JsonObject channelObj = new JsonObject();
                channelObj.addProperty("channel_name", rs.getString("channel_name"));
                channelObj.addProperty("chat_id", rs.getInt("chat_id"));
                channelObj.addProperty("is_private", rs.getBoolean("is_private"));
                channelsArray.add(channelObj);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching channels: " + e.getMessage());
        }
        return channelsArray;
    }

    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        if (type.equals("getAllChannels")) {
            System.out.println("Processing getAllChannels request...");
            Gson gson = new Gson();

            // Retrieve all channels
            JsonArray channelsArray = getAllChannels();

            // Send response to the client
            if (channelsArray.size() > 0) {
                out.println(gson.toJson(channelsArray)); // Send JSON array of channels
            } else {
                out.println("null"); // Send null if no channels are found
            }
        }
    }
}