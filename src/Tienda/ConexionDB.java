package tienda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConexionDB {
//AQUI CAMBIAN SU URL DE JAVA A SQL
    public static Connection conectar() throws SQLException {
        String connectionUrl =
            "jdbc:sqlserver://XRSTARTEA\\SQLEXPRESS:1433;" +
            "database=tienda_juegos;" +
            "user=sa;" +
            "password=admin;" +
            "encrypt=true;" +
            "trustServerCertificate=true;" +
            "loginTimeout=30;";
        return DriverManager.getConnection(connectionUrl);
    }
}