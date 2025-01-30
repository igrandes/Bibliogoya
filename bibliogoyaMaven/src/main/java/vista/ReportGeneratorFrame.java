package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

public class ReportGeneratorFrame extends JFrame {

    private JButton btnBooksLoanedReport;
    private JButton btnTopUsersReport;
    private JButton btnPopularBooksReport;
    private JButton btnFinancialReport;
    private JButton btnGenreTrendsReport;
    private JButton btnBackToAdminFrame;

    public ReportGeneratorFrame() {
        setTitle("Generador de Informes");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Panel superior (estilo similar al AdminFrame)
        add(createTopPanel(), BorderLayout.NORTH);

        // Panel central con los botones
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior (estilo similar al AdminFrame)
        add(createBottomPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // Creación del panel superior
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(70, 130, 180)); // Azul acero
        JLabel titleLabel = new JLabel("📚 Generador de Informes");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel);
        return topPanel;
    }

    // Creación del panel central con los botones
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255)); // Fondo celeste claro
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addButton(centerPanel, gbc, "Informe de libros prestados", 0, this::generateBooksLoanedReport);
        addButton(centerPanel, gbc, "Informe de usuarios con más libros prestados", 1, this::generateTopUsersReport);
        addButton(centerPanel, gbc, "Informe de libros más populares", 2, this::generatePopularBooksReport);
        addButton(centerPanel, gbc, "Reporte financiero", 3, this::generateFinancialReport);
        addButton(centerPanel, gbc, "Tendencias por género literario", 4, this::generateGenreTrendsReport);
        addButton(centerPanel, gbc, "Volver", 5, this::goBackToAdminFrame);

        return centerPanel;
    }

    // Creación del panel inferior
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(70, 130, 180));
        JLabel footer = new JLabel("© 2024 Biblioteca Goya");
        footer.setForeground(Color.WHITE);
        bottomPanel.add(footer);
        return bottomPanel;
    }

    // Método para añadir botones al panel central
    private void addButton(JPanel panel, GridBagConstraints gbc, String text, int y, Runnable action) {
        JButton button = createStyledButton(text);
        button.addActionListener(e -> action.run());
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(button, gbc);
    }

    // Método para crear botones con estilo consistente
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    // Lógica de generación de informes utilizando BIRT con formato PDF y guardado en "InformesGenerados"
    private void generateBooksLoanedReport() {
        generateBIRTReport("birt/InformeDeLibrosPrestados.rptdesign", "Informe de Libros Prestados", "InformesGenerados/InformeDeLibrosPrestados.pdf");
    }

    private void generateTopUsersReport() {
        generateBIRTReport("birt/InformeDeUsuariosConMasLibrosPrestados.rptdesign", "Informe de Usuarios Top", "InformesGenerados/InformeDeUsuariosConMasLibrosPrestados.pdf");
    }

    private void generatePopularBooksReport() {
        generateBIRTReport("birt/InformeDeLibrosMasPopulares.rptdesign", "Informe de Libros Populares", "InformesGenerados/InformeDeLibrosMasPopulares.pdf");
    }

    private void generateFinancialReport() {
        generateBIRTReport("birt/ReporteFinanciero.rptdesign", "Reporte Financiero", "InformesGenerados/ReporteFinanciero.pdf");
    }

    private void generateGenreTrendsReport() {
        generateBIRTReport("birt/TendenciasPorGeneroLiterario.rptdesign", "Tendencias por Género Literario", "InformesGenerados/TendenciasPorGeneroLiterario.pdf");
    }

    // Método que genera informes utilizando el motor BIRT con formato PDF y guardados en el paquete "InformesGenerados"
    private void generateBIRTReport(String relativeReportPath, String reportTitle, String relativeOutputPath) {
        IReportEngine engine = null;
        try {
            // Configuración del motor BIRT
            EngineConfig config = new EngineConfig();
            Platform.startup(config);
            IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
            engine = factory.createReportEngine(config);

            // Obtener la ruta absoluta del diseño del informe desde el directorio de recursos
            String reportPath = new File(getClass().getClassLoader().getResource(relativeReportPath).toURI()).getAbsolutePath();
            IReportRunnable report = engine.openReportDesign(reportPath);

            // Crear y configurar la tarea para ejecutar y renderizar
            IRunAndRenderTask task = engine.createRunAndRenderTask(report);

            // Configuración de salida del informe en formato PDF
            String outputFilePath = new File(relativeOutputPath).getAbsolutePath();
            PDFRenderOption options = new PDFRenderOption();
            options.setOutputFileName(outputFilePath);
            options.setOutputFormat("pdf");

            // Ejecutar y generar el informe
            task.setRenderOption(options);
            task.run();
            task.close();

            // Mostrar mensaje al usuario
            JOptionPane.showMessageDialog(this, "Informe '" + reportTitle + "' generado correctamente en: " + outputFilePath);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar el informe: " + e.getMessage());
        } finally {
            if (engine != null) {
                engine.destroy();
            }
            Platform.shutdown();
        }
    }

    // Método para regresar al AdminFrame
    private void goBackToAdminFrame() {
        SwingUtilities.invokeLater(() -> {
            new AdminFrame().setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        new ReportGeneratorFrame();
    }
}
