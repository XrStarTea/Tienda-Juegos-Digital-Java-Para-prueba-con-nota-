package tienda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import tienda.JuegoTienda;

public class VentanaDescripcion extends JPanel {
    private JuegoTienda juegoActual; // <--- Nuevo campo para almacenar el juego
    private VentanaCarrito ventanaCarrito; // Necesitas este campo para el action listener
    
    public VentanaDescripcion(JuegoTienda juego, CardLayout cardLayout,
                              JPanel panelContenido, VentanaCarrito carrito) { // <--- MODIFICA LA FIRMA
        this.juegoActual = juego; // Asigna el objeto JuegoTienda al campo
        this.ventanaCarrito = carrito; // Asigna la referencia al carrito
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Hacemos copias finales de las variables que usaremos en el lambda
        //final String nombre = nombreJuego;
        //final String imagen = imagenJuego;
        
        JLabel labelTitulo = new JLabel(juegoActual.getNombre(), SwingConstants.CENTER); // <--- Usa juegoActual.getNombre()
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JTextArea textAreaDescripcion = new JTextArea(juegoActual.getDescripcion()); // <--- Usa juegoActual.getDescripcion()
        textAreaDescripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        textAreaDescripcion.setEditable(false);
        textAreaDescripcion.setLineWrap(true);
        textAreaDescripcion.setWrapStyleWord(true);
        textAreaDescripcion.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(textAreaDescripcion);
        
        
        // Botón "Agregar al Carrito"
        JButton botonAgregarCarrito = new JButton("Agregar al Carrito");
        botonAgregarCarrito.addActionListener((ActionEvent e) -> {
            // Ahora pasas el ID del juego, y el nombre, precio, imagen directamente del objeto 'juegoActual'
            ventanaCarrito.agregarProductoDesdeTienda(
                juegoActual.getId(), // <--- ¡Esto es lo que faltaba!
                juegoActual.getNombre(),
                juegoActual.getPrecio(), // <--- Precio directo del objeto, no de la descripción
                juegoActual.getImagen()
            );
            JOptionPane.showMessageDialog(this, juegoActual.getNombre() + " agregado al carrito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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