package uptackomsai.chatupt.providers;

import com.google.gson.Gson;
import uptackomsai.chatupt.model.Channel;
import uptackomsai.chatupt.network.ServerModule;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditChannelProvider implements ServerModule {
    private int chatId, channelId;
    private Boolean addChannel(Channel channel) {
        // Setup database connection
        DbBaseProvider dbProvider = new DbBaseProvider();
        Connection dbConnection = dbProvider.getConnection();

        String insertUserSQL = "INSERT INTO chat (is_channel) VALUES (?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(insertUserSQL,PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setBoolean(1, true);

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

        insertUserSQL = "INSERT INTO channel (channel_name, is_private, chat_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(insertUserSQL,PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, channel.getChannelName());
            stmt.setBoolean(2, channel.getIsPrivate());
            stmt.setInt(3, chatId);

            stmt.executeUpdate();
            // Retrieve the generated key
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    channelId = generatedKeys.getInt(1);
                    System.out.println("Channel Added with channel_id: " + channelId);
                } else {
                    System.err.println("Failed to retrieve channel_id.");
                }
            }
            System.out.println("Added Channel: " + channel.getChannelName());
        } catch (SQLException e) {
            System.err.println("Failed to add channel: " + e.getMessage());
            return false;
        }

        insertUserSQL = "INSERT INTO user_channel (user_id, channel_id, role) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(insertUserSQL)) {
            stmt.setInt(1, channel.getCreatorID()); // Set user_id
            stmt.setInt(2, channelId); // Set channel_id
            stmt.setString(3, "admin"); // Set role ('admin', 'member', or 'pending')

            stmt.executeUpdate();
            System.out.println("Added User_"+channel.getCreatorID()+" as admin to Channel "+channelId);
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to add channel: " + e.getMessage());
            return false;
        }
    }
    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        if (type.equals("addChannel")) {
            Gson gson = new Gson();
            Channel channel = gson.fromJson(content, Channel.class); // Deserialize the JSON content to a User object

            if (addChannel(channel)) {
                out.println(chatId); // Send success response
            } else {
                out.println("Unable to set up channel"); // Send failure response
            }
        }
    }
}
