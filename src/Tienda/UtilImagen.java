import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class UtilImagen {

    // Ruta fija a la carpeta donde se copiarán las imágenes
    private static final String CARPETA_DESTINO = "C:\\Users\\nombr\\Documents\\NetBeansProjects\\TiendaDigital\\src\\Tienda\\imagenes\\";

    public static String copiarImagen(File archivoOriginal) throws IOException {
        // Obtener nombre del archivo (ej: "elden_ring.jpg")
        String nombreArchivo = archivoOriginal.getName();

        // Crear archivo destino
        File destino = new File(CARPETA_DESTINO + nombreArchivo);

        // Copiar el archivo, reemplazando si ya existe
        Files.copy(archivoOriginal.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // Devolver la ruta relativa que guardarás en la base de datos
        return "imagenes/" + nombreArchivo;
    }
}
