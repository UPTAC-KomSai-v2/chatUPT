package uptackomsai.chatupt.network;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import com.google.gson.Gson;
import uptackomsai.chatupt.model.Message;
import uptackomsai.chatupt.providers.RegisterProvider;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 12345;
    private final ExecutorService pool;
    private final ConcurrentHashMap<String, PrintWriter> clients;
    private final List<ServerModule> modules;

    public Server() {
        pool = Executors.newCachedThreadPool();
        clients = new ConcurrentHashMap<>();
        modules = new ArrayList<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;
        private String jsonMessage;
        private String username;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                // First, read the username sent by the client
                jsonMessage = in.readLine();
                Gson gson = new Gson();
                Message messageJson = gson.fromJson(jsonMessage, Message.class);
                username = messageJson.getContent();
                
                if (username != null) {
                    // Add the client to the clients map
                    out = new PrintWriter(socket.getOutputStream(), true);
                    clients.put(username, out);
                    System.out.println(username + " connected.");

                    // Broadcast the new user connection to all clients
                    broadcast(username, " has joined the chat.");

                    String message;
                    // Handle the incoming chat messages
                    while ((message = in.readLine()) != null) {
                        broadcast(username, message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }

        private void broadcast(String username, String message) {
            for (PrintWriter client : clients.values()) {
                client.println(username + ": " + message);
            }
        }

        private void disconnect() {
            try {
                if (username != null) {
                    clients.remove(username);
                    broadcast("Server", username + " has left the chat.");
                }
                socket.close();
                System.out.println("A client disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Register a module that can handle specific types of requests
    public void registerModule(ServerModule module) {
        modules.add(module);
    }

    public static void main(String[] args) {
        Server server = new Server();
        // Register different modules
        server.registerModule(new RegisterProvider());
        server.start();
    }
}