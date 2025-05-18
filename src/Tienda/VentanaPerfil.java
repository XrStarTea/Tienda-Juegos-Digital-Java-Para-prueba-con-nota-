package tienda;

import javax.swing.*;
import java.awt.*;

public class VentanaPerfil extends JPanel {

    public VentanaPerfil(CardLayout cardLayout, JPanel panelContenido) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel panelEdicion = new JPanel(new GridLayout(4, 2, 10, 10));
        panelEdicion.setBackground(Color.WHITE);
        panelEdicion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel labelNombre = new JLabel("Nombre:");
        JTextField campoNombre = new JTextField(20);

        JLabel labelCorreo = new JLabel("Correo:");
        JTextField campoCorreo = new JTextField(20);

        JLabel labelTelefono = new JLabel("Teléfono:");
        JTextField campoTelefono = new JTextField(20);

        JLabel labelDireccion = new JLabel("Dirección:");
        JTextField campoDireccion = new JTextField(20);

        panelEdicion.add(labelNombre);
        panelEdicion.add(campoNombre);
        panelEdicion.add(labelCorreo);
        panelEdicion.add(campoCorreo);
        panelEdicion.add(labelTelefono);
        panelEdicion.add(campoTelefono);
        panelEdicion.add(labelDireccion);
        panelEdicion.add(campoDireccion);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.setBackground(Color.WHITE);

        JButton botonVolver = new JButton("Volver");
        botonVolver.setFont(new Font("Arial", Font.BOLD, 12));
        botonVolver.setBackground(new Color(0, 123, 255));
        botonVolver.setForeground(Color.WHITE);
        botonVolver.setFocusPainted(false);
        botonVolver.setPreferredSize(new Dimension(80, 30));
        botonVolver.addActionListener(e -> cardLayout.show(panelContenido, "Tienda"));

        JButton botonGuardar = new JButton("Guardar Cambios");
        botonGuardar.setFont(new Font("Arial", Font.BOLD, 12));
        botonGuardar.setBackground(new Color(40, 167, 69));
        botonGuardar.setForeground(Color.WHITE);
        botonGuardar.setFocusPainted(false);
        botonGuardar.setPreferredSize(new Dimension(120, 30));

        JButton botonEliminar = new JButton("Eliminar Perfil");
        botonEliminar.setFont(new Font("Arial", Font.BOLD, 12));
        botonEliminar.setBackground(new Color(220, 53, 69));
        botonEliminar.setForeground(Color.WHITE);
        botonEliminar.setFocusPainted(false);
        botonEliminar.setPreferredSize(new Dimension(120, 30));

        panelBotones.add(botonVolver);
        panelBotones.add(botonGuardar);
        panelBotones.add(botonEliminar);

        add(panelEdicion, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
}