/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.network;

/**
 *
 * @author Lei
 */
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 12345;
    private final ExecutorService pool;
    private final ConcurrentHashMap<String, PrintWriter> clients;

    public Server() {
        pool = Executors.newCachedThreadPool();
        clients = new ConcurrentHashMap<>();
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

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String username = in.readLine();
                clients.put(username, out);
                System.out.println(username + " connected.");

                String message;
                while ((message = in.readLine()) != null) {
                    broadcast(username, message);
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
                clients.remove(socket.getInetAddress().getHostName());
                socket.close();
                System.out.println("A client disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}