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
import uptackomsai.chatupt.model.Attachment;
import uptackomsai.chatupt.providers.ChatMessageProvider;
import uptackomsai.chatupt.providers.DbBaseProvider;
import uptackomsai.chatupt.providers.GetAllChannelsProvider;
import uptackomsai.chatupt.providers.GetAllUsersProvider;
import uptackomsai.chatupt.providers.GetChatWindowDetailsProvider;
import uptackomsai.chatupt.providers.GetOnlineUsersProvider;
import uptackomsai.chatupt.providers.GetUserByIdProvider;
import uptackomsai.chatupt.providers.JoinChannelProvider;
import uptackomsai.chatupt.providers.LoginProvider;
import uptackomsai.chatupt.providers.MessageHandlerProvider;
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
                DataInputStream dataIn = new DataInputStream(socket.getInputStream()); // exclusive for file upload
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
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        case "editProfile": // Handle edit profile
                            System.out.println("Processing Edit Profile: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof EditProfileProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                        case "sendMessage":  // setting up chatwindow
                            System.out.println("Sending message: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof MessageHandlerProvider) {
                                    module.handleRequest(request.getType(), request.getContent(), out);
                                    break;
                                }
                            }
                            break;
                        case "uploadAttachment":  // Handle file attachment
                            System.out.println("Getting the attachment metadata: " + request.getContent());
                            handleFileUpload(request.getContent(), dataIn);
                            break;
                        case "userData": // Handle Get Username
                            System.out.println("Getting username: " + request.getContent());
                            for (ServerModule module : modules) {
                                if (module instanceof UserDataProvider) {
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
        
        private void handleFileUpload(String jsonMessage, DataInputStream dataIn) {
            // Read received file metadata
            Gson gson = new Gson();
            Attachment attachment = gson.fromJson(jsonMessage, Attachment.class);
            String fileName = attachment.getFileName();
            long fileSize = attachment.getFileSize();
            
            // Save the file in the "uploads" directory
            File uploadsDir = new File("uploads");
            if (!uploadsDir.exists()) {
                uploadsDir.mkdir();
            }

            File uploadedFile = new File(uploadsDir, fileName);
            try (FileOutputStream fileOut = new FileOutputStream(uploadedFile)) {
                byte[] buffer = new byte[4096];
                long bytesRemaining = fileSize;
                int bytesRead;
                while (bytesRemaining > 0) {
                    bytesRead = dataIn.read(buffer, 0, (int) Math.min(buffer.length, bytesRemaining));
                    if (bytesRead == -1) {
                        throw new IOException("Stream closed before file fully read");
                    }
                    fileOut.write(buffer, 0, bytesRead);
                    bytesRemaining -= bytesRead;
                }

                System.out.println("File uploaded successfully: " + uploadedFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error receiving file: " + e.getMessage());
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
        server.registerModule(new MessageHandlerProvider());
        server.registerModule(new EditProfileProvider());
        server.registerModule(new UserDataProvider());
        
        DbBaseProvider db = new DbBaseProvider();
        db.setupDatabase();
        server.start();
    }
}