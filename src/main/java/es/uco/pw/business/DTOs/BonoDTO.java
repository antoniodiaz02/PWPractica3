package es.uco.pw.business.DTOs;

import java.util.Date;

public class BonoDTO {
    
    private int idBono;
    private int usuarioId;
    private int sesiones;
    private Date fechaInicio;
    private Date fechaCaducidad;
    private String tipoPista;
    
    /**
     * Constructor completo para la clase BonoDTO
     * 
     * @param idBono Identificador único del bono
     * @param usuarioId Identificador del usuario asociado al bono
     * @param sesiones Número de sesiones disponibles con este bono
     * @param fechaInicio Fecha de inicio de validez del bono
     * @param fechaCaducidad Fecha de caducidad del bono
     * @param tipoPista Tipo de pista asociada al bono
     */
    public BonoDTO(int idBono, int usuarioId, int sesiones, Date fechaInicio, Date fechaCaducidad, String tipoPista) {
        this.idBono = idBono;
        this.usuarioId = usuarioId;
        this.sesiones = sesiones;
        this.fechaInicio = fechaInicio;
        this.fechaCaducidad = fechaCaducidad;
        this.tipoPista = tipoPista;
    }
    
    /**
     * Constructor vacío para la clase BonoDTO
     */
    public BonoDTO() {}
    
    // Getters y Setters
    
    public int getIdBono() {
        return idBono;
    }

    public void setIdBono(int idBono) {
        this.idBono = idBono;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getSesiones() {
        return sesiones;
    }

    public void setSesiones(int sesiones) {
        this.sesiones = sesiones;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getTipoPista() {
        return tipoPista;
    }

    public void setTipoPista(String tipoPista) {
        this.tipoPista = tipoPista;
    }

    @Override
    public String toString() {
        return "BonoDTO{" +
                "idBono=" + idBono +
                ", usuarioId=" + usuarioId +
                ", sesiones=" + sesiones +
                ", fechaInicio=" + (fechaInicio != null ? fechaInicio.toString() : "N/A") +
                ", fechaCaducidad=" + (fechaCaducidad != null ? fechaCaducidad.toString() : "N/A") +
                ", tipoPista='" + tipoPista + '\'' +
                '}';
    }
} 
