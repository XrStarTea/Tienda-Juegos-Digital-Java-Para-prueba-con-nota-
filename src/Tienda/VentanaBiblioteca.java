package tienda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;


public class VentanaBiblioteca extends JPanel {
    private JPanel panelJuegos;
    private CardLayout cardLayout;
    private JPanel panelContenido;
    private int idUsuarioLogueado;
    private JScrollPane scrollPane; // Declarado aquí

    public VentanaBiblioteca(CardLayout cardLayout, JPanel panelContenido, int idUsuarioLogueado) {
        this.cardLayout = cardLayout;
        this.panelContenido = panelContenido;
        this.idUsuarioLogueado = idUsuarioLogueado;

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

        // Asignar el JScrollPane a la variable de instancia (esto soluciona el "boton de biblioteca no aparece")
        this.scrollPane = new JScrollPane(panelJuegos); // <--- CAMBIO AQUÍ: Asigna a 'this.scrollPane'
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(16);

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
        add(this.scrollPane, BorderLayout.CENTER); // <--- CAMBIO AQUÍ: Usa 'this.scrollPane'
        add(panelBoton, BorderLayout.SOUTH);

        // La carga inicial se realiza en el constructor, pero también puede ser llamada desde fuera
        cargarJuegosCompradosDesdeDB(idUsuarioLogueado);
    }

    public void cargarJuegosCompradosDesdeDB(int idUsuario) {
        panelJuegos.removeAll(); // Limpiar el panel antes de cargar nuevos juegos
        List<Integer> idsJuegosComprados = new ArrayList<>();

        try (Connection connection = ConexionDB.conectar()) {
            String sql = "SELECT DISTINCT j.id AS id_juego, j.nombre, j.imagen, j.precio " + // <--- CORRECTO: j.descripcion ELIMINADA
                         "FROM juegos j JOIN compras c ON j.id = c.id_juego " +
                         "WHERE c.id_usuario = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idUsuario);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int idJuego = resultSet.getInt("id_juego");
                        if (!idsJuegosComprados.contains(idJuego)) {
                            String nombreJuego = resultSet.getString("nombre");
                            // String descripcionJuego = resultSet.getString("descripcion"); // <--- ELIMINAR ESTA LÍNEA, YA NO SE SELECCIONA
                            String imagenJuego = resultSet.getString("imagen");
                            double precioJuego = resultSet.getDouble("precio");

                            // Crear el objeto JuegoTienda (la descripción puede ser vacía o null aquí)
                            // Utiliza las variables locales nombreJuego, imagenJuego, precioJuego
                            JuegoTienda juego = new JuegoTienda(idJuego, nombreJuego, "", precioJuego, imagenJuego); // <--- CORREGIDO: variables y "" para descripcion

                            // Llama a tu método existente crearCardJuego con los datos de la DB
                            // Pasa una cadena vacía para la descripción si tu crearCardJuego la necesita
                            // y no la estás seleccionando de la DB
                            JPanel juegoPanel = crearCardJuego(nombreJuego, "", imagenJuego, precioJuego); // <--- CORREGIDO: "" para descripcion
                            panelJuegos.add(juegoPanel);
                            idsJuegosComprados.add(idJuego);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar juegos de la biblioteca: " + ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            panelJuegos.revalidate();
            panelJuegos.repaint();
        }
    }

    // Método adaptado para crear cada panel de juego para la biblioteca
    private JPanel crearCardJuego(String nombre, String descripcion, String rutaImagen, double precio) { // <--- PARAMETROS CORRECTOS
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(240, 240, 240));
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        card.setPreferredSize(new Dimension(200, 250));

        JLabel labelImagen = cargarImagenJuego(rutaImagen);

        labelImagen.setHorizontalAlignment(SwingConstants.CENTER);
        labelImagen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelInfo = new JPanel(new GridLayout(2, 1));
        panelInfo.setBackground(new Color(230, 230, 230));

        JLabel labelNombre = new JLabel(nombre, SwingConstants.CENTER);
        labelNombre.setFont(new Font("Arial", Font.BOLD, 14));
        labelNombre.setToolTipText(descripcion); // La descripción viene del parámetro, que ahora será ""

        JLabel labelPrecio = new JLabel(String.format("Comprado: $%.2f", precio), SwingConstants.CENTER);
        labelPrecio.setFont(new Font("Arial", Font.PLAIN, 12));

        panelInfo.add(labelNombre);
        panelInfo.add(labelPrecio);

        card.add(labelImagen, BorderLayout.CENTER);
        card.add(panelInfo, BorderLayout.SOUTH);

        return card;
    }

    // Métodos cargarImagenJuego, crearLabelDesdeImagen, crearLabelSinImagen, crearLabelErrorImagen
    // (Estos los dejas como están, ya que no son parte de este problema específico)
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
            e.printStackTrace();
            return crearLabelErrorImagen();
        }
    }

    private JLabel crearLabelDesdeImagen(Image image) {
        Image img = image.getScaledInstance(180, 180, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(img));
    }

    private JLabel crearLabelSinImagen() {
        JLabel label = new JLabel("Sin imagen", JLabel.CENTER);
        label.setPreferredSize(new Dimension(180, 180));
        label.setFont(new Font("Arial", Font.ITALIC, 14));
        label.setForeground(Color.RED);
        return label;
    }

    private JLabel crearLabelErrorImagen() {
        JLabel label = new JLabel("Imagen no encontrada", JLabel.CENTER);
        label.setPreferredSize(new Dimension(180, 180));
        label.setFont(new Font("Arial", Font.ITALIC, 14));
        label.setForeground(Color.RED);
        return label;
    }
}