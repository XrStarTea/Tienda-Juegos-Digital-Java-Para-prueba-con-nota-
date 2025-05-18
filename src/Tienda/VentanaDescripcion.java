package tienda;

import javax.swing.*;
import java.awt.*;

public class VentanaDescripcion extends JPanel {

    public VentanaDescripcion(String nombre, String descripcion, CardLayout cardLayout, JPanel panelContenido) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel labelTituloJuego = new JLabel(nombre, SwingConstants.CENTER);
        labelTituloJuego.setFont(new Font("Arial", Font.BOLD, 18));
        labelTituloJuego.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JTextArea areaDescripcion = new JTextArea(descripcion);
        areaDescripcion.setEditable(false);
        areaDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        areaDescripcion.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(areaDescripcion);
        add(labelTituloJuego, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton botonComprar = new JButton("Comprar");
        botonComprar.setFont(new Font("Arial", Font.BOLD, 14));
        botonComprar.setBackground(new Color(40, 167, 69));
        botonComprar.setForeground(Color.WHITE);
        botonComprar.setFocusPainted(false);
        botonComprar.addActionListener(e -> JOptionPane.showMessageDialog(this, "¡Has comprado " + nombre + " exitosamente!"));

        JButton botonVolver = new JButton("Volver");
        botonVolver.setFont(new Font("Arial", Font.BOLD, 14));
        botonVolver.setBackground(new Color(0, 123, 255));
        botonVolver.setForeground(Color.WHITE);
        botonVolver.setFocusPainted(false);
        botonVolver.addActionListener(e -> cardLayout.show(panelContenido, "Tienda"));

        panelBotones.add(botonComprar);
        panelBotones.add(botonVolver);
        add(panelBotones, BorderLayout.SOUTH);
    }
}