package es.uco.pw.data.DAOs;

import es.uco.pw.business.DTOs.BonoDTO;
import es.uco.pw.common.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BonoDAO {

    private Connection connection;
    
    /**
     * Constructor para inicializar la conexión a la base de datos
     */
    public BonoDAO() {
        DBConnection db = new DBConnection();
        this.connection = db.getConnection();
    }
    
    /**
     * Obtiene la lista de bonos de un usuario por su correo electrónico
     * 
     * @param correoUser El correo electrónico del usuario
     * @return Lista de bonos asociados al usuario
     */
    public List<BonoDTO> obtenerBonosPorCorreo(String correoUser) {
        List<BonoDTO> bonos = new ArrayList<>();
        String query = "SELECT b.idBono, b.usuarioId, b.sesiones, b.fechaInicio, b.fechaCaducidad, b.tipoPista " +
                       "FROM Bonos b " +
                       "JOIN Usuarios u ON b.usuarioId = u.idUsuario " +
                       "WHERE u.correoElectronico = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, correoUser);
            ResultSet resultSet = stmt.executeQuery();
            
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
            System.out.println("ERROR! No se pudo obtener los bonos del usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarConexion();
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
        String query = "INSERT INTO Bonos (usuarioId, sesiones, fechaInicio, fechaCaducidad, tipoPista) " +
                       "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bono.getUsuarioId());
            stmt.setInt(2, bono.getSesiones());
            stmt.setDate(3, new java.sql.Date(bono.getFechaInicio().getTime()));
            stmt.setDate(4, new java.sql.Date(bono.getFechaCaducidad().getTime()));
            stmt.setString(5, bono.getTipoPista());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("ERROR! No se pudo insertar el bono: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarConexion();
        }
        return false;
    }
    
    /**
     * Cierra la conexión con la base de datos
     */
    private void cerrarConexion() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("ERROR! No se pudo cerrar la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
} 
