package es.uco.pw.servlets.user;

import es.uco.pw.business.Gestores.GestorReservas;
import es.uco.pw.business.DTOs.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RealizarReservaBonoController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de registro
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Capturar los parámetros del formulario
    	String bonoIdStr= request.getParameter("bonoId") != null ? request.getParameter("bonoId").trim() : "";
    	String correoUser = request.getParameter("correoUser") != null ? request.getParameter("correoUser").trim() : "";
        String nombrePista = request.getParameter("nombrePista") != null ? request.getParameter("nombrePista").trim() : "";
        String duracionStr = request.getParameter("duracion") != null ? request.getParameter("duracion").trim() : "";
        String tipoReserva = request.getParameter("tipoReserva") != null ? request.getParameter("tipoReserva").trim() : "";
        String fechaReservaStr = request.getParameter("fechaHora") != null ? request.getParameter("fechaHora").trim() : "";
        
        String numParticipStr = request.getParameter("numParticipantes") != null ? request.getParameter("numParticipantes").trim() : "";
        String numNinosStr = request.getParameter("numNinos") != null ? request.getParameter("numNinos").trim() : "";
        String numAdultosStr = request.getParameter("numAdultos") != null ? request.getParameter("numAdultos").trim() : "";

        // Validar los campos obligatorios
        if (bonoIdStr.isEmpty() || nombrePista.isEmpty() || duracionStr.isEmpty() || tipoReserva.isEmpty() || fechaReservaStr.isEmpty()) {
            request.setAttribute("error", "Todos los campos obligatorios deben completarse.");
            request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
            return;
        }
        

        // Transformar el formato de la fecha
        java.util.Date fechaReserva = null;
        if (!fechaReservaStr.isEmpty()) {
            try {
                // Ajustar el formato de la fecha al formato devuelto por un input datetime-local
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                fechaReserva = sdf.parse(fechaReservaStr);
            } catch (ParseException e) {
                request.setAttribute("error", "El formato de la fecha de reserva es incorrecto.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
                return;
            }
        }
        
        int duracion= Integer.parseInt(duracionStr);
        int bonoId= Integer.parseInt(bonoIdStr);
        int numNinos=0;
        int numAdultos=0;
        
        int numParticip=0;
        Class<? extends ReservaDTO> Reserva;
        
        //Se inicializan las variables dependiendo el tipo de reserva.
        if(tipoReserva.equals("FAMILIAR")){
        	if((numNinosStr.isEmpty() || numAdultosStr.isEmpty())){
            	request.setAttribute("error", "Introduzca el número de participantes que jugarán.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
                return;
            }
        	numNinos= Integer.parseInt(numNinosStr);
        	numAdultos= Integer.parseInt(numAdultosStr);
        }
        
        else {
        	if(numParticipStr.isEmpty()){
            	request.setAttribute("error", "Introduzca el número de participantes que jugarán.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
                return;
            }
        	numParticip= Integer.parseInt(numParticipStr);
        }

        try {
            //Se realiza la reserva y se guarda el precio de la reserva.
            GestorReservas nuevaReserva = new GestorReservas();
            int resultado= 1;
            
            if(tipoReserva.equals("FAMILIAR")) {
            	Reserva= ReservaFamiliarDTO.class;
            	resultado= nuevaReserva.hacerReservaBono(correoUser,nombrePista,fechaReserva,duracion,numAdultos,numNinos,Reserva,bonoId);            	
            }
            
            else if(tipoReserva.equals("ADULTOS")){
            	Reserva= ReservaAdultosDTO.class;
            	resultado= nuevaReserva.hacerReservaBono(correoUser,nombrePista,fechaReserva,duracion,numParticip,numNinos,Reserva,bonoId);
            }
            
            else {
            	Reserva= ReservaInfantilDTO.class;
            	resultado= nuevaReserva.hacerReservaBono(correoUser,nombrePista,fechaReserva,duracion,numAdultos,numParticip,Reserva,bonoId);
            }
            
            
            //Si se realiza la reserva con éxto se redirecciona a la página de éxito con el precio.
            if (resultado == 0){
            	response.sendRedirect("/MVC/Views/user/reservaExito.jsp");
            } 
            
            //Si hay algún error
            else if (resultado == -1) {
                request.setAttribute("error", "Ya existe una reserva para la misma hora y pista.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
            } else if (resultado == -2) {
                request.setAttribute("error", "El usuario no existe");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
            } else if (resultado == -3) {
                request.setAttribute("error", "La pista no existe.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
            } else if (resultado == -4) {
                request.setAttribute("error", "La pista no está disponible.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
            } else if (resultado == -5) {
                request.setAttribute("error", "No se puede reservar una pista antes de 24 horas.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
            } else if (resultado == -6) {
                request.setAttribute("error", "Numero de participantes superior al permitido.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
            } else if (resultado == -7) {
                request.setAttribute("error", "La reserva que se intenta hacer no es del mismo tipo que el del bono.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
            } else if (resultado == -8) {
                request.setAttribute("error", "El bono utilizado no existe.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
            } else if (resultado == -9) {
                request.setAttribute("error", "Error al acceder a la base de datos de los bonos.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
            } else if (resultado == -10) {
                request.setAttribute("error", "El bono ya no tiene sesiones disponibles.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
            } else if (resultado == -11) {
                request.setAttribute("error", "El bono no pertenece al usuario que intenta hacer la reserva.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
            } else if (resultado == -12) {
                request.setAttribute("error", "El bono está caducado.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Error desconocido al reservar. Inténtelo más tarde.");
                request.getRequestDispatcher("/MVC/Views/user/reservaBono.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor. Inténtelo más tarde.");
            request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
        }
    }
}
