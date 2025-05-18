package tienda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class VentanaBiblioteca extends JPanel {
    private List<VentanaCarrito.Producto> productosComprados;
    private JPanel panelJuegos;
    private CardLayout cardLayout;
    private JPanel panelContenido;

    public VentanaBiblioteca(CardLayout cardLayout, JPanel panelContenido) {
        this.cardLayout = cardLayout;
        this.panelContenido = panelContenido;
        productosComprados = new ArrayList<>();
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Título
        JLabel labelBiblioteca = new JLabel("Mi Biblioteca", SwingConstants.CENTER);
        labelBiblioteca.setFont(new Font("Arial", Font.BOLD, 24));
        labelBiblioteca.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Panel para mostrar los juegos
        panelJuegos = new JPanel(new GridLayout(0, 3, 20, 20));
        panelJuegos.setBackground(Color.WHITE);
        panelJuegos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(panelJuegos);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Botón para volver
        JButton botonVolver = new JButton("Volver a la Tienda");
        botonVolver.setFont(new Font("Arial", Font.BOLD, 14));
        botonVolver.setBackground(new Color(0, 123, 255));
        botonVolver.setForeground(Color.WHITE);
        botonVolver.setFocusPainted(false);
        botonVolver.addActionListener(e -> cardLayout.show(panelContenido, "Tienda"));
        
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(Color.WHITE);
        panelBoton.add(botonVolver);

        add(labelBiblioteca, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);
    }

    // Método para agregar juegos comprados a la biblioteca
    public void agregarJuegosComprados(List<VentanaCarrito.Producto> productos) {
        productosComprados.addAll(productos);
        actualizarBiblioteca();
    }

    private void actualizarBiblioteca() {
        panelJuegos.removeAll();
        
        for (VentanaCarrito.Producto producto : productosComprados) {
            JPanel cardJuego = crearCardJuego(producto);
            panelJuegos.add(cardJuego);
        }
        
        panelJuegos.revalidate();
        panelJuegos.repaint();
    }

    private JPanel crearCardJuego(VentanaCarrito.Producto producto) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(240, 240, 240));
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        card.setPreferredSize(new Dimension(200, 250));

        // Imagen del juego (usando el nombre para buscar la imagen correspondiente)
        ImageIcon icono = cargarImagenJuego(producto.getNombre());
        JLabel labelImagen = new JLabel();
        
        if (icono != null) {
            // Escalar la imagen si es necesario
            Image imagen = icono.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            labelImagen.setIcon(new ImageIcon(imagen));
        } else {
            labelImagen.setIcon(new ImageIcon()); // Icono vacío si no hay imagen
            labelImagen.setText("Imagen no disponible");
            labelImagen.setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        labelImagen.setHorizontalAlignment(SwingConstants.CENTER);
        labelImagen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Información del juego
        JPanel panelInfo = new JPanel(new GridLayout(2, 1));
        panelInfo.setBackground(new Color(230, 230, 230));
        
        JLabel labelNombre = new JLabel(producto.getNombre(), SwingConstants.CENTER);
        labelNombre.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel labelPrecio = new JLabel(String.format("Comprado: $%.2f", producto.getPrecio()), SwingConstants.CENTER);
        labelPrecio.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panelInfo.add(labelNombre);
        panelInfo.add(labelPrecio);

        card.add(labelImagen, BorderLayout.CENTER);
        card.add(panelInfo, BorderLayout.SOUTH);

        return card;
    }

    private ImageIcon cargarImagenJuego(String nombreJuego) {
        // Aquí deberías implementar la lógica para cargar la imagen correspondiente al juego
        // Por ejemplo, podrías tener una carpeta de recursos con imágenes nombradas según los juegos
        
        // Esto es un ejemplo básico - en una implementación real, deberías cargar las imágenes
        // desde archivos o recursos del proyecto
        try {
            // Simulación: si el nombre contiene "Mario", usar una imagen de Mario
            if (nombreJuego.toLowerCase().contains("mario")) {
                return new ImageIcon("ruta/a/imagenes/mario.jpg"); // Reemplazar con ruta real
            } else if (nombreJuego.toLowerCase().contains("zelda")) {
                return new ImageIcon("ruta/a/imagenes/zelda.jpg"); // Reemplazar con ruta real
            }
            // Añadir más casos según tus juegos
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    // Método para verificar si un juego ya está en la biblioteca
    public boolean contieneJuego(String nombreJuego) {
        for (VentanaCarrito.Producto producto : productosComprados) {
            if (producto.getNombre().equalsIgnoreCase(nombreJuego)) {
                return true;
            }
        }
        return false;
    }
}