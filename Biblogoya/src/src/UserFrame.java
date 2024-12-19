package src;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UserFrame extends JFrame {
    private JTable bookTable;
    private static DefaultTableModel tableModel;
    private static String url = "jdbc:mysql://localhost/biblioteca";
    private static String user = "root";
    private static String passwd = "root";

    public UserFrame(){
        setTitle("User View - Available Books");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create table model
        String[] columns = {"Name", "Author", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(tableModel);

        // Add sample data
        try (Connection c = DriverManager.getConnection(url, user, passwd)) {
            recuperarDatos(c);

        } catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
     
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add table with scroll pane
        JScrollPane scrollPane = new JScrollPane(bookTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton borrowButton = new JButton("Borrow");
        JButton viewUnavailableButton = new JButton("View Unavailable");
        JButton backButton = new JButton("Back");

        buttonPanel.add(borrowButton);
        buttonPanel.add(viewUnavailableButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        borrowButton.addActionListener(e -> borrowBook());
        viewUnavailableButton.addActionListener(e -> viewUnavailableBooks());
        backButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });

        add(mainPanel);
    }

    private void borrowBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            if ("Available".equals(tableModel.getValueAt(selectedRow, 2))) {
                tableModel.setValueAt("Borrowed", selectedRow, 2);
                JOptionPane.showMessageDialog(this, "Book borrowed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Book is not available");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a book to borrow");
        }
    }

    private void viewUnavailableBooks() {
        JFrame unavailableFrame = new JFrame("Unavailable Books");
        unavailableFrame.setSize(600, 400);
        unavailableFrame.setLocationRelativeTo(null);

        DefaultTableModel unavailableModel = new DefaultTableModel(
            new String[]{"Name", "Author", "Status"}, 0);
        JTable unavailableTable = new JTable(unavailableModel);
        
        // Add sample unavailable books
        unavailableModel.addRow(new Object[]{"Book 3", "Author 3", "Borrowed"});
        unavailableModel.addRow(new Object[]{"Book 4", "Author 4", "Reserved"});

        unavailableFrame.add(new JScrollPane(unavailableTable));
        unavailableFrame.setVisible(true);
    }
    private static void recuperarDatos(Connection c) throws SQLException {
    	String selectQuery = "SELECT * FROM usuarios";
    	try(PreparedStatement pst = c.prepareStatement(selectQuery)) {
    		ResultSet rs = pst.executeQuery();
    		String DNI = rs.getString("DNI");
    		String nombre = rs.getString("Nombre");
    		String rol = rs.getString("Rol");
    		System.out.println("DNI");
    		   tableModel.addRow(new Object[]{DNI, nombre, rol});

    	}
    	
    }
}