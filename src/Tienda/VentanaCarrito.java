package tienda;

import javax.swing.*;
import java.awt.*;

public class VentanaCarrito extends JPanel {

    public VentanaCarrito(CardLayout cardLayout, JPanel panelContenido) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel labelCarrito = new JLabel("Carrito de Compras", SwingConstants.CENTER);
        labelCarrito.setFont(new Font("Arial", Font.BOLD, 18));
        labelCarrito.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Aquí se pueden agregar elementos al carrito en una lista o tabla

        JButton botonVolver = new JButton("Volver");
        botonVolver.setFont(new Font("Arial", Font.BOLD, 14));
        botonVolver.setBackground(new Color(0, 123, 255));
        botonVolver.setForeground(Color.WHITE);
        botonVolver.setFocusPainted(false);
        botonVolver.addActionListener(e -> cardLayout.show(panelContenido, "Tienda"));

        add(labelCarrito, BorderLayout.NORTH);
        add(botonVolver, BorderLayout.CENTER);
    }
}