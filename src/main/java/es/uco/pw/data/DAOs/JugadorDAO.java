package es.uco.pw.data.DAOs;

import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.common.DBConnection;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

public class JugadorDAO {

    private Connection connection;
    private Properties properties;

    public JugadorDAO() {
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

    // Modificado para aceptar tipoUsuario y contraseña
    public int insertJugador(JugadorDTO jugador) {
        int codigo = 0;
        String queryInsert = properties.getProperty("insert_usuario");
        String queryBuscar = properties.getProperty("buscar_por_correo");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statementBuscar = connection.prepareStatement(queryBuscar)) {

            // Comprobar si el usuario ya existe mediante el correo electrónico.
            statementBuscar.setString(1, jugador.getCorreoElectronico());
            ResultSet rs = statementBuscar.executeQuery();

            if (rs.next()) {
                codigo = -2; // Código para indicar que el usuario ya está registrado.
                return codigo;
            }

            // Si el usuario no existe, procedemos a la inserción.
            try (PreparedStatement statementInsert = connection.prepareStatement(queryInsert)) {
                statementInsert.setString(1, jugador.getNombre());
                statementInsert.setString(2, jugador.getApellidos());
                statementInsert.setDate(3, new java.sql.Date(jugador.getFechaNacimiento().getTime()));
                statementInsert.setDate(4, new java.sql.Date(jugador.getFechaInscripcion().getTime())); // fecha de inscripción
                statementInsert.setString(5, jugador.getCorreoElectronico());
                statementInsert.setString(6, jugador.getContraseña()); // Se inserta la contraseña
                statementInsert.setString(7, jugador.getTipoUsuario()); // Se inserta el tipo de usuario

                int rowsInserted = statementInsert.executeUpdate();
                codigo = rowsInserted > 0 ? 1 : 0; // Retorna 1 si se insertó correctamente, 0 en caso contrario.
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar el usuario en la base de datos: " + e.getMessage());
            return -1; // Código para indicar error general de base de datos.
        } finally {
            db.closeConnection();
        }

        return codigo;
    }
    
    public int buscarUsuarioPorCorreo(String correo) {
    	int codigo = 0;
        String queryBuscar = properties.getProperty("buscar_por_correo");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statementBuscar = connection.prepareStatement(queryBuscar)) {

            statementBuscar.setString(1, correo);
            ResultSet rs = statementBuscar.executeQuery();

            if (rs.next()) {
            	codigo = 1;
                return codigo;
            }
	    } catch (SQLException e) {
	        System.err.println("Error al buscar el usuario en la base de datos: " + e.getMessage());
	        return -1; // Código para indicar error general de base de datos.
	    } finally {
	        db.closeConnection();
	    }
	    return codigo;
    }


