package es.uco.pw.data.DAOs;

import es.uco.pw.business.DTOs.PistaDTO;
import es.uco.pw.common.DBConnection;

import java.sql.*;
import java.util.Properties;
import java.util.Vector;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Clase que gestiona las pistas en la base de datos.
 */
public class PistaDAO {

	/**
     * Objeto connection para crear la conexión con la base de datos.
     */
    private Connection connection;
    
    /**
     * Objeto properties encargado de las sentencias SQL.
     */
    private Properties properties;

    /**
     * Constructor que inicializa la conexión con base de datos.
     */
    public PistaDAO() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("sql.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new FileNotFoundException("Properties file 'sql.properties' not found in classpath");
            }
        } catch (IOException e) {
            System.err.println("Error loading SQL properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inserta una nueva pista en la base de datos si cumple con las validaciones necesarias.
     * 
     * @param pista Objeto {@link PistaDTO} que contiene los datos de la pista a insertar.
     * @return respuesta Código entero que representa el resultado de la operación.
     */
    public int insertPista(PistaDTO pista) {
        int respuesta = -1; // Valor por defecto para error desconocido
        String query = properties.getProperty("insert_pista");
        String checkQuery = properties.getProperty("check_pista_nombre"); // Consulta para verificar nombre existente

        if (pista == null) {
        	respuesta = -2;
            return respuesta; // Error: Pista no proporcionada
        }

        if (pista.getNombre() == null || pista.getNombre().isEmpty()) {
        	respuesta = -3;
            return respuesta; // Error: Nombre de la pista no válido
        }

        if (pista.getMaxJugadores() <= 0) {
        	respuesta = -4;
            return respuesta; // Error: Número máximo de jugadores no válido
        }

        if (pista.getTamanoPista() == null) {
        	respuesta = -5;
            return respuesta; // Error: Tamaño de la pista no especificado
        }

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try {
            // Verificar si ya existe una pista con el mismo nombre
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, pista.getNombre());
                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next()) {
                	respuesta = -6;
                    return respuesta; // Error: Ya existe una pista con este nombre
                }
            }

            // Insertar la nueva pista
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, pista.getNombre());
                statement.setBoolean(2, pista.isDisponible());
                statement.setBoolean(3, pista.isInterior());
                statement.setString(4, pista.getTamanoPista().name());
                statement.setInt(5, pista.getMaxJugadores());

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    respuesta = 0; // Operación exitosa
                } else {
                    respuesta = -7; // Error: No se pudo insertar la pista
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            respuesta = -8; // Error: Excepción SQL
        } finally {
            db.closeConnection();
        }

        return respuesta;
    }


    /**
     * Busca una pista por su nombre asociado.
     * 
     * @param nombre Nombre de la pista a buscar.
     * @return pista Objeto pista si se encuentra.
     */
    public PistaDTO findPistaByNombre(String nombre) {
        PistaDTO pista = null;
        String query = properties.getProperty("find_pista_by_nombre");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nombre);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    PistaDTO.TamanoPista tamanoPista = PistaDTO.TamanoPista.valueOf(resultSet.getString("tamano").toUpperCase());
                    pista = new PistaDTO(
                        resultSet.getString("nombre"),
                        resultSet.getBoolean("estado"),
                        resultSet.getString("tipo").equalsIgnoreCase("INTERIOR"),
                        tamanoPista,
                        resultSet.getInt("numMaxJugadores")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }

        return pista;
    }

    /**
     * Actualiza la información de una pista en la base de datos.
     *
     * @param pista Objeto PistaDTO con los nuevos datos.
     * @return respuesta True si la operación es exitosa, false de lo contrario.
     */
    public int updatePista(PistaDTO pista) {
        int respuesta = -1; // Valor por defecto para error desconocido
        String query = properties.getProperty("update_pista");
        String checkQuery = properties.getProperty("check_pista_nombre"); // Consulta para verificar existencia de pista

        if (pista == null) {
            return -2; // Error: Pista no proporcionada
        }

        if (pista.getNombre() == null || pista.getNombre().isEmpty()) {
            return -3; // Error: Nombre de la pista no válido
        }

        if (pista.getMaxJugadores() <= 0) {
            return -4; // Error: Número máximo de jugadores no válido
        }

        if (pista.getTamanoPista() == null) {
            return -5; // Error: Tamaño de la pista no especificado
        }

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try {
            // Verificar si la pista existe
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, pista.getNombre());
                ResultSet resultSet = checkStatement.executeQuery();
                if (!resultSet.next()) {
                    return -6; // Error: La pista no existe
                }
            }

            // Actualizar la pista
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setBoolean(1, pista.isDisponible());
                statement.setBoolean(2, pista.isInterior());
                statement.setString(3, pista.getTamanoPista().name());
                statement.setInt(4, pista.getMaxJugadores());
                statement.setString(5, pista.getNombre());

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    respuesta = 0; // Operación exitosa
                } else {
                    respuesta = -7; // Error: No se pudo actualizar la pista
                }
            }
        } catch (SQLException e) {

            System.err.println("Error al actualizar la pista: " + e.getMessage());

            e.printStackTrace();
            respuesta = -8; // Error: Excepción SQL
        } finally {
            db.closeConnection();
        }

        return respuesta;
    }


    /**
     * Elimina una pista de la base de datos.
     *
     * @param nombre Nombre de la pista que se desea eliminar.
     * @return respuesta True si la operación es exitosa, false de lo contrario.
     */
    public int eliminarPista(String nombre) {
        int respuesta = -1; // Valor por defecto para error desconocido
        String query = properties.getProperty("delete_pista");
        String checkQuery = properties.getProperty("check_pista_nombre"); // Consulta para verificar existencia

        if (nombre == null || nombre.isEmpty()) {
            return -2; // Error: Nombre no proporcionado o vacío
        }

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try {
            // Verificar si existe una pista con el nombre dado
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, nombre);
                ResultSet resultSet = checkStatement.executeQuery();
                if (!resultSet.next()) {
                    return -3; // Error: No existe una pista con este nombre
                }
            }

            // Eliminar la pista
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, nombre);

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    respuesta = 0; // Operación exitosa
                } else {
                    respuesta = -4; // Error: No se pudo eliminar la pista
                }
            }
        } catch (SQLException e) {

            System.err.println("Error al eliminar la pista: " + e.getMessage());

            e.printStackTrace();
            respuesta = -5; // Error: Excepción SQL
        } finally {
            db.closeConnection();
        }

        return respuesta;
    }


    /**
     * Lista las pistas en la base de datos.
     * 
     * @param vectorPistas Vector donde se almacenarán las pistas recuperadas.
     * @return resultado Código de resultado (0 si es exitoso, o un número negativo para errores).
     */
    public int listarPistas(Vector<PistaDTO> vectorPistas) {
    	int resultado = -1; //Resultado por defecto
        String query = properties.getProperty("listar_todas_las_pistas");
        DBConnection db = new DBConnection();
        connection = db.getConnection();

        if (vectorPistas == null) {
            // Error: El vector proporcionado es null
            return resultado;
        }

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Limpiar el vector antes de llenarlo
            vectorPistas.clear();

            // Iterar sobre los resultados y agregarlos al vector
            while (resultSet.next()) {
                try {
                    PistaDTO.TamanoPista tamanoPista = PistaDTO.TamanoPista.valueOf(resultSet.getString("tamano").toUpperCase());
                    PistaDTO pista = new PistaDTO(
                        resultSet.getString("nombre"),
                        resultSet.getBoolean("estado"),
                        resultSet.getBoolean("tipo"),
                        tamanoPista,
                        resultSet.getInt("numMaxJugadores")
                    );
                    vectorPistas.add(pista);
                } catch (IllegalArgumentException e) {
                    // Error: Formato de datos inválido en el ResultSet
                    resultado = -2;
                    return resultado;
                }
            }

            if (vectorPistas.isEmpty()) {
                // No se encontraron pistas
            	resultado = -3;
                return resultado;
            }

            // Operación exitosa
            resultado = 0;
            return resultado;

        } catch (SQLException e) {
            // Error en la consulta SQL
            e.printStackTrace();
            resultado = -4;
            return resultado;
        } finally {
            db.closeConnection();
        }
    }
    
    /**
     * Asocia el material a una pista.
     * 
     * @param nombrePista Nombre de la pista a asociar.
     * @param idMaterial Id del material a asociar.
     * @return respuesta Código de resultado.
     */
    public int asociarMaterialAPista(String nombrePista, int idMaterial) {
    	
    	int resultado = -1; //Código de error por defecto
    	
        String queryPista = properties.getProperty("obtener_tipo_pista");
        String queryMaterial = properties.getProperty("obtener_uso_material");
        String queryActualizarEstado = properties.getProperty("actualizar_estado_material");
        String queryInsert = properties.getProperty("insertar_material_a_pista");
        DBConnection db = new DBConnection();
        connection = db.getConnection();

        if (nombrePista == null || nombrePista.isEmpty()) {
            // Error: El nombre de la pista es null o vacío
            return resultado;
        }

        if (idMaterial <= 0) {
            // Error: El ID del material no es válido
        	resultado = -2;
            return resultado;
        }

        try {
            // Obtener el tipo de la pista (0: Exterior, 1: Interior)
            int tipoPista;
            try (PreparedStatement pstmtPista = connection.prepareStatement(queryPista)) {
                pstmtPista.setString(1, nombrePista);
                try (ResultSet rs = pstmtPista.executeQuery()) {
                    if (rs.next()) {
                        tipoPista = rs.getInt("tipo");
                    } else {
                        // Error: La pista no existe
                    	resultado = -3;
                        return resultado;
                    }
                }
            }

            // Obtener el uso y estado del material (uso: 0 Exterior, 1 Interior; estado: Disponible, Reservado, Mal Estado)
            int usoMaterial;
            String estadoMaterial;
            try (PreparedStatement pstmtMaterial = connection.prepareStatement(queryMaterial)) {
                pstmtMaterial.setInt(1, idMaterial);
                try (ResultSet rs = pstmtMaterial.executeQuery()) {
                    if (rs.next()) {
                        usoMaterial = rs.getInt("uso");
                        estadoMaterial = rs.getString("estado");
                    } else {
                        // Error: El material no existe
                    	resultado = -4;
                        return resultado;
                    }
                }
            }

            // Verificar si el material está en estado RESERVADO o MAL_ESTADO
            if ("RESERVADO".equalsIgnoreCase(estadoMaterial) || "MAL_ESTADO".equalsIgnoreCase(estadoMaterial)) {
                // Error: El material no está disponible para asociarse
            	resultado = -5;
                return resultado;
            }

            // Verificar restricción: no se pueden asociar materiales de interior a pistas de exterior y viceversa
            if (tipoPista != usoMaterial) {
                // Error: Tipos incompatibles
            	resultado = -6;
                return resultado;
            }

            // Realizar la asociación
            try (PreparedStatement pstmtInsert = connection.prepareStatement(queryInsert)) {
                pstmtInsert.setString(1, nombrePista);
                pstmtInsert.setInt(2, idMaterial);

                int rowsAffected = pstmtInsert.executeUpdate();

                if (rowsAffected > 0) {
                    // Actualizar el estado del material a RESERVADO
                    try (PreparedStatement pstmtActualizarEstado = connection.prepareStatement(queryActualizarEstado)) {
                        pstmtActualizarEstado.setString(1, "RESERVADO");
                        pstmtActualizarEstado.setInt(2, idMaterial);
                        pstmtActualizarEstado.executeUpdate();
                    }

                    // Operación exitosa
                    resultado = 0;
                    return resultado;
                } else {
                    // Error: No se afectaron filas, probablemente el nombre de la pista no existe
                	resultado = -7;
                    return resultado;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

            // Error en la consulta SQL
            resultado = -8;
            return resultado;

        } finally {
            db.closeConnection();
        }
    }
    
    
    
    /**
     * Devuelve el nombre de una pista según el id.
     * @param pistaId Es el id de la pista a buscar su nombre.
     * @return Devuelve el nombre de la pista.
     */
    public String nombrePistas(int pistaId) {
        String query = properties.getProperty("find_pista_by_id");
        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pistaId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {    
                    return resultSet.getString("nombre");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding pista by nombre: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }

        return "";
    }
    
    public int idPistaByNombre(String nombre) {
        String query = properties.getProperty("find_pista_by_nombre");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nombre);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                	return resultSet.getInt("idPista");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding pista by nombre: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return -1;
    }


}