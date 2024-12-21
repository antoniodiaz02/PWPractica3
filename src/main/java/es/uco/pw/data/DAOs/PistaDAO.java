package es.uco.pw.data.DAOs;

import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.business.DTOs.PistaDTO;


import es.uco.pw.common.DBConnection;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
     * Inserta una nueva pista en la base de datos.
     *
     * @param pista El objeto PistaDTO que se desea insertar.
     * @return respuesta True si la operación es exitosa, false de lo contrario.
     */
    public int insertPista(PistaDTO pista) {
        int respuesta = -1; // Valor por defecto para error desconocido
        String query = properties.getProperty("insert_pista");
        String checkQuery = properties.getProperty("check_pista_nombre"); // Consulta para verificar nombre existente

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
            // Verificar si ya existe una pista con el mismo nombre
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, pista.getNombre());
                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next()) {
                    return -6; // Error: Ya existe una pista con este nombre
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
            System.err.println("Error al insertar la pista: " + e.getMessage());
            e.printStackTrace();
            respuesta = -8; // Error: Excepción SQL
        } finally {
            db.closeConnection();
        }

        return respuesta;
    }


    /**
     * Busca una pista por su nombre asociado.
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
            System.err.println("Error finding pista by nombre: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }

        return pista;
    }

    /**
     * Actualiza la información de una pista en la base de datos.
     *
     * @param pista El objeto PistaDTO con los nuevos datos.
     * @return respuesta True si la operación es exitosa, false de lo contrario.
     */
    public boolean updatePista(PistaDTO pista) {
        boolean respuesta = false;
        String query = properties.getProperty("update_pista");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, pista.isDisponible());
            statement.setBoolean(2, pista.isInterior());
            statement.setString(3, pista.getTamanoPista().name());
            statement.setInt(4, pista.getMaxJugadores());
            statement.setString(5, pista.getNombre());

            int rowsUpdated = statement.executeUpdate();
            respuesta = rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating pista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return respuesta;
    }

    /**
     * Elimina una pista de la base de datos.
     *
     * @param nombre El nombre de la pista que se desea eliminar.
     * @return respuesta True si la operación es exitosa, false de lo contrario.
     */
    public boolean deletePista(String nombre) {
        boolean respuesta = false;
        String query = properties.getProperty("delete_pista");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nombre);

            int rowsDeleted = statement.executeUpdate();
            respuesta = rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting pista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return respuesta;
    }

    /**
     * Lista las pistas en la base de datos.
     * @return todasLasPistas Todas las pistas de la base de datos.
     */
    /**
     * Lista las pistas en la base de datos.
     * @param vectorPistas Vector donde se almacenarán las pistas recuperadas.
     * @return Código de resultado (0 si es exitoso, o un número negativo para errores).
     */
    public int listarPistas(Vector<PistaDTO> vectorPistas) {
        String query = properties.getProperty("listar_todas_las_pistas");
        DBConnection db = new DBConnection();
        connection = db.getConnection();

        if (vectorPistas == null) {
            // Error: El vector proporcionado es null
            return -1;
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
                        resultSet.getString("tipo").equalsIgnoreCase("INTERIOR"),
                        tamanoPista,
                        resultSet.getInt("numMaxJugadores")
                    );
                    vectorPistas.add(pista);
                } catch (IllegalArgumentException e) {
                    // Error: Formato de datos inválido en el ResultSet
                    System.err.println("Error processing pista data: " + e.getMessage());
                    return -2;
                }
            }

            if (vectorPistas.isEmpty()) {
                // No se encontraron pistas
                return -3;
            }

            // Operación exitosa
            return 0;

        } catch (SQLException e) {
            // Error en la consulta SQL
            System.err.println("Error listing pistas: " + e.getMessage());
            e.printStackTrace();
            return -4;
        } finally {
            db.closeConnection();
        }
    }
    
    /**
     * Asocia el material a una pista.
     * @param nombrePista Nombre de la pista a asociar.
     * @param idMaterial Id del material a asociar.
     * @return respuesta True si la operación es exitosa, false de lo contrario.
     */
    public int asociarMaterialAPista(String nombrePista, int idMaterial) {
        String queryPista = properties.getProperty("obtener_tipo_pista");
        String queryMaterial = properties.getProperty("obtener_uso_material");
        String queryInsert = properties.getProperty("insertar_material_a_pista");
        DBConnection db = new DBConnection();
        connection = db.getConnection();

        if (nombrePista == null || nombrePista.isEmpty()) {
            // Error: El nombre de la pista es null o vacío
            return -1;
        }

        if (idMaterial <= 0) {
            // Error: El ID del material no es válido
            return -2;
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
                        return -3;
                    }
                }
            }

            // Obtener el uso del material (0: Exterior, 1: Interior)
            int usoMaterial;
            try (PreparedStatement pstmtMaterial = connection.prepareStatement(queryMaterial)) {
                pstmtMaterial.setInt(1, idMaterial);
                try (ResultSet rs = pstmtMaterial.executeQuery()) {
                    if (rs.next()) {
                        usoMaterial = rs.getInt("uso");
                    } else {
                        // Error: El material no existe
                        return -4;
                    }
                }
            }

            // Verificar restricción: no se pueden asociar materiales de interior a pistas de exterior y viceversa
            if (tipoPista != usoMaterial) {
                // Error: Tipos incompatibles
                return -5;
            }

            // Realizar la asociación
            try (PreparedStatement pstmtInsert = connection.prepareStatement(queryInsert)) {
                pstmtInsert.setString(1, nombrePista);
                pstmtInsert.setInt(2, idMaterial);

                int rowsAffected = pstmtInsert.executeUpdate();

                if (rowsAffected > 0) {
                    // Operación exitosa
                    return 0;
                } else {
                    // Error: No se afectaron filas, probablemente el nombre de la pista no existe
                    return -6;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al asociar material a pista: " + e.getMessage());
            e.printStackTrace();

            // Error en la consulta SQL
            return -7;

        } finally {
            db.closeConnection();
        }
    }
}
