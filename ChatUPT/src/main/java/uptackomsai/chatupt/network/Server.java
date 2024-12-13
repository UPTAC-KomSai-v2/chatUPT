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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.ConcurrentHashMap;
import uptackomsai.chatupt.model.Message;
import uptackomsai.chatupt.model.User;

import io.github.cdimascio.dotenv.Dotenv;
import uptackomsai.chatupt.providers.RegisterProvider;

public class Server {
    private static final int PORT = 12345;
    private final ExecutorService pool;
    private final ConcurrentHashMap<String, PrintWriter> clients;
    private ServerSocket serverSocket; // Added as a class member for cleanup
    private final List<ServerModule> modules; // List of registered modules

    public Server() {
        modules = new ArrayList<>();
        pool = Executors.newCachedThreadPool();
        clients = new ConcurrentHashMap<>();
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

                // Loop through all registered modules and invoke them
                for (ServerModule module : modules) {
                    module.handleRequest(message.getType(), message.getContent(), out);
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
                String hostname = socket.getInetAddress().getHostName();
                clients.remove(hostname);
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
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            System.out.println("Shutting down server...");
//            new Server().cleanup();
//        }));
//        new Server().start();

        Server server = new Server();
        // Register different modules
        server.registerModule(new RegisterProvider());
        server.start();
    }
}