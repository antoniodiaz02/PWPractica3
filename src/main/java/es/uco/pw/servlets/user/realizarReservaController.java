package es.uco.pw.servlets.user;

import es.uco.pw.business.Gestores.GestorReservas;
import es.uco.pw.business.DTOs.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class realizarReservaController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de registro
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/user/realizarReserva.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Capturar los parámetros del formulario
    	String correoUser = request.getParameter("correoUser") != null ? request.getParameter("correoUser").trim() : "";
        String nombrePista = request.getParameter("nombrePista") != null ? request.getParameter("nombrePista").trim() : "";
        String duracionStr = request.getParameter("duracion") != null ? request.getParameter("duracion").trim() : "";
        String tipoReserva = request.getParameter("tipoReserva") != null ? request.getParameter("tipoReserva").trim() : "";
        String fechaReservaStr = request.getParameter("fechaHora") != null ? request.getParameter("fechaHora").trim() : "";
        
        String numParticipStr = request.getParameter("numParticipantes") != null ? request.getParameter("numParticipantes").trim() : "";
        String numNinosStr = request.getParameter("numNiños") != null ? request.getParameter("numNiños").trim() : "";
        String numAdultosStr = request.getParameter("numAdultos") != null ? request.getParameter("numAdultos").trim() : "";

        // Validar los campos obligatorios
        if (nombrePista.isEmpty() || duracionStr.isEmpty() || tipoReserva.isEmpty() || fechaReservaStr.isEmpty()) {
            request.setAttribute("error", "Todos los campos obligatorios deben completarse.");
            request.getRequestDispatcher("/MVC/Views/user/realizarReserva.jsp").forward(request, response);
            return;
        }
        
        // Campos de numero de participantes obligatorios
        if (numParticipStr.isEmpty() || (numNinosStr.isEmpty() && numAdultosStr.isEmpty())) {
        	request.setAttribute("error", "Todos los campos obligatorios deben completarse.");
            request.getRequestDispatcher("/MVC/Views/user/realizarReserva.jsp").forward(request, response);
            return;
        }

        // Transformar el formato de la fecha
        java.util.Date fechaReserva = null;
        if (!fechaReservaStr.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM / dd / yyyy , hh : mm");
                fechaReserva = sdf.parse(fechaReservaStr);
            } catch (ParseException e) {
                request.setAttribute("error", "El formato de la fecha de nacimiento es incorrecto. Debe ser MM / dd / yyyy , hh : mm");
                request.getRequestDispatcher("/MVC/Views/user/realizarReserva.jsp").forward(request, response);
                return;
            }
        }
        
        int duracion= Integer.parseInt(duracionStr);
        int numNinos=0;
        int numAdultos=0;
        
        int numParticip=0;
        Class<? extends ReservaDTO> Reserva;
        
        //Se inicializan las variables dependiendo el tipo de reserva.
        if(!numParticipStr.isEmpty()) {
        	numParticip= Integer.parseInt(numParticipStr);
        }
        else {
        	numNinos= Integer.parseInt(numNinosStr);
        	numAdultos= Integer.parseInt(numAdultosStr);
        }

        try {
            //Se realiza la reserva y se guarda el precio de la reserva.
            GestorReservas nuevaReserva = new GestorReservas();
            int resultado;
            if(tipoReserva == "FAMILIAR") {
            	Reserva= ReservaFamiliarDTO.class;
            	resultado= nuevaReserva.hacerReservaIndividual(correoUser,nombrePista,fechaReserva,duracion,numAdultos,numNinos,Reserva);            	
            }
            
            else if(tipoReserva == "ADULTOS"){
            	Reserva= ReservaAdultosDTO.class;
            	resultado= nuevaReserva.hacerReservaIndividual(correoUser,nombrePista,fechaReserva,duracion,numParticip,numNinos,Reserva);
            }
            
            else {
            	Reserva= ReservaInfantilDTO.class;
            	resultado= nuevaReserva.hacerReservaIndividual(correoUser,nombrePista,fechaReserva,duracion,numAdultos,numParticip,Reserva);
            }
            
            
            //Si se realiza la reserva con éxto se redirecciona a la página de éxito con el precio.
            if (resultado == 0){
            	response.sendRedirect("/MVC/Views/user/registroExito.jsp");
            } 
            
            //Si hay algún error
            else if (resultado == -1) {
                request.setAttribute("error", "Ya existe una reserva para la misma hora y pista.");
                request.getRequestDispatcher("/MVC/Views/user/realizarReserva.jsp").forward(request, response);
            } else if (resultado == -2) {
                request.setAttribute("error", "El usuario no existe");
                request.getRequestDispatcher("/MVC/Views/user/realizarReserva.jsp").forward(request, response);
            } else if (resultado == -3) {
                request.setAttribute("error", "La pista no existe.");
                request.getRequestDispatcher("/MVC/Views/user/realizarReserva.jsp").forward(request, response);
            } else if (resultado == -4) {
                request.setAttribute("error", "La pista no está disponible.");
                request.getRequestDispatcher("/MVC/Views/user/realizarReserva.jsp").forward(request, response);
            } else if (resultado == -5) {
                request.setAttribute("error", "No se puede reservar una pista antes de 24 horas.");
                request.getRequestDispatcher("/MVC/Views/user/realizarReserva.jsp").forward(request, response);
            } else if (resultado == -6) {
                request.setAttribute("error", "Tipo incorrecto de reserva.");
                request.getRequestDispatcher("/MVC/Views/user/realizarReserva.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Error desconocido al reservar. Inténtelo más tarde.");
                request.getRequestDispatcher("/MVC/Views/user/realizarReserva.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor. Inténtelo más tarde.");
            request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
        }
    }
}
