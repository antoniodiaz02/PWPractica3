package es.uco.pw.business.Gestores;

import java.util.Vector;

import es.uco.pw.business.DTOs.MaterialDTO;
import es.uco.pw.data.DAOs.MaterialDAO;
import es.uco.pw.data.DAOs.PistaDAO;

/**
 * Clase que gestiona las pistas.
 */
public class GestorMateriales {

    // Instanciación de los DAOs para acceder a las bases de datos correspondientes
    MaterialDAO daoMaterial = new MaterialDAO();
    PistaDAO daoPista = new PistaDAO();

    /**
     * Constructor de la clase GestorPistasDAO.
     */
    public GestorMateriales() {}

    
    public int insertMaterial(MaterialDTO material) {
    
    	return daoMaterial.insertMaterial(material);
    }
    
    public int eliminarMaterial(int idMaterial) {
        
    	return daoMaterial.eliminarMaterial(idMaterial);
    }
    
    public int listarMateriales(Vector<MaterialDTO> todosLosMateriales) {
    	
    	return daoMaterial.listarMateriales(todosLosMateriales);
    	
    }
    
    public int updateMaterial(MaterialDTO material) {
    	return daoMaterial.updateMaterial(material);
    }
    
    
    
    
    
    
}
