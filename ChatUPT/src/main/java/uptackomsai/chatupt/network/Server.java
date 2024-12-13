/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.network;

/**
 *
 * @author Lei
 */
import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.concurrent.*;
import java.util.concurrent.ConcurrentHashMap;
import uptackomsai.chatupt.model.Message;
import uptackomsai.chatupt.model.User;

import io.github.cdimascio.dotenv.Dotenv;

public class Server {
    private static final int PORT = 12345;
    private final ExecutorService pool;
    private final ConcurrentHashMap<String, PrintWriter> clients;
    private Connection dbConnection;
    private ServerSocket serverSocket; // Added as a class member for cleanup

    public Server() {
        pool = Executors.newCachedThreadPool();
        clients = new ConcurrentHashMap<>();
        connectToDatabase();
        setupDatabase();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true); // Enable port reuse
            serverSocket.bind(new InetSocketAddress(PORT));
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
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

    private void setupDatabase() {
        try (Statement stmt = dbConnection.createStatement()) {
            // Drop all existing tables dynamically
            try (ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'chatupt';")) {
                while (rs.next()) {
                    String tableName = rs.getString("table_name");
                    stmt.executeUpdate("DROP TABLE IF EXISTS " + tableName + " CASCADE;");
                }
            }
            String createUsersTable = """
                CREATE TABLE users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    role ENUM('admin', 'moderator', 'user') DEFAULT 'user',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                );
                """;

            String createChannelsTable = """
                CREATE TABLE channels (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) UNIQUE NOT NULL,
                    is_private BOOLEAN DEFAULT FALSE,
                    created_by INT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
                );
                """;

            String createMessagesTable = """
                CREATE TABLE messages (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    sender_id INT NOT NULL,
                    channel_id INT DEFAULT NULL,
                    recipient_id INT DEFAULT NULL,
                    content TEXT NOT NULL,
                    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    is_read BOOLEAN DEFAULT FALSE,
                    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
                    FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
                    FOREIGN KEY (recipient_id) REFERENCES users(id) ON DELETE CASCADE
                );
                """;

            String createChannelMembersTable = """
                CREATE TABLE channel_members (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id INT NOT NULL,
                    channel_id INT NOT NULL,
                    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                    FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE
                );
                """;

            String createFilesTable = """
                CREATE TABLE files (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    message_id INT NOT NULL,
                    file_path VARCHAR(255) NOT NULL,
                    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE
                );
                """;

            stmt.executeUpdate(createUsersTable);
            stmt.executeUpdate(createChannelsTable);
            stmt.executeUpdate(createMessagesTable);
            stmt.executeUpdate(createChannelMembersTable);
            stmt.executeUpdate(createFilesTable);

            System.out.println("Database schema set up.");
        } catch (SQLException e) {
            System.err.println("Failed to set up database schema: " + e.getMessage());
        }
    }

    private void cleanup() {
        // Close the server socket
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Server socket closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Shutdown the thread pool
        pool.shutdown();
        System.out.println("Thread pool shutdown.");
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String initialMessage = in.readLine();
                if (initialMessage == null) return;

                Gson gson = new Gson();
                Message message = gson.fromJson(initialMessage, Message.class);

                if (message.getType().equals("register")) {
                    // Handle user registration
                    User user = gson.fromJson(message.getContent(), User.class);
                    if (registerUser(user)) {
                        out.println("Registration successful.");
                    } else {
                        out.println("Registration failed: Username or email already exists.");
                    }
                } else if (message.getType().equals("connect")) {
                    // Real-time messaging connection
                    String username = message.getContent();
                    clients.put(username, out);
                    System.out.println(username + " connected.");

                    // Broadcast messages
                    String receivedMessage;
                    while ((receivedMessage = in.readLine()) != null) {
                        broadcast(username, receivedMessage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }

        private boolean registerUser(User user) {
            String insertUserSQL = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = dbConnection.prepareStatement(insertUserSQL)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getEmail());

                stmt.executeUpdate();
                System.out.println("User registered: " + user.getUsername());
                return true;
            } catch (SQLException e) {
                System.err.println("Failed to register user: " + e.getMessage());
                return false; // Likely due to duplicate username/email
            }
        }

        private void broadcast(String username, String message) {
            for (PrintWriter client : clients.values()) {
                client.println(username + ": " + message);
            }
        }

        private void disconnect() {
            try {
                String hostname = socket.getInetAddress().getHostName();
                clients.remove(hostname);
                socket.close();
                System.out.println("A client disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            new Server().cleanup();
        }));
        new Server().start();
    }
}