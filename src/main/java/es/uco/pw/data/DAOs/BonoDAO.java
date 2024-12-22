package es.uco.pw.data.DAOs;

import es.uco.pw.business.DTOs.BonoDTO;
import es.uco.pw.common.DBConnection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona los bonos en la base de datos.
 */
public class BonoDAO {

	/**
     * Objeto connection para crear la conexión con la base de datos.
     */
    private Connection connection;
    
    /**
     * Objeto properties encargado de las sentencias SQL.
     */
    private Properties properties;
    
    /**
     * Constructor para inicializar la conexión a la base de datos
     */
    public BonoDAO() {
    	properties = new Properties();
    try (InputStream input = getClass().getClassLoader().getResourceAsStream("sql.properties")) {
        if (input == null) {
            System.err.println("No se pudo encontrar el archivo sql.properties.");
            return;
        }
        properties.load(input);
    } catch (IOException e) {
        System.err.println("Error cargando el archivo sql.properties: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    /**
     * Obtiene la lista de bonos de un usuario por su correo electrónico
     * 
     * @param correoUser Correo electrónico del usuario.
     * @return bonos Lista de bonos asociados al usuario
     */
    public List<BonoDTO> obtenerBonosPorCorreo(String correoUser) {
    	
        List<BonoDTO> bonos = new ArrayList<>();
        
        String queryBonosPorCorreo = properties.getProperty("obtener_bonos_por_correo");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(queryBonosPorCorreo)) {
            // Configuramos el parámetro de la consulta
            stmt.setString(1, correoUser);
            ResultSet resultSet = stmt.executeQuery();

            // Iteramos sobre el resultado para crear la lista de bonos
            while (resultSet.next()) {
                BonoDTO bono = new BonoDTO();
                bono.setIdBono(resultSet.getInt("idBono"));
                bono.setUsuarioId(resultSet.getInt("usuarioId"));
                bono.setSesiones(resultSet.getInt("sesiones"));
                bono.setFechaInicio(resultSet.getDate("fechaInicio"));
                bono.setFechaCaducidad(resultSet.getDate("fechaCaducidad"));
                bono.setTipoPista(resultSet.getString("tipoPista"));
                bonos.add(bono);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }

        return bonos;
    }
    
    /**
     * Inserta un nuevo bono en la base de datos
     * 
     * @param bono Objeto BonoDTO con la información del bono a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertarBono(BonoDTO bono) {
    	
        String queryInsert = properties.getProperty("insertar_bono");
        
        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(queryInsert)) {
            stmt.setInt(1, bono.getUsuarioId());
            stmt.setInt(2, bono.getSesiones());
            stmt.setDate(3, new java.sql.Date(bono.getFechaInicio().getTime()));
            stmt.setDate(4, new java.sql.Date(bono.getFechaCaducidad().getTime()));
            stmt.setString(5, bono.getTipoPista());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	db.closeConnection();
        }
        return false;
    }

    
} 
