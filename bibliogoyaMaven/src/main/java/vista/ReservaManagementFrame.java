package vista;



import javax.swing.*;

import javax.swing.table.DefaultTableModel;

import java.awt.*;

import java.io.Serializable;

import java.time.LocalDate;

import java.util.List;

import org.hibernate.Session;

import org.hibernate.Transaction;

import modelo.HibernateUtil;
import modelo.Libro;
import modelo.Prestamo;
import modelo.Usuario;



public class ReservaManagementFrame extends JFrame implements Serializable {

    private static final long serialVersionUID = 1L;

    private JTable reservaTable;

    private DefaultTableModel tableModel;



    public ReservaManagementFrame() {

        initializeFrame();

        setupComponents();

        loadReservas();

    }



    private void initializeFrame() {

        setTitle("Gestión de Reservas");

        setSize(900, 500);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

    }



    private void setupComponents() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.setBackground(Color.WHITE);



        JPanel topPanel = createTopPanel();

        setupReservaTable();

        JScrollPane scrollPane = new JScrollPane(reservaTable);

        mainPanel.add(scrollPane, BorderLayout.CENTER);



        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);



        add(topPanel, BorderLayout.NORTH);

        add(mainPanel, BorderLayout.CENTER);

    }



    private JPanel createTopPanel() {

        JPanel topPanel = new JPanel();

        topPanel.setBackground(new Color(70, 130, 180));

        JLabel titleLabel = new JLabel("📚 Gestión de Reservas", SwingConstants.CENTER);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        titleLabel.setForeground(Color.WHITE);

        topPanel.add(titleLabel);

        return topPanel;

    }



    private void setupReservaTable() {

        String[] columns = {"ID", "Libro", "Autor", "Fecha Reserva", "Reservado por"};

        tableModel = new DefaultTableModel(columns, 0) {

            @Override

            public boolean isCellEditable(int row, int column) {

                return false;

            }

        };

        reservaTable = new JTable(tableModel);

        reservaTable.setFont(new Font("Arial", Font.PLAIN, 14));

        reservaTable.setRowHeight(20);

        reservaTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        reservaTable.setSelectionBackground(new Color(210, 228, 238));

    }



    private JPanel createButtonPanel() {

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        buttonPanel.setBackground(Color.WHITE);



        JButton deleteButton = createStyledButton("Eliminar");

        JButton backButton = createStyledButton("Volver");



        deleteButton.addActionListener(e -> deleteReserva());

        backButton.addActionListener(e -> {

            new AdminFrame().setVisible(true);

            dispose();

        });



        buttonPanel.add(deleteButton);

        buttonPanel.add(backButton);



        return buttonPanel;

    }



    private JButton createStyledButton(String text) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 16));

        button.setBackground(new Color(70, 130, 180));

        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);

        return button;

    }



    private void loadReservas() {

        tableModel.setRowCount(0);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql = "SELECT p FROM Prestamo p JOIN FETCH p.libro l JOIN FETCH p.usuario u";

            List<Prestamo> prestamos = session.createQuery(hql, Prestamo.class).list();



            for (Prestamo prestamo : prestamos) {

                tableModel.addRow(new Object[]{

                    prestamo.getLibro().getId(),

                    prestamo.getLibro().getTitulo(),

                    prestamo.getLibro().getAutor(),

                    prestamo.getFechaPrestamo(),

                    prestamo.getUsuario().getNombre()

                });

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,

                "Error al cargar los préstamos: " + e.getMessage(),

                "Error de Base de Datos",

                JOptionPane.ERROR_MESSAGE);

        }

    }



    private void deleteReserva() {

        int selectedRow = reservaTable.getSelectedRow();

        if (selectedRow < 0) {

            JOptionPane.showMessageDialog(this, "Por favor, seleccione un préstamo para eliminar");

            return;

        }



        Long libroID = (Long) tableModel.getValueAt(selectedRow, 0);



        int confirm = JOptionPane.showConfirmDialog(this,

            "¿Está seguro de que desea eliminar este préstamo?",

            "Confirmar eliminación",

            JOptionPane.YES_NO_OPTION);



        if (confirm == JOptionPane.YES_OPTION) {

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {

                Transaction transaction = session.beginTransaction();

                try {

                    String hql = "FROM Prestamo p WHERE p.libro.id = :libroID";

                    Prestamo prestamo = session.createQuery(hql, Prestamo.class)

                                               .setParameter("libroID", libroID)

                                               .uniqueResult();



                    if (prestamo != null) {

                        prestamo.getLibro().setDisponibilidad(true);

                        session.merge(prestamo.getLibro());

                        session.remove(prestamo);

                        transaction.commit();

                        loadReservas();

                        JOptionPane.showMessageDialog(this, "Préstamo eliminado con éxito y libro actualizado");

                    } else {

                        JOptionPane.showMessageDialog(this, "No se encontró un préstamo con este libroID");

                    }

                } catch (Exception e) {

                    transaction.rollback();

                    JOptionPane.showMessageDialog(this, "Error al eliminar el préstamo: " + e.getMessage(),

                            "Error", JOptionPane.ERROR_MESSAGE);

                }

            }

        }

    }

}