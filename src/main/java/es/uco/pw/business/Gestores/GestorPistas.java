package es.uco.pw.business.Gestores;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import es.uco.pw.business.DTOs.PistaDTO;
import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.business.DTOs.MaterialDTO;
import es.uco.pw.data.DAOs.MaterialDAO;
import es.uco.pw.data.DAOs.PistaDAO;

/**
 * Clase que gestiona las pistas.
 * 
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */
public class GestorPistas {

    // Instanciación de los DAOs para acceder a las bases de datos correspondientes
    MaterialDAO daoMaterial = new MaterialDAO();
    PistaDAO daoPista = new PistaDAO();

    /**
     * Constructor de la clase GestorPistasDAO.
     */
    public GestorPistas() {}
    
    
 // In GestorPistas.java
    public MaterialDAO getDaoMaterial() {
        return daoMaterial;
    }

    /**
     * Método para insertar una pista en la base de datos.
     * 
     * @param pista PistaDTO con los datos de la pista a crear.
     * @return int Código de respuesta.
     */
    public int insertPista(PistaDTO pista) {
        // Insertar la nueva pista a través del DAO
        return daoPista.insertPista(pista);
    }
    
    /**
     * Método para crear un material en la base de datos.
     * 
     * @param material Objeto MaterialDTO a introducir.
     * @return int Código de respuesta.
     */
    public int crearMaterial(MaterialDTO material) {
        return daoMaterial.insertMaterial(material);
    }

    /**
     * Método para listar todas las pistas disponibles en la base de datos.
     * 
     * @param vectorPistas Vector de objetos PistaDTO donde se añadirán las pistas.
     * @return int Código de respuesta.
     */
    public int listarPistas(Vector<PistaDTO> vectorPistas) {
        // Obtener todas las pistas a través del DAO
        return daoPista.listarPistas(vectorPistas);
    }
    
    /**
     * Método para obtener los materiales pertenecientes a pistas.
     * 
     * @param nombrePista Nombre de la pista a consultar.
     * @return list MaterialDTO Lista de objetos MaterialDTO con los materiales asociados.
     */
    public List<MaterialDTO> obtenerMaterialesDePista(String nombrePista) {
        return daoMaterial.obtenerMaterialesPorPista(nombrePista);
    }

    /**
     * Método para buscar una pista por nombre.
     * 
     * @param nombre Nombre de la pista a buscar.
     * @return PistaDTO Pista encontrada.
     */
    public PistaDTO buscarPistaPorNombre(String nombre) {
        // Buscar la pista por nombre a través del DAO
        return daoPista.findPistaByNombre(nombre);
    }

    /**
     * Método para actualizar los datos de una pista.
     * 
     * @param pista Objeto PistaDTO con los datos actualizados.
     * @return int Código de respuesta.
     */
    public int updatePista(PistaDTO pista) {
        return daoPista.updatePista(pista);
    }

    /**
     * Método para eliminar una pista.
     * 
     * @param nombre Nombre de la pista a eliminar.
     * @return int Código de respuesta.
     */
    public int eliminarPista(String nombre) {
        return daoPista.eliminarPista(nombre);
    }

    /**
     * Método para asociar un material a una pista, si es posible.
     * 
     * @param nombrePista Nombre de la pista a asociar.
     * @param idMaterial Identificador del material a modificar.
     * @return int Código de respuesta.
     */
    public int asociarMaterialAPista(String nombrePista, int idMaterial) {
        // Crear una instancia de PistaDAO y llamar al método asociarMaterialAPista
        return daoPista.asociarMaterialAPista(nombrePista, idMaterial);
    }

    /**
     * Método para buscar pistas libres según el número de jugadores y tipo de pista.
     * 
     * @param numeroJugadores Número de jugadores que caben en la pista
     * @param esInterior Si la pista debe ser interior o exterior
     * @param List<PistaDTO> Lista de pistas libresç
     * @return result Código de resultado.
     */
    public int buscarPistasLibres(int numeroJugadores, boolean esInterior, Vector<PistaDTO> vectorPistas) {
        List<PistaDTO> pistasLibres = new ArrayList<>();

        int result = daoPista.listarPistas(vectorPistas);
        
        // Verificar pistas disponibles y cumplir requisitos de capacidad y tipo
        for (PistaDTO pista : vectorPistas) {
            if (pista.isDisponible() && pista.getMaxJugadores() >= numeroJugadores && pista.isInterior() == esInterior) {
                pistasLibres.add(pista);
            }
        }

        return result;
    }
    
    /**
     * Método para buscar el nombre de la pista.
     * 
     * @param pistaId Identificador de la pista a buscar el nombre.
     * @return String Nombre de la pista.
     */
    public String nombrePistas(int pistaId) {
        return daoPista.nombrePistas(pistaId);
    }
    
    /**
     * Busca una pista por su nombre asociado.
     * 
     * @param nombre Nombre de la pista a buscar.
     * @return PistaDTO Objeto pista si se encuentra.
     */
    public PistaDTO findPistaByNombre(String nombre) {
    	return daoPista.findPistaByNombre(nombre);
    }
    
    /**
     * Busca el identificador de la pista asociado a un nombre.
     * 
     * @param nombre Nombre de la pista a buscar.
     * @return int Identificador de la pista.
     */
    public int idPistaByNombre(String nombre) {
    	return daoPista.idPistaByNombre(nombre);
    }
}
