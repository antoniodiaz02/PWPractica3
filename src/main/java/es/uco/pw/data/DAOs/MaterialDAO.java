package es.uco.pw.data.DAOs;

import es.uco.pw.business.DTOs.MaterialDTO;
import es.uco.pw.business.DTOs.MaterialDTO.EstadoMaterial;
import es.uco.pw.business.DTOs.MaterialDTO.TipoMaterial;
import es.uco.pw.common.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

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
        try (FileInputStream input = new FileInputStream("src/sql.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading SQL properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inserta un nuevo material en la base de datos.
     *
     * @param material El objeto MaterialDTO que se desea insertar.
     * @return respuesta True si la operación es exitosa, false de lo contrario.
     */
    public boolean insertMaterial(MaterialDTO material) {
        boolean respuesta = false;
        String query = properties.getProperty("insert_material");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, material.getIdMaterial());
            statement.setString(2, material.getTipoMaterial().name());
            statement.setBoolean(3, material.getUsoInterior());
            statement.setString(4, material.getEstadoMaterial().name());

            int rowsInserted = statement.executeUpdate();
            respuesta = rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting material: " + e.getMessage());
            e.printStackTrace();
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

        // Verificación adicional para ver si la consulta se cargó correctamente
        if (query == null) {
            System.err.println("La consulta SQL para 'find_material_by_id' no se cargó correctamente.");
            return null;
        }

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
                System.out.println("Material no encontrado para el ID: " + idMaterial);
            }
        } catch (SQLException e) {
            System.err.println("Error finding material by ID: " + e.getMessage());
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
     * @return respuesta True si la operación es exitosa, false de lo contrario.
     */
    public boolean updateMaterial(MaterialDTO material) {
        boolean respuesta = false;
        String query = properties.getProperty("update_material");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, material.getTipoMaterial().name());
            statement.setBoolean(2, material.getUsoInterior());
            statement.setString(3, material.getEstadoMaterial().name());
            statement.setInt(4, material.getIdMaterial());

            int rowsUpdated = statement.executeUpdate();
            respuesta = rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating material: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return respuesta;
    }

    /**
     * Elimina un material de la base de datos.
     *
     * @param idMaterial El ID del material que se desea eliminar.
     * @return respuesta True si la operación es exitosa, false de lo contrario.
     */
    public boolean deleteMaterial(int idMaterial) {
        boolean respuesta = false;
        String query = properties.getProperty("delete_material");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idMaterial);

            int rowsDeleted = statement.executeUpdate();
            respuesta = rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting material: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return respuesta;
    }

    /**
     * Recupera todos los materiales de la base de datos.
     * @return materials Una lista de objetos MaterialDTO.
     */
    public List<MaterialDTO> findAllMaterials() {
        List<MaterialDTO> materials = new ArrayList<>();
        String query = properties.getProperty("find_all_materials");

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int idMaterial = resultSet.getInt("idMaterial");
                TipoMaterial tipoMaterial = TipoMaterial.valueOf(resultSet.getString("tipoMaterial"));
                boolean usoInterior = resultSet.getBoolean("usoInterior");
                EstadoMaterial estadoMaterial = EstadoMaterial.valueOf(resultSet.getString("estadoMaterial"));

                MaterialDTO material = new MaterialDTO(idMaterial, tipoMaterial, usoInterior, estadoMaterial);
                materials.add(material);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving materials: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return materials;
    }
    
    /**
     * Obtiene los materiales por pista.
     * @return materiales Una lista de objetos MaterialDTO.
     */
    public List<MaterialDTO> obtenerMaterialesPorPista(String nombrePista) {
        List<MaterialDTO> materiales = new ArrayList<>();
        String query = "SELECT m.idMaterial, m.tipo, m.uso, m.estado " +
                "FROM Materiales m " +
                "JOIN Material_Pista mp ON m.idMaterial = mp.idMaterial " +
                "WHERE mp.nombrePista = ?";



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
            System.err.println("Error obteniendo materiales de la pista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }

        return materiales;
    }

}
