package es.uco.pw.data.DAOs;

import es.uco.pw.business.DTOs.MaterialDTO;
import es.uco.pw.business.DTOs.MaterialDTO.EstadoMaterial;
import es.uco.pw.business.DTOs.MaterialDTO.TipoMaterial;
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
 * Clase que gestiona los materiales en la base de datos.
 */
public class MaterialDAO {

	/**
     * Objeto connection para la conexión con la base de datos.
     */
    private Connection connection;
    
    /**
     * Objeto properties para las sentencias SQL.
     */
    private Properties properties;

    /**
     * Constructor que inicializa la conexión con base de datos.
     */
    public MaterialDAO() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("sql.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new FileNotFoundException("Properties file 'sql.properties' not found in classpath");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserta un nuevo material en la base de datos.
     *
     * @param material Objeto MaterialDTO que se desea insertar.
     * @return respuesta True si la operación es exitosa, false de lo contrario.
     */
    public int insertMaterial(MaterialDTO material) {
    	
        int respuesta = -1; // Valor por defecto para error desconocido
        String query = properties.getProperty("insert_material");
        String checkQuery = properties.getProperty("check_material_id"); // Consulta para verificar ID existente

        // Validaciones iniciales
        if (material == null) {
        	respuesta = -2;
            return respuesta; // Error: Material no proporcionado
        }

        if (material.getTipoMaterial() == null) {
        	respuesta = -3;
            return respuesta; // Error: Tipo de material no válido
        }

        if (material.getEstadoMaterial() == null) {
        	respuesta = -4;
            return respuesta; // Error: Estado del material no válido
        }

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try {
            // Verificar si ya existe un material con el mismo ID
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, material.getIdMaterial());
                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next()) {
                	respuesta = -5;
                    return respuesta; // Error: Ya existe un material con este ID
                }
            }

