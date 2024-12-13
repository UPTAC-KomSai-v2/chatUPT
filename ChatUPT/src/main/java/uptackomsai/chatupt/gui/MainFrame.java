/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uptackomsai.chatupt.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;//for testing
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import uptackomsai.chatupt.network.Client;

/**
 *
 * @author Lei
 */
public class MainFrame extends javax.swing.JFrame {
    private Client user;
    /**
     * Creates new form ChatFrame
     */
    public MainFrame(String username) { // for testing, I think UserID would be better instead of username
        initComponents();
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setTitle("ChatUPT");
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        usernameItem.setText(username);
        
        intializeChannelList();
        intializeOnlineUserList();
        intializeAllUserList();
        
        // Initialize the ChatWindow
        ChatWindow chatWindow = new ChatWindow("localhost", 12345, username); 
        add(chatWindow, BorderLayout.CENTER);
    }
    
    private void intializeChannelList(){
        // Temporary Placeholder for channelsPanel
        for (int i = 1; i <= 10; i++) {
            JButton button = new JButton("Channel " + i);
            button.setPreferredSize(new Dimension(160, 30)); // Fixed width of 95, height of 30
            button.setMaximumSize(new Dimension(160, 30)); // Enforce max size
            button.setToolTipText("<port_number> "+i); // Maybe we can use this value for getting what port number the conversation is
            button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align in BoxLayout
            // Add an ActionListener to the button
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Action to perform on button click
                    JOptionPane.showMessageDialog(null, button.getToolTipText(), "Alert", JOptionPane.INFORMATION_MESSAGE);
                    // user should be able open a chatWindow with for the converstion with the associated portnumber of the button
                }
            });
            channelsPanel.add(button);
            channelsPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Add spacing between buttons
        }
    }
    
    private void intializeOnlineUserList(){
        // Temporary Placeholder for onlineUsersPanel
        for (int i = 1; i <= 7; i++) {
            JButton button = new JButton("User " + i);
            button.setPreferredSize(new Dimension(160, 30)); // Fixed width of 95, height of 30
            button.setMaximumSize(new Dimension(160, 30)); // Enforce max size
            button.setToolTipText("<port_number> "+i); // Maybe we can use this value for getting what port number the conversation is
            button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align in BoxLayout
            // Add an ActionListener to the button
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Action to perform on button click
                    JOptionPane.showMessageDialog(null, button.getToolTipText(), "Alert", JOptionPane.INFORMATION_MESSAGE);
                    // user should be able open a chatWindow with for the converstion with the associated portnumber of the button
                }
            });

            onlineUsersPanel.add(button);
            onlineUsersPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Add spacing between buttons
        }
    }
    
    private void intializeAllUserList(){
        // Temporary Placeholder for allUsersPanel
        for (int i = 1; i <= 10; i++) {
            JButton button = new JButton("User " + i);
            button.setPreferredSize(new Dimension(160, 30)); // Fixed width of 95, height of 30
            button.setMaximumSize(new Dimension(160, 30)); // Enforce max size
            button.setToolTipText("<port_number> "+i); // Maybe we can use this value for getting what port number the conversation is
            button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align in BoxLayout
            // Add an ActionListener to the button
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Action to perform on button click
                    JOptionPane.showMessageDialog(null, button.getToolTipText(), "Alert", JOptionPane.INFORMATION_MESSAGE);
                    // user should be able open a chatWindow with for the converstion with the associated portnumber of the button
                }
            });
            allUsersPanel.add(button);
            allUsersPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Add spacing between buttons
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
        channelListLabel = new javax.swing.JLabel();
        channelScrollPane = new javax.swing.JScrollPane();
        channelsPanel = new javax.swing.JPanel();
        userListPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        onlineUserScrollPane = new javax.swing.JScrollPane();
        onlineUsersPanel = new javax.swing.JPanel();
        allUserScrollPane = new javax.swing.JScrollPane();
        allUsersPanel = new javax.swing.JPanel();

        popupMenu.setComponentPopupMenu(popupMenu);

        usernameItem.setText("jMenuItem1");
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
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        headPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        headPanel.setPreferredSize(new java.awt.Dimension(800, 50));
        headPanel.setLayout(new java.awt.BorderLayout());

        appiconLabel.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        appiconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        appiconLabel.setText("<logo>");
        appiconLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        appiconLabel.setPreferredSize(new java.awt.Dimension(40, 40));
        appPanel.add(appiconLabel);

        appnameLabel.setText("ChatUPT");
        appPanel.add(appnameLabel);

        headPanel.add(appPanel, java.awt.BorderLayout.LINE_START);

        proficonToggleButton.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        proficonToggleButton.setText("<prof>");
        proficonToggleButton.setBorder(null);
        proficonToggleButton.setComponentPopupMenu(popupMenu);
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
        LeftSidebar.setEnabled(false);
        LeftSidebar.setPreferredSize(new java.awt.Dimension(200, 0));

        channelListPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        channelListPanel.setLayout(new java.awt.BorderLayout());

        channelListLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        channelListLabel.setText("Channels");
        channelListPanel.add(channelListLabel, java.awt.BorderLayout.PAGE_START);

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
            popupMenu.show(proficonToggleButton, -proficonToggleButton.getWidth(), proficonToggleButton.getHeight());
        } else {
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame("user").setVisible(true); // for testing, temporary parameter username
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
    private javax.swing.JLabel channelListLabel;
    private javax.swing.JPanel channelListPanel;
    private javax.swing.JScrollPane channelScrollPane;
    private javax.swing.JPanel channelsPanel;
    private javax.swing.JPanel headPanel;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JScrollPane onlineUserScrollPane;
    private javax.swing.JPanel onlineUsersPanel;
    private javax.swing.JPopupMenu popupMenu;
    private javax.swing.JToggleButton proficonToggleButton;
    private javax.swing.JPanel profilePanel;
    private javax.swing.JPanel userListPanel;
    private javax.swing.JMenuItem usernameItem;
    // End of variables declaration//GEN-END:variables
}
