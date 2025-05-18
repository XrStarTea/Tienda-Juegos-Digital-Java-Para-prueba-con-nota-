package tienda;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

public class VentanaTienda extends JPanel {

    private CardLayout cardLayout;
    private JPanel panelContenido;
    private VentanaCarrito ventanaCarrito;

    public VentanaTienda(CardLayout cardLayout, JPanel panelContenido, VentanaCarrito ventanaCarrito) {
        this.cardLayout = cardLayout;
        this.panelContenido = panelContenido;
        this.ventanaCarrito = ventanaCarrito;
        setLayout(new BorderLayout());
        add(crearPanelTienda(), BorderLayout.CENTER);
    }

    private JPanel crearPanelTienda() {
        JPanel panelTienda = new JPanel(new BorderLayout());
        panelTienda.setBackground(Color.darkGray);

        JPanel panelJuegos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelJuegos.setBackground(Color.darkGray);

        java.util.List<JPanel> juegosPaneles = new ArrayList<>();

        try (Connection con = ConexionDB.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM juegos")) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String imagen = rs.getString("imagen");
                int precio = rs.getInt("precio");
                String descripcion = rs.getString("descripcion");

                juegosPaneles.add(crearPanelJuego(nombre, imagen, precio, descripcion));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los juegos desde la base de datos.");
        }

        final int juegosPorPagina = 6;
        final int[] paginaActual = {0};

        Runnable mostrarPagina = () -> {
            panelJuegos.removeAll();
            int inicio = paginaActual[0] * juegosPorPagina;
            for (int i = inicio; i < inicio + juegosPorPagina && i < juegosPaneles.size(); i++) {
                panelJuegos.add(juegosPaneles.get(i));
            }
            panelJuegos.revalidate();
            panelJuegos.repaint();
        };

        JLabel btnIzq = crearBotonNavegacion("<", () -> {
            if (paginaActual[0] > 0) {
                paginaActual[0]--;
                mostrarPagina.run();
            }
        });

        JLabel btnDer = crearBotonNavegacion(">", () -> {
            int totalPaginas = (int) Math.ceil((double) juegosPaneles.size() / juegosPorPagina);
            if (paginaActual[0] < totalPaginas - 1) {
                paginaActual[0]++;
                mostrarPagina.run();
            }
        });

        mostrarPagina.run();

        panelTienda.add(btnIzq, BorderLayout.WEST);
        panelTienda.add(panelJuegos, BorderLayout.CENTER);
        panelTienda.add(btnDer, BorderLayout.EAST);

        return panelTienda;
    }

    private JLabel crearBotonNavegacion(String texto, Runnable accion) {
        JLabel boton = new JLabel(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setForeground(Color.WHITE);
        boton.setOpaque(true);
        boton.setBackground(new Color(40, 40, 40));
        boton.setPreferredSize(new Dimension(30, 300));
        boton.setHorizontalAlignment(JLabel.CENTER);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }
        });
        return boton;
    }

    private JPanel crearPanelJuego(String nombre, String imagen, int precio, String descripcion) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(173, 216, 230));
        panel.setPreferredSize(new Dimension(230, 300));
        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255), 3));

        JLabel labelImagen = cargarImagenJuego(imagen);
        JLabel labelNombre = crearLabelNombre(nombre);
        JLabel labelPrecio = crearLabelPrecio(precio);

        panel.addMouseListener(crearMouseListener(nombre, descripcion, precio));
        panel.add(labelImagen, BorderLayout.NORTH);
        panel.add(labelNombre, BorderLayout.CENTER);
        panel.add(labelPrecio, BorderLayout.SOUTH);

        return panel;
    }

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
            return crearLabelErrorImagen();
        }
    }

    private JLabel crearLabelDesdeImagen(Image image) {
        Image img = image.getScaledInstance(230, 180, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(img));
    }

    private JLabel crearLabelSinImagen() {
        JLabel label = new JLabel("Sin imagen", JLabel.CENTER);
        label.setPreferredSize(new Dimension(230, 180));
        label.setFont(new Font("Arial", Font.ITALIC, 14));
        label.setForeground(Color.RED);
        return label;
    }

    private JLabel crearLabelErrorImagen() {
        JLabel label = new JLabel("Imagen no encontrada", JLabel.CENTER);
        label.setPreferredSize(new Dimension(230, 180));
        label.setFont(new Font("Arial", Font.ITALIC, 14));
        label.setForeground(Color.RED);
        return label;
    }

    private JLabel crearLabelNombre(String nombre) {
        JLabel label = new JLabel(nombre, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
        return label;
    }

    private JLabel crearLabelPrecio(int precio) {
        JLabel label = new JLabel("CLP$ " + precio, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(Color.darkGray);
        return label;
    }

    private MouseAdapter crearMouseListener(String nombre, String descripcion, int precio) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String descripcionCompleta = descripcion + "\n\nPrecio: CLP$ " + precio;
                VentanaDescripcion ventanaDescripcion = new VentanaDescripcion(
                    nombre,
                    descripcionCompleta,
                    cardLayout,
                    panelContenido,
                    ventanaCarrito
                );
                panelContenido.add(ventanaDescripcion, "DetalleJuego");
                cardLayout.show(panelContenido, "DetalleJuego");
            }
        };
    }
}