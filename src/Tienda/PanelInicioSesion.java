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
    private JFrame ventanaPrincipal; // Para mostrar la ventana principal
    private JFrame ventanaAdmin; // Para mostrar la ventana de administrador

    public PanelInicioSesion(CardLayout layout, JPanel contenedor, JFrame ventanaPrin, JFrame ventanaAdm) {
        this.cardLayout = layout;
        this.panelContenedor = contenedor;
        this.ventanaPrincipal = ventanaPrin;
        this.ventanaAdmin = ventanaAdm;
        setLayout(new GridLayout(4, 2, 5, 5)); // Volvemos a GridLayout
        setBackground(new Color(204, 204, 204));
        setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        Font fuentePequena = new Font("Segoe UI", Font.PLAIN, 14);
        Dimension botonDimension = new Dimension(90, 25);
        Dimension textFieldDimension = new Dimension(150, 10); // Reducir la altura preferida

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(fuentePequena);
        add(lblUsuario);
        txtUsuario = new JTextField(15);
        txtUsuario.setFont(fuentePequena);
        txtUsuario.setPreferredSize(textFieldDimension); // Establecer altura preferida
        add(txtUsuario);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(fuentePequena);
        add(lblContrasena);
        txtContrasena = new JPasswordField(15);
        txtContrasena.setFont(fuentePequena);
        txtContrasena.setPreferredSize(textFieldDimension); // Establecer altura preferida
        add(txtContrasena);

        // Panel para los botones con FlowLayout
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelBotones.setOpaque(false); // Hacer el fondo del panel transparente

        btnVolver = new JButton("Volver");
        btnVolver.setFont(fuentePequena);
        btnVolver.setPreferredSize(botonDimension);
        panelBotones.add(btnVolver); // Se eliminaron las líneas de transparencia del botón

        btnIniciar = new JButton("Iniciar");
        btnIniciar.setFont(fuentePequena);
        btnIniciar.setPreferredSize(botonDimension);
        panelBotones.add(btnIniciar); // Se eliminaron las líneas de transparencia del botón

        add(new JLabel()); // Celda vacía en la tercera fila, primera columna
        add(panelBotones); // Panel de botones en la tercera fila, segunda columna

        btnIniciar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String usuario = txtUsuario.getText().trim();
                String contrasena = new String(txtContrasena.getPassword());

                if (usuario.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(PanelInicioSesion.this, "Por favor, ingrese usuario y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                    connection = ConexionDB.conectar();
                    String sql = "SELECT rol, contrasena FROM usuarios WHERE usuario = ?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, usuario);
                    resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        String rol = resultSet.getString("rol");
                        String contrasenaBD = resultSet.getString("contrasena");

                        if (contrasena.equals(contrasenaBD)) {
                            JOptionPane.showMessageDialog(PanelInicioSesion.this, "Inicio de sesión exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            if (rol.equals("admin")) {
                                ventanaAdmin.setVisible(true);
                            } else {
                                ventanaPrincipal.setVisible(true);
                            }
                            JFrame ventanaIDS = (JFrame) SwingUtilities.getWindowAncestor(PanelInicioSesion.this);
                            if (ventanaIDS != null) {
                                ventanaIDS.dispose();
                            }
                        } else {
                            JOptionPane.showMessageDialog(PanelInicioSesion.this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(PanelInicioSesion.this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(PanelInicioSesion.this, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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