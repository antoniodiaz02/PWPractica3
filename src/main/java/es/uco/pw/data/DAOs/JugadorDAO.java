package es.uco.pw.data.DAOs;

import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.common.DBConnection;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

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
    
    public int listarUsuarios() {
        String query = properties.getProperty("listar_usuarios");
        int codigo = 0;

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            boolean hasUsers = false; 

            while (resultSet.next()) {
                hasUsers = true; 
                String nombre = resultSet.getString("nombre");
                String apellidos = resultSet.getString("apellidos");
                Date fechaNacimiento = resultSet.getDate("fechaNacimiento");
                Date fechaInscripcion = resultSet.getDate("fechaInscripcion");
                String correoElectronico = resultSet.getString("correoElectronico");
                String tipoUsuario = resultSet.getString("tipoUsuario"); // Mostrar el tipo de usuario

                System.out.printf("Nombre: %s %s\n", nombre, apellidos);
                System.out.printf("Fecha de Nacimiento: %s\n", fechaNacimiento);
                System.out.printf("Fecha de Inscripción: %s\n", fechaInscripcion);
                System.out.printf("Correo Electrónico: %s\n", correoElectronico);
                System.out.printf("Tipo de Usuario: %s\n", tipoUsuario);
                System.out.println("───────────────────────────────────────");
            }

            if (!hasUsers) {
            	codigo = 2;
            }

        } catch (SQLException e) {
            System.err.println("Error listando usuarios: " + e.getMessage());
            codigo = -1; 
        } finally {
            db.closeConnection();
        }

        return codigo;
    }
    
    public int modificarUsuario(JugadorDTO jugador, String correo) {
        int codigo = 0;
        StringBuilder queryActualizar = new StringBuilder("UPDATE Usuarios SET ");
        boolean firstField = true;

        if (jugador.getNombre() != null) {
            queryActualizar.append("nombre = ?");
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
            // Implementar la lógica para asignar los parámetros
        } catch (SQLException e) {
            System.err.println("Error al modificar el usuario en la base de datos: " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return codigo;
    }
    
    public JugadorDTO obtenerJugadorPorCorreoYContraseña(String correo, String contraseña) {
        JugadorDTO jugador = null;
        String query = "SELECT * FROM Usuarios WHERE correoElectronico = ? AND contraseña = ?";

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, correo);
            stmt.setString(2, contraseña); // O puedes aplicar alguna encriptación si la contraseña está encriptada

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Crear el objeto JugadorDTO con la información del jugador
                jugador = new JugadorDTO(
                    rs.getString("nombreCompleto"),
                    rs.getDate("fechaNacimiento"),
                    rs.getString("correo"),
                    rs.getString("contraseña"),
                    rs.getString("tipoUsuario")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el jugador: " + e.getMessage());
        } finally {
            db.closeConnection();
        }

        return jugador;  // Si no se encontró el usuario, devuelve null.
    }

    
    
}