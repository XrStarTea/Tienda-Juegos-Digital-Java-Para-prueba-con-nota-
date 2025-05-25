package tienda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PanelInicioSesion extends JPanel {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIniciar;
    private JButton btnVolver;
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private JFrame ventanaPrincipal;
    private JFrame ventanaAdmin;

    public PanelInicioSesion(CardLayout layout, JPanel contenedor, JFrame ventanaPrin, JFrame ventanaAdm) {
        this.cardLayout = layout;
        this.panelContenedor = contenedor;
        this.ventanaPrincipal = ventanaPrin;
        this.ventanaAdmin = ventanaAdm;

        // **1. Diseño principal con GridBagLayout para mayor flexibilidad**
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240)); // Un gris más claro para el fondo
        setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50)); // Más padding

        // **2. Definición de fuentes y colores (tema más consistente)**
        Font fontTitulo = new Font("Segoe UI", Font.BOLD, 24);
        Font fontLabels = new Font("Segoe UI", Font.PLAIN, 16);
        Font fontCampos = new Font("Segoe UI", Font.PLAIN, 16);
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 15);

        Color colorPrimario = new Color(52, 152, 219); // Azul para botones
        Color colorSecundario = new Color(149, 165, 166); // Gris para "Volver"
        Color colorTextoClaro = Color.WHITE;
        Color colorBordeCampo = new Color(180, 180, 180);

        // **3. Componentes del UI**
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5); // Margen entre componentes

        // Título
        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(fontTitulo);
        lblTitulo.setForeground(new Color(44, 62, 80)); // Azul oscuro casi negro
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitulo, gbc);

        // Separador visual
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(200, 2));
        separator.setForeground(new Color(200, 200, 200));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 5, 20, 5); // Menos espacio arriba, más abajo
        add(separator, gbc);
        gbc.insets = new Insets(10, 5, 10, 5); // Restaurar insets

        // Etiqueta Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(fontLabels);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Vuelve a una columna
        gbc.anchor = GridBagConstraints.WEST; // Alineado a la izquierda
        add(lblUsuario, gbc);

        // Campo de texto Usuario
        txtUsuario = new JTextField(20); // Tamaño preferido para el campo
        txtUsuario.setFont(fontCampos);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBordeCampo, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8))); // Borde y padding interno
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Rellena el espacio horizontalmente
        add(txtUsuario, gbc);

        // Etiqueta Contraseña
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(fontLabels);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE; // No rellena
        gbc.anchor = GridBagConstraints.WEST;
        add(lblContrasena, gbc);

        // Campo de Contraseña
        txtContrasena = new JPasswordField(20);
        txtContrasena.setFont(fontCampos);
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBordeCampo, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtContrasena, gbc);

        // Panel de botones (para centrarlos o agruparlos)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0)); // Espacio entre botones
        panelBotones.setOpaque(false); // Para que se vea el fondo del padre

        btnVolver = new JButton("Volver");
        btnVolver.setFont(fontBotones);
        btnVolver.setBackground(colorSecundario);
        btnVolver.setForeground(colorTextoClaro);
        btnVolver.setFocusPainted(false); // Quitar el borde de foco
        btnVolver.setBorderPainted(false); // Quitar el borde nativo
        btnVolver.setPreferredSize(new Dimension(100, 35)); // Tamaño consistente
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de mano
        panelBotones.add(btnVolver);

        btnIniciar = new JButton("Iniciar Sesión");
        btnIniciar.setFont(fontBotones);
        btnIniciar.setBackground(colorPrimario);
        btnIniciar.setForeground(colorTextoClaro);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setBorderPainted(false);
        btnIniciar.setPreferredSize(new Dimension(120, 35)); // Tamaño consistente
        btnIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelBotones.add(btnIniciar);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 5, 0, 5); // Más espacio arriba de los botones
        add(panelBotones, gbc);

        // **4. Lógica de los botones (sin cambios funcionales, solo estéticos)**
        btnIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String usuario = txtUsuario.getText().trim();
                String contrasena = new String(txtContrasena.getPassword());

                if (usuario.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(PanelInicioSesion.this, "Por favor, ingrese usuario y contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                    connection = ConexionDB.conectar();
                    String sql = "SELECT id, rol, contrasena FROM usuarios WHERE usuario = ?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, usuario);
                    resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        String rol = resultSet.getString("rol");
                        String contrasenaBD = resultSet.getString("contrasena");
                        int idUsuario = resultSet.getInt("id");

                        if (contrasena.equals(contrasenaBD)) {
                            JOptionPane.showMessageDialog(PanelInicioSesion.this, "¡Bienvenido, " + usuario + "!", "Inicio de Sesión Exitoso", JOptionPane.INFORMATION_MESSAGE);
                            if (rol.equals("admin")) {
                                ventanaAdmin.setVisible(true);
                            } else {
                                // Asegúrate de que VentanaPrincipal reciba el JFrame correcto para el padre
                                ventanaPrincipal = new VentanaPrincipal((VentanaIDS) SwingUtilities.getWindowAncestor(PanelInicioSesion.this), idUsuario);
                                ventanaPrincipal.setVisible(true);
                            }
                            JFrame ventanaIDS = (JFrame) SwingUtilities.getWindowAncestor(PanelInicioSesion.this);
                            if (ventanaIDS != null) {
                                ventanaIDS.dispose(); // Cierra la ventana actual de inicio de sesión
                            }
                        } else {
                            JOptionPane.showMessageDialog(PanelInicioSesion.this, "Contraseña incorrecta. Inténtelo de nuevo.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                            txtContrasena.setText(""); // Limpiar campo de contraseña
                        }
                    } else {
                        JOptionPane.showMessageDialog(PanelInicioSesion.this, "Usuario no encontrado. Verifique su nombre de usuario.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                        txtUsuario.setText(""); // Limpiar campo de usuario
                        txtContrasena.setText("");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(PanelInicioSesion.this, "Error de base de datos: " + ex.getMessage() + "\nPor favor, contacte al soporte.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
                } finally {
                    try {
                        if (resultSet != null) resultSet.close();
                        if (preparedStatement != null) preparedStatement.close();
                        if (connection != null) connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cardLayout.show(panelContenedor, "Principal");
                txtUsuario.setText("");
                txtContrasena.setText("");
            }
        });
    }
}