package tienda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;       // Importar Connection
import java.sql.PreparedStatement; // Importar PreparedStatement
import java.sql.SQLException;      // Importar SQLException

public class VentanaCarrito extends JPanel {
    private DefaultTableModel modeloTabla;
    private JTable tablaCarrito;
    private List<Producto> productosEnCarrito;
    private JLabel labelTotal;
    private JPanel panelContenido;
    private CardLayout cardLayout;
    private int idUsuarioLogueado; // Campo para el ID del usuario logueado

    public VentanaCarrito(CardLayout cardLayout, JPanel panelContenido, int idUsuarioLogueado) {
        this.cardLayout = cardLayout;
        this.panelContenido = panelContenido;
        this.idUsuarioLogueado = idUsuarioLogueado; // Asignar el ID del usuario
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
    public void agregarProductoDesdeTienda(int idJuego, String nombre, double precio, String imagen) {
        // Verificar si el producto ya está en el carrito
        for (int i = 0; i < productosEnCarrito.size(); i++) {
            Producto p = productosEnCarrito.get(i);
            if (p.getIdJuego() == idJuego) { // Comparar por idJuego en lugar de nombre para mayor precisión
                p.setCantidad(p.getCantidad() + 1);
                modeloTabla.setValueAt(p.getCantidad(), i, 2);
                modeloTabla.setValueAt(p.getPrecio() * p.getCantidad(), i, 3);
                actualizarTotal();
                return;
            }
        }

        // Si no existe, agregarlo como nuevo
        Producto nuevoProducto = new Producto(idJuego, nombre, precio, 1, imagen); // Pasa el idJuego
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
        // Lógica para guardar la compra en la base de datos
        boolean compraExitosa = guardarCompraEnDB(); // <--- LLAMADA AL NUEVO MÉTODO

        if (compraExitosa) {
            JOptionPane.showMessageDialog(this, "Compra realizada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            // Vaciar el carrito después de guardar en DB
            vaciarCarrito(null);

            // Actualizar la biblioteca después de la compra exitosa
            VentanaBiblioteca biblioteca = null;
            for (Component comp : panelContenido.getComponents()) {
                if (comp instanceof VentanaBiblioteca) {
                    biblioteca = (VentanaBiblioteca) comp;
                    break;
                }
            }
            if (biblioteca != null) {
                // Llama a cargarJuegosCompradosDesdeDB para refrescar desde la BD
                // NOTA: 'cargarJuegosCompradosDesdeDB' es un método que crearemos en VentanaBiblioteca
                // en el siguiente paso. Por ahora, si VentanaBiblioteca no lo tiene, ignorará esta línea
                // o te dará un error de compilación que corregiremos después.
                biblioteca.cargarJuegosCompradosDesdeDB(idUsuarioLogueado);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al procesar la compra. Intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }

    private void actualizarTotal() {
        double total = 0.0;
        for (Producto producto : productosEnCarrito) {
            total += producto.getPrecio() * producto.getCantidad();
        }
        labelTotal.setText(String.format("Total: $%.2f", total));
    }
    // Nuevo método para guardar la compra en la base de datos
    private boolean guardarCompraEnDB() {
        if (idUsuarioLogueado == 0) { // Asegúrate de que el ID del usuario esté establecido
            JOptionPane.showMessageDialog(this, "Error: Sesión de usuario no válida. Por favor, inicie sesión nuevamente.", "Error de Sesión", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try (Connection connection = ConexionDB.conectar()) {
            // Desactiva el auto-commit para manejar la transacción manualmente
            connection.setAutoCommit(false);
            String insertSql = "INSERT INTO compras (id_usuario, id_juego, fecha_compra) VALUES (?, ?, GETDATE())";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                for (Producto producto : productosEnCarrito) {
                    preparedStatement.setInt(1, idUsuarioLogueado);
                    preparedStatement.setInt(2, producto.getIdJuego()); // Usar el idJuego
                    preparedStatement.addBatch(); // Añadir la operación al lote
                }
                int[] filasAfectadas = preparedStatement.executeBatch(); // Ejecutar todas las inserciones

                // Verificar si todas las inserciones fueron exitosas (opcional, pero buena práctica)
                for (int count : filasAfectadas) {
                    if (count < 0) { // Indica que una operación en el lote falló
                        connection.rollback(); // Deshacer si algo falla
                        System.err.println("DEBUG: Una inserción en el lote falló.");
                        return false;
                    }
                }
                connection.commit(); // Confirmar la transacción
                return true;
            }
        } catch (SQLException ex) {
            System.err.println("ERROR SQL al guardar compra:");
            ex.printStackTrace();
            // No es necesario un rollback aquí, el try-with-resources lo cierra y la conexión se maneja
            // Si la conexión no se cierra, la base de datos eventualmente hará un rollback.
            return false;
        }
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
        private String imagen;
        private int idJuego;

        public Producto(int idJuego, String nombre, double precio, int cantidad, String imagen) { // <--- MODIFICAR EL CONSTRUCTOR
            this.nombre = nombre;
            this.precio = precio;
            this.cantidad = cantidad;
            this.imagen = imagen;
            this.idJuego = idJuego; // <--- ASIGNACIÓN DEL ID
        }

        public String getNombre() { return nombre; }
        public double getPrecio() { return precio; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
        public String getImagen() { return imagen; }
        public int getIdJuego() { return idJuego; } // <--- AGREGAR ESTE GETTER
    }
}