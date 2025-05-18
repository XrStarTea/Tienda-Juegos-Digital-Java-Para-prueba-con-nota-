package tienda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaDescripcion extends JPanel {
    
    public VentanaDescripcion(String nombreJuego, String descripcionJuego, CardLayout cardLayout, 
                            JPanel panelContenido, VentanaCarrito ventanaCarrito) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Hacemos copias finales de las variables que usaremos en el lambda
        final String nombre = nombreJuego;
        
        JLabel labelTitulo = new JLabel(nombre, SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JTextArea textAreaDescripcion = new JTextArea(descripcionJuego);
        textAreaDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        textAreaDescripcion.setEditable(false);
        textAreaDescripcion.setLineWrap(true);
        textAreaDescripcion.setWrapStyleWord(true);
        textAreaDescripcion.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(textAreaDescripcion);
        
        // Extraer el precio de la descripción y hacerlo final
        final double[] precio = {0};
        try {
            String[] partes = descripcionJuego.split("Precio: CLP\\$ ");
            if (partes.length > 1) {
                String precioStr = partes[1].split("\n")[0].trim();
                precio[0] = Double.parseDouble(precioStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JButton botonAgregarCarrito = new JButton("Agregar al Carrito");
        botonAgregarCarrito.addActionListener((ActionEvent e) -> {
            ventanaCarrito.agregarProductoDesdeTienda(nombre, precio[0]);
            JOptionPane.showMessageDialog(this, nombre + " agregado al carrito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton botonVolver = new JButton("Volver");
        botonVolver.addActionListener(e -> cardLayout.show(panelContenido, "Tienda"));
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.add(botonAgregarCarrito);
        panelBotones.add(botonVolver);
        
        add(labelTitulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
}