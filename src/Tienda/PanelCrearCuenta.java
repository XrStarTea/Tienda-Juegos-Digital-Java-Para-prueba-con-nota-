package tienda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PanelCrearCuenta extends JPanel {

    private JTextField txtNombreCompleto;
    private JTextField txtNombreUsuario;
    private JPasswordField txtContrasena;
    private JPasswordField txtConfirmarContrasena;
    private JCheckBox chkAdministrador;
    private JButton btnCrearCuenta;
    private JButton btnVolver;
    private CardLayout cardLayout;
    private JPanel panelContenedor;

    public PanelCrearCuenta(CardLayout layout, JPanel contenedor) {
        this.cardLayout = layout;
        this.panelContenedor = contenedor;

        // **1. Diseño principal con GridBagLayout para mayor flexibilidad**
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240)); // Un gris más claro para el fondo
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // Más padding

        // **2. Definición de fuentes y colores (tema más consistente)**
        Font fontTitulo = new Font("Segoe UI", Font.BOLD, 24);
        Font fontLabels = new Font("Segoe UI", Font.PLAIN, 16);
        Font fontCampos = new Font("Segoe UI", Font.PLAIN, 16);
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 15);
        Font fontCheckbox = new Font("Segoe UI", Font.PLAIN, 16);

        Color colorPrimario = new Color(52, 152, 219); // Azul para botones principales
        Color colorSecundario = new Color(149, 165, 166); // Gris para "Volver"
        Color colorTextoClaro = Color.WHITE;
        Color colorBordeCampo = new Color(180, 180, 180);
        Color colorTitulo = new Color(44, 62, 80); // Azul oscuro casi negro

        // **3. Componentes del UI**
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); // Margen entre componentes

        // Título
        JLabel lblTitulo = new JLabel("Crear Nueva Cuenta");
        lblTitulo.setFont(fontTitulo);
        lblTitulo.setForeground(colorTitulo);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitulo, gbc);

        // Separador visual
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(250, 2));
        separator.setForeground(new Color(200, 200, 200));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 5, 25, 5); // Menos espacio arriba, más abajo
        add(separator, gbc);
        gbc.insets = new Insets(8, 5, 8, 5); // Restaurar insets

        // Nombre Completo
        JLabel lblNombreCompleto = new JLabel("Nombre completo:");
        lblNombreCompleto.setFont(fontLabels);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(lblNombreCompleto, gbc);

        txtNombreCompleto = new JTextField(20);
        txtNombreCompleto.setFont(fontCampos);
        txtNombreCompleto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBordeCampo, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtNombreCompleto, gbc);

        // Nombre de Usuario
        JLabel lblNombreUsuario = new JLabel("Nombre de usuario:");
        lblNombreUsuario.setFont(fontLabels);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        add(lblNombreUsuario, gbc);

        txtNombreUsuario = new JTextField(20);
        txtNombreUsuario.setFont(fontCampos);
        txtNombreUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBordeCampo, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtNombreUsuario, gbc);

        // Contraseña
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(fontLabels);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        add(lblContrasena, gbc);

        txtContrasena = new JPasswordField(20);
        txtContrasena.setFont(fontCampos);
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBordeCampo, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtContrasena, gbc);

        // Confirmar Contraseña
        JLabel lblConfirmarContrasena = new JLabel("Confirmar contraseña:");
        lblConfirmarContrasena.setFont(fontLabels);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        add(lblConfirmarContrasena, gbc);

        txtConfirmarContrasena = new JPasswordField(20);
        txtConfirmarContrasena.setFont(fontCampos);
        txtConfirmarContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBordeCampo, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtConfirmarContrasena, gbc);

        // ¿Administrador?
        JLabel lblAdministrador = new JLabel("¿Es administrador?");
        lblAdministrador.setFont(fontLabels);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        add(lblAdministrador, gbc);

        chkAdministrador = new JCheckBox();
        chkAdministrador.setFont(fontCheckbox);
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST; // Alinea el checkbox a la izquierda en su celda
        add(chkAdministrador, gbc);

        // **MODIFICACIÓN AQUÍ: Añadir botones directamente, no en un panel anidado**
        // Botón Volver
        btnVolver = new JButton("Volver");
        btnVolver.setFont(fontBotones);
        btnVolver.setBackground(colorSecundario);
        btnVolver.setForeground(colorTextoClaro);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setPreferredSize(new Dimension(110, 38));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0; // Columna 0
        gbc.gridy = 7; // Fila 7
        gbc.gridwidth = 1; // Ocupa 1 columna
        gbc.anchor = GridBagConstraints.EAST; // Alineado a la derecha en su celda
        gbc.insets = new Insets(30, 5, 0, 10); // Más espacio a la derecha para separar de "Crear Cuenta"
        gbc.fill = GridBagConstraints.NONE; // No queremos que el botón se estire
        add(btnVolver, gbc);

        // Botón Crear Cuenta
        btnCrearCuenta = new JButton("Crear Cuenta");
        btnCrearCuenta.setFont(fontBotones);
        btnCrearCuenta.setBackground(colorPrimario);
        btnCrearCuenta.setForeground(colorTextoClaro);
        btnCrearCuenta.setFocusPainted(false);
        btnCrearCuenta.setBorderPainted(false);
        btnCrearCuenta.setPreferredSize(new Dimension(130, 38));
        btnCrearCuenta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 1; // Columna 1
        gbc.gridy = 7; // Fila 7 (la misma que "Volver")
        gbc.gridwidth = 1; // Ocupa 1 columna
        gbc.anchor = GridBagConstraints.WEST; // Alineado a la izquierda en su celda
        gbc.insets = new Insets(30, 10, 0, 5); // Más espacio a la izquierda para separar de "Volver"
        gbc.fill = GridBagConstraints.NONE; // No queremos que el botón se estire
        add(btnCrearCuenta, gbc);

        // **4. Lógica de los botones (sin cambios funcionales, solo estéticos)**
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenedor, "Principal");
                txtNombreCompleto.setText("");
                txtNombreUsuario.setText("");
                txtContrasena.setText("");
                txtConfirmarContrasena.setText("");
                chkAdministrador.setSelected(false);
            }
        });

        btnCrearCuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreCompleto = txtNombreCompleto.getText().trim();
                String usuario = txtNombreUsuario.getText().trim();
                String contrasena = new String(txtContrasena.getPassword());
                String confirmarContrasena = new String(txtConfirmarContrasena.getPassword());
                boolean esAdministrador = chkAdministrador.isSelected();
                String rol = esAdministrador ? "admin" : "usuario";

                if (nombreCompleto.isEmpty() || usuario.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(PanelCrearCuenta.this, "Todos los campos son obligatorios. Por favor, rellénelos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!contrasena.equals(confirmarContrasena)) {
                    JOptionPane.showMessageDialog(PanelCrearCuenta.this, "Las contraseñas no coinciden. Inténtelo de nuevo.", "Error de Contraseña", JOptionPane.ERROR_MESSAGE);
                    txtContrasena.setText("");
                    txtConfirmarContrasena.setText("");
                    return;
                }

                if (contrasena.length() < 1) { // ¡Ojo! Contraseñas tan cortas no son seguras.
                    JOptionPane.showMessageDialog(PanelCrearCuenta.this, "La contraseña debe tener al menos 1 carácter.", "Contraseña Corta", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Connection connection = null;
                PreparedStatement preparedStatement = null;

                try {
                    connection = ConexionDB.conectar();
                    String sql = "INSERT INTO usuarios (nombre, usuario, contrasena, rol, tarjeta) VALUES (?, ?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(sql);

                    preparedStatement.setString(1, nombreCompleto);
                    preparedStatement.setString(2, usuario);
                    preparedStatement.setString(3, contrasena);
                    preparedStatement.setString(4, rol);
                    preparedStatement.setString(5, null);

                    int filasAfectadas = preparedStatement.executeUpdate();

                    if (filasAfectadas > 0) {
                        JOptionPane.showMessageDialog(PanelCrearCuenta.this, "¡Cuenta creada exitosamente! Ahora puedes iniciar sesión.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cardLayout.show(panelContenedor, "InicioSesion");
                        txtNombreCompleto.setText("");
                        txtNombreUsuario.setText("");
                        txtContrasena.setText("");
                        txtConfirmarContrasena.setText("");
                        chkAdministrador.setSelected(false);
                    } else {
                        JOptionPane.showMessageDialog(PanelCrearCuenta.this, "No se pudo crear la cuenta. Inténtelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    if (ex.getMessage() != null && (ex.getMessage().toLowerCase().contains("duplicate key") || ex.getMessage().toLowerCase().contains("unique constraint"))) {
                        JOptionPane.showMessageDialog(PanelCrearCuenta.this, "El nombre de usuario '" + usuario + "' ya está en uso. Por favor, elija otro.", "Usuario Duplicado", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(PanelCrearCuenta.this, "Error de base de datos: " + ex.getMessage() + "\nPor favor, contacte al soporte técnico.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
                    }
                } finally {
                    try {
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                        if (connection != null) {
                            connection.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}