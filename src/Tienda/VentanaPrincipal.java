package tienda;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenido;
    private VentanaIDS ventanaIDS;

public VentanaPrincipal(VentanaIDS ventanaIDS) {
    this.ventanaIDS = ventanaIDS;
        setTitle("Tienda de Juegos");
        setSize(800, 710);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        

        // CardLayout
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        // Paneles
        JPanel panelTienda = new VentanaTienda(cardLayout, panelContenido);
        JPanel panelPerfil = new VentanaPerfil(cardLayout, panelContenido);
        JPanel panelSoporte = new VentanaSoporte(cardLayout, panelContenido);
        JPanel panelCarrito = new VentanaCarrito(cardLayout, panelContenido);
        JPanel panelBiblioteca = new VentanaBiblioteca(cardLayout, panelContenido);

        // Agregar paneles al CardLayout
        panelContenido.add(panelTienda, "Tienda");
        panelContenido.add(panelPerfil, "Perfil");
        panelContenido.add(panelSoporte, "Soporte");
        panelContenido.add(panelCarrito, "Carrito");
        panelContenido.add(panelBiblioteca, "Biblioteca");

        // Agregar contenido principal
        add(panelContenido, BorderLayout.CENTER);

        // Panel superior con los botones
        JPanel panelSuperior = new JPanel(new BorderLayout()); // Usamos BorderLayout
        panelSuperior.setOpaque(false);

        // Botón Cerrar Sesión (a la izquierda)
        JButton botonCerrarSesion = new JButton("Volver");
        configurarBotonSuperior(botonCerrarSesion);
        botonCerrarSesion.addActionListener(e -> {
        this.dispose(); // Cierra la ventana de la tienda
        ventanaIDS.setVisible(true); // Muestra la ventana IDS (pantalla principal de login)
        });
        panelSuperior.add(botonCerrarSesion, BorderLayout.WEST);

        // Panel para los botones de la derecha
        JPanel panelBotonesDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonesDerecha.setOpaque(false);

        // Botón Carrito
        JButton botonCarrito = new JButton("Carrito");
        configurarBotonSuperior(botonCarrito);
        botonCarrito.addActionListener(e -> cardLayout.show(panelContenido, "Carrito"));
        panelBotonesDerecha.add(botonCarrito);

        // Botón Biblioteca
        JButton botonBiblioteca = new JButton("Biblioteca");
        configurarBotonSuperior(botonBiblioteca);
        botonBiblioteca.addActionListener(e -> cardLayout.show(panelContenido, "Biblioteca"));
        panelBotonesDerecha.add(botonBiblioteca);

        // Botón Soporte
        JButton botonSoporte = new JButton("Soporte");
        configurarBotonSuperior(botonSoporte);
        botonSoporte.addActionListener(e -> cardLayout.show(panelContenido, "Soporte"));
        panelBotonesDerecha.add(botonSoporte);

        // Botón Perfil
        JButton botonPerfil = new JButton("Perfil");
        configurarBotonSuperior(botonPerfil);
        botonPerfil.addActionListener(e -> cardLayout.show(panelContenido, "Perfil"));
        panelBotonesDerecha.add(botonPerfil);

        panelSuperior.add(panelBotonesDerecha, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);
    }

    // Método auxiliar para configurar la apariencia de los botones superiores
    private void configurarBotonSuperior(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(0, 123, 255));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(120, 40)); // Ajusta el ancho según necesites
    }
    // El método main ahora iniciará la VentanaIDS
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaIDS());
    }
}