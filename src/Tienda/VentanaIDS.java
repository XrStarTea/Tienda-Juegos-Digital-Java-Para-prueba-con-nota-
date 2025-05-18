package tienda;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingUtilities;

public class VentanaIDS extends JFrame {
    
    
    private JButton btnIniciarSesionPrincipal;
    private JButton btnCrearCuentaPrincipal;
    private JLabel lblTituloPrincipal;
    private JLabel lblFondoPrincipal;
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private PanelCrearCuenta panelCrearCuenta;
    private PanelInicioSesion panelInicioSesion;
    private VentanaPrincipal ventanaPrincipal;
    private VentanaAdmin ventanaAdmin;

    public VentanaIDS() {
        // Inicializar las ventanas principales (por ahora, solo la instancia)
        ventanaPrincipal = new VentanaPrincipal(this);
        ventanaAdmin = new VentanaAdmin();
        
        // Configuración básica de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(540, 540));
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        // Panel Principal (Bienvenida)
        JPanel panelPrincipal = new JPanel(new AbsoluteLayout());
        panelPrincipal.setBackground(new Color(51, 51, 51));

        lblTituloPrincipal = new JLabel("TIENDA DIGITAL");
        lblTituloPrincipal.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTituloPrincipal.setForeground(new Color(255, 255, 255));
        panelPrincipal.add(lblTituloPrincipal, new AbsoluteConstraints(130, 150));

        btnIniciarSesionPrincipal = new JButton("Iniciar sesión");
        btnIniciarSesionPrincipal.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnIniciarSesionPrincipal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cardLayout.show(panelContenedor, "InicioSesion");
            }
        });
        panelPrincipal.add(btnIniciarSesionPrincipal, new AbsoluteConstraints(180, 250, 180, 40));

        btnCrearCuentaPrincipal = new JButton("Crear cuenta");
        btnCrearCuentaPrincipal.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnCrearCuentaPrincipal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cardLayout.show(panelContenedor, "CrearCuenta");
            }
        });
        panelPrincipal.add(btnCrearCuentaPrincipal, new AbsoluteConstraints(180, 310, 180, 40));

        lblFondoPrincipal = new JLabel();
        lblFondoPrincipal.setIcon(new ImageIcon(getClass().getResource("/tienda/imagenes/fondoIDS.jpg")));
        panelPrincipal.add(lblFondoPrincipal, new AbsoluteConstraints(0, 0, 540, 540));
        escalarImagenDeFondo(lblFondoPrincipal);

        // Panel de Inicio de Sesión (ahora una instancia de la clase PanelInicioSesion)
        panelInicioSesion = new PanelInicioSesion(cardLayout, panelContenedor, ventanaPrincipal, ventanaAdmin);
        panelInicioSesion.setBackground(new Color(204, 204, 204));

        // Panel de Crear Cuenta (sin cambios en la instancia)
        panelCrearCuenta = new PanelCrearCuenta(cardLayout, panelContenedor);
        panelCrearCuenta.setBackground(new Color(204, 204, 204));

        panelContenedor.add(panelPrincipal, "Principal");
        panelContenedor.add(panelInicioSesion, "InicioSesion");
        panelContenedor.add(panelCrearCuenta, "CrearCuenta");

        getContentPane().add(panelContenedor);
        cardLayout.show(panelContenedor, "Principal");

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                escalarImagenDeFondo(lblFondoPrincipal);
            }
        });

        setVisible(true);
    }

    private void escalarImagenDeFondo(JLabel lbl) {
        ImageIcon imagenOriginal = (ImageIcon) lbl.getIcon();
        if (imagenOriginal != null && lbl.getWidth() > 0 && lbl.getHeight() > 0) {
            Image imagenEscalada = imagenOriginal.getImage().getScaledInstance(lbl.getWidth(), lbl.getHeight(), Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(imagenEscalada));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaIDS());
    }
}