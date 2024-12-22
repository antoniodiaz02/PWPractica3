package es.uco.pw.servlets.user;

import javax.servlet.*;
import javax.servlet.http.*;

import es.uco.pw.business.DTOs.ReservaDTO;
import es.uco.pw.business.Gestores.GestorReservas;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

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
			String action = request.getParameter("action");
			
			if (action == null) {
		        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no especificada.");
		        return;
		    }

		    switch (action) {
		        case "buscar":
		            buscarReserva(request, response);
		            break;
		        case "modificar":
		            break;
		        default:
		            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida.");
		            break;
		    }
	}
	
	private void buscarReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String correoUser = request.getParameter("correoUser") != null ? request.getParameter("correoUser").trim() : "";
    	String nombrePista = request.getParameter("nombrePista") != null ? request.getParameter("nombrePista").trim() : "";
    	String fechaStr = request.getParameter("fechaBuscar") != null ? request.getParameter("fechaBuscar").trim() : "";
        
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        java.util.Date fechaHora = null;
        
        try {
            if (!fechaStr.isEmpty()) {
            	fechaHora = sdf.parse(fechaStr);
            }
        } catch (ParseException e) {
            request.setAttribute("error", "Formato de fecha incorrecto. Use el formato correcto: yyyy-MM-dd'T'HH:mm.");
            request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
            return;
        }

        try {
	        // Lógica de consulta a la base de datos (simulada aquí)
	        GestorReservas gestor = new GestorReservas();
	        Vector<ReservaDTO> vectorReservas = new Vector<>();
	        
	        AtomicInteger idReserva= new AtomicInteger(-1);
	        int resultado= gestor.listarReservasPorFechaYPista(vectorReservas, fechaHora, nombrePista, correoUser, idReserva);
	
	        switch (resultado) {
                case 0: // Reservas encontradas
                    request.setAttribute("reservas", vectorReservas);
                    request.setAttribute("idReserva", idReserva);
                    request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
                    break;
                case -1: // Se encuentra una reserva pero no es del usuario.
                    request.setAttribute("error", "No existe ninguna reserva tuya para esa fecha.");
                    request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
                    break;
                case -2: // No existe ninguna reserva para ese día
                    request.setAttribute("error", "No existe ninguna reserva tuya para esa fecha.");
                    request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
                    break;
                case -3: // Error en la base de datos
                    request.setAttribute("error", "Error al intentar a la base de datos.");
                    request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
                    break;
                default: // Error desconocido
                    request.setAttribute("error", "Ha ocurrido un error desconocido.");
                    request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
        }
	    
        } catch (Exception e) {
        	e.printStackTrace();
            request.setAttribute("error", "Error en el servidor. Inténtelo más tarde.");
            request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
        }
	}
}