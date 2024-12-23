package es.uco.pw.data.DAOs;

import es.uco.pw.business.DTOs.JugadorDTO;

import es.uco.pw.common.DBConnection;

import java.sql.*;
import java.util.Properties;
import java.util.Vector;
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

/**
 * Clase que gestiona los jugadores en la base de datos.
 */
public class JugadorDAO {

	/**
     * Objeto connection para la conexión con la base de datos.
     */
    private Connection connection;
    
    /**
     * Objeto properties para las sentencias SQL.
     */
    private Properties properties;

    /**
     * Constructor para inicializar la conexión a la base de datos.
     */
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

    /**
	 * Inserta un usuario en la base de datos.
	 * @param jugador Objeto JugadorDTO.
	 * @return codigo Código de respuesta.
	 */
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
        	codigo = -1;
            return codigo; // Código para indicar error general de base de datos.

        } finally {
            db.closeConnection();
        }

        return codigo;
    }
    
    /**
	 * Busca un usuario por correo electrónico asociado.
	 * @param correo Correo del usuario asociado.
	 * @return codigo Devuelve el código de error.
	 */
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
	    	codigo = -1;
	        return codigo; // Código para indicar error general de base de datos.
	    } finally {
	        db.closeConnection();
	    }
	    return codigo;
    }

    /**
	 * Lista los usuarios en la base de datos.
	 * @param vectorUsuarios Vector vacío donde se guardarán los usuarios.
	 * @return codigo Devuelve el código de error.
	 */
    public int listarUsuarios(Vector<JugadorDTO> vectorUsuarios) {
    	
    	int codigo = -1; // Codigo error por defecto.
    	
        String query = properties.getProperty("listar_usuarios");
        DBConnection db = new DBConnection();
        connection = db.getConnection();

        if (vectorUsuarios == null) {
            // Error: El vector proporcionado es null
            return codigo;
        }

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Limpiar el vector antes de llenarlo
            vectorUsuarios.clear();

            // Iterar sobre los resultados y agregarlos al vector
            while (resultSet.next()) {
                try {
                    String nombre = resultSet.getString("nombreCompleto");
                    String apellidos = resultSet.getString("apellidos");
                    Date fechaNacimiento = resultSet.getDate("fechaNacimiento");
                    Date fechaInscripcion = resultSet.getDate("fechaInscripcion");
                    String correoElectronico = resultSet.getString("correoElectronico");
                    String tipoUsuario = resultSet.getString("tipoUsuario");

                    String nombreCompleto = nombre + " " + apellidos;

                    // Crear un objeto JugadorDTO y agregarlo al vector
                    JugadorDTO jugador = new JugadorDTO();
                    jugador.setNombreCompleto(nombreCompleto);
                    jugador.setFechaNacimiento(fechaNacimiento);
                    jugador.setFechaInscripcion(fechaInscripcion);
                    jugador.setCorreoElectronico(correoElectronico);
                    jugador.setTipoUsuario(tipoUsuario);

                    vectorUsuarios.add(jugador);
                } catch (IllegalArgumentException e) {
                    // Error: Formato de datos inválido en el ResultSet
                    codigo = -2;
                    return codigo;
                }
            }

            if (vectorUsuarios.isEmpty()) {
                // No se encontraron usuarios
            	codigo = -3;
                return codigo;
            }

            // Operación exitosa
            codigo = 0;
            return codigo;

        } catch (SQLException e) {
            // Error en la consulta SQL
            e.printStackTrace();
            codigo = -4;
            return codigo;
        } finally {
            db.closeConnection();
        }
    }

    /**
	 * Modifica los datos de un usuario por su correo.
	 * @param jugador Objeto JugadorDTO.
	 * @param correo Correo del usuario a modificar.
	 * @return codigo Devuelve el código de error.
	 */
    public int modificarUsuario(JugadorDTO jugador, String correo) {
        int codigo = 0;

        // Construcción dinámica de la query (No podemos añadirla a Sql.Properties.
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
        	//Error SQL
            codigo = -10;
            return codigo;
        } finally {
            db.closeConnection();
        }
        return codigo;
    }

    /**
	 * Valida las credenciales de un usuario.
	 * @param correo Correo del usuario a validar.
	 * @param contraseña Contraseña del usuario a validar.
	 * @return codigo Devuelve el código de error.
	 */
    public boolean validarCredenciales(String correo, String contraseña) {
        boolean credencialesValidas = false;
        
        // Recuperar la consulta desde el archivo properties
        String query = properties.getProperty("validar_credenciales");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, correo);
            stmt.setString(2, contraseña);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                credencialesValidas = true; // Se encontró una fila, las credenciales son válidas
            }
        } catch (SQLException e) {
        	return credencialesValidas;
        } finally {
            db.closeConnection();
        }

        return credencialesValidas;
    }


    /**
     * Obtiene la información de un jugador a partir de su correo electrónico.
     * @param correo Correo electrónico del jugador.
     * @return jugador Objeto JugadorDTO con la información del jugador o null si no se encuentra.
     */
    public JugadorDTO obtenerJugadorPorCorreo(String correo) {
        JugadorDTO jugador = null;
        
        // Recuperar la consulta desde el archivo properties
        String query = properties.getProperty("obtener_jugador_por_correo");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

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
            }
        } catch (SQLException e) {
        	jugador = null;
        	return jugador;
        	
        } finally {
            db.closeConnection();
        }

        return jugador;
    }
    
    
    
}