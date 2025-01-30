package modelo;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;


public class EnlazarBirt extends JFrame {

    private JEditorPane editorPane;

    public EnlazarBirt() {
        setTitle("Visor de Informe BIRT");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Editor Pane para mostrar el informe
        editorPane = new JEditorPane();
        editorPane.setEditable(false);

        // Scroll para el Editor Pane
        JScrollPane scrollPane = new JScrollPane(editorPane);
        add(scrollPane, BorderLayout.CENTER);

        // Generar y cargar el informe
        generateAndLoadReport();
        setVisible(true);
    }

    private void generateAndLoadReport() {
        IReportEngine engine = null;
        try {
            // Configuración del motor BIRT
            EngineConfig config = new EngineConfig();
            Platform.startup(config);
            IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
            engine = factory.createReportEngine(config);

            // Ruta del informe
            String reportPath = "C:\\Users\\aritz\\eclipseinformes\\EnlaceInforme\\informes\\ejercicio14.rptdesign";

            // Abre el informe
            IReportRunnable report = engine.openReportDesign(reportPath);

            // Crear tarea para ejecutar y renderizar
            IRunAndRenderTask task = engine.createRunAndRenderTask(report);

            // Opciones de renderización en HTML
            String outputFilePath = "C:\\Users\\aritz\\eclipseinformes\\EnlaceInforme\\informes\\ejercicio14.html";
            HTMLRenderOption options = new HTMLRenderOption();
            options.setOutputFileName(outputFilePath);
            options.setOutputFormat("html");

            // Configurar las opciones en la tarea
            task.setRenderOption(options);

            // Ejecutar para crear el archivo HTML
            task.run();

            // Ahora finalizamos la tarea
            task.close();

            // Cargar el informe generado en el editor
            File htmlFile = new File(outputFilePath);
            editorPane.setPage(htmlFile.toURI().toURL());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el bloque finally, nos aseguramos de que se liberen los recursos del motor BIRT
            if (engine != null) {
                engine.destroy();
            }
            // Apagamos la plataforma BIRT
            Platform.shutdown();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EnlazarBirt());
    }
}
