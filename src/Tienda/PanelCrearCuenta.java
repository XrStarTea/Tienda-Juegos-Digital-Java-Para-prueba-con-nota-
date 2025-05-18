package tienda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

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
        setLayout(new GridLayout(7, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblNombreCompleto = new JLabel("Nombre completo:");
        txtNombreCompleto = new JTextField(20);

        JLabel lblNombreUsuario = new JLabel("Nombre de usuario:");
        txtNombreUsuario = new JTextField(20);

        JLabel lblContrasena = new JLabel("Contraseña:");
        txtContrasena = new JPasswordField(20);

        JLabel lblConfirmarContrasena = new JLabel("Confirmar contraseña:");
        txtConfirmarContrasena = new JPasswordField(20);

        JLabel lblAdministrador = new JLabel("¿Administrador?:");
        chkAdministrador = new JCheckBox();

        btnCrearCuenta = new JButton("Crear Cuenta");
        btnVolver = new JButton("Volver");

        add(lblNombreCompleto);
        add(txtNombreCompleto);
        add(lblNombreUsuario);
        add(txtNombreUsuario);
        add(lblContrasena);
        add(txtContrasena);
        add(lblConfirmarContrasena);
        add(txtConfirmarContrasena);
        add(lblAdministrador);
        add(chkAdministrador);
        add(new JLabel());
        add(btnCrearCuenta);
        add(new JLabel());
        add(btnVolver);

        // ActionListener para el botón Volver
        btnVolver.addActionListener(new ActionListener() {
        @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenedor, "Principal"); 
            }
        });

        // ActionListener para el botón Crear Cuenta (validación y guardar en la base de datos)
        btnCrearCuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreCompleto = txtNombreCompleto.getText().trim();
                String usuario = txtNombreUsuario.getText().trim();
                String contrasena = new String(txtContrasena.getPassword());
                String confirmarContrasena = new String(txtConfirmarContrasena.getPassword());
                boolean esAdministrador = chkAdministrador.isSelected();
                String rol = esAdministrador ? "admin" : "usuario";

                // Validación de campos
                if (nombreCompleto.isEmpty() || usuario.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(PanelCrearCuenta.this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!contrasena.equals(confirmarContrasena)) {
                    JOptionPane.showMessageDialog(PanelCrearCuenta.this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (contrasena.length() < 1) {
                    JOptionPane.showMessageDialog(PanelCrearCuenta.this, "La contraseña debe tener al menos 1 carácter.", "Error", JOptionPane.ERROR_MESSAGE);
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
                        JOptionPane.showMessageDialog(PanelCrearCuenta.this, "Cuenta creada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cardLayout.show(panelContenedor, "InicioSesion");
                        txtNombreCompleto.setText("");
                        txtNombreUsuario.setText("");
                        txtContrasena.setText("");
                        txtConfirmarContrasena.setText("");
                        chkAdministrador.setSelected(false);
                    } else {
                        JOptionPane.showMessageDialog(PanelCrearCuenta.this, "Error al crear la cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    if (ex.getMessage().contains("duplicate key")) {
                        JOptionPane.showMessageDialog(PanelCrearCuenta.this, "El nombre de usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(PanelCrearCuenta.this, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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