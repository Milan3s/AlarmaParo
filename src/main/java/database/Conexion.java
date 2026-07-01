package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String HOST = "192.168.18.186";
    private static final String PORT = "3306";
    private static final String DATABASE = "mi_paro";
    private static final String USER = "root";
    private static final String PASSWORD = "milanes";

    private static final String URL
            = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=Europe/Madrid"
            + "&characterEncoding=UTF-8";

    private static Connection conexion;

    public static Connection getConexion() {

        try {

            if (conexion == null || conexion.isClosed()) {

                Class.forName("com.mysql.cj.jdbc.Driver");

                conexion = DriverManager.getConnection(URL, USER, PASSWORD);

                System.out.println("Conexión establecida correctamente.");

            }

        } catch (ClassNotFoundException | SQLException ex) {

            System.err.println("Error al conectar con la base de datos.");
            ex.printStackTrace();

        }

        return conexion;
    }

    public static void cerrarConexion() {

        try {

            if (conexion != null && !conexion.isClosed()) {

                conexion.close();

                System.out.println("Conexión cerrada.");

            }

        } catch (SQLException ex) {

            ex.printStackTrace();

        }

    }

}
