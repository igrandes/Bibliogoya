package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
<<<<<<< HEAD

import java.awt.*;

public class BookManagementFrame extends JFrame {
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public BookManagementFrame() {
        setTitle("Book Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create table model
        String[] columns = {"Name", "Author", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(tableModel);

        // Add sample data
        tableModel.addRow(new Object[]{"Book 1", "Author 1", "Available"});
        tableModel.addRow(new Object[]{"Book 2", "Author 2", "Borrowed"});

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add table with scroll pane
        JScrollPane scrollPane = new JScrollPane(bookTable);
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
        addButton.addActionListener(e -> addBook());
        editButton.addActionListener(e -> editBook());
        deleteButton.addActionListener(e -> deleteBook());
        backButton.addActionListener(e -> {
        	openBookManagment();
        	dispose();
        	});

        add(mainPanel);
    }
    
    private void openBookManagment() {
    	new AdminFrame().setVisible(true);
=======
import java.awt.*;

public class BookManagementFrame extends JFrame {
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public BookManagementFrame() {
        setTitle("Gestion de libros");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create table model
        String[] columns = {"Name", "Author", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        bookTable = new JTable(tableModel);

        // Add sample data
        tableModel.addRow(new Object[]{"Libro 1", "Autor 1", "Disponible"});
        tableModel.addRow(new Object[]{"Libro 2", "Autor 2", "Prestado"});

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add table with scroll pane
        JScrollPane scrollPane = new JScrollPane(bookTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Añadir");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Borrar");
        JButton backButton = new JButton("Volver");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> addBook());
        editButton.addActionListener(e -> editBook());
        deleteButton.addActionListener(e -> deleteBook());
        backButton.addActionListener(e -> dispose());

        add(mainPanel);
>>>>>>> branch 'master' of https://github.com/igrandes/Bibliogoya.git
    }

    private void addBook() {
        String name = JOptionPane.showInputDialog(this, "Introduzca un nuevo nombre de libro:");
        if (name != null && !name.trim().isEmpty()) {
            tableModel.addRow(new Object[]{name, "Nuevo Autor", "Disponible"});
        }
    }

    private void editBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            String newName = JOptionPane.showInputDialog(this, "Introduce un nuevo nombre de libro:",
                    tableModel.getValueAt(selectedRow, 0));
            if (newName != null && !newName.trim().isEmpty()) {
                tableModel.setValueAt(newName, selectedRow, 0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un libro para editar");
        }
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un libro para eliminar");
        }
    }
}