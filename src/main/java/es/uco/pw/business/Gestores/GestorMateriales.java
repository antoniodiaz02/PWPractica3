package es.uco.pw.business.Gestores;

import java.util.Vector;

import es.uco.pw.business.DTOs.MaterialDTO;
import es.uco.pw.data.DAOs.MaterialDAO;
import es.uco.pw.data.DAOs.PistaDAO;

/**
 * Clase que gestiona los materiales.
 * 
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

public class GestorMateriales {

    // Instanciación de los DAOs para acceder a las bases de datos correspondientes.
    MaterialDAO daoMaterial = new MaterialDAO();
    PistaDAO daoPista = new PistaDAO();

    /**
     * Constructor
     */
    public GestorMateriales() {}

    /**
     * Inserta una material en la base de datos.
     * 
     * @param material Objeto MaterialDTO a insertar.
     * @return int Código de salida.
     */
    public int insertMaterial(MaterialDTO material) {
    
    	return daoMaterial.insertMaterial(material);
    }
    
    /**
     * Elimina un material en la base de datos.
     * 
     * @param idMaterial Identificador del material.
     * @return int Código de salida.
     */
    public int eliminarMaterial(int idMaterial) {
        
    	return daoMaterial.eliminarMaterial(idMaterial);
    }
    
    /**
     * Lista los materiales de la base de datos.
     * 
     * @param todosLosMateriales Vector de Objetos MaterialDTO donde se guardarán el listado.
     * @return int Código de salida.
     */
    public int listarMateriales(Vector<MaterialDTO> todosLosMateriales) {
    	
    	return daoMaterial.listarMateriales(todosLosMateriales);
    	
    }
    
    /**
     * Actualiza el material en la base de datos.
     * 
     * @param material Objeto MaterialDTO a actualizar.
     * @return int Código de salida.
     */
    public int updateMaterial(MaterialDTO material) {
    	return daoMaterial.updateMaterial(material);
    }
    
    
}
