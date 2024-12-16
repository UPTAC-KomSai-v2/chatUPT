/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uptackomsai.chatupt.gui;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

import io.github.cdimascio.dotenv.Dotenv;
import uptackomsai.chatupt.model.Request;
import uptackomsai.chatupt.model.User;

/**
 *
 * @author Lei
 */
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import uptackomsai.chatupt.utils.DatabaseUtils;
import uptackomsai.chatupt.utils.ImageLoader;
public class LoginFrame extends javax.swing.JFrame {
    private String serverHost = "localhost";
    /**
     * Creates new form LoginFrame
     */
    public LoginFrame() {
        
        
        // Proceed with the application using the provided serverHost
        System.out.println("Server Host: " + serverHost);
        
        setResizable(false);
        
        initComponents();
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setTitle("Login Form");
        setLocationRelativeTo(null);
        
        logoLabel.setIcon(new ImageIcon(
            ImageLoader.loadImageIcon("login_slogan.png").getImage().getScaledInstance(
            logoLabel.getPreferredSize().width,
            logoLabel.getPreferredSize().height,
            Image.SCALE_SMOOTH)
        ));
        
        
        passwordField.addActionListener(e -> login()); 
    }
    
    private void login(){
        String username = usernameTextField.getText();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Socket socket = new Socket(serverHostField.getText(), 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Create a User object and a Message object for the login request
            Gson gson = new Gson();
            User user = new User(username, password, null); // Email is not required for login
            String userJson = gson.toJson(user);
            Request message = new Request("login", userJson); // Use the Message class for consistency
            String jsonMessage = gson.toJson(message);

            // Send login request
            out.println(jsonMessage);

            // Read server response
            String response = in.readLine();

            if (response == null) {
                JOptionPane.showMessageDialog(this, "No response from server.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (response.equalsIgnoreCase("Invalid username or password")||
                    response.equalsIgnoreCase("Account already logged in")) {
                JOptionPane.showMessageDialog(this, response, "Login Failed", JOptionPane.ERROR_MESSAGE);
            } else {
                MainFrame mainFrame = new MainFrame(serverHostField.getText(),Integer.parseInt(response)); // userID parameter from response
                mainFrame.setVisible(true);
                this.dispose();
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
        logoLabel = new javax.swing.JLabel();
        inputPanel = new javax.swing.JPanel();
        center = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        serverHostField = new javax.swing.JTextField();
        usernameLabel = new javax.swing.JLabel();
        usernameTextField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        loginButton = new javax.swing.JButton();
        leftPadding = new javax.swing.JPanel();
        rightPadding = new javax.swing.JPanel();
        footPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        signupButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        headPanel.setBackground(new java.awt.Color(255, 255, 255));
        headPanel.setPreferredSize(new java.awt.Dimension(400, 150));
        headPanel.setLayout(new java.awt.BorderLayout());

        logoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoLabel.setPreferredSize(new java.awt.Dimension(450, 150));
        headPanel.add(logoLabel, java.awt.BorderLayout.CENTER);

        getContentPane().add(headPanel, java.awt.BorderLayout.PAGE_START);

        inputPanel.setPreferredSize(new java.awt.Dimension(400, 185));
        inputPanel.setLayout(new java.awt.BorderLayout());

        center.setPreferredSize(new java.awt.Dimension(250, 185));
        center.setLayout(new java.awt.GridLayout(6, 0, 0, 5));

        jLabel2.setText("Server Host");
        jLabel2.setToolTipText("");
        jPanel1.add(jLabel2);

        serverHostField.setText("localhost");
        serverHostField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverHostFieldActionPerformed(evt);
            }
        });
        jPanel1.add(serverHostField);

        center.add(jPanel1);

        usernameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        usernameLabel.setText("Username ");
        usernameLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        center.add(usernameLabel);

        usernameTextField.setPreferredSize(new java.awt.Dimension(250, 30));
        center.add(usernameTextField);

        passwordLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        passwordLabel.setText("Password");
        passwordLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        center.add(passwordLabel);

        passwordField.setPreferredSize(new java.awt.Dimension(250, 30));
        center.add(passwordField);

        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        center.add(loginButton);

        inputPanel.add(center, java.awt.BorderLayout.CENTER);

        leftPadding.setPreferredSize(new java.awt.Dimension(80, 100));
        leftPadding.setRequestFocusEnabled(false);
        inputPanel.add(leftPadding, java.awt.BorderLayout.WEST);

        rightPadding.setPreferredSize(new java.awt.Dimension(80, 10));
        inputPanel.add(rightPadding, java.awt.BorderLayout.EAST);

        getContentPane().add(inputPanel, java.awt.BorderLayout.CENTER);

        footPanel.setPreferredSize(new java.awt.Dimension(400, 50));

        jLabel1.setText("No Account?");
        footPanel.add(jLabel1);

        signupButton.setText("Signup");
        signupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signupButtonActionPerformed(evt);
            }
        });
        footPanel.add(signupButton);

        getContentPane().add(footPanel, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void signupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signupButtonActionPerformed
        SignupFrame signupFrame = new SignupFrame(serverHostField.getText()); // Open SettingsFrame
        signupFrame.setVisible(true); // Show the new frame
        this.dispose(); 
    }//GEN-LAST:event_signupButtonActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        login();
    }//GEN-LAST:event_loginButtonActionPerformed

    private void serverHostFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serverHostFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_serverHostFieldActionPerformed

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
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        try {
            // Set the FlatLaf Look and Feel
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        

        // Initialize the login screen or main application logic
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(); // Pass serverHost to login
            loginFrame.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel center;
    private javax.swing.JPanel footPanel;
    private javax.swing.JPanel headPanel;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel leftPadding;
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPanel rightPadding;
    private javax.swing.JTextField serverHostField;
    private javax.swing.JButton signupButton;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameTextField;
    // End of variables declaration//GEN-END:variables
}
