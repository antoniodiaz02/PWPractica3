package es.uco.pw.servlets.user;

import javax.servlet.*;
import javax.servlet.http.*;

import es.uco.pw.business.DTOs.ReservaDTO;
import es.uco.pw.business.Gestores.GestorReservas;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ModificarReservaController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de gestión de bonos.
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp");
        dispatcher.forward(request, response);
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        // Obtener los parámetros del formulario
	    	String correoUser = request.getParameter("correoUser") != null ? request.getParameter("correoUser").trim() : "";
	    	String nombrePista = request.getParameter("nombrePista") != null ? request.getParameter("nombrePista").trim() : "";
	    	String fechaStr = request.getParameter("fechaBuscar") != null ? request.getParameter("fechaBuscar").trim() : "";
	        
	        java.util.Date fechaHora = null;
	        if (!fechaStr.isEmpty()) {
	            try {
	                // Ajustar el formato de la fecha al formato devuelto por un input datetime-local
	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
	                fechaHora = sdf.parse(fechaStr);
	            } catch (ParseException e) {
	                request.setAttribute("error-buscar", "El formato de la fecha inicial es incorrecto.");
	                request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
	                return;
	            }
	        }
	
	        try {
		        // Lógica de consulta a la base de datos (simulada aquí)
		        GestorReservas gestor = new GestorReservas();
		        ReservaDTO reserva;
		        int idReserva= -1;
		
		        int resultado= 0;
		        reserva= gestor.listarReservasPorFechaYPista(fechaHora, nombrePista, correoUser, idReserva, resultado);
		
		        // Manejar resultados según el código devuelto
		        if (resultado == 0) {
		        	// Éxito: Pasar la lista de pistas a la vista
		        	request.setAttribute("reserva", reserva);
		        	RequestDispatcher dispatcher = request.getRequestDispatcher("/usermenu/gestionreservas/modificar");
		        	dispatcher.forward(request, response);
		        } else if (resultado == -1) {
		        	request.setAttribute("error-buscar", "No se puede modificar una reserva que no es tuya.");
		        	response.sendRedirect(request.getContextPath() + "/usermenu/gestionreservas/modificar");
		        } else if (resultado == -2) {
		        	request.setAttribute("error-buscar", "No se encontró la reserva.");
		        	response.sendRedirect(request.getContextPath() + "/usermenu/gestionreservas/modificar");
		        } else if (resultado == -3) {
		        	request.setAttribute("error-buscar", "Ha ocurrido un error al intentar acceder a la base de datos.");
		        	response.sendRedirect(request.getContextPath() + "/usermenu/gestionreservas/modificar");
		        } else {
		        	request.setAttribute("error-buscar", "Ha ocurrido un error desconocido.");
		        	response.sendRedirect(request.getContextPath() + "/usermenu/gestionreservas/modificar");
		        }
		    
	        } catch (Exception e) {
	        	e.printStackTrace();
	            request.setAttribute("error", "Error en el servidor. Inténtelo más tarde.");
	            request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
	        }
	    }
}