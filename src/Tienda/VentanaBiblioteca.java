/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tienda;
import javax.swing.*;
import java.awt.*;
import javax.swing.JPanel;
import java.awt.CardLayout;

/**
 *
 * @author nombr
 */
public class VentanaBiblioteca extends JPanel {
    public VentanaBiblioteca (CardLayout cardLayout, JPanel panelContenido) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel labelBiblioteca = new JLabel("Biblioteca", SwingConstants.CENTER);
        labelBiblioteca.setFont(new Font("Arial", Font.BOLD, 18));
        labelBiblioteca.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Aquí se pueden agregar elementos al carrito en una lista o tabla

        JButton botonVolver = new JButton("Volver");
        botonVolver.setFont(new Font("Arial", Font.BOLD, 14));
        botonVolver.setBackground(new Color(0, 123, 255));
        botonVolver.setForeground(Color.WHITE);
        botonVolver.setFocusPainted(false);
        botonVolver.addActionListener(e -> cardLayout.show(panelContenido, "Tienda"));

        add(labelBiblioteca, BorderLayout.NORTH);
        add(botonVolver, BorderLayout.CENTER);
    }
}