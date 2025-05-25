package tienda;

public class JuegoTienda {
    private int id; // ID del juego en la base de datos
    private String nombre;
    private String descripcion;
    private double precio;
    private String imagen; // Ruta o nombre del archivo de imagen

    public JuegoTienda(int id, String nombre, String descripcion, double precio, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
    }

    // Getters para acceder a la información del juego
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public String getImagen() {
        return imagen;
    }
}