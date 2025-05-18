package tienda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class VentanaCarrito extends JPanel {
    private DefaultTableModel modeloTabla;
    private JTable tablaCarrito;
    private List<Producto> productosEnCarrito;
    private JLabel labelTotal;
    private JPanel panelContenido;
    private CardLayout cardLayout;

    public VentanaCarrito(CardLayout cardLayout, JPanel panelContenido) {
        this.cardLayout = cardLayout;
        this.panelContenido = panelContenido;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        productosEnCarrito = new ArrayList<>();

        // Título
        JLabel labelCarrito = new JLabel("Carrito de Compras", SwingConstants.CENTER);
        labelCarrito.setFont(new Font("Arial", Font.BOLD, 18));
        labelCarrito.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Modelo de tabla para el carrito
        modeloTabla = new DefaultTableModel(new Object[]{"Producto", "Precio", "Cantidad", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };

        tablaCarrito = new JTable(modeloTabla);
        tablaCarrito.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaCarrito.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(tablaCarrito);

        // Panel de botones CRUD
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton botonActualizar = new JButton("Actualizar Cantidad");
        botonActualizar.addActionListener(this::actualizarCantidad);
        estilizarBoton(botonActualizar);

        JButton botonEliminar = new JButton("Eliminar Producto");
        botonEliminar.addActionListener(this::eliminarProducto);
        estilizarBoton(botonEliminar);

        JButton botonVaciar = new JButton("Vaciar Carrito");
        botonVaciar.addActionListener(this::vaciarCarrito);
        estilizarBoton(botonVaciar);

        panelBotones.add(botonActualizar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonVaciar);

        // Panel inferior con total y botón volver
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        labelTotal = new JLabel("Total: $0.00", SwingConstants.LEFT);
        labelTotal.setFont(new Font("Arial", Font.BOLD, 16));

        JButton botonVolver = new JButton("Volver");
        botonVolver.addActionListener(e -> cardLayout.show(panelContenido, "Tienda"));
        estilizarBoton(botonVolver);

        JButton botonComprar = new JButton("Finalizar Compra");
        botonComprar.addActionListener(this::finalizarCompra);
        estilizarBoton(botonComprar);

        JPanel panelBotonesInferiores = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotonesInferiores.add(botonComprar);
        panelBotonesInferiores.add(botonVolver);

        panelInferior.add(labelTotal, BorderLayout.WEST);
        panelInferior.add(panelBotonesInferiores, BorderLayout.EAST);

        add(labelCarrito, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.PAGE_END);
    }

    private void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(0, 123, 255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    // Método para agregar productos desde la tienda
    public void agregarProductoDesdeTienda(String nombre, double precio) {
        // Verificar si el producto ya está en el carrito
        for (int i = 0; i < productosEnCarrito.size(); i++) {
            Producto p = productosEnCarrito.get(i);
            if (p.getNombre().equals(nombre)) {
                p.setCantidad(p.getCantidad() + 1);
                modeloTabla.setValueAt(p.getCantidad(), i, 2);
                modeloTabla.setValueAt(p.getPrecio() * p.getCantidad(), i, 3);
                actualizarTotal();
                return;
            }
        }

        // Si no existe, agregarlo como nuevo
        Producto nuevoProducto = new Producto(nombre, precio, 1);
        productosEnCarrito.add(nuevoProducto);
        modeloTabla.addRow(new Object[]{
            nombre,
            precio,
            1,
            precio
        });
        actualizarTotal();
    }

    private void actualizarCantidad(ActionEvent e) {
        int filaSeleccionada = tablaCarrito.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un producto", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int nuevaCantidad = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            if (nuevaCantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Producto producto = productosEnCarrito.get(filaSeleccionada);
            producto.setCantidad(nuevaCantidad);
            modeloTabla.setValueAt(producto.getPrecio() * nuevaCantidad, filaSeleccionada, 3);
            actualizarTotal();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto(ActionEvent e) {
        int filaSeleccionada = tablaCarrito.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un producto", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        productosEnCarrito.remove(filaSeleccionada);
        modeloTabla.removeRow(filaSeleccionada);
        actualizarTotal();
    }

    private void vaciarCarrito(ActionEvent e) {
        if (productosEnCarrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito ya está vacío", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro que desea vaciar el carrito?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            productosEnCarrito.clear();
            modeloTabla.setRowCount(0);
            actualizarTotal();
        }
    }

    private void finalizarCompra(ActionEvent e) {
        if (productosEnCarrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = 0.0;
        StringBuilder resumen = new StringBuilder("Resumen de compra:\n\n");
        for (Producto producto : productosEnCarrito) {
            resumen.append(String.format("- %s: %d x $%.2f = $%.2f\n", 
                producto.getNombre(), 
                producto.getCantidad(), 
                producto.getPrecio(), 
                producto.getPrecio() * producto.getCantidad()));
            total += producto.getPrecio() * producto.getCantidad();
        }
        resumen.append(String.format("\nTotal: $%.2f", total));

        int opcion = JOptionPane.showConfirmDialog(
            this, 
            resumen.toString(), 
            "Confirmar Compra", 
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );

        if (opcion == JOptionPane.OK_OPTION) {
            VentanaBiblioteca biblioteca = null;
            for (Component comp : panelContenido.getComponents()) {
                if (comp instanceof VentanaBiblioteca) {
                    biblioteca = (VentanaBiblioteca) comp;
                    break;
                }
            }
            
            if (biblioteca != null) {
                biblioteca.agregarJuegosComprados(new ArrayList<>(productosEnCarrito));
            }
            
            JOptionPane.showMessageDialog(this, "Compra realizada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            vaciarCarrito(null);
        }
    }

    private void actualizarTotal() {
        double total = 0.0;
        for (Producto producto : productosEnCarrito) {
            total += producto.getPrecio() * producto.getCantidad();
        }
        labelTotal.setText(String.format("Total: $%.2f", total));
    }

    // Método para obtener los productos en el carrito (para el contador)
    public List<Producto> getProductosEnCarrito() {
        return new ArrayList<>(productosEnCarrito);
    }

    // Clase interna para representar productos en el carrito
    public static class Producto {
        private String nombre;
        private double precio;
        private int cantidad;

        public Producto(String nombre, double precio, int cantidad) {
            this.nombre = nombre;
            this.precio = precio;
            this.cantidad = cantidad;
        }

        public String getNombre() { return nombre; }
        public double getPrecio() { return precio; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }
}