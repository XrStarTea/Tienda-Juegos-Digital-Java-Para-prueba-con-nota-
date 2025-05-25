package tienda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL; // ¡Importante! Añadir esta importación
import java.awt.MediaTracker; 
import java.awt.Container;

public class VentanaPerfil extends JPanel {
    private CardLayout cardLayout;
    private JPanel panelContenido;

    private JLabel lblImagenPerfil;
    private JLabel lblUsuarioNombre;
    private JLabel lblNombreCompletoValor;
    private Image fondoGeneral; 
    
    // Rutas para cargar como recursos del classpath (sin "src/")
    private static final String DEFAULT_USER_IMAGE_RESOURCE = "tienda/imagenes/perfil.png";
    private static final String DEFAULT_BACKGROUND_IMAGE_RESOURCE = "tienda/imagenes/fondo.jpg";

    public VentanaPerfil(CardLayout cardLayout, JPanel panelContenido) {
        this.cardLayout = cardLayout;
        this.panelContenido = panelContenido;

        initComponentes();
    }

    private void cargarImagenes() {
        try {
            // Cargar imagen de fondo principal
            // Usa ClassLoader para obtener el recurso
            URL backgroundUrl = getClass().getClassLoader().getResource(DEFAULT_BACKGROUND_IMAGE_RESOURCE);
            if (backgroundUrl != null) {
                fondoGeneral = new ImageIcon(backgroundUrl).getImage();
            } else {
                System.err.println("Recurso de fondo no encontrado: " + DEFAULT_BACKGROUND_IMAGE_RESOURCE);
                fondoGeneral = null;
            }
        } catch (Exception e) {
            System.err.println("Error al cargar imagen de fondo (excepción): " + e.getMessage());
            fondoGeneral = null;
        }
    }

    private void initComponentes() {
        setLayout(new GridBagLayout());
        setOpaque(false); 

        // ** Definición de fuentes y colores (consistentes con otros paneles) **
        Font fontTitulo = new Font("Segoe UI", Font.BOLD, 24);
        Font fontLabels = new Font("Segoe UI", Font.BOLD, 16); 
        Font fontValores = new Font("Segoe UI", Font.PLAIN, 16); 
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 15);

        Color colorPrimario = new Color(52, 152, 219); 
        Color colorSecundario = new Color(149, 165, 166); 
        Color colorTextoClaro = Color.WHITE;
        Color colorTitulo = new Color(44, 62, 80); 
        Color colorFondoPanelInfo = new Color(0, 0, 0, 150); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 

