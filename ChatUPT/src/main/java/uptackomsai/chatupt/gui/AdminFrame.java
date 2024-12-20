/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uptackomsai.chatupt.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Lei
 */
public class AdminFrame extends javax.swing.JFrame {

    /**
     * Creates new form AdminFrame
     */
    public AdminFrame() {
        setUndecorated(true); 
        setResizable(false);
        
        initComponents();
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setTitle("ChatUPT");
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        borderPanel = new javax.swing.JPanel();
        headPanel = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        headerLabel = new javax.swing.JLabel();
        isPrivateCheckbox = new javax.swing.JCheckBox();
        deleteChannelButton = new javax.swing.JButton();
        closePanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        listPanel = new javax.swing.JPanel();
        listTabbedPanel = new javax.swing.JTabbedPane();
        adminListPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        adminList = new javax.swing.JList<>();
        adminActionPanel = new javax.swing.JPanel();
        removeAdminButton = new javax.swing.JButton();
        memberListPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        memberList = new javax.swing.JList<>();
        memberActionPanel = new javax.swing.JPanel();
        newadminButton = new javax.swing.JButton();
        removememberButton = new javax.swing.JButton();
        requestListPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        memberList1 = new javax.swing.JList<>();
        requestActionPanel = new javax.swing.JPanel();
        acceptRequestButton = new javax.swing.JButton();
        rejectRequestButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        borderPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        borderPanel.setLayout(new java.awt.BorderLayout());

        headPanel.setPreferredSize(new java.awt.Dimension(396, 40));
        headPanel.setLayout(new java.awt.BorderLayout());

        header.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 10));

        headerLabel.setText("Channel Settings");
        header.add(headerLabel);

        isPrivateCheckbox.setText("Private");
        header.add(isPrivateCheckbox);

        deleteChannelButton.setText("Delete Channel");
        deleteChannelButton.setToolTipText("");
        header.add(deleteChannelButton);

        headPanel.add(header, java.awt.BorderLayout.CENTER);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        closePanel.add(closeButton);

        headPanel.add(closePanel, java.awt.BorderLayout.LINE_END);

        borderPanel.add(headPanel, java.awt.BorderLayout.PAGE_START);

        listPanel.setLayout(new java.awt.BorderLayout());

        adminListPanel.setLayout(new java.awt.BorderLayout());

        adminList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Admin1" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(adminList);

        adminListPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        adminActionPanel.setPreferredSize(new java.awt.Dimension(396, 40));
        adminActionPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        removeAdminButton.setText("Remove Admin");
        adminActionPanel.add(removeAdminButton);

        adminListPanel.add(adminActionPanel, java.awt.BorderLayout.PAGE_END);

        listTabbedPanel.addTab("Admin List", adminListPanel);

        memberListPanel.setLayout(new java.awt.BorderLayout());

        memberList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Member1", "Member2" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(memberList);

        memberListPanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        memberActionPanel.setPreferredSize(new java.awt.Dimension(396, 40));
        memberActionPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        newadminButton.setText("Add as Admin");
        memberActionPanel.add(newadminButton);

        removememberButton.setText("Remove Member");
        memberActionPanel.add(removememberButton);

        memberListPanel.add(memberActionPanel, java.awt.BorderLayout.PAGE_END);

        listTabbedPanel.addTab("Member List", memberListPanel);

        requestListPanel.setLayout(new java.awt.BorderLayout());

        memberList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Request1", "Request2", "Request3", " " };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(memberList1);

        requestListPanel.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        requestActionPanel.setPreferredSize(new java.awt.Dimension(396, 40));
        requestActionPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        acceptRequestButton.setText("Accept Request");
        requestActionPanel.add(acceptRequestButton);

        rejectRequestButton.setText("Reject Request");
        requestActionPanel.add(rejectRequestButton);

        requestListPanel.add(requestActionPanel, java.awt.BorderLayout.PAGE_END);

        listTabbedPanel.addTab("Request List", requestListPanel);

        listPanel.add(listTabbedPanel, java.awt.BorderLayout.CENTER);

        borderPanel.add(listPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(borderPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

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
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new AdminFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptRequestButton;
    private javax.swing.JPanel adminActionPanel;
    private javax.swing.JList<String> adminList;
    private javax.swing.JPanel adminListPanel;
    private javax.swing.JPanel borderPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel closePanel;
    private javax.swing.JButton deleteChannelButton;
    private javax.swing.JPanel headPanel;
    private javax.swing.JPanel header;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JCheckBox isPrivateCheckbox;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel listPanel;
    private javax.swing.JTabbedPane listTabbedPanel;
    private javax.swing.JPanel memberActionPanel;
    private javax.swing.JList<String> memberList;
    private javax.swing.JList<String> memberList1;
    private javax.swing.JPanel memberListPanel;
    private javax.swing.JButton newadminButton;
    private javax.swing.JButton rejectRequestButton;
    private javax.swing.JButton removeAdminButton;
    private javax.swing.JButton removememberButton;
    private javax.swing.JPanel requestActionPanel;
    private javax.swing.JPanel requestListPanel;
    // End of variables declaration//GEN-END:variables
}
