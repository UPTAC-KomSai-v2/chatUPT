package uptackomsai.chatupt.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<String> getActiveUsers() {
        List<String> activeUsers = new ArrayList<>();
        String query = "SELECT username FROM users WHERE is_Active = true";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                activeUsers.add(resultSet.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return activeUsers;
    }
    
    public List<String> getAllUsers() {
        List<String> activeUsers = new ArrayList<>();
        String query = "SELECT username FROM users";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                activeUsers.add(resultSet.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return activeUsers;
    }
}
