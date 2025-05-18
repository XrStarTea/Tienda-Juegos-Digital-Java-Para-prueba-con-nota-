package tienda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class VentanaPerfil extends JPanel {
    private CardLayout cardLayout;
    private JPanel panelContenido;

    private JLabel lblImagenPerfil;
    private JButton btnCambiarImagen;
    private JLabel lblUsuarioNombre;
    private JLabel lblNombreCompletoValor;
    private Image fondoGeneral;
    private Image fondoPerfil;

    public VentanaPerfil(CardLayout cardLayout, JPanel panelContenido) {
        this.cardLayout = cardLayout;
        this.panelContenido = panelContenido;

        initComponentes();
    }

    private void initComponentes() {
        setLayout(null);  // Layout absoluto para superponer imágenes y panel personalizado
        setBackground(new Color(240, 240, 240));

        try {
            fondoGeneral = new ImageIcon("src/tienda/imagenes/fondo.jpg").getImage();
            fondoPerfil = new ImageIcon("src/tienda/imagenes/perfil.jpg").getImage();
        } catch (Exception e) {
            fondoGeneral = null;
            fondoPerfil = null;
        }

        // Panel principal perfil (fondo con sombra)
        JPanel panelPerfil = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panelPerfil.setOpaque(false);
        panelPerfil.setBounds(30, 30, 340, 280);
        add(panelPerfil);

        // Fondo decorativo detrás imagen perfil
        JLabel fondoImagenPerfil = new JLabel();
        fondoImagenPerfil.setBounds(30, 30, 120, 120);
        if (fondoPerfil != null) {
            Image imgFondo = fondoPerfil.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            fondoImagenPerfil.setIcon(new ImageIcon(imgFondo));
        }
        panelPerfil.add(fondoImagenPerfil);

        // Imagen perfil redonda
        lblImagenPerfil = new JLabel();
        lblImagenPerfil.setBounds(30, 30, 120, 120);
        lblImagenPerfil.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        try {
            ImageIcon originalIcon = new ImageIcon("src/tienda/imagenes/usuario.jpg");
            Image img = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblImagenPerfil.setIcon(new ImageIcon(makeRoundedImage(img, 60)));
        } catch (Exception e) {
            lblImagenPerfil.setIcon(new ImageIcon(makeRoundedImage(
                new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB), 60)));
        }
        panelPerfil.add(lblImagenPerfil);
        panelPerfil.setComponentZOrder(lblImagenPerfil, 0);

        // Botón cambiar imagen
        btnCambiarImagen = new JButton("Cambiar Imagen");
        btnCambiarImagen.setBounds(30, 160, 140, 35);
        btnCambiarImagen.setBackground(new Color(30, 144, 255));
        btnCambiarImagen.setForeground(Color.WHITE);
        btnCambiarImagen.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnCambiarImagen.setFocusPainted(false);
        btnCambiarImagen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCambiarImagen.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnCambiarImagen.setBackground(new Color(0, 105, 217));
                btnCambiarImagen.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent evt) {
                btnCambiarImagen.setBackground(new Color(30, 144, 255));
            }
        });
        btnCambiarImagen.addActionListener(e -> cambiarImagenPerfil());
        panelPerfil.add(btnCambiarImagen);

        // Etiqueta Usuario
        JLabel lblUsuarioTexto = new JLabel("Usuario:");
        lblUsuarioTexto.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUsuarioTexto.setForeground(Color.WHITE);
        lblUsuarioTexto.setBounds(170, 50, 160, 30);
        panelPerfil.add(lblUsuarioTexto);

        lblUsuarioNombre = new JLabel("\"nombre de usuario\"");
        lblUsuarioNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblUsuarioNombre.setForeground(Color.WHITE);
        lblUsuarioNombre.setBounds(170, 80, 160, 30);
        panelPerfil.add(lblUsuarioNombre);

        // Etiqueta Nombre Completo
        JLabel lblNombreCompletoTexto = new JLabel("Nombre Completo:");
        lblNombreCompletoTexto.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNombreCompletoTexto.setForeground(Color.WHITE);
        lblNombreCompletoTexto.setBounds(30, 210, 200, 30);
        panelPerfil.add(lblNombreCompletoTexto);

        lblNombreCompletoValor = new JLabel("\"nombre completo\"");
        lblNombreCompletoValor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNombreCompletoValor.setForeground(Color.WHITE);
        lblNombreCompletoValor.setBounds(30, 240, 300, 30);
        panelPerfil.add(lblNombreCompletoValor);

        // Botón Volver centrado debajo
        JButton btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnVolver.setBackground(new Color(0, 123, 255));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setBounds(120, 320, 140, 40);
        btnVolver.addActionListener(e -> cardLayout.show(panelContenido, "Tienda"));
        add(btnVolver);
    }

    private Image makeRoundedImage(Image image, int radius) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage rounded = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = rounded.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, width, height, radius, radius));
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return rounded;
    }

    private void cambiarImagenPerfil() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar nueva imagen de perfil");
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            ImageIcon nuevaImagen = new ImageIcon(chooser.getSelectedFile().getAbsolutePath());
            Image img = nuevaImagen.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblImagenPerfil.setIcon(new ImageIcon(makeRoundedImage(img, 60)));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fondoGeneral != null) {
            g.drawImage(fondoGeneral, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Métodos para actualizar datos del usuario
    public void setNombreUsuario(String nombreUsuario) {
        lblUsuarioNombre.setText("\"" + nombreUsuario + "\"");
    }

    public void setNombreCompleto(String nombreCompleto) {
        lblNombreCompletoValor.setText("\"" + nombreCompleto + "\"");
    }
}