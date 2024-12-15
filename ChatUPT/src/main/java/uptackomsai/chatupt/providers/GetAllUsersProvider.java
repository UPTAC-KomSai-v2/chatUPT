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

public class GetAllUsersProvider implements ServerModule {
    private final Connection dbConnection;

    public GetAllUsersProvider() {
        DbBaseProvider dbProvider = new DbBaseProvider();
        this.dbConnection = dbProvider.getConnection();
    }

    // Fetch all users excluding the current user
    private JsonArray getAllUsers(int currentUserId) {
        String query = "SELECT user_id, username FROM User WHERE user_id != ?";
        JsonArray usersArray = new JsonArray();

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, currentUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                JsonObject userObj = new JsonObject();
                userObj.addProperty("user_id", rs.getInt("user_id"));
                userObj.addProperty("username", rs.getString("username"));
                usersArray.add(userObj);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all users: " + e.getMessage());
        }
        return usersArray;
    }

    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        if (type.equals("getAllUsers")) {
            System.out.println("Processing getAllUsers request...");
            Gson gson = new Gson();

            // Parse the incoming JSON request
            JsonObject request = gson.fromJson(content, JsonObject.class);
            int currentUserId = request.get("user_id").getAsInt();

            // Fetch the list of users
            JsonArray usersArray = getAllUsers(currentUserId);

            // Send the response back to the client
            if (usersArray.size() > 0) {
                out.println(gson.toJson(usersArray)); // Send JSON array of users
            } else {
                out.println("null"); // Send null if no users are found
            }
        }
    }
}

