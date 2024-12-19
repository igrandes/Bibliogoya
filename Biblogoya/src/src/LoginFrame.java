package src;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;

    public LoginFrame() {
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

        // Panel Central para el Formulario
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255)); // Fondo celeste claro
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

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

        // Añadir a la Ventana Principal
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Evento del Botón Login
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                if (username.equals("admin") && password.equals("")) {
                    JOptionPane.showMessageDialog(null, "Bienvenido Administrador");
                    new AdminFrame().setVisible(true);
                    dispose();
                } else if (username.equals("user") && password.equals("")) {
                    JOptionPane.showMessageDialog(null, "Bienvenido Usuario");
                    new UserFrame().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}