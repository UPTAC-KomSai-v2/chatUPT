/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uptackomsai.chatupt.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import uptackomsai.chatupt.network.Client;
import uptackomsai.chatupt.utils.ImageLoader;
import com.formdev.flatlaf.FlatDarkLaf;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import uptackomsai.chatupt.model.Channel;
import uptackomsai.chatupt.model.DirectChat;
import uptackomsai.chatupt.model.Request;
import uptackomsai.chatupt.model.UserChannel;
import uptackomsai.chatupt.model.UserRole;
import uptackomsai.chatupt.utils.DatabaseUtils;
import uptackomsai.chatupt.utils.SessionUtil;
/**
 *
 * @author Lei
 */
public class MainFrame extends javax.swing.JFrame {
    private String serverHost;
    private final int userID;
//    DatabaseUtils dbUtils = new DatabaseUtils();
    /**
     * Creates new form ChatFrame
     * @param userID
     */
    public MainFrame(String serverHost, int userID) { // UserID is received here instead of username
        this.serverHost = serverHost;
        this.userID = userID;
        setUserOnline(userID);
        setResizable(false);
        initComponents();
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setTitle("Welcome");
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logoutUser(userID);
                System.exit(0); // Exit the application
            }
        });
        
        initializeAppPanel();
        initializeProfilePanel();
        initializeChannelList();
        initializeOnlineUserList();
        initializeAllUserList();
    }
    
    
    private void initializeAppPanel(){
        appiconLabel.setIcon(new ImageIcon(
            ImageLoader.loadImageIcon("logo.png").getImage().getScaledInstance(
            appiconLabel.getPreferredSize().width,
            appiconLabel.getPreferredSize().height,
            Image.SCALE_SMOOTH)
        ));
        
        appnameLabel.setIcon(new ImageIcon(
            ImageLoader.loadImageIcon("appname.png").getImage().getScaledInstance(
            appnameLabel.getPreferredSize().width,
            appnameLabel.getPreferredSize().height,
            Image.SCALE_SMOOTH)
        ));
    }
    
    private void initializeProfilePanel() {
        String profile_path = "default.png"; // Default profile picture

        try (Socket socket = new Socket(serverHost, 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Prepare the request JSON to get user data
            Gson gson = new Gson();
            JsonObject userRequest = new JsonObject();
            userRequest.addProperty("user_id", userID);

            Request request = new Request("getUserById", gson.toJson(userRequest));
            String jsonMessage = gson.toJson(request);

            // Send request to the server
            out.println(jsonMessage);

            // Read server response
            String response = in.readLine();

            if (response == null || response.equalsIgnoreCase("null")) {
                //JOptionPane.showMessageDialog(this, "No response from server.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Parse the JSON response
                JsonObject userObj = gson.fromJson(response, JsonObject.class);

                // Extract user details
                profile_path = userObj.has("profile_path") && !userObj.get("profile_path").isJsonNull()
                        ? userObj.get("profile_path").getAsString() : "default.png";
                String username = userObj.get("username").getAsString();

                // Set profile picture and username
                usernameItem.setText(username);
                proficonToggleButton.setIcon(new ImageIcon(
                        ImageLoader.loadImageIcon(profile_path).getImage().getScaledInstance(
                                proficonToggleButton.getPreferredSize().width,
                                proficonToggleButton.getPreferredSize().height,
                                Image.SCALE_SMOOTH)
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeChannelList() {
        channelsPanel.removeAll(); // Remove all components
        channelsPanel.revalidate(); // Recalculate layout
        channelsPanel.repaint(); 
        try (Socket socket = new Socket(serverHost, 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Prepare the request JSON
            Gson gson = new Gson();
            Request request = new Request("getAllChannels", ""); // Request to fetch all channels
            String jsonMessage = gson.toJson(request);

            // Send request to the server
            out.println(jsonMessage);

            // Read server response
            String response = in.readLine();

            if (response == null) {
                //JOptionPane.showMessageDialog(this, "No response from server.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (response.equalsIgnoreCase("null")) {
                //JOptionPane.showMessageDialog(this, "No channels found.", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Parse the JSON array of channels
                JsonArray channelsArray = gson.fromJson(response, JsonArray.class);

                // Iterate over the channels and add them to the panel
                for (int i = 0; i < channelsArray.size(); i++) {
                    JsonObject channelObj = channelsArray.get(i).getAsJsonObject();

                    // Extract channel details
                    String channelName = channelObj.get("channel_name").getAsString();
                    int chatId = channelObj.get("chat_id").getAsInt();
                    boolean isPrivate = channelObj.get("is_private").getAsBoolean();

                    // Add the channel to the panel
                    addChannelToPanel(channelName, channelsPanel, chatId, isPrivate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeOnlineUserList() {
        onlineUsersPanel.removeAll(); // Remove all components
        onlineUsersPanel.revalidate(); // Recalculate layout
        onlineUsersPanel.repaint(); 
        try (Socket socket = new Socket(serverHost, 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Prepare the request JSON
            Gson gson = new Gson();
            JsonObject userRequest = new JsonObject();
            userRequest.addProperty("user_id", userID); // Exclude the current user

            Request request = new Request("getOnlineUsers", gson.toJson(userRequest));
            String jsonMessage = gson.toJson(request);

            // Send the request to the server
            out.println(jsonMessage);

            // Read the server response
            String response = in.readLine();

            if (response == null) {
                //JOptionPane.showMessageDialog(this, "No response from server.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (response.equalsIgnoreCase("null")) {
                //JOptionPane.showMessageDialog(this, "No online users found.", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Parse the JSON array of online users
                JsonArray usersArray = gson.fromJson(response, JsonArray.class);

                // Iterate over the JSON array and add users to the panel
                for (int i = 0; i < usersArray.size(); i++) {
                    JsonObject userObj = usersArray.get(i).getAsJsonObject();

                    // Extract user details
                    String username = userObj.get("username").getAsString();
                    int otherUserId = userObj.get("user_id").getAsInt();

                    // Add user to the panel
                    addUserToPanel(username, onlineUsersPanel, otherUserId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeAllUserList() {
        allUsersPanel.removeAll(); // Remove all components
        allUsersPanel.revalidate(); // Recalculate layout
        allUsersPanel.repaint(); 
        try (Socket socket = new Socket(serverHost, 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Prepare the request JSON
            Gson gson = new Gson();
            JsonObject userRequest = new JsonObject();
            userRequest.addProperty("user_id", userID); // Exclude the current user

            Request request = new Request("getAllUsers", gson.toJson(userRequest));
            String jsonMessage = gson.toJson(request);

            // Send the request to the server
            out.println(jsonMessage);

            // Read the server response
            String response = in.readLine();

            if (response == null) {
                //JOptionPane.showMessageDialog(this, "No response from server.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (response.equalsIgnoreCase("null")) {
                //JOptionPane.showMessageDialog(this, "No users found.", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Parse the JSON array of users
                JsonArray usersArray = gson.fromJson(response, JsonArray.class);

                // Iterate over the JSON array and add users to the panel
                for (int i = 0; i < usersArray.size(); i++) {
                    JsonObject userObj = usersArray.get(i).getAsJsonObject();

                    // Extract user details
                    String username = userObj.get("username").getAsString();
                    int otherUserId = userObj.get("user_id").getAsInt();

                    // Add user to the panel
                    addUserToPanel(username, allUsersPanel, otherUserId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addUserToPanel(String username,JPanel panel,int otherUserID){
        JButton button = new JButton(username);
        button.setPreferredSize(new Dimension(160, 30)); 
        button.setMaximumSize(new Dimension(160, 30));
        button.setToolTipText(""+otherUserID);
        button.setAlignmentX(Component.CENTER_ALIGNMENT); 
        // Add an ActionListener to the button
        button.addActionListener((ActionEvent e) -> {
            // Action to perform on button click
            // JOptionPane.showMessageDialog(null, "other_user_id: "+button.getToolTipText(), "Alert", JOptionPane.INFORMATION_MESSAGE);
            newDirectChat(otherUserID);
        });
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.revalidate(); // Recalculate layout
        panel.repaint(); 
        // scroll down
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalBar = onlineUserScrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
            verticalBar = allUserScrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
        });
    }
    
    public void newDirectChat(int otherUserID){
        try (Socket socket = new Socket(serverHost, 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Create a User object and a Message object for the login request
            Gson gson = new Gson();
            DirectChat directChat = new DirectChat(userID, otherUserID); 
            String directChatJson = gson.toJson(directChat);
            Request request = new Request("newDirectChat", directChatJson); 
            String jsonMessage = gson.toJson(request);

            // Send request
            out.println(jsonMessage);

            // Read server response
            String response = in.readLine();

            if (response == null) {
                //JOptionPane.showMessageDialog(this, "No response from server.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (response.equalsIgnoreCase("Unable to set up direct chat")||
                    response.equalsIgnoreCase("Server error while creating direct chat")) {
                // when failed
                //JOptionPane.showMessageDialog(this, response, "Error while setting up direct chat", JOptionPane.ERROR_MESSAGE);
            } else { // when chat_id is returned, success
                openChatWindow(Integer.parseInt(response),false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
        }
    }
    
    public void openChatWindow(int chatID, boolean isChannel){
        // Remove the existing chat window if it exists
        chatWindowPane.removeAll(); // Remove all components
        chatWindowPane.revalidate(); // Recalculate layout
        chatWindowPane.repaint(); 
        ChatWindow chatWindow = new ChatWindow(serverHost,chatID,userID,isChannel); 
        chatWindowPane.add(chatWindow, BorderLayout.CENTER);
    }
    
    public void addChannel(String channelName, boolean isPrivate) {
        try (Socket socket = new Socket(serverHost, 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Create a User object and a Message object for the login request
            Gson gson = new Gson();
            Channel channel = new Channel(channelName, isPrivate, userID); 
            String channelJson = gson.toJson(channel);
            Request request = new Request("addChannel", channelJson); 
            String jsonMessage = gson.toJson(request);

            // Send request
            out.println(jsonMessage);

            // Read server response
            String response = in.readLine();

            if (response == null) {
                JOptionPane.showMessageDialog(this, "No response from server.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (response.equalsIgnoreCase("Unable to set up channel")) {
                // when failed
                JOptionPane.showMessageDialog(this, response, "Error while adding channel", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Channel '" + channelName + "' has been created as " + (isPrivate ? "Private" : "Public"));
                addChannelToPanel(channelName,channelsPanel,Integer.parseInt(response),isPrivate);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
        }
    }
    
    private void addChannelToPanel(String channelName,JPanel panel,int chatId,boolean isPrivate){
        JButton button = new JButton(channelName);
        button.setPreferredSize(new Dimension(160, 30)); 
        button.setMaximumSize(new Dimension(160, 30));
        button.setToolTipText(""+chatId);
        button.setAlignmentX(Component.CENTER_ALIGNMENT); 
        // Add an ActionListener to the button
        button.addActionListener((ActionEvent e) -> {
            // Action to perform on button click
             // Check the user's role for this chat
            String userRole = getUserRoleInChannel(chatId, userID); // Method to query the role from server

            if ("admin".equals(userRole) || "member".equals(userRole)) {
                // User is already admin or member, open the chat directly
                openChatWindow(chatId, true); // Opens the chat window for channels
            } else if (isPrivate) {
                // Private channel - Prompt the user
                int choice = JOptionPane.showConfirmDialog(
                        null,
                        "This channel is private. Only members and admins can enter.\nWould you like to request to join?",
                        "Private Channel",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (choice == JOptionPane.YES_OPTION) {
                    // Request to join the private channel
                    if (requestToJoinChannel(chatId, "pending")) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Your request to join has been sent. Waiting for approval.",
                                "Request Sent",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Failed to send the request. Please try again.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            } else {
                // Public channel - Automatically join with "pending" role
                if(userRole == null) userRole = "pending";
                if (requestToJoinChannel(chatId, userRole)) {
                    openChatWindow(chatId, true);
                    JOptionPane.showMessageDialog(
                            null,
                            "You have entered a public channel.",
                            "Channel Joined",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Failed to join the channel. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.revalidate(); // Recalculate layout
        panel.repaint(); 
        // scroll down
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalBar = channelScrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
        });
    }
    
    private boolean requestToJoinChannel(int chatId, String role) {
        try (Socket socket = new Socket(serverHost, 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Prepare the request object
            Gson gson = new Gson();
            UserChannel userChannelObject = new UserChannel(userID,chatId,role);

            Request request = new Request("joinChannel", gson.toJson(userChannelObject));
            String jsonRequest = gson.toJson(request);

            // Send the request to the server
            out.println(jsonRequest);

            // Wait for the server response
            String response = in.readLine();

            return response != null && response.equalsIgnoreCase("success");

        } catch (IOException e) {
            System.err.println("Error while requesting to join channel: " + e.getMessage());
            return false;
        }
    }

    private String getUserRoleInChannel(int chatId, int userId) {
        try (Socket socket = new Socket(serverHost, 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Prepare the request JSON
            Gson gson = new Gson();
            UserRole request = new UserRole(chatId,userId);

            Request serverRequest = new Request("getUserRole", gson.toJson(request));
            out.println(gson.toJson(serverRequest));

            // Read server response
            String response = in.readLine();
            return response; // Return the role (e.g., "admin", "member", "pending", or null)

        } catch (IOException e) {
            System.err.println("Error checking user role: " + e.getMessage());
            return null; // Return null on failure
        }
    }
    
    private void logoutUser(int userID) {
        try (Socket socket = new Socket(serverHost, 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Prepare the request JSON
            Gson gson = new Gson();
            JsonObject userRequest = new JsonObject();
            userRequest.addProperty("user_id", userID);

            Request request = new Request("logoutUser", gson.toJson(userRequest));
            String jsonMessage = gson.toJson(request);

            // Send request to the server
            out.println(jsonMessage);

            // Read server response
            String response = in.readLine();
            
            // Destroy Session variable
            SessionUtil.sessionUserId = -1;
            SessionUtil.sessionUsername = null;

            if ("success".equalsIgnoreCase(response)) {
                System.out.println("User logged out successfully.");
            } else {
                System.err.println("Failed to log out user.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error connecting to server for logout: " + e.getMessage());
        }
    }
    
    private void setUserOnline(int userID) {
        try (Socket socket = new Socket(serverHost, 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Prepare the request JSON
            Gson gson = new Gson();
            JsonObject userRequest = new JsonObject();
            userRequest.addProperty("user_id", userID);

            Request request = new Request("setUserOnline", gson.toJson(userRequest));
            String jsonMessage = gson.toJson(request);

            // Send the request to the server
            out.println(jsonMessage);

            // Read the server response
            String response = in.readLine();

            if ("success".equalsIgnoreCase(response)) {
                System.out.println("User with ID " + userID + " is now online.");
            } else {
                System.err.println("Failed to set user with ID " + userID + " as online.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error connecting to server for setting user online: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenu = new javax.swing.JPopupMenu();
        usernameItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        Settings = new javax.swing.JMenuItem();
        Logout = new javax.swing.JMenuItem();
        headPanel = new javax.swing.JPanel();
        appPanel = new javax.swing.JPanel();
        appiconLabel = new javax.swing.JLabel();
        appnameLabel = new javax.swing.JLabel();
        profilePanel = new javax.swing.JPanel();
        proficonToggleButton = new javax.swing.JToggleButton();
        LeftSidebar = new javax.swing.JSplitPane();
        channelListPanel = new javax.swing.JPanel();
        channelHeadPanel = new javax.swing.JPanel();
        channelListLabel = new javax.swing.JLabel();
        newChannelButton = new javax.swing.JButton();
        channelScrollPane = new javax.swing.JScrollPane();
        channelsPanel = new javax.swing.JPanel();
        userListPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        onlineUserScrollPane = new javax.swing.JScrollPane();
        onlineUsersPanel = new javax.swing.JPanel();
        allUserScrollPane = new javax.swing.JScrollPane();
        allUsersPanel = new javax.swing.JPanel();
        chatWindowPane = new javax.swing.JPanel();

        popupMenu.setComponentPopupMenu(popupMenu);

        usernameItem.setEnabled(false);
        popupMenu.add(usernameItem);
        popupMenu.add(jSeparator1);

        Settings.setText("Settings");
        Settings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SettingsActionPerformed(evt);
            }
        });
        popupMenu.add(Settings);

        Logout.setText("Logout");
        Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutActionPerformed(evt);
            }
        });
        popupMenu.add(Logout);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        headPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        headPanel.setPreferredSize(new java.awt.Dimension(800, 50));
        headPanel.setLayout(new java.awt.BorderLayout());

        appPanel.setBackground(new java.awt.Color(255, 255, 255));
        appPanel.setOpaque(false);
        appPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 3));

        appiconLabel.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        appiconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        appiconLabel.setMaximumSize(new java.awt.Dimension(40, 14));
        appiconLabel.setPreferredSize(new java.awt.Dimension(40, 40));
        appPanel.add(appiconLabel);

        appnameLabel.setPreferredSize(new java.awt.Dimension(187, 40));
        appPanel.add(appnameLabel);

        headPanel.add(appPanel, java.awt.BorderLayout.LINE_START);

        profilePanel.setBackground(new java.awt.Color(255, 255, 255));
        profilePanel.setOpaque(false);

        proficonToggleButton.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        proficonToggleButton.setToolTipText("Menu");
        proficonToggleButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        proficonToggleButton.setComponentPopupMenu(popupMenu);
        proficonToggleButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        proficonToggleButton.setInheritsPopupMenu(true);
        proficonToggleButton.setPreferredSize(new java.awt.Dimension(40, 40));
        proficonToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proficonToggleButtonActionPerformed(evt);
            }
        });
        profilePanel.add(proficonToggleButton);

        headPanel.add(profilePanel, java.awt.BorderLayout.LINE_END);

        getContentPane().add(headPanel, java.awt.BorderLayout.PAGE_START);

        LeftSidebar.setDividerLocation(200);
        LeftSidebar.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        LeftSidebar.setResizeWeight(0.5);
        LeftSidebar.setToolTipText("");
        LeftSidebar.setPreferredSize(new java.awt.Dimension(200, 0));

        channelListPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        channelListPanel.setLayout(new java.awt.BorderLayout());

        channelHeadPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        channelListLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        channelListLabel.setText("Channels");
        channelHeadPanel.add(channelListLabel);

        newChannelButton.setText("+");
        newChannelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newChannelButtonActionPerformed(evt);
            }
        });
        channelHeadPanel.add(newChannelButton);

        channelListPanel.add(channelHeadPanel, java.awt.BorderLayout.NORTH);

        channelScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        channelScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        channelScrollPane.setViewportView(channelsPanel);

        channelsPanel.setLayout(new javax.swing.BoxLayout(channelsPanel, javax.swing.BoxLayout.Y_AXIS));
        channelScrollPane.setViewportView(channelsPanel);

        channelListPanel.add(channelScrollPane, java.awt.BorderLayout.CENTER);

        LeftSidebar.setTopComponent(channelListPanel);

        userListPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        userListPanel.setLayout(new java.awt.BorderLayout());

        onlineUserScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        onlineUserScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        onlineUsersPanel.setLayout(new javax.swing.BoxLayout(onlineUsersPanel, javax.swing.BoxLayout.Y_AXIS));
        onlineUserScrollPane.setViewportView(onlineUsersPanel);

        jTabbedPane1.addTab("Online", onlineUserScrollPane);

        allUserScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        allUserScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        allUsersPanel.setLayout(new javax.swing.BoxLayout(allUsersPanel, javax.swing.BoxLayout.Y_AXIS));
        allUserScrollPane.setViewportView(allUsersPanel);

        jTabbedPane1.addTab("All", allUserScrollPane);

        userListPanel.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        LeftSidebar.setRightComponent(userListPanel);

        getContentPane().add(LeftSidebar, java.awt.BorderLayout.LINE_START);

        chatWindowPane.setLayout(new java.awt.BorderLayout());
        getContentPane().add(chatWindowPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void proficonToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proficonToggleButtonActionPerformed
        if (proficonToggleButton.isSelected()) {
            proficonToggleButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            popupMenu.show(proficonToggleButton, -proficonToggleButton.getWidth(), proficonToggleButton.getHeight());
        } else {
            proficonToggleButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            popupMenu.setVisible(false);
        }
    }//GEN-LAST:event_proficonToggleButtonActionPerformed

    private void SettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SettingsActionPerformed
        proficonToggleButton.setSelected(false);
        
        ProfileManageFrame settingsFrame = new ProfileManageFrame(userID); 
        settingsFrame.setVisible(true);
    }//GEN-LAST:event_SettingsActionPerformed

    private void LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutActionPerformed
        proficonToggleButton.setSelected(false);
        
        logoutUser(userID);
        
        LoginFrame loginFrame = new LoginFrame(); 
        loginFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LogoutActionPerformed

    private void newChannelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newChannelButtonActionPerformed
         // Create the input field for the channel name
        JTextField channelNameField = new JTextField(20);

        // Create the checkbox for determining if the channel is private or public
        JCheckBox privateChannelCheckBox = new JCheckBox("Private Channel");
        privateChannelCheckBox.setSelected(false); // Default to public (unchecked)

        // Create a panel to hold the form inputs
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Enter Channel Name:"));
        panel.add(channelNameField);
        panel.add(privateChannelCheckBox);

        // Show the custom dialog using JOptionPane
        int result = JOptionPane.showConfirmDialog(
            null,
            panel,
            "Add New Channel",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        // Handle the dialog result
        if (result == JOptionPane.OK_OPTION) {
            String channelName = channelNameField.getText().trim();
            boolean isPrivate = privateChannelCheckBox.isSelected();
            
            if (!channelName.isEmpty()) {
                // Here you can handle the creation of the channel
                addChannel(channelName,isPrivate);
            } else {
                JOptionPane.showMessageDialog(null, "Channel name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Channel creation cancelled.");
        }
    }//GEN-LAST:event_newChannelButtonActionPerformed

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        initializeChannelList();
        initializeOnlineUserList();
        initializeAllUserList();
    }//GEN-LAST:event_formMouseMoved

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        initializeChannelList();
        initializeOnlineUserList();
        initializeAllUserList();
    }//GEN-LAST:event_formMouseEntered

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        initializeChannelList();
        initializeOnlineUserList();
        initializeAllUserList();
    }//GEN-LAST:event_formKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        try {
            // Set the FlatLaf Look and Feel
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame("localhost",-1).setVisible(true); 
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane LeftSidebar;
    private javax.swing.JMenuItem Logout;
    private javax.swing.JMenuItem Settings;
    private javax.swing.JScrollPane allUserScrollPane;
    private javax.swing.JPanel allUsersPanel;
    private javax.swing.JPanel appPanel;
    private javax.swing.JLabel appiconLabel;
    private javax.swing.JLabel appnameLabel;
    private javax.swing.JPanel channelHeadPanel;
    private javax.swing.JLabel channelListLabel;
    private javax.swing.JPanel channelListPanel;
    private javax.swing.JScrollPane channelScrollPane;
    private javax.swing.JPanel channelsPanel;
    private javax.swing.JPanel chatWindowPane;
    private javax.swing.JPanel headPanel;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton newChannelButton;
    private javax.swing.JScrollPane onlineUserScrollPane;
    private javax.swing.JPanel onlineUsersPanel;
    private javax.swing.JPopupMenu popupMenu;
    private javax.swing.JToggleButton proficonToggleButton;
    private javax.swing.JPanel profilePanel;
    private javax.swing.JPanel userListPanel;
    private javax.swing.JMenuItem usernameItem;
    // End of variables declaration//GEN-END:variables
}
