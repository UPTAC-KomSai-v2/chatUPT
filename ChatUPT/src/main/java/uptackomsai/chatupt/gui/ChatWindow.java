/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package uptackomsai.chatupt.gui;

/**
 *
 * @author Lei
 */
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.HierarchyEvent;
import java.awt.event.WindowAdapter;
import java.io.File;
import uptackomsai.chatupt.network.Client;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import uptackomsai.chatupt.model.ChatMessage;
import uptackomsai.chatupt.model.DirectChat;
import uptackomsai.chatupt.model.Message;
import uptackomsai.chatupt.model.Request;
import uptackomsai.chatupt.utils.DatabaseUtils;
import java.net.URISyntaxException;
import uptackomsai.chatupt.network.ChatServer;

public class ChatWindow extends javax.swing.JPanel {
    private ChatServer client;
    private String serverHost;
    private int chatID;
    private int userID;
    private boolean isChannel;
    private File attachedFile; // Holds attached file while sendButton is not clicked
    /**
     * Creates new form ChatWindow
     */
    public ChatWindow(String serverHost,int chatID, int userID ,boolean isChannel) {
        this.serverHost = serverHost;
        this.client = new ChatServer(serverHost);
        this.chatID = chatID;
        this.userID = userID;
        this.isChannel = isChannel;
        
        initComponents();
        
        initChatWindow();
        initPrevMessages(chatID,isChannel);

        if(!isChannel) {
            adminPanel.setVisible(false); 
            revalidate(); 
            repaint();
        } 
        
        
    }
    
    private void initChatWindow() {
        try (Socket socket = new Socket(serverHost, 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Prepare the request JSON
            Gson gson = new Gson();
            JsonObject requestContent = new JsonObject();
            requestContent.addProperty("chat_id", chatID);
            requestContent.addProperty("user_id", userID);
            requestContent.addProperty("is_channel", isChannel);

            Request serverRequest = new Request("getChatWindowDetails", gson.toJson(requestContent));
            String jsonRequest = gson.toJson(serverRequest);

            // Send request to server
            out.println(jsonRequest);

            // Read the server response
            String response = in.readLine();

            if (response == null) {
                JOptionPane.showMessageDialog(this, "No response from server.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse the response
            JsonObject responseData = gson.fromJson(response, JsonObject.class);
            String username = responseData.get("username").getAsString();
            boolean isOnline = responseData.get("is_online").getAsBoolean();

            // Update the labels
            usernameLabel.setText(username);
            activeStatus.setText(isOnline ? "[Online]" : "[Offline]");

            System.out.println("Chat initialized with user: " + username + ", Status: " + (isOnline ? "Online" : "Offline"));

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void initPrevMessages(int chatId, boolean isChannel) {
        messagesPanel.removeAll(); // Remove all components
        messagesPanel.revalidate(); // Recalculate layout
        messagesPanel.repaint(); 
        try (Socket socket = new Socket(serverHost, 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Create a ChatMessage object and wrap it in a Request
            Gson gson = new Gson();
            ChatMessage chatmessage = new ChatMessage(chatId, isChannel); 
            String chatmessageJson = gson.toJson(chatmessage);
            Request request = new Request("prevMessages", chatmessageJson); 
            String jsonMessage = gson.toJson(request);

            // Send request to the server
            out.println(jsonMessage);

            // Read server response
            String response = in.readLine();

            if (response == null) {
                //JOptionPane.showMessageDialog(this, "No response from server.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (response.equalsIgnoreCase("No messages found!")) {
                //JOptionPane.showMessageDialog(this, "No messages found in this chat.", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Parse the JSON array of messages received from the server
                JsonArray messagesArray = gson.fromJson(response, JsonArray.class);

                // Iterate over the JSON array and display the messages
                for (int i = 0; i < messagesArray.size(); i++) {
                    JsonObject messageObj = messagesArray.get(i).getAsJsonObject();

                    // Extract message details
                    String username = messageObj.get("username").getAsString();
                    String profilePath = messageObj.get("profile_path").getAsString();
                    String timeSent = messageObj.get("time_sent").getAsString();
                    String content = messageObj.get("content").getAsString();
                    String filePath = messageObj.has("file_path") && !messageObj.get("file_path").isJsonNull()
                            ? messageObj.get("file_path").getAsString() : "No Attachment";
                    boolean isRead = messageObj.get("is_read").getAsBoolean(); // Add is_read flag

                    // Add the message to the chat window or panel
                    
                    addMessageToMessagesPanel(profilePath, username, timeSent, content, filePath, isRead);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
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

        headPanel = new javax.swing.JPanel();
        chatheaderPanel = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        activeStatus = new javax.swing.JLabel();
        typingIndicator = new javax.swing.JLabel();
        adminPanel = new javax.swing.JPanel();
        adminSettings = new javax.swing.JButton();
        chatScrollPane = new javax.swing.JScrollPane();
        messagesPanel = new javax.swing.JPanel();
        inputPanel = new javax.swing.JPanel();
        attachmentPane = new javax.swing.JEditorPane();
        attachButton = new javax.swing.JButton();
        inputField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
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
        setLayout(new java.awt.BorderLayout());

        headPanel.setLayout(new java.awt.BorderLayout());

        chatheaderPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 9));

        usernameLabel.setText("<chatname>");
        chatheaderPanel.add(usernameLabel);

        activeStatus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        activeStatus.setText("<active_status>");
        chatheaderPanel.add(activeStatus);
        chatheaderPanel.add(typingIndicator);

        headPanel.add(chatheaderPanel, java.awt.BorderLayout.LINE_START);

        adminSettings.setText("Settings");
        adminSettings.setToolTipText("");
        adminSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminSettingsActionPerformed(evt);
            }
        });
        adminPanel.add(adminSettings);

        headPanel.add(adminPanel, java.awt.BorderLayout.LINE_END);

        add(headPanel, java.awt.BorderLayout.NORTH);

        chatScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        chatScrollPane.setMaximumSize(new java.awt.Dimension(750, 32767));
        chatScrollPane.setViewportView(messagesPanel);

        messagesPanel.setLayout(new javax.swing.BoxLayout(messagesPanel, javax.swing.BoxLayout.Y_AXIS));
        chatScrollPane.setViewportView(messagesPanel);

        add(chatScrollPane, java.awt.BorderLayout.CENTER);

        inputPanel.setPreferredSize(new java.awt.Dimension(400, 50));
        inputPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        attachmentPane.setEditable(false);
        attachmentPane.setContentType("text/html"); // NOI18N
        attachmentPane.setText("<html>\r   <head>\r \r   </head>\r   <body>\r     <p style=\"margin-top: 0\">\r            </p>\r   </body>\r </html>\r ");
        attachmentPane.setEnabled(false);
        attachmentPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                attachmentPaneHyperlinkUpdate(evt);
            }
        });
        inputPanel.add(attachmentPane);

        attachButton.setText("Attach File");
        attachButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        attachButton.setMargin(new java.awt.Insets(2, 5, 3, 5));
        attachButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        attachButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attachButtonActionPerformed(evt);
            }
        });
        inputPanel.add(attachButton);

        inputField.setToolTipText("Enter Message");
        inputField.setFocusTraversalPolicyProvider(true);
        inputField.setPreferredSize(new java.awt.Dimension(200, 30));
        inputField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inputFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputFieldKeyReleased(evt);
            }
        });
        inputPanel.add(inputField);

        sendButton.setText("Send");
        sendButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        sendButton.setMargin(new java.awt.Insets(2, 5, 3, 5));
        sendButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });
        inputPanel.add(sendButton);

        add(inputPanel, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents
    
    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalBar = chatScrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
        });
    }
    
    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        String content = inputField.getText();
        String attachmentPath = attachmentPane.getText().trim(); // File path for attachments

        if (!content.trim().isEmpty() || !attachmentPath.isEmpty()) {
            try (Socket socket = new Socket(serverHost, 12345);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // Create the current timestamp
                String timeSent = java.time.LocalDateTime.now().toString();

                // Prepare the Message object
                Message message = new Message(
                        this.chatID,                // chat_id
                        null,                       // profile_path (will be filled by server)
                        this.userID,                // user_id
                        null,                       // username (will be filled by server)
                        timeSent,                   // time_sent
                        content,                    // content
                        attachmentPath,             // file_path
                        false                       // is_read (default: false)
                );

                // Wrap the Message object in a Request
                Gson gson = new Gson();
                String messageJson = gson.toJson(message);
                Request request = new Request("sendMessage", messageJson);

                // Send the request to the server
                out.println(gson.toJson(request));

                // Read the server response
                String response = in.readLine();
                if (response == null) {
                    JOptionPane.showMessageDialog(this, "No response from server.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (response.equalsIgnoreCase("failure")) {
                    JOptionPane.showMessageDialog(this, "Failed to send message.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Parse the JSON response from the server
                    JsonObject messageObj = gson.fromJson(response, JsonObject.class);

                    // Extract message details
                    int chatId = messageObj.get("chat_id").getAsInt();
                    String profilePath = messageObj.get("profile_path").getAsString();
                    String username = messageObj.get("username").getAsString();
                    String time = messageObj.get("time_sent").getAsString();
                    String messageContent = messageObj.get("content").getAsString();
                    String filePath = messageObj.has("file_path") && !messageObj.get("file_path").isJsonNull()
                            ? messageObj.get("file_path").getAsString() : "";

                    // Add the message to the chat panel
                    initPrevMessages(chatId, false);
                    //addMessageToMessagesPanel(profilePath, username, time, messageContent, filePath, false);
                }

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Clear the input field
            inputField.setText("");
        }
    }//GEN-LAST:event_sendButtonActionPerformed
    
    private void addMessageToMessagesPanel(String profile_path, String username,
            String time_sent, String content, String file_path, boolean is_read) { 
        MessagePanel messagePanel = new MessagePanel(profile_path,username,time_sent, 
                content,file_path,is_read);
        messagesPanel.add(messagePanel);
        messagesPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between panels
        messagesPanel.revalidate(); // Recalculate layout
        messagesPanel.repaint();   // Refresh display
        scrollToBottom();
    }
    private void inputFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputFieldKeyPressed
        typingIndicator.setText("Typing...");
    }//GEN-LAST:event_inputFieldKeyPressed

    private void inputFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputFieldKeyReleased
        typingIndicator.setText(" ");
    }//GEN-LAST:event_inputFieldKeyReleased

    private void adminSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminSettingsActionPerformed
        AdminFrame adminFrame = new AdminFrame(); 
        adminFrame.setVisible(true);
    }//GEN-LAST:event_adminSettingsActionPerformed

    private void attachButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attachButtonActionPerformed
        // Choose a file to upload
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(inputPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            attachedFile = selectedFile;
            
            String fileName = selectedFile.getName();
            String filePath = selectedFile.getAbsolutePath();
            String url = selectedFile.toURI().toString();
            
            attachmentPane.setText("<html><body><a href=\"" + url + "\">" + fileName + "</a></body></html>");     
            attachmentPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
                public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                    attachmentPaneHyperlinkUpdate(evt);
                }
            });
        }
    }//GEN-LAST:event_attachButtonActionPerformed

    private void attachmentPaneHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_attachmentPaneHyperlinkUpdate
        // if the attachPreview hyperlink is clicked, perform this
        if (evt.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
            // Open the file
            try {
                if (Desktop.isDesktopSupported()) {
                    // Convert the URL back to a URI and then to a File
                    Desktop.getDesktop().open(new File(evt.getURL().toURI()));
                }
            } catch (IOException | URISyntaxException ex) {
                JOptionPane.showMessageDialog(inputPanel, "Failed to open the file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_attachmentPaneHyperlinkUpdate

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        initPrevMessages(chatID, false);
    }//GEN-LAST:event_formMouseMoved

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        initPrevMessages(chatID, false);
    }//GEN-LAST:event_formMouseEntered

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        initPrevMessages(chatID, false);
    }//GEN-LAST:event_formKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel activeStatus;
    private javax.swing.JPanel adminPanel;
    private javax.swing.JButton adminSettings;
    private javax.swing.JButton attachButton;
    private javax.swing.JEditorPane attachmentPane;
    private javax.swing.JScrollPane chatScrollPane;
    private javax.swing.JPanel chatheaderPanel;
    private javax.swing.JPanel headPanel;
    private javax.swing.JTextField inputField;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JPanel messagesPanel;
    private javax.swing.JButton sendButton;
    private javax.swing.JLabel typingIndicator;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
