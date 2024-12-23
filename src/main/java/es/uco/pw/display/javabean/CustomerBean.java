package es.uco.pw.display.javabean;

/**
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

/**
 * CustomerBeean
 */
public class CustomerBean {
	
	/**
     * Identificador del usuario.
     */
    private int idUsuario;
    
    /**
     * nombre.
     */
    private String nombre;
    
    /**
     * apellidos.
     */
    private String apellidos;
    
    /**
     * Correo electrónico.
     */
    private String correoElectronico;
    
    /**
     * Tipo de usuario.
     */
    private String tipoUsuario; // cliente o administrador
    
    /**
     * contraseña
     */
    private String contraseña;

    /**
     * Obtiene el Id del usuario.
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece un id de usuario. 
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el correo electrónico.
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    /**
     * Establece el correo electrónico
     */
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    /**
     * Obtiene el tipo de usuario.
     */
    public String getTipoUsuario() {
        return tipoUsuario;
    }

    /**
     * Establece el tipo de usuario.
     */
    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Obltiene la contraseña.
     */
    public String getContraseña() {
        return contraseña;
    }

    /**
     * Establece la contraseña
     */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}

