package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import modelo.HibernateUtil;
import modelo.Libro;
import modelo.Prestamo;
import modelo.SessionManager;

public class UserBorrowedBooks extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTable reservedBooksTable;
    private DefaultTableModel tableModel;

    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font TABLE_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font TABLE_HEADER_FONT = new Font("Arial", Font.BOLD, 14);

    public UserBorrowedBooks() {
        initializeFrame();
        setupComponents();
        loadBorrowedBooks();
    }

    private void initializeFrame() {
        setTitle("Libros Prestados");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void setupComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(createTopPanel(), BorderLayout.NORTH);
        
        setupBorrowedBooksTable();
        JScrollPane scrollPane = new JScrollPane(reservedBooksTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setBackground(PRIMARY_COLOR);

        JLabel titleLabel = new JLabel("Libros Prestados", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        return topPanel;
    }

    private void setupBorrowedBooksTable() {
        String[] columns = {"ID", "Título", "Autor", "Género", "Fecha de Préstamo"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reservedBooksTable = new JTable(tableModel);
        reservedBooksTable.setFont(TABLE_FONT);
        reservedBooksTable.setRowHeight(20);
        reservedBooksTable.getTableHeader().setFont(TABLE_HEADER_FONT);
        reservedBooksTable.setSelectionBackground(new Color(210, 228, 238));
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton backButton = createStyledButton("Volver");
        backButton.addActionListener(e -> {
            new UserFrame().setVisible(true);
            dispose();
        });

        JButton devolverButton = createStyledButton("Devolver");
        devolverButton.addActionListener(this::devolverLibro);

        buttonPanel.add(backButton);
        buttonPanel.add(devolverButton);

        return buttonPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void loadBorrowedBooks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT p FROM Prestamo p JOIN FETCH p.libro l WHERE p.usuario.id = :usuarioId";
            Query<Prestamo> query = session.createQuery(hql, Prestamo.class);
            query.setParameter("usuarioId", SessionManager.getClienteId());
            List<Prestamo> prestamos = query.list();

            tableModel.setRowCount(0);

            for (Prestamo prestamo : prestamos) {
                Libro libro = prestamo.getLibro();
                tableModel.addRow(new Object[]{
                    prestamo.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getGenero(),
                    prestamo.getFechaPrestamo()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al recuperar los libros prestados: " + e.getMessage(),
                "Error de Base de Datos", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void devolverLibro(ActionEvent e) {
        int selectedRow = reservedBooksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un libro para devolver.", 
                "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long prestamoId = (Long) tableModel.getValueAt(selectedRow, 0);
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            
            Prestamo prestamo = session.get(Prestamo.class, prestamoId);
            if (prestamo != null) {
                prestamo.setFechaDevolucion(java.time.LocalDate.now());
                session.update(prestamo);
                transaction.commit();
                
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "El libro ha sido devuelto exitosamente.", 
                    "Devolución exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el préstamo seleccionado.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al devolver el libro: " + ex.getMessage(), 
                "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
