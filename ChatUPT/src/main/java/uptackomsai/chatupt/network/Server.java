package uptackomsai.chatupt.network;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import uptackomsai.chatupt.model.Request;
import uptackomsai.chatupt.providers.*;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import uptackomsai.chatupt.providers.ChatMessageProvider;
import uptackomsai.chatupt.providers.DbBaseProvider;
import uptackomsai.chatupt.providers.GetAllChannelsProvider;
import uptackomsai.chatupt.providers.GetAllUsersProvider;
import uptackomsai.chatupt.providers.GetChatWindowDetailsProvider;
import uptackomsai.chatupt.providers.GetOnlineUsersProvider;
import uptackomsai.chatupt.providers.GetUserByIdProvider;
import uptackomsai.chatupt.providers.JoinChannelProvider;
import uptackomsai.chatupt.providers.LoginProvider;
import uptackomsai.chatupt.providers.NewChannelProvider;
import uptackomsai.chatupt.providers.NewDirectChatProvider;
import uptackomsai.chatupt.providers.SetUserOfflineProvider;
import uptackomsai.chatupt.providers.SetUserOnlineProvider;
import uptackomsai.chatupt.providers.UserRoleProvider;

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
        
        private String getOnlineUsersAsJson() {
            Gson gson = new Gson();
            List<String> onlineUsers = new ArrayList<>(clients.keySet());
            return gson.toJson(onlineUsers);
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
                                    // Assume login success; parse the username from the content
                                    gson = new Gson();
                                    JsonObject loginData = gson.fromJson(request.getContent(), JsonObject.class);
                                    username = loginData.get("username").getAsString(); 

                                    // Add client to connected clients map
                                    clients.put(username, out);
                                    System.out.println(username + " logged in and added to clients.");

                                    // Broadcast updated online users list
                                    broadcast("updateOnlineUsers","");
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
                        case "newDirectChat": // Handle new direct chats
                            System.out.println("Processing New Direct Chat: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof NewDirectChatProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        case "prevMessages": // Gets the previous Messages stored in Database
                            System.out.println("Getting Previous Chat Messagest: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof ChatMessageProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        case "joinChannel": // Handle channel joins
                            System.out.println("joining Channel: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof JoinChannelProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        case "getUserRole": //Get Channel User Role
                            System.out.println("Getting User Channel Role: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof UserRoleProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        case "getUserById": //Get User Details By Id
                            System.out.println("Getting User Detail by ID: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof GetUserByIdProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        case "getAllChannels": //Get All Channels
                            System.out.println("Getting All Channels in server: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof GetAllChannelsProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        case "getOnlineUsers": //Get Online Users
                            System.out.println("Getting all Online Users: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof GetOnlineUsersProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        case "getAllUsers": // Get all users in server
                            System.out.println("Getting all Users: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof GetAllUsersProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        case "logoutUser": // Set status as offline
                            System.out.println("Setting user offline status: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof SetUserOfflineProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        case "setUserOnline": // Set status as online
                            System.out.println("Setting user online status: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof SetUserOnlineProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        case "getChatWindowDetails":  // setting up chatwindow
                            System.out.println("Getting other user's username and status: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof GetChatWindowDetailsProvider) {
                        case "editProfile": // Handle edit profile
                            System.out.println("Processing Edit Profile: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof EditProfileProvider) {
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

        private void broadcast(String type, String content) {
            Gson gson = new Gson();
            Request broadcastRequest = new Request(type, content);
            String jsonMessage = gson.toJson(broadcastRequest);

            for (PrintWriter client : clients.values()) {
                client.println(jsonMessage);
            }
        }

        private void disconnect() {
            try {
                if (username != null) {
                    clients.remove(username);
                    System.out.println(username + " has been removed from clients.");
                    broadcast("updateOnlineUsers", getOnlineUsersAsJson());
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
        server.registerModule(new NewDirectChatProvider());
        server.registerModule(new ChatMessageProvider());
        server.registerModule(new JoinChannelProvider());
        server.registerModule(new UserRoleProvider());
        server.registerModule(new GetUserByIdProvider());
        server.registerModule(new GetAllChannelsProvider());
        server.registerModule(new GetOnlineUsersProvider());
        server.registerModule(new GetAllUsersProvider());
        server.registerModule(new SetUserOfflineProvider());
        server.registerModule(new SetUserOnlineProvider());
        server.registerModule(new GetChatWindowDetailsProvider());
        
        DbBaseProvider db = new DbBaseProvider();
        db.setupDatabase();
        server.start();
    }
}