package es.uco.pw.business.DTOs;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;

/**
 * Clase que representa a un usuario que va a hacer uso de las instalaciones deportivas.
 * 
 *  @author Antonio Diaz
 *  @author Carlos Marín 
 *  @author Carlos De la Torre 
 *  @author Daniel 
 *  @since 08-10-2024
 *  @version 1.0
 */
public class JugadorDTO {

    // Nombre y apellidos
    private String nombreCompleto;
    private String nombre;
    private String apellidos;

    // Fecha de nacimiento e inscripción
    private Date fechaNacimiento;
    private Date fechaInscripcion;

    // Correo electrónico (único)
    private String correoElectronico;

    // Contraseña (encriptada o en texto claro para simplificación, debe protegerse)
    private String contraseña;

    // Tipo de usuario (por ejemplo: "cliente", "admin", etc.)
    private String tipoUsuario;

    /**
     * Constructor vacío clase Jugador.
     */
    public JugadorDTO() {
        // Constructor sin parámetros
    }
    
    /**
     * Constructor parametizado de la clase Jugador.
     * 
     * @param nombreCompleto Nombre completo del jugador.
     * @param fechaNacimiento Fecha de nacimiento del jugador.
     * @param correoElectronico Correo electrónico del jugador.
     * @param contraseña Contraseña del jugador.
     * @param tipoUsuario Tipo de usuario (ej. "cliente").
     */
    public JugadorDTO(String nombreCompleto, Date fechaNacimiento, String correoElectronico, String contraseña, String tipoUsuario) {
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.correoElectronico = correoElectronico;
        this.contraseña = contraseña;
        this.tipoUsuario = tipoUsuario;
        this.fechaInscripcion = new Date(); // Fecha actual por defecto
        separarNombreYApellidos(nombreCompleto); // Descomponer nombre y apellidos
    }

    // Getters y Setters para los atributos principales

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        separarNombreYApellidos(nombreCompleto); // Actualiza nombre y apellidos al cambiar nombre completo
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    // Métodos auxiliares y funcionales

    /**
     * Calcula la antigüedad del jugador en años desde la fecha de inscripción.
     * @return Años de antigüedad.
     */
    public int calcularAntiguedad() {
        if (fechaInscripcion == null) {
            return 0;
        }
        long diffInMillis = new Date().getTime() - fechaInscripcion.getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) / 365;
    }

    /**
     * Devuelve una representación en String de los datos del jugador.
     * @return String Información del jugador.
     */
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return "Jugador [Nombre: " + nombreCompleto +
               ", Fecha de nacimiento: " + sdf.format(fechaNacimiento) +
               ", Fecha de inscripción: " + sdf.format(fechaInscripcion) +
               ", Correo: " + correoElectronico +
               ", Tipo de Usuario: " + tipoUsuario +
               ", Antigüedad: " + calcularAntiguedad() + " años]";
    }

    /**
     * Separa el nombre completo en nombre y apellidos.
     * @param nombreCompleto Nombre completo del jugador.
     */
    private void separarNombreYApellidos(String nombreCompleto) {
        String[] partes = nombreCompleto.split(" ");

        if (partes.length == 2 || partes.length == 3) {
            // Si hay 2 o 3 partes: El primer término es el nombre, el resto son apellidos
            this.nombre = partes[0];
            this.apellidos = String.join(" ", Arrays.copyOfRange(partes, 1, partes.length));
        } else if (partes.length == 4) {
            // Si hay 4 partes: Los dos primeros son el nombre, el resto son los apellidos
            this.nombre = partes[0] + " " + partes[1];
            this.apellidos = partes[2] + " " + partes[3];
        } else {
            // Para otros casos, solo considera el primer nombre
            this.nombre = partes[0];
            this.apellidos = partes.length > 1 ? String.join(" ", Arrays.copyOfRange(partes, 1, partes.length)) : "";
        }
    }
}
