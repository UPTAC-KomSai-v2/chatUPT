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
                profile_path VARCHAR(255),
                last_visited DATETIME
            );
        """;

            String createChannelTable = """
            CREATE TABLE Channel (
                channel_id INT AUTO_INCREMENT PRIMARY KEY,
                channel_name VARCHAR(100) UNIQUE NOT NULL,
                is_public BOOLEAN DEFAULT TRUE,
                chatwindow_id INT,
                FOREIGN KEY (chatwindow_id) REFERENCES ChatWindow(chatwindow_id) ON DELETE CASCADE
            );
        """;

            String createUserChannelTable = """
            CREATE TABLE User_Channel (
                user_id INT NOT NULL,
                channel_id INT NOT NULL,
                channel_role VARCHAR(50),
                PRIMARY KEY (user_id, channel_id),
                FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
                FOREIGN KEY (channel_id) REFERENCES Channel(channel_id) ON DELETE CASCADE
            );
        """;

            String createChatWindowTable = """
            CREATE TABLE ChatWindow (
                chatwindow_id INT AUTO_INCREMENT PRIMARY KEY,
                port_number INT NOT NULL
            );
        """;

            String createConvoTable = """
            CREATE TABLE Convo (
                user_id INT NOT NULL,
                chatwindow_id INT NOT NULL,
                PRIMARY KEY (user_id, chatwindow_id),
                FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
                FOREIGN KEY (chatwindow_id) REFERENCES ChatWindow(chatwindow_id) ON DELETE CASCADE
            );
        """;

            String createMessageTable = """
            CREATE TABLE Message (
                message_id INT AUTO_INCREMENT PRIMARY KEY,
                content TEXT NOT NULL,
                time_sent DATETIME DEFAULT CURRENT_TIMESTAMP,
                attachment_id INT,
                user_id INT NOT NULL,
                chatwindow_id INT NOT NULL,
                FOREIGN KEY (attachment_id) REFERENCES Attachment(attachment_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
                FOREIGN KEY (chatwindow_id) REFERENCES ChatWindow(chatwindow_id) ON DELETE CASCADE
            );
        """;

            String createAttachmentTable = """
            CREATE TABLE Attachment (
                attachment_id INT AUTO_INCREMENT PRIMARY KEY,
                message_id INT NOT NULL,
                file_name VARCHAR(255) NOT NULL,
                file_path VARCHAR(255) NOT NULL,
                file_type VARCHAR(50),
                file_size INT,
                FOREIGN KEY (message_id) REFERENCES Message(message_id) ON DELETE CASCADE
            );
        """;

            String createMessageStatusTable = """
            CREATE TABLE Message_Status (
                message_id INT NOT NULL,
                user_id INT NOT NULL,
                is_seen BOOLEAN DEFAULT FALSE,
                PRIMARY KEY (message_id, user_id),
                FOREIGN KEY (message_id) REFERENCES Message(message_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
            );
        """;

            // Execute the SQL statements
            stmt.executeUpdate(createUserTable);
            stmt.executeUpdate(createChatWindowTable);
            stmt.executeUpdate(createChannelTable);
            stmt.executeUpdate(createUserChannelTable);
            stmt.executeUpdate(createConvoTable);
            stmt.executeUpdate(createMessageTable);
            stmt.executeUpdate(createAttachmentTable);
            stmt.executeUpdate(createMessageStatusTable);

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
