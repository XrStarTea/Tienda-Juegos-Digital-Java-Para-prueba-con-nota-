package tienda;

import javax.swing.*;
import java.awt.*;

public class VentanaSoporte extends JPanel {

    public VentanaSoporte(CardLayout cardLayout, JPanel panelContenido) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JButton botonVolver = new JButton("Volver");
        botonVolver.setFont(new Font("Arial", Font.BOLD, 14));
        botonVolver.setBackground(new Color(0, 123, 255));
        botonVolver.setForeground(Color.WHITE);
        botonVolver.setFocusPainted(false);
        botonVolver.addActionListener(e -> cardLayout.show(panelContenido, "Tienda"));

        add(botonVolver, BorderLayout.CENTER);
    }
}