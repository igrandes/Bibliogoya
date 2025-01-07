package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    // Atributos
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;
    
    String username = "Mikel";
    String password = "Garcia";
    String USER = "root";
    String PASSWORD = "root";
    String URL = "jdbc:mysql://localhost:3306/biblioteca";

    // Constructor
    public LoginFrame() {
        // Configuración de Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configuración de la ventana principal
        setTitle("Biblioteca Goya - Inicio de Sesión");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel Superior con Logo
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(70, 130, 180)); // Azul acero
        JLabel logoLabel = new JLabel("📚 Biblioteca Goya");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(logoLabel);

        // Panel Central para el Formulario de Inicio de Sesión
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255)); // Fondo celeste claro
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Campos del formulario
        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        userField = new JTextField(15);

        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passField = new JPasswordField(15);

        loginButton = new JButton("Iniciar Sesión");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(100, 149, 237)); // Azul cornflower
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createRaisedBevelBorder());

        // Añadir componentes al panel central
        gbc.gridx = 0; gbc.gridy = 0; centerPanel.add(userLabel, gbc);
        gbc.gridx = 1; centerPanel.add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; centerPanel.add(passLabel, gbc);
        gbc.gridx = 1; centerPanel.add(passField, gbc);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 2; centerPanel.add(loginButton, gbc);

        // Panel Inferior
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(70, 130, 180));
        JLabel footer = new JLabel("© 2024 Biblioteca");
        footer.setForeground(Color.WHITE);
        bottomPanel.add(footer);

        // Añadir todo a la ventana principal
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Evento del Botón de Login
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                username = userField.getText();
                password = new String(passField.getPassword());

                // Conexión a la base de datos para hacer el login
                try {
                    Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                    recuperarDatos(conn);
                } catch (SQLException a) {
                    a.printStackTrace();
                }
            }
        });

        // Hacer visible la ventana
        setVisible(true);
    }

    // Método para recuperar los datos de la base de datos
    private void recuperarDatos(Connection conn) throws SQLException {
        String sql = "SELECT nombre, apellidos, rol FROM usuarios";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            if (username.equals(rs.getString("nombre")) && password.equals(rs.getString("apellidos"))) {
                if (rs.getString("rol").equals("Administrador")) {
                    System.out.println("Administrador encontrado exitosamente");
                    System.out.println("Logeado");
                    new AdminFrame().setVisible(true);
                    dispose();
                    } else {
                    System.out.println("Usuario encontrado exitosamente");
                    System.out.println("Logeado");
                    new UserFrame();
                    break;
                }
            }
        }
    }
   

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
