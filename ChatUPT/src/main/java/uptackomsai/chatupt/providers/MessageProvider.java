package uptackomsai.chatupt.providers;

import com.google.gson.Gson;
import uptackomsai.chatupt.model.User;
import uptackomsai.chatupt.network.ServerModule;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MessageProvider implements ServerModule {

    private void broadcast(String username, String message) {
//        for (PrintWriter client : clients.values()) {
//            client.println(username + ": " + message);
//        }
    }

    @Override
    public void handleRequest(String type, String content, PrintWriter out) {
        if (type.equals("register")) {
            Gson gson = new Gson();
            User user = gson.fromJson(content, User.class);

//            if (registerUser(user)) {
//                out.println("Registration successful.");
//            } else {
//                out.println("Registration failed: Username or email already exists.");
//            }
        }
    }
}
