package src;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {
    public AdminFrame() {
        setTitle("Admin Panel");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Book Management Button
        JButton bookManagementBtn = new JButton("Book Management");
        bookManagementBtn.addActionListener(e -> openBookManagement());
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(bookManagementBtn, gbc);

        // User Management Button
        JButton userManagementBtn = new JButton("User Management");
        userManagementBtn.addActionListener(e -> openUserManagement());
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(userManagementBtn, gbc);

        // Back Button
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(backBtn, gbc);

        add(mainPanel);
    }

    private void openBookManagement() {
        new BookManagementFrame().setVisible(true);
    }

    private void openUserManagement() {
        new UserManagementFrame().setVisible(true);
    }
}