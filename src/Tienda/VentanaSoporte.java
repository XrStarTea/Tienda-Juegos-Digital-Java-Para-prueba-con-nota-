package tienda;

import javax.swing.*;
import java.sql.ResultSet;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static java.awt.Component.CENTER_ALIGNMENT;

public class VentanaSoporte extends JPanel {
    
    private CardLayout cardLayout;
    private JPanel panelContenido;
    private JTextArea areaDescripcion;
    private int idUsuarioLogueado; // Recibimos el ID del usuario logueado

    public VentanaSoporte(CardLayout cardLayout, JPanel panelContenido, int idUsuarioLogueado) {
        this.cardLayout = cardLayout;
        this.panelContenido = panelContenido;
        this.idUsuarioLogueado = idUsuarioLogueado; // Asignamos el ID del usuario

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Panel superior con botón "Volver"
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBackground(Color.WHITE);

        JButton botonVolver = new JButton("Volver");
        botonVolver.setFont(new Font("Arial", Font.BOLD, 14));
        botonVolver.setBackground(new Color(0, 123, 255));
        botonVolver.setForeground(Color.WHITE);
        botonVolver.setFocusPainted(false);
        botonVolver.addActionListener(e -> cardLayout.show(panelContenido, "Tienda"));

        panelSuperior.add(botonVolver);
        add(panelSuperior, BorderLayout.NORTH);

        // Panel central para soporte
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Título
        JLabel labelTitulo = new JLabel("Soporte Técnico");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitulo.setAlignmentX(CENTER_ALIGNMENT);
        panelCentral.add(labelTitulo);
        panelCentral.add(Box.createVerticalStrut(20));

        // Área de texto para descripción del problema
        JLabel labelDescripcionTexto = new JLabel("Describa su problema:");
        labelDescripcionTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        labelDescripcionTexto.setAlignmentX(CENTER_ALIGNMENT);
        panelCentral.add(labelDescripcionTexto);
        panelCentral.add(Box.createVerticalStrut(5));

        areaDescripcion = new JTextArea(5, 30);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(areaDescripcion);
        panelCentral.add(scrollDescripcion);
        panelCentral.add(Box.createVerticalStrut(15));

        // Preguntas frecuentes
        JLabel labelFAQ = new JLabel("Preguntas Frecuentes:");
        labelFAQ.setFont(new Font("Arial", Font.BOLD, 16));
        labelFAQ.setAlignmentX(CENTER_ALIGNMENT);
        panelCentral.add(labelFAQ);
        panelCentral.add(Box.createVerticalStrut(5));

        String[] preguntasFrecuentes = {
            "¿Cómo realizo un pedido?",
            "¿Cómo puedo contactar al soporte técnico?",
            "¿Qué métodos de pago están disponibles?",
            "Correo de soporte técnico: back_up@gmail.com",
            "Número de contacto: +569 5555 3322"
        };
        JList<String> listaFAQ = new JList<>(preguntasFrecuentes);
        listaFAQ.setVisibleRowCount(5);
        JScrollPane scrollFAQ = new JScrollPane(listaFAQ);
        panelCentral.add(scrollFAQ);
        panelCentral.add(Box.createVerticalStrut(15));

        // Botón para enviar mensaje al soporte
        JButton botonEnviar = new JButton("Enviar Mensaje");
        botonEnviar.setFont(new Font("Arial", Font.BOLD, 14));
        botonEnviar.setBackground(new Color(40, 167, 69));
        botonEnviar.setForeground(Color.WHITE);
        botonEnviar.setFocusPainted(false);
        botonEnviar.setAlignmentX(CENTER_ALIGNMENT);
        botonEnviar.addActionListener(e -> enviarMensajeSoporte()); // Llamamos al nuevo método
        panelCentral.add(botonEnviar);

        add(panelCentral, BorderLayout.CENTER);
    }

    public void setIdUsuarioLogueado(int idUsuarioLogueado) {
    this.idUsuarioLogueado = idUsuarioLogueado;
    }
    
    private void enviarMensajeSoporte() {
        System.out.println("DEBUG: Intento de enviar mensaje con usuario ID: " + idUsuarioLogueado);

        String descripcion = areaDescripcion.getText().trim();
        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, describa su problema.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = ConexionDB.conectar()) {
            // Verificación EXTENDIDA del usuario (con más información)
            String checkSql = "SELECT id, usuario, rol FROM usuarios WHERE id = ?";
            try (PreparedStatement checkUser = connection.prepareStatement(checkSql)) {
                checkUser.setInt(1, idUsuarioLogueado);
                System.out.println("DEBUG: SQL a ejecutar: " + checkUser.toString());

                try (ResultSet rs = checkUser.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("DEBUG: Usuario encontrado - ID: " + rs.getInt("id") 
                            + ", Nombre: " + rs.getString("usuario")
                            + ", Rol: " + rs.getString("rol"));
                    } else {
                        System.out.println("DEBUG: No se encontró usuario con ID: " + idUsuarioLogueado);
                        JOptionPane.showMessageDialog(this, 
                            "Error: Su sesión no es válida. Por favor, inicie sesión nuevamente.", 
                            "Error de Sesión", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            // Insertar mensaje con validación adicional
            String insertSql = "INSERT INTO soporte_mensajes (id_usuario, descripcion_problema) VALUES (?, ?)";
            try (PreparedStatement insertMessage = connection.prepareStatement(insertSql)) {
                insertMessage.setInt(1, idUsuarioLogueado);
                insertMessage.setString(2, descripcion);

                System.out.println("DEBUG: Intentando insertar mensaje para usuario ID: " + idUsuarioLogueado);

                int filasAfectadas = insertMessage.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("DEBUG: Mensaje insertado correctamente");
                    JOptionPane.showMessageDialog(this, 
                        "Mensaje enviado con éxito. Gracias por contactarnos.", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    areaDescripcion.setText("");
                } else {
                    System.out.println("DEBUG: No se pudo insertar el mensaje");
                    JOptionPane.showMessageDialog(this, 
                        "Error al enviar el mensaje. Intente nuevamente.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            System.err.println("ERROR SQL DETALLADO:");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error técnico al enviar mensaje: " + ex.getMessage(), 
                "Error de Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }
}