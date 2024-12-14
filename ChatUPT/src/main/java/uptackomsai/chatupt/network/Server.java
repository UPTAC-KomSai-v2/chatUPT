package uptackomsai.chatupt.network;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import com.google.gson.Gson;
import uptackomsai.chatupt.model.Request;
import uptackomsai.chatupt.providers.RegisterProvider;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import uptackomsai.chatupt.providers.DbBaseProvider;
import uptackomsai.chatupt.providers.LoginProvider;
import uptackomsai.chatupt.providers.NewChannelProvider;

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
                Gson gson = new Gson();
                out = new PrintWriter(socket.getOutputStream(), true);

                while ((jsonMessage = in.readLine()) != null) {
                    // Parse the received message
                    Request request = gson.fromJson(jsonMessage, Request.class);
                    
                    // Process the message based on its type
                    switch (request.getType()) {
                        case "register": // Handle registration request
                            System.out.println("Processing register: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof RegisterProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        case "login": // Handle login
                            System.out.println("Processing login: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof LoginProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        case "addChannel": // Handle add channel
                            System.out.println("Processing Channel Add: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof NewChannelProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        default:
                            System.err.println("Unknown request type: " + request.getType());
                    }
                }
            } catch (com.google.gson.JsonSyntaxException e) {
                System.err.println("Invalid JSON received: " + jsonMessage);
            } catch (IOException e) {
                e.printStackTrace();
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
        server.registerModule(new LoginProvider());
        server.registerModule(new NewChannelProvider());
        
        DbBaseProvider db = new DbBaseProvider();
        db.setupDatabase();
        server.start();
    }
}