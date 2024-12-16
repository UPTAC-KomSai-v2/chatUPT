package uptackomsai.chatupt.network;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import uptackomsai.chatupt.model.Attachment;
import uptackomsai.chatupt.model.Request;

public class Client {
    private final String serverHost;
    private final int serverPort = 54321;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private DataOutputStream dataOut;

    public Client(String serverHost) {
        this.serverHost = serverHost;
    }

    public void connect(String username) throws IOException {
        socket = new Socket(serverHost, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        dataOut = new DataOutputStream(socket.getOutputStream());   // exclusive for file upload

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
    
    public void uploadFileToServer(File file) throws IOException {
        // Create an Attachment object
        String file_name = file.getName();
        String file_path = file.getAbsolutePath();
        int dotIndex = file_name.lastIndexOf('.');
        String file_type = file_name.substring(dotIndex + 1);
        int file_size = (int) file.length();
        Attachment attachment = new Attachment(file_name, file_path, file_type, file_size);
        
        // Serialize the file metadata to JSON
        Gson gson = new Gson();
        String fileMetadataJson = gson.toJson(attachment);
        Request request = new Request("uploadAttachment", fileMetadataJson); 
        String jsonMessage = gson.toJson(request);
        
        // Send the jsonMessage to server for file upload
        out.println(jsonMessage);
        
        // Send file content to server (equivalent to upload to server)
        // use dataOut for handling file upload
        try (FileInputStream fileIn = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileIn.read(buffer)) != -1) {
                dataOut.write(buffer, 0, bytesRead);
            }
            dataOut.flush();
        }
    }
    
}