            // Insertar el nuevo material
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, material.getIdMaterial());
                statement.setString(2, material.getTipoMaterial().name());
                statement.setBoolean(3, material.getUsoInterior());
                statement.setString(4, material.getEstadoMaterial().name());

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    respuesta = 0; // Operación exitosa
                } else {
                    respuesta = -7; // Error: No se pudo insertar el material
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            respuesta = -6; // Error: Excepción SQL
        } finally {
            db.closeConnection();
        }

        return respuesta;
    }
    /**
     * Busca un material por su ID.
     *
     * @param idMaterial El ID del material que se desea buscar.
     * @return material Un objeto MaterialDTO si se encuentra, null en caso contrario.
     */
    public MaterialDTO findMaterialById(int idMaterial) {
        MaterialDTO material = null;
        String query = properties.getProperty("find_material_by_id");

        DBConnection db = new DBConnection();
        Connection connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idMaterial);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Ajuste de nombres de columna para coincidir con la base de datos
                TipoMaterial tipoMaterial = TipoMaterial.valueOf(resultSet.getString("tipo"));
                boolean usoInterior = resultSet.getBoolean("uso");
                EstadoMaterial estadoMaterial = EstadoMaterial.valueOf(resultSet.getString("estado"));

                material = new MaterialDTO(idMaterial, tipoMaterial, usoInterior, estadoMaterial);
            } else {
            	// No se encontró el material.
            	material = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return material;
    }


    /**
     * Actualiza la información de un material en la base de datos.
     *
     * @param material El objeto MaterialDTO con los nuevos datos.
     * @return respuesta Codigo de respuesta.
     */
    public int updateMaterial(MaterialDTO material) {
        int respuesta = -1; // Valor por defecto para error desconocido
        String query = properties.getProperty("update_material");
        String checkQuery = properties.getProperty("check_material_id"); // Consulta para verificar existencia de material

        if (material == null) {
        	respuesta = -2;
            return respuesta; // Error: Material no proporcionado
        }

        if (material.getTipoMaterial() == null) {
        	respuesta = -3;
            return respuesta; // Error: Tipo de material no especificado
        }

        if (material.getEstadoMaterial() == null) {
        	respuesta = -4;
            return respuesta; // Error: Estado del material no especificado
        }

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try {
            // Verificar si el material existe
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, material.getIdMaterial());
                ResultSet resultSet = checkStatement.executeQuery();
                if (!resultSet.next()) {
                	respuesta = -5;
                    return respuesta; // Error: El material no existe
                }
            }

            // Actualizar el material
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, material.getTipoMaterial().name());
                statement.setBoolean(2, material.getUsoInterior());
                statement.setString(3, material.getEstadoMaterial().name());
                statement.setInt(4, material.getIdMaterial());

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    respuesta = 0; // Operación exitosa
                } else {
                    respuesta = -6; // Error: No se pudo actualizar el material
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            respuesta = -7; // Error: Excepción SQL
        } finally {
            db.closeConnection();
        }

        return respuesta;
    }


    /**
     * Elimina un material de la base de datos.
     *
     * @param idMaterial El ID del material que se desea eliminar.
     * @return respuesta Código de respuesta.
     */
    public int eliminarMaterial(int idMaterial) {
        int respuesta = -1; // Valor por defecto para error desconocido
        String query = properties.getProperty("delete_material");
        String checkQuery = properties.getProperty("check_material_id"); // Consulta para verificar existencia

        if (idMaterial <= 0) {
        	respuesta = -2;
            return respuesta; // Error: ID de material no válido
        }

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try {
            // Verificar si existe un material con el ID dado
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, idMaterial);
                ResultSet resultSet = checkStatement.executeQuery();
                if (!resultSet.next()) {
                	respuesta = -3;
                    return respuesta; // Error: No existe un material con este ID
                }
            }

            // Eliminar el material
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, idMaterial);

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    respuesta = 0; // Operación exitosa
                } else {
                    respuesta = -4; // Error: No se pudo eliminar el material
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            respuesta = -5; // Error: Excepción SQL
        } finally {
            db.closeConnection();
        }

        return respuesta;
    }


    /**
     * Recupera todos los materiales de la base de datos.
     * 
     * @param vectorMateriales Vector de Objetos MaterialesDTO donde se guardaran los materiales listados.
     * @return materials Una lista de objetos MaterialDTO.
     */
    public int listarMateriales(Vector<MaterialDTO> vectorMateriales) {
    	
    	int respuesta = -1;
        String query = properties.getProperty("find.all.materials");
        DBConnection db = new DBConnection();
        connection = db.getConnection();

        if (vectorMateriales == null) {
            // Error: El vector proporcionado es null
            return respuesta;
        }

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Limpiar el vector antes de llenarlo
            vectorMateriales.clear();

            // Iterar sobre los resultados y agregarlos al vector
            while (resultSet.next()) {
                try {
                    int idMaterial = resultSet.getInt("idMaterial");
                    TipoMaterial tipoMaterial = TipoMaterial.valueOf(resultSet.getString("tipo"));
                    boolean usoInterior = resultSet.getBoolean("uso");
                    EstadoMaterial estadoMaterial = EstadoMaterial.valueOf(resultSet.getString("estado"));

                    MaterialDTO material = new MaterialDTO(idMaterial, tipoMaterial, usoInterior, estadoMaterial);
                    vectorMateriales.add(material);
                } catch (IllegalArgumentException e) {
                    // Error: Formato de datos inválido en el ResultSet
                	respuesta = -2;
                    return respuesta;
                }
            }

            if (vectorMateriales.isEmpty()) {
                // No se encontraron materiales
            	respuesta = -3;
                return respuesta;
            }

            // Operación exitosa
            respuesta = 0;
            return respuesta;

        } catch (SQLException e) {
            // Error en la consulta SQL
            e.printStackTrace();
            respuesta = -4;
            return respuesta;
        } finally {
            db.closeConnection();
        }
    }
    
    /**
     * Obtiene los materiales por pista.
     * 
     * @param nombrePista Nombre de la pista a obtener los materiales.
     * @return materiales Una lista de objetos MaterialDTO.
     */
    public List<MaterialDTO> obtenerMaterialesPorPista(String nombrePista) {
        List<MaterialDTO> materiales = new ArrayList<>();

        // Recuperar la consulta desde el archivo properties
        String query = properties.getProperty("obtener_materiales_por_pista");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nombrePista);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idMaterial = resultSet.getInt("idMaterial");
                TipoMaterial tipoMaterial = TipoMaterial.valueOf(resultSet.getString("tipo").toUpperCase());
                boolean usoInterior = resultSet.getBoolean("uso");
                EstadoMaterial estadoMaterial = EstadoMaterial.valueOf(resultSet.getString("estado").toUpperCase());

                MaterialDTO material = new MaterialDTO(idMaterial, tipoMaterial, usoInterior, estadoMaterial);
                materiales.add(material);
            }
        } catch (SQLException e) {
            
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }

        System.err.println("HOLA");
        return materiales;
    }


    
}