        // ------------------- Panel Principal de Información del Perfil -------------------
        JPanel panelInformacionPerfil = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(colorFondoPanelInfo); 
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); 
                g2d.dispose();
                super.paintComponent(g); 
            }
        };
        panelInformacionPerfil.setOpaque(false); 
        panelInformacionPerfil.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0; 
        gbc.weighty = 0; 
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER; 
        gbc.insets = new Insets(60, 0, 20, 0); 
        add(panelInformacionPerfil, gbc);

        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.insets = new Insets(5, 5, 5, 5); 

        // Panel para la imagen de perfil y el botón de cambiar imagen
        JPanel panelImagenYBoton = new JPanel(new GridBagLayout());
        panelImagenYBoton.setOpaque(false); 
        panelImagenYBoton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); 

        GridBagConstraints gbcImgBtn = new GridBagConstraints();
        gbcImgBtn.insets = new Insets(5, 5, 5, 5);
        gbcImgBtn.gridx = 0;
        gbcImgBtn.gridy = 0;
        gbcImgBtn.anchor = GridBagConstraints.CENTER;

        lblImagenPerfil = new JLabel();
        lblImagenPerfil.setPreferredSize(new Dimension(120, 120)); 
        lblImagenPerfil.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 3)); 
        
        // Carga la imagen de usuario por defecto desde el classpath aquí
        try {
            URL userImageUrl = getClass().getClassLoader().getResource(DEFAULT_USER_IMAGE_RESOURCE);
            if (userImageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(userImageUrl);
                Image img = originalIcon.getImage();

                // *** Uso del MediaTracker ***
                MediaTracker mt = new MediaTracker(new Container()); // Un contenedor dummy
                mt.addImage(img, 0);
                try {
                    mt.waitForID(0); // Espera a que la imagen se cargue completamente
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt(); // Restablece el estado de interrupción
                    System.err.println("Carga de imagen interrumpida.");
                }

                if (img.getWidth(null) != -1 && img.getHeight(null) != -1) {
                    System.out.println("Imagen de usuario cargada con éxito. Dimensiones: " + img.getWidth(null) + "x" + img.getHeight(null));
                    BufferedImage scaledImage = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = scaledImage.createGraphics();
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(img, 0, 0, 120, 120, null);
                    g2d.dispose();
                    lblImagenPerfil.setIcon(new ImageIcon(makeRoundedImage(scaledImage, 60)));
                } else {
                    System.err.println("ImageIcon no pudo cargar la imagen completamente. Dimensiones: " + img.getWidth(null) + "x" + img.getHeight(null));
                    System.err.println("Probable problema con el contenido del archivo de imagen: " + DEFAULT_USER_IMAGE_RESOURCE);
                    lblImagenPerfil.setIcon(new ImageIcon(makeRoundedImage(new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB), 60)));
                }
            } else {
                System.err.println("Recurso de imagen de usuario NO ENCONTRADO en el classpath: " + DEFAULT_USER_IMAGE_RESOURCE);
                lblImagenPerfil.setIcon(new ImageIcon(makeRoundedImage(new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB), 60)));
            }
        } catch (Exception e) {
            System.err.println("Error general al cargar imagen de usuario por defecto (excepción): " + e.getMessage());
            e.printStackTrace();
            lblImagenPerfil.setIcon(new ImageIcon(makeRoundedImage(new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB), 60)));
        }

        panelImagenYBoton.add(lblImagenPerfil, gbcImgBtn);

        gbcInfo.gridx = 0;
        gbcInfo.gridy = 0;
        gbcInfo.gridheight = 3; 
        gbcInfo.anchor = GridBagConstraints.NORTHWEST; 
        panelInformacionPerfil.add(panelImagenYBoton, gbcInfo);

        JLabel lblUsuarioTexto = new JLabel("Usuario:");
        lblUsuarioTexto.setFont(fontLabels);
        lblUsuarioTexto.setForeground(Color.WHITE);
        gbcInfo.gridx = 1;
        gbcInfo.gridy = 0;
        gbcInfo.gridheight = 1; 
        gbcInfo.anchor = GridBagConstraints.WEST;
        panelInformacionPerfil.add(lblUsuarioTexto, gbcInfo);

        lblUsuarioNombre = new JLabel("nombre de usuario"); // Valor por defecto sin comillas
        lblUsuarioNombre.setFont(fontValores);
        lblUsuarioNombre.setForeground(Color.WHITE);
        gbcInfo.gridy = 1; 
        panelInformacionPerfil.add(lblUsuarioNombre, gbcInfo);

        JLabel lblNombreCompletoTexto = new JLabel("Nombre Completo:");
        lblNombreCompletoTexto.setFont(fontLabels);
        lblNombreCompletoTexto.setForeground(Color.WHITE);
        gbcInfo.gridx = 0; 
        gbcInfo.gridy = 4; 
        gbcInfo.gridwidth = 2; 
        gbcInfo.anchor = GridBagConstraints.WEST;
        panelInformacionPerfil.add(lblNombreCompletoTexto, gbcInfo);

        lblNombreCompletoValor = new JLabel("nombre completo"); // Valor por defecto sin comillas
        lblNombreCompletoValor.setFont(fontValores);
        lblNombreCompletoValor.setForeground(Color.WHITE);
        gbcInfo.gridy = 5; 
        panelInformacionPerfil.add(lblNombreCompletoValor, gbcInfo);

        JButton btnVolver = new JButton("Volver");
        btnVolver.setFont(fontBotones); 
        btnVolver.setBackground(colorSecundario); 
        btnVolver.setForeground(colorTextoClaro);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setPreferredSize(new Dimension(140, 40));
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> cardLayout.show(panelContenido, "Tienda"));

        gbc.gridx = 0;
        gbc.gridy = 1; 
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0); 
        add(btnVolver, gbc);
    }

    private Image makeRoundedImage(Image image, int cornerRadius) {
        if (image == null) {
            // Devuelve una imagen en blanco si la entrada es nula o inválida
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); 
        }
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        // Asegúrate de que el ancho y alto sean válidos antes de crear BufferedImage
        if (width <= 0 || height <= 0) {
            System.err.println("Imagen con dimensiones inválidas en makeRoundedImage: " + width + "x" + height);
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); 
        }
        BufferedImage rounded = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = rounded.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, width, height)); 
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return rounded;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fondoGeneral != null) {
            g.drawImage(fondoGeneral, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(30, 40, 50)); 
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public void setNombreUsuario(String nombreUsuario) {
        lblUsuarioNombre.setText(nombreUsuario != null && !nombreUsuario.isEmpty() ? nombreUsuario : "N/A");
    }

    public void setNombreCompleto(String nombreCompleto) {
        lblNombreCompletoValor.setText(nombreCompleto != null && !nombreCompleto.isEmpty() ? nombreCompleto : "N/A");
    }
}