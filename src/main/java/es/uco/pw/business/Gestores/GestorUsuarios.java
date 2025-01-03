package es.uco.pw.business.Gestores;

import java.util.Vector;

import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.data.DAOs.JugadorDAO;

/**
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

/**
 * Clase pricipal para gestionar usuarios.
 */
public class GestorUsuarios {
	
	// Instanciación de los DAOs para acceder a la base de datos.
    JugadorDAO daoJugador = new JugadorDAO();
    
    /**
	 * Añade un nuevo usuario.
	 * @param jugador JugadorDTO a añadir.
	 * @return int Código de salida.
	 */
    public int insertarUsuario(JugadorDTO jugador) {
        return daoJugador.insertJugador(jugador);
    }

    /**
	 * Lista usuarios en la base de datos.
	 * @param vectorUsuarios Vector de objetos JugadorDTO a rellenar.
	 * @return int Código de salida.
	 */
    public int listarUsuarios(Vector<JugadorDTO> vectorUsuarios) {
        return daoJugador.listarUsuarios(vectorUsuarios);
    }
    
    /**
	 * Modifica el usuario en base a el parámetro deseado. 
	 * @param jugador Objeto Jugador de JugadorDTO.
	 * @param correoModificar Correo del usuario a identificar.
	 * @return int Código de salida.
	 */
    public int modificarUsuario(JugadorDTO jugador, String correoModificar) {
        return daoJugador.modificarUsuario(jugador, correoModificar);
    }

    /**
	 * Busca usuario por correo.
	 * @param correo Correo del jugador a buscar en la base de datos.
	 * @return int Código de salida.
	 */
    public int buscarUsuarioPorCorreo(String correo) {
        return daoJugador.buscarUsuarioPorCorreo(correo);
    }
    
    /**
	 * Valida las credenciales de un usuario.
	 * @param correo Correo del jugador a buscar en la base de datos.
	 * @param contraseña Contraseña del usuario.
	 * @return boolean Código de salida True o False.
	 */
    public boolean validarCredenciales(String correo, String contraseña) {
    	return daoJugador.validarCredenciales(correo, contraseña);
    }
    
    /**
	 * Obtiene el jugador mediante su correo.
	 * @param correo Correo del jugador a buscar en la base de datos.
	 * @return JugadorDTO Objeto JugadorDTO con los datos del usuario.
	 */
    public JugadorDTO obtenerJugadorPorCorreo(String correo) {
    	return daoJugador.obtenerJugadorPorCorreo(correo);
    }

    
}