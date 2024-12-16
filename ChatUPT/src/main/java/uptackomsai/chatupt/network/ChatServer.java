/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.network;

import com.google.gson.JsonObject;
import java.io.*;
import java.net.*;

public class ChatServer {
    private final int PORT = 54321;
    private String serverHost = "localhost";
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ChatServer(String serverHost) {
        this.serverHost = serverHost;
    }

    public void connect() throws IOException {
        socket = new Socket(serverHost, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }
    
    public void sendMessage(JsonObject message) {
        if (out != null) {
            out.println(message.toString());
        }
    }

    public String receiveMessage() throws IOException {
        if (in != null) {
            return in.readLine();
        }
        return null;
    }

    public void disconnect() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }
}
