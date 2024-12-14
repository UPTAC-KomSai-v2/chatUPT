package uptackomsai.chatupt.providers;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class DbBaseProvider {
    private Connection dbConnection;

    public DbBaseProvider(){
        connectToDatabase();
    }

    public Connection getConnection(){
        return dbConnection;
    }

    private void connectToDatabase() {
        try {
            // Load environment variables from the .env file
            Dotenv dotenv = Dotenv.load();

            String url = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            dbConnection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            System.exit(1); // Terminate if the database connection fails
        } catch (Exception e) {
            System.err.println("Failed to load environment variables: " + e.getMessage());
            System.exit(1); // Terminate if environment variables cannot be loaded
        }
    }

    public void setupDatabase() {
        Statement stmt = null;
        ResultSet rs = null;
        Statement queryStmt = null;

        try{
            stmt = dbConnection.createStatement();
            queryStmt = dbConnection.createStatement();

            // Disable foreign key checks for dropping tables
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");

            // Drop all existing tables dynamically
            rs = queryStmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'chatupt';");

            while (rs.next()) {
                String tableName = rs.getString("table_name");
                // Skip system tables like 'information_schema'
                if (tableName.equals("information_schema")) continue;
                stmt.executeUpdate("DROP TABLE IF EXISTS " + tableName + " CASCADE;");
                System.out.println("Deleted all data from table: " + tableName);
            }


            String createUserTable = """
            CREATE TABLE User (
                user_id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50) UNIQUE NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                profile_path VARCHAR(255) DEFAULT "default.png",
                is_online BOOLEAN DEFAULT FALSE,
                last_visited DATETIME DEFAULT NULL
            );
            """;

            String createChannelTable = """
            CREATE TABLE Channel (
                channel_id INT AUTO_INCREMENT PRIMARY KEY,
                channel_name VARCHAR(100) UNIQUE NOT NULL,
                is_private BOOLEAN DEFAULT FALSE,
                chat_id INT,
                FOREIGN KEY (chat_id) REFERENCES Chat(chat_id) ON DELETE CASCADE
            );
            """;
            
            String createChatTable = """
            CREATE TABLE Chat (
                chat_id INT AUTO_INCREMENT PRIMARY KEY,
                is_channel BOOLEAN NOT NULL
            );                
            """;
            
            String createUserChannelTable = """
            CREATE TABLE User_Channel (
                user_id INT NOT NULL,
                channel_id INT NOT NULL,
                role ENUM('admin', 'member', 'pending') DEFAULT 'member',
                PRIMARY KEY (user_id, channel_id),
                FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
                FOREIGN KEY (channel_id) REFERENCES Channel(channel_id) ON DELETE CASCADE
            );
            """;

            String createUserChatTable = """
            CREATE TABLE User_Chat (
                user_id INT NOT NULL,
                chat_id INT NOT NULL,
                PRIMARY KEY (user_id, chat_id),
                FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
                FOREIGN KEY (chat_id) REFERENCES Chat(chat_id) ON DELETE CASCADE
            );
            """;
            
            String createMessageTable = """
            CREATE TABLE Message (
                message_id INT AUTO_INCREMENT PRIMARY KEY,
                content TEXT NOT NULL,
                time_sent DATETIME DEFAULT CURRENT_TIMESTAMP,
                is_read BOOLEAN DEFAULT FALSE,
                user_id INT NOT NULL,
                attachment_id INT DEFAULT NULL,
                FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
                FOREIGN KEY (attachment_id) REFERENCES Attachment(attachment_id) ON DELETE CASCADE
            );
            """;
            
            String createChatMessageTable = """
            CREATE TABLE Chat_Message (
                chat_id INT NOT NULL,
                message_id INT NOT NULL,
                PRIMARY KEY (chat_id, message_id),
                FOREIGN KEY (chat_id) REFERENCES Chat(chat_id) ON DELETE CASCADE,
                FOREIGN KEY (message_id) REFERENCES Message(message_id) ON DELETE CASCADE
            );
            """;

            String createAttachmentTable = """
            CREATE TABLE Attachment (
                attachment_id INT AUTO_INCREMENT PRIMARY KEY,
                file_name VARCHAR(255) NOT NULL,
                file_path VARCHAR(255) NOT NULL,
                file_type VARCHAR(50),
                file_size INT
            );
            """;

            // Execute the SQL statements
            stmt.executeUpdate(createUserTable);
            stmt.executeUpdate(createChannelTable);
            stmt.executeUpdate(createChatTable);
            stmt.executeUpdate(createUserChannelTable);
            stmt.executeUpdate(createUserChatTable);
            stmt.executeUpdate(createMessageTable);
            stmt.executeUpdate(createChatMessageTable);
            stmt.executeUpdate(createAttachmentTable);
            stmt.executeUpdate("CREATE INDEX idx_user_is_online ON User(is_online);");
            stmt.executeUpdate("CREATE INDEX idx_channel_is_private ON Channel(is_private);");
            stmt.executeUpdate("CREATE INDEX idx_message_time_sent ON Message(time_sent);");
            stmt.executeUpdate("CREATE INDEX idx_chat_is_channel ON Chat(is_channel);");

            // Re-enable foreign key checks after table creation
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");

            System.out.println("Database schema set up.");
        } catch (SQLException e) {
            System.err.println("Failed to set up database schema: " + e.getMessage());
        } finally {
            // Explicitly close the ResultSet and Statement in the finally block
            try {
                if (rs != null) {
                    rs.close();
                }
                if (queryStmt != null){
                    queryStmt.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.err.println("Failed to close resources: " + e.getMessage());
            }
        }
    }
}