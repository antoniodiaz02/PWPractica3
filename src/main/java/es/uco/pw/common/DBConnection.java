package es.uco.pw.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para conectarnos a la base de datos.
 * 
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

public class DBConnection {

    protected Connection connection = null;

    protected String url = "jdbc:mysql://oraclepr.uco.es:3306/i82dibaa";
    protected String user = "i82dibaa";
    protected String password = "default";

    /**
     * Obtiene la conexión con la base de datos
     *  @return connection Conexión con la base de datos
     */
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
        } 
        catch (SQLException e) {
            System.err.println("Connection to MySQL has failed!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found.");
            e.printStackTrace();
        }
        return this.connection;
    }

    // We can include here other methods to encapsulate CRUD commands...

    /**
     * Cierra la conexión con la base de datos.
     *  @return None
     */
    public void closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error while trying to close the connection.");
            e.printStackTrace();
        }
    }
}
