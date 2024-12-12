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

public class Client {
    private final String serverHost;
    private final int serverPort;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void connect(String username) throws IOException {
        socket = new Socket(serverHost, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        out.println(username);
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
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