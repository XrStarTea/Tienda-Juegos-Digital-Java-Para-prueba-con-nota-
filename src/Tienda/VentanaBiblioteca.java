package tienda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL; // Import URL for resource loading
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
        // Only add products that are not already in the library to avoid duplicates
        for (VentanaCarrito.Producto nuevoProducto : productos) {
            boolean found = false;
            for (VentanaCarrito.Producto existenteProducto : productosComprados) {
                if (existenteProducto.getNombre().equals(nuevoProducto.getNombre())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                productosComprados.add(nuevoProducto);
            }
        }
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
        JLabel labelImagen = cargarImagenJuego(producto.getImagen()); // Use producto.getImagen()
        
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

    // This method is adapted from VentanaTienda's cargarImagenJuego
    private JLabel cargarImagenJuego(String nombreImagen) {
        if (nombreImagen == null || nombreImagen.isEmpty()) {
            return crearLabelSinImagen();
        }

        try {
            URL imgUrl = getClass().getResource("/tienda/imagenes/" + nombreImagen);
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    return crearLabelDesdeImagen(icon.getImage());
                }
            }

            // Fallback for development environments if loading from resources fails
            String rutaBase = System.getProperty("user.dir");
            String[] rutasPrueba = {
                rutaBase + "/src/tienda/imagenes/" + nombreImagen,
                rutaBase + "/build/classes/tienda/imagenes/" + nombreImagen,
                "src/tienda/imagenes/" + nombreImagen
            };

            for (String ruta : rutasPrueba) {
                ImageIcon icon = new ImageIcon(ruta);
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    return crearLabelDesdeImagen(icon.getImage());
                }
            }

            return crearLabelErrorImagen();

        } catch (Exception e) {
            e.printStackTrace();
            return crearLabelErrorImagen();
        }
    }

    private JLabel crearLabelDesdeImagen(Image image) {
        // Adjust the size for the library cards
        Image img = image.getScaledInstance(180, 180, Image.SCALE_SMOOTH); 
        return new JLabel(new ImageIcon(img));
    }

    private JLabel crearLabelSinImagen() {
        JLabel label = new JLabel("Sin imagen", JLabel.CENTER);
        label.setPreferredSize(new Dimension(180, 180)); // Consistent size
        label.setFont(new Font("Arial", Font.ITALIC, 14));
        label.setForeground(Color.RED);
        return label;
    }

    private JLabel crearLabelErrorImagen() {
        JLabel label = new JLabel("Imagen no encontrada", JLabel.CENTER);
        label.setPreferredSize(new Dimension(180, 180)); // Consistent size
        label.setFont(new Font("Arial", Font.ITALIC, 14));
        label.setForeground(Color.RED);
        return label;
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