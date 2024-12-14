/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uptackomsai.chatupt.gui;

import java.awt.BorderLayout;
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
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import org.mindrot.jbcrypt.BCrypt;
import uptackomsai.chatupt.model.Channel;
import uptackomsai.chatupt.model.Request;
import uptackomsai.chatupt.model.User;
import uptackomsai.chatupt.providers.DbBaseProvider;
import uptackomsai.chatupt.utils.DatabaseUtils;
/**
 *
 * @author Lei
 */
public class MainFrame extends javax.swing.JFrame {
    private final int userID;
    /**
     * Creates new form ChatFrame
     * @param userID
     */
    public MainFrame(int userID) { // UserID is received here instead of username
        this.userID = userID;
        setResizable(false);
        initComponents();
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setTitle("Welcome");
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // from the UserID parameter, get the user's username, last visited ChatWindow, and profile_path
        // stored in the server's database. Then the initializeProfilePanel() should set the usernameItem's text,
        // proficonToggleButton's icon, and add/open the lastvisisted ChatWindow (unless 0, meaning new user).
        
//        usernameItem.setText(username);
        
        initializeAppPanel();
        initializeProfilePanel();
        intializeChannelList();
        intializeOnlineUserList();
        intializeAllUserList();
        
        // Initialize the ChatWindow for testing
//        ChatWindow chatWindow = new ChatWindow("localhost", 12345, username); 
//        add(chatWindow, BorderLayout.CENTER);
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
    
    private void initializeProfilePanel(){
        ResultSet user;
        String profile_path="default.png";
        DatabaseUtils dbUtils = new DatabaseUtils();
        //int lastVisitedChatId=0;
        try {
            user = dbUtils.getUserById(userID);
            if (user.next()) {
                profile_path = user.getString("profile_path");
                usernameItem.setText(user.getString("username"));
                // Initialize the ChatWindow here, user.getString("username"))
                //lastVisitedChatId = user.getInt("last_visited");
            }
            user.close(); // Always close ResultSet
        } catch (SQLException e) {
            System.err.println("Error fetching user: " + e.getMessage());
        }
        proficonToggleButton.setIcon(new ImageIcon(
            ImageLoader.loadImageIcon(profile_path).getImage().getScaledInstance(
            proficonToggleButton.getPreferredSize().width,
            proficonToggleButton.getPreferredSize().height,
            Image.SCALE_SMOOTH)
        ));
    }
    
    private void intializeChannelList(){
        // Temporary Placeholder for channelsPanel
//        for (int i = 1; i <= 2; i++) {
//            addChannelToChannelPanel("Channel "+i,i);
//        }
    }
    
    private void intializeOnlineUserList(){
        // Temporary Placeholder for onlineUsersPanel
//        for (int i = 1; i <= 7; i++) {
//            JButton button = new JButton("User " + i);
//            button.setPreferredSize(new Dimension(160, 30)); // Fixed width of 95, height of 30
//            button.setMaximumSize(new Dimension(160, 30)); // Enforce max size
//            button.setToolTipText("<port_number> "+i); // Maybe we can use this value for getting what port number the conversation is
//            button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align in BoxLayout
//            // Add an ActionListener to the button
//            button.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    // Action to perform on button click
//                    JOptionPane.showMessageDialog(null, button.getToolTipText(), "Alert", JOptionPane.INFORMATION_MESSAGE);
//                    // user should be able open a chatWindow with for the converstion with the associated portnumber of the button
//                }
//            });
//
//            onlineUsersPanel.add(button);
//            onlineUsersPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Add spacing between buttons
//        }
    }
    
    private void intializeAllUserList(){
//        // Temporary Placeholder for allUsersPanel
//        for (int i = 1; i <= 10; i++) {
//            JButton button = new JButton("User " + i);
//            button.setPreferredSize(new Dimension(160, 30)); // Fixed width of 95, height of 30
//            button.setMaximumSize(new Dimension(160, 30)); // Enforce max size
//            button.setToolTipText("<port_number> "+i); // Maybe we can use this value for getting what port number the conversation is
//            button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align in BoxLayout
//            // Add an ActionListener to the button
//            button.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    // Action to perform on button click
//                    JOptionPane.showMessageDialog(null, button.getToolTipText(), "Alert", JOptionPane.INFORMATION_MESSAGE);
//                    // user should be able open a chatWindow with for the converstion with the associated portnumber of the button
//                }
//            });
//            allUsersPanel.add(button);
//            allUsersPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Add spacing between buttons
//        }
    }
    
    private void addChannelToChannelPanel(String channelName,int chatId){
        JButton button = new JButton(channelName);
        button.setPreferredSize(new Dimension(160, 30)); 
        button.setMaximumSize(new Dimension(160, 30));
        button.setToolTipText(""+chatId);
        button.setAlignmentX(Component.CENTER_ALIGNMENT); 
        // Add an ActionListener to the button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to perform on button click
                JOptionPane.showMessageDialog(null, "chat_id: "+button.getToolTipText(), "Alert", JOptionPane.INFORMATION_MESSAGE);
                // user should be able open a chatWindow with for the converstion with the associated portnumber of the button
            }
        });
        channelsPanel.add(button);
        channelsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        channelsPanel.revalidate(); // Recalculate layout
        channelsPanel.repaint(); 
        
        // scroll down
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalBar = channelScrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
        });
    }
    
    public void addChannel(String channelName, boolean isPrivate) {
        try (Socket socket = new Socket("localhost", 12345);
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
                addChannelToChannelPanel(channelName,Integer.parseInt(response));
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
        channelScrollPane.setViewportView(channelsPanel);

        channelsPanel.setLayout(new javax.swing.BoxLayout(channelsPanel, javax.swing.BoxLayout.Y_AXIS));
        channelScrollPane.setViewportView(channelsPanel);

        channelListPanel.add(channelScrollPane, java.awt.BorderLayout.CENTER);

        LeftSidebar.setTopComponent(channelListPanel);

        userListPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        userListPanel.setLayout(new java.awt.BorderLayout());

        onlineUserScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        onlineUsersPanel.setLayout(new javax.swing.BoxLayout(onlineUsersPanel, javax.swing.BoxLayout.Y_AXIS));
        onlineUserScrollPane.setViewportView(onlineUsersPanel);

        jTabbedPane1.addTab("Online", onlineUserScrollPane);

        allUserScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        allUsersPanel.setLayout(new javax.swing.BoxLayout(allUsersPanel, javax.swing.BoxLayout.Y_AXIS));
        allUserScrollPane.setViewportView(allUsersPanel);

        jTabbedPane1.addTab("All", allUserScrollPane);

        userListPanel.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        LeftSidebar.setRightComponent(userListPanel);

        getContentPane().add(LeftSidebar, java.awt.BorderLayout.LINE_START);

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
        
        ProfileManageFrame settingsFrame = new ProfileManageFrame(); 
        settingsFrame.setVisible(true);
    }//GEN-LAST:event_SettingsActionPerformed

    private void LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutActionPerformed
        proficonToggleButton.setSelected(false);
        
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
                new MainFrame(-1).setVisible(true); 
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
