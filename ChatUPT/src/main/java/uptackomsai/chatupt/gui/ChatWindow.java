/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package uptackomsai.chatupt.gui;

/**
 *
 * @author Lei
 */
import uptackomsai.chatupt.network.Client;

import javax.swing.*;
import java.io.IOException;
public class ChatWindow extends javax.swing.JPanel {
    private Client client;
    /**
     * Creates new form ChatWindow
     */
    public ChatWindow(String serverHost, int serverPort, String username) {
        initComponents();
        chatArea.setEditable(false);
        usernameLabel.setText(username);
        // Initialize client and connect to the server
        client = new Client(serverHost, serverPort);
        try {
            client.connect(username);
            new Thread(() -> {
                try {
                    String message;
                    while ((message = client.receiveMessage()) != null) {
                        chatArea.append(message + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to server!", "Error", JOptionPane.ERROR_MESSAGE);
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

        chatHeader = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        chatStatus = new javax.swing.JLabel();
        chatDisplayPanel = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        inputPanel = new javax.swing.JPanel();
        attachButton = new javax.swing.JButton();
        emojiButton = new javax.swing.JButton();
        inputField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setLayout(new java.awt.BorderLayout());

        usernameLabel.setText("User");
        chatHeader.add(usernameLabel);

        chatStatus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chatStatus.setText("is Idle");
        chatHeader.add(chatStatus);

        add(chatHeader, java.awt.BorderLayout.NORTH);

        chatArea.setEditable(false);
        chatArea.setColumns(20);
        chatArea.setLineWrap(true);
        chatArea.setRows(5);
        chatArea.setWrapStyleWord(true);
        chatDisplayPanel.setViewportView(chatArea);

        add(chatDisplayPanel, java.awt.BorderLayout.CENTER);

        inputPanel.setPreferredSize(new java.awt.Dimension(400, 50));

        attachButton.setText("Attach File");
        attachButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        attachButton.setMargin(new java.awt.Insets(2, 5, 3, 5));
        attachButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        inputPanel.add(attachButton);

        emojiButton.setText("Emoji");
        emojiButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        emojiButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        inputPanel.add(emojiButton);

        inputField.setText("Enter Message");
        inputField.setFocusTraversalPolicyProvider(true);
        inputField.setPreferredSize(new java.awt.Dimension(200, 30));
        inputField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inputFieldMouseClicked(evt);
            }
        });
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

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        String message = inputField.getText();
        if (!message.trim().isEmpty()) {
            client.sendMessage(message);
            inputField.setText("");
        }
    }//GEN-LAST:event_sendButtonActionPerformed

    private void inputFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputFieldKeyPressed
        chatStatus.setText("is Typing...");
    }//GEN-LAST:event_inputFieldKeyPressed

    private void inputFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputFieldKeyReleased
        chatStatus.setText("is Idle");
    }//GEN-LAST:event_inputFieldKeyReleased

    private void inputFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inputFieldMouseClicked
        inputField.setText("");
    }//GEN-LAST:event_inputFieldMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton attachButton;
    private javax.swing.JTextArea chatArea;
    private javax.swing.JScrollPane chatDisplayPanel;
    private javax.swing.JPanel chatHeader;
    private javax.swing.JLabel chatStatus;
    private javax.swing.JButton emojiButton;
    private javax.swing.JTextField inputField;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JButton sendButton;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