    public List<JugadorDTO> listarUsuarios() {
        String query = properties.getProperty("listar_usuarios");
        List<JugadorDTO> usuarios = new ArrayList<>(); // Lista para almacenar los usuarios

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombreCompleto");
                String apellidos = resultSet.getString("apellidos");
                Date fechaNacimiento = resultSet.getDate("fechaNacimiento");
                Date fechaInscripcion = resultSet.getDate("fechaInscripcion");
                String correoElectronico = resultSet.getString("correoElectronico");
                String tipoUsuario = resultSet.getString("tipoUsuario");

                String nombreCompleto = nombre + " " + apellidos;
                
                // Crear un objeto JugadorDTO y agregarlo a la lista
                JugadorDTO jugador = new JugadorDTO();
                jugador.setNombreCompleto(nombreCompleto);
                jugador.setFechaNacimiento(fechaNacimiento);
                jugador.setFechaInscripcion(fechaInscripcion);
                jugador.setCorreoElectronico(correoElectronico);
                jugador.setTipoUsuario(tipoUsuario); // Asignar tipo de usuario

                usuarios.add(jugador);
            }

        } catch (SQLException e) {
            System.err.println("Error listando usuarios: " + e.getMessage());
            usuarios = null; // En caso de error, retornar null
        } finally {
            db.closeConnection();
        }

        return usuarios; // Devolver la lista de usuarios
    }

    
    public int modificarUsuario(JugadorDTO jugador, String correo) {
        int codigo = 0;

        // Construcción dinámica de la query
        StringBuilder queryActualizar = new StringBuilder("UPDATE Usuarios SET ");
        boolean firstField = true;

        if (jugador.getNombre() != null) {
            queryActualizar.append("nombreCompleto = ?");
            firstField = false;
        }
        if (jugador.getApellidos() != null) {
            if (!firstField) queryActualizar.append(", ");
            queryActualizar.append("apellidos = ?");
            firstField = false;
        }
        if (jugador.getFechaNacimiento() != null) {
            if (!firstField) queryActualizar.append(", ");
            queryActualizar.append("fechaNacimiento = ?");
            firstField = false;
        }
        if (jugador.getContraseña() != null) {
            if (!firstField) queryActualizar.append(", ");
            queryActualizar.append("contraseña = ?");
        }
        queryActualizar.append(" WHERE correoElectronico = ?");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statementActualizar = connection.prepareStatement(queryActualizar.toString())) {
            int parameterIndex = 1;

            // Asignar valores dinámicamente
            if (jugador.getNombre() != null) {
                statementActualizar.setString(parameterIndex++, jugador.getNombre());
            }
            if (jugador.getApellidos() != null) {
                statementActualizar.setString(parameterIndex++, jugador.getApellidos());
            }
            if (jugador.getFechaNacimiento() != null) {
                statementActualizar.setDate(parameterIndex++, new java.sql.Date(jugador.getFechaNacimiento().getTime()));
            }
            if (jugador.getContraseña() != null) {
                statementActualizar.setString(parameterIndex++, jugador.getContraseña());
            }
            // Asignar el correo electrónico
            statementActualizar.setString(parameterIndex, correo);

            // Ejecutar la consulta
            codigo = statementActualizar.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al modificar el usuario en la base de datos: " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return codigo;
    }
    
    public boolean validarCredenciales(String correo, String contraseña) {
        boolean credencialesValidas = false;
        String query = "SELECT 1 FROM Usuarios WHERE correoElectronico = ? AND contraseña = ?";

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        if (connection == null) {
            System.err.println("Error: No se pudo establecer la conexión con la base de datos.");
            return false;
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, correo);
            stmt.setString(2, contraseña); // Aplica la función de cifrado si la contraseña está cifrada

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                credencialesValidas = true; // Se encontró una fila, las credenciales son válidas
            }
        } catch (SQLException e) {
            System.err.println("Error al validar las credenciales: " + e.getMessage());
        } finally {
            db.closeConnection();
        }

        return credencialesValidas;
    }

    /**
     * Obtiene la información de un jugador a partir de su correo electrónico.
     * @param correo Correo electrónico del jugador.
     * @return Objeto JugadorDTO con la información del jugador o null si no se encuentra.
     */
    public JugadorDTO obtenerJugadorPorCorreo(String correo) {
        JugadorDTO jugador = null;
        String query = "SELECT * FROM Usuarios WHERE correoElectronico = ?";

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        if (connection == null) {
            System.err.println("Error: No se pudo establecer la conexión con la base de datos.");
            return null;
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, correo);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                jugador = new JugadorDTO(
                    rs.getString("nombreCompleto"),         // Nombre del jugador
                    rs.getDate("fechaNacimiento"),          // Fecha de nacimiento
                    rs.getString("correoElectronico"),      // Correo electrónico
                    rs.getString("contraseña"),             // Contraseña (asegúrate de no exponerla)
                    rs.getString("tipoUsuario")             // Tipo de usuario (cliente o administrador)
                );
                System.out.println("Jugador recuperado: " + jugador.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la información del jugador: " + e.getMessage());
        } finally {
            db.closeConnection();
        }

        return jugador;
    }

    public int validarUsuario(String correo, String contrasena) {
    	
    	return 0;
    	
    	}
    
    
    
}