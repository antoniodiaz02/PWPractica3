package es.uco.pw.servlets.user;

import javax.servlet.*;
import javax.servlet.http.*;

import es.uco.pw.business.DTOs.ReservaDTO;
import es.uco.pw.business.Gestores.GestorReservas;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class ConsultarReservasController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/user/consultarReservas.jsp");
        dispatcher.forward(request, response);
    }
    
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parámetros del formulario
    	String correoUser = request.getParameter("correoUser") != null ? request.getParameter("correoUser").trim() : "";
    	String fechaInicioStr = request.getParameter("fechaInicio") != null ? request.getParameter("fechaInicio").trim() : "";
    	String fechaFinalStr = request.getParameter("fechaFinal") != null ? request.getParameter("fechaFinal").trim() : "";
        
        java.util.Date fechaInicio = null;
        if (!fechaInicioStr.isEmpty()) {
            try {
                // Ajustar el formato de la fecha al formato devuelto por un input datetime-local
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                fechaInicio = sdf.parse(fechaInicioStr);
            } catch (ParseException e) {
                request.setAttribute("error", "El formato de la fecha inicial es incorrecto.");
                request.getRequestDispatcher("/MVC/Views/user/consultarReservas.jsp").forward(request, response);
                return;
            }
        }
        
        java.util.Date fechaFinal = null;
        if(!fechaFinalStr.isEmpty()) {
            try {
                // Ajustar el formato de la fecha al formato devuelto por un input datetime-local
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                fechaFinal = sdf.parse(fechaFinalStr);
            } catch (ParseException e) {
                request.setAttribute("error", "El formato de la fecha final es incorrecto.");
                request.getRequestDispatcher("/MVC/Views/user/consultarReservas.jsp").forward(request, response);
                return;
            }
        }

        try {
	        // Lógica de consulta a la base de datos (simulada aquí)
	        GestorReservas gestor = new GestorReservas();
	        Vector<ReservaDTO> vectorReservas = new Vector<>();
	
	        int resultado= gestor.listarReservasEntreFechas(vectorReservas,fechaInicio,fechaFinal,correoUser);
	
	        // Manejar resultados según el código devuelto
	        if (resultado == 0) {
	        	// Éxito: Pasar la lista de pistas a la vista
	        	request.setAttribute("reservas", vectorReservas);
	        	RequestDispatcher dispatcher = request.getRequestDispatcher("/usermenu/consultareservas");
	        	dispatcher.forward(request, response);
	        } else if (resultado == -1) {
	        	request.setAttribute("error", "Se produjo un error al intentar buscar las soluciones");
	        	response.sendRedirect(request.getContextPath() + "/usermenu/consultareservas");
	        } else if (resultado == -2) {
	        	request.setAttribute("error", "Ocurrió un error al acceder a la base de datos");
	        	response.sendRedirect(request.getContextPath() + "/usermenu/consultareservas");
	        } else if (resultado == -3) {
	        	request.setAttribute("error", "No existe ninguna reserva para las fechas indicadas.");
	        	response.sendRedirect(request.getContextPath() + "/usermenu/consultareservas");
	        } else {
	        	request.setAttribute("error", "Ha ocurrido un error desconocido.");
	        	response.sendRedirect(request.getContextPath() + "/usermenu/consultareservas");
	        }
	    
        } catch (Exception e) {
        	e.printStackTrace();
            request.setAttribute("error", "Error en el servidor. Inténtelo más tarde.");
            request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
        }
    }
}
