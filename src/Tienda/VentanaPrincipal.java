package tienda;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenido;
    private VentanaTienda ventanaTienda;
    private VentanaIDS ventanaIDS;
    private VentanaCarrito ventanaCarrito;
    private VentanaBiblioteca ventanaBiblioteca; // <--- Esta es la variable de instancia
    private int idUsuarioLogueado;

    // Nuevo método para establecer el ID del usuario logueado
    public void setIdUsuarioLogueado(int idUsuario) {
        this.idUsuarioLogueado = idUsuario;
    }

    public VentanaPrincipal(VentanaIDS ventanaIDS, int idUsuarioLogueado) {
        this.ventanaIDS = ventanaIDS;
        this.idUsuarioLogueado = idUsuarioLogueado;
        setTitle("Tienda de Juegos");
        setSize(800, 710);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        // Crear el carrito primero
        ventanaCarrito = new VentanaCarrito(cardLayout, panelContenido, idUsuarioLogueado);

        // Crear paneles pasando las dependencias necesarias
        // Asigna a la variable de instancia 'ventanaTienda'
        ventanaTienda = new VentanaTienda(cardLayout, panelContenido, ventanaCarrito);
        JPanel panelPerfil = new VentanaPerfil(cardLayout, panelContenido);
        // Aquí se inicializa VentanaSoporte con el idUsuarioLogueado
        JPanel panelSoporte = new VentanaSoporte(cardLayout, panelContenido, this.idUsuarioLogueado);
        
        // ¡¡¡CAMBIO CLAVE AQUÍ!!!
        // Asigna la instancia creada a la variable de instancia 'this.ventanaBiblioteca'
        this.ventanaBiblioteca = new VentanaBiblioteca(cardLayout, panelContenido, idUsuarioLogueado); // <--- ¡CAMBIO AQUÍ!

        panelContenido.add(ventanaTienda, "Tienda"); // Usa la variable de instancia
        panelContenido.add(panelPerfil, "Perfil");
        panelContenido.add(panelSoporte, "Soporte");
        panelContenido.add(ventanaCarrito, "Carrito");
        panelContenido.add(this.ventanaBiblioteca, "Biblioteca"); // <--- Usa la variable de instancia
        //panelContenido.add(new VentanaDescripcion("", "", cardLayout, panelContenido, ventanaCarrito), "DetalleJuego");

        add(panelContenido, BorderLayout.CENTER);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        JButton botonCerrarSesion = new JButton("Salir");
        configurarBotonSuperior(botonCerrarSesion);
        botonCerrarSesion.addActionListener(e -> {
            this.dispose();
            ventanaIDS.setVisible(true);
        });
        panelSuperior.add(botonCerrarSesion, BorderLayout.WEST);

        JPanel panelBotonesDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonesDerecha.setOpaque(false);

        JButton botonCarrito = new JButton("Carrito");
        configurarBotonSuperior(botonCarrito);
        botonCarrito.addActionListener(e -> cardLayout.show(panelContenido, "Carrito"));
        panelBotonesDerecha.add(botonCarrito);

        JButton botonBiblioteca = new JButton("Biblioteca");
        configurarBotonSuperior(botonBiblioteca);
        botonBiblioteca.addActionListener(e -> {
            // Ahora 'this.ventanaBiblioteca' ya no será null
            this.ventanaBiblioteca.cargarJuegosCompradosDesdeDB(idUsuarioLogueado); // <--- AHORA ESTA LÍNEA FUNCIONARÁ
            cardLayout.show(panelContenido, "Biblioteca");
        });
        panelBotonesDerecha.add(botonBiblioteca);

        JButton botonSoporte = new JButton("Soporte");
        configurarBotonSuperior(botonSoporte);
        // Se modifica el action listener del boton soporte para pasar el idUsuarioLogueado
        botonSoporte.addActionListener(e -> {
            ((VentanaSoporte) panelContenido.getComponent(2)).setIdUsuarioLogueado(idUsuarioLogueado);
            cardLayout.show(panelContenido, "Soporte");
        });
        panelBotonesDerecha.add(botonSoporte);

        JButton botonPerfil = new JButton("Perfil");
        configurarBotonSuperior(botonPerfil);
        botonPerfil.addActionListener(e -> cardLayout.show(panelContenido, "Perfil"));
        panelBotonesDerecha.add(botonPerfil);

        panelSuperior.add(panelBotonesDerecha, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);
    }

    private void configurarBotonSuperior(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(0, 123, 255));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(120, 40));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaIDS());
    }
}