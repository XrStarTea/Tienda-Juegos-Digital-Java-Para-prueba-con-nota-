package tienda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    public static Connection conectar() throws SQLException {
        String connectionUrl =
            "jdbc:sqlserver://HERNAN\\SQLEXPRESS:1433;" +
            "database=tienda_juegos;" +
            "user=sa;" +
            "password=Hernan1891;" +
            "encrypt=true;" +
            "trustServerCertificate=true;" +
            "loginTimeout=30;";

        return DriverManager.getConnection(connectionUrl);
    }
}
