package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserManagementFrame extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserManagementFrame() {
        setTitle("User Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create table model
        String[] columns = {"Name", "Email", "Phone"};
        tableModel = new DefaultTableModel(columns, 0);
        userTable = new JTable(tableModel);

        // Add sample data
       
        tableModel.addRow(new Object[]{"User 1", "user1@email.com", "123-456-7890"});
        tableModel.addRow(new Object[]{"User 2", "user2@email.com", "098-765-4321"});

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add table with scroll pane
        JScrollPane scrollPane = new JScrollPane(userTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> addUser());
        editButton.addActionListener(e -> editUser());
        deleteButton.addActionListener(e -> deleteUser());
<<<<<<< HEAD
        backButton.addActionListener(e -> {
        	openAdminFrame();
        	dispose();
        	});

        add(mainPanel);
    }
    
    private void openAdminFrame() {
        new AdminFrame().setVisible(true);
    }
    
=======
        backButton.addActionListener(e -> dispose());

        add(mainPanel);
    }

>>>>>>> branch 'master' of https://github.com/igrandes/Bibliogoya.git
    private void addUser() {
        String name = JOptionPane.showInputDialog(this, "Enter user name:");
        if (name != null && !name.trim().isEmpty()) {
            tableModel.addRow(new Object[]{name, "email@example.com", "000-000-0000"});
        }
    }

    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            String newName = JOptionPane.showInputDialog(this, "Enter new user name:",
                    tableModel.getValueAt(selectedRow, 0));
            if (newName != null && !newName.trim().isEmpty()) {
                tableModel.setValueAt(newName, selectedRow, 0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to edit");
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete");
        }
    }
}