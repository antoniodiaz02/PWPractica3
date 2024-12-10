package es.uco.pw.business.DTOs;

import java.util.Date;

/**
 * Clase que representa una reserva de una pista en las instalaciones.
 * 
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since -10-2024
 *  @version 1.0
 */
public abstract class ReservaDTO {

    /**
     * Identificador del usuario que realiza la reserva (previamente registrado).
     */
    private int usuarioId;

    /**
     * Fecha y hora de la reserva.
     */
    private Date fechaHora;

    /**
     * Duración de la reserva en minutos.
     */
    private int duracion;

    /**
     * Identificador de la pista reservada (debe existir).
     */
    private int pistaId;

    /**
     * Precio de la reserva (en euros).
     */
    private float precio;

    /**
     * Descuento aplicado a la reserva.
     */
    private float descuento;
    
    /**
     * Bono de la reserva.
     */
    private int bonoId;  

    
    /**
     * Número de sesion del bono.
     */
    private int sesion; 

    /**
     * Constructor parametrizable de la clase Reserva.
     * @param idUsuario Id del usuario.
     * @param fechaHora Fecha hora.
     * @param duracionMinutos Duración en minutos.
     * @param idPista Identificador de la pista.
     * @param precio Precio.
     * @param descuento Descuento a aplicar.
     */
    public ReservaDTO(int idUsuario, Date fechaHora, int duracionMinutos, int idPista, float precio, float descuento) {
        this.usuarioId = idUsuario;
        this.fechaHora = fechaHora;
        this.duracion = duracionMinutos;
        this.pistaId = idPista;
        this.precio = precio;
        this.descuento = descuento;
        this.bonoId= -1;
    }
    
    /**
     * Constructor vacío de la clase Reserva.
     */
    public ReservaDTO() {}

    /**
     * Devuelve el identificador del usuario.
     * @return usuarioId Identificador del usuario.
     */
    public int getUsuarioId() {
        return usuarioId;
    }

    /**
     * Modifica el identificador del usuario.
     * @param usuarioId Identificador del usuario.
     */
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * Devuelve la fecha y hora de la reserva.
     * @return fechaHora Fecha y hora de la reserva.
     */
    public Date getFechaHora() {
        return fechaHora;
    }

    /**
     * Modifica la fecha y hora de la reserva.
     * @param fechaHora Fecha y hora de la reserva.
     */
    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * Devuelve la duración de la reserva en minutos.
     * @return duracion Duración de la reserva.
     */
    public int getDuracion() {
        return duracion;
    }

    /**
     * Modifica la duración de la reserva.
     * @param duracion Duración de la reserva.
     */
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    /**
     * Devuelve el identificador de la pista reservada.
     * @return pistaId Identificador de la pista.
     */
    public int getPistaId() {
        return pistaId;
    }

    /**
     * Modifica el identificador de la pista reservada.
     * @param pistaId Identificador de la pista.
     */
    public void setPistaId(int pistaId) {
        this.pistaId = pistaId;
    }

    /**
     * Devuelve el precio de la reserva.
     * @return precio Precio de la reserva.
     */
    public float getPrecio() {
        return precio;
    }

    /**
     * Modifica el precio de la reserva.
     * @param precio Precio de la reserva.
     */
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    /**
     * Devuelve el descuento aplicado a la reserva.
     * @return descuento Descuento de la reserva.
     */
    public float getDescuento() {
        return descuento;
    }

    /**
     * Modifica el descuento aplicado a la reserva.
     * @param descuento Descuento de la reserva.
     */
    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }
    
    /**
     * Devuelve el identificador del bono.
     * @return bonoID Identificador del bono.
     */
    public int getBonoId() {
        return bonoId;
    }

    /**
     * Modifica el identificador del bono.
     * @param bonoId Identificador del bono.
     */
    public void setBonoId(int bonoId) {
        this.bonoId = bonoId;
    }

    /**
     * Devuelve el número de sesion del bono.
     * @return sesion Número de sesion del bono.
     */
    public int getSesion() {
        return sesion;
    }

    /**
     * Modifica el número de sesion del bono.
     * @param sesion número de sesion del bono.
     */
    public void setSesion(int sesion) {
        this.sesion = sesion;
    }

    /**
     * Muestra la información de la reserva.
     * @return String Información de la reserva.
     */
    @Override
    public String toString() {
        String info = "Reserva [Usuario: " + usuarioId + ", Fecha y hora: " + fechaHora + ", Duración: " + duracion 
                      + " minutos, Pista: " + pistaId + ", Precio: " + precio + " €, Descuento: " + descuento + " €]";
        if (bonoId == -1) {
            info += ", Bono: " + bonoId + ", Sesión: " + sesion;
        }
        return info;
    }

}
