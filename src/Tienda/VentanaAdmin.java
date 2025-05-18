package tienda;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VentanaAdmin extends JFrame {
    private JTable tablaJuegos;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtPrecio;
    private JTextArea txtDescripcion;
    private JButton btnAgregar, btnGuardar, btnEliminar;
    private boolean modoOscuro = false;

    public VentanaAdmin() {
        // Estilo visual inicial
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Administración de Juegos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Modelo de tabla
        modeloTabla = new DefaultTableModel(new Object[]{"Nombre", "Precio", "ID", "Descripción"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaJuegos = new JTable(modeloTabla);
        add(new JScrollPane(tablaJuegos), BorderLayout.CENTER);

        // Panel de controles
        JPanel panelControles = new JPanel(new GridLayout(0, 2, 10, 10));
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNombre = new JTextField(15);
        txtPrecio = new JTextField(10);
        txtDescripcion = new JTextArea(4, 15);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);

        // Añadir componentes al panel
        panelControles.add(new JLabel("Nombre:"));
        panelControles.add(txtNombre);
        panelControles.add(new JLabel("Precio:"));
        panelControles.add(txtPrecio);
        panelControles.add(new JLabel("Descripción:"));
        panelControles.add(scrollDescripcion);

        add(panelControles, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnAgregar = new JButton("Agregar");
        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        JButton btnModo = new JButton("Modo Oscuro");
        JButton btnVolver = new JButton("Volver");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnModo);
        panelBotones.add(btnVolver);
        add(panelBotones, BorderLayout.SOUTH);

        // Acciones de botones
        btnAgregar.addActionListener(e -> agregarJuego());
        btnGuardar.addActionListener(e -> guardarJuego());
        btnEliminar.addActionListener(e -> eliminarJuego());

        btnModo.addActionListener(e -> cambiarModo(btnModo));
        btnVolver.addActionListener(e -> {
            dispose();
            new VentanaIDS().setVisible(true);
        });

        tablaJuegos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarJuegoSeleccionado();
        });

        cargarJuegos();
        setVisible(false);
    }

    private void cambiarModo(JButton boton) {
        try {
            if (modoOscuro) {
                UIManager.setLookAndFeel(new FlatLightLaf());
                boton.setText("Modo Oscuro");
            } else {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                boton.setText("Modo Claro");
            }
            modoOscuro = !modoOscuro;
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cargarJuegos() {
        modeloTabla.setRowCount(0);
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT id, nombre, precio, descripcion FROM juegos");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("id"),
                        rs.getString("descripcion")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los juegos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarJuego() {
        String nombre = txtNombre.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (nombre.isEmpty() || precioStr.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            if (precio <= 0) throw new NumberFormatException();

            try (Connection con = ConexionDB.conectar();
                 PreparedStatement ps = con.prepareStatement("INSERT INTO juegos (nombre, precio, descripcion) VALUES (?, ?, ?)")) {
                ps.setString(1, nombre);
                ps.setDouble(2, precio);
                ps.setString(3, descripcion);
                ps.executeUpdate();
                cargarJuegos();
                limpiarCampos();
            }

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al agregar el juego.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarJuego() {
        int fila = tablaJuegos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un juego.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 2);
        String nombre = txtNombre.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        try {
            double precio = Double.parseDouble(precioStr);
            if (precio <= 0) throw new NumberFormatException();

            try (Connection con = ConexionDB.conectar();
                 PreparedStatement ps = con.prepareStatement("UPDATE juegos SET nombre = ?, precio = ?, descripcion = ? WHERE id = ?")) {
                ps.setString(1, nombre);
                ps.setDouble(2, precio);
                ps.setString(3, descripcion);
                ps.setInt(4, id);
                ps.executeUpdate();
                cargarJuegos();
                limpiarCampos();
            }

        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos o error de base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarJuego() {
        int fila = tablaJuegos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un juego para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 2);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar este juego?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection con = ConexionDB.conectar();
                 PreparedStatement ps = con.prepareStatement("DELETE FROM juegos WHERE id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
                cargarJuegos();
                limpiarCampos();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el juego.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarJuegoSeleccionado() {
        int fila = tablaJuegos.getSelectedRow();
        if (fila != -1) {
            txtNombre.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtPrecio.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtDescripcion.setText(modeloTabla.getValueAt(fila, 3).toString());
        } else {
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtDescripcion.setText("");
    }
}