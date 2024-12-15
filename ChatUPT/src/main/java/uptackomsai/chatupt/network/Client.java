package uptackomsai.chatupt.network;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import uptackomsai.chatupt.model.Request;

public class Client {
    private final String serverHost;
    private final int serverPort = 54321;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String serverHost) {
        this.serverHost = serverHost;
    }

    public void connect(String username) throws IOException {
        socket = new Socket(serverHost, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Create a Message object (assuming you have a Message class with "type" and "content" fields)
        Request message = new Request("username", username);

        // Convert the Message object to JSON
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(message);

        // Send the JSON message to the server
        out.println(jsonMessage);
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
