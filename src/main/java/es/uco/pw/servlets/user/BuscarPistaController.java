package es.uco.pw.servlets.user;

import javax.servlet.*;
import javax.servlet.http.*;

import es.uco.pw.business.DTOs.ReservaDTO;
import es.uco.pw.business.DTOs.PistaDTO;
import es.uco.pw.business.Gestores.GestorReservas;
import es.uco.pw.business.Gestores.GestorPistas;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class BuscarPistaController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/user/buscarPista.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// Obtener los parámetros del formulario
    	String fechaBuscarStr = request.getParameter("fechaBuscar") != null ? request.getParameter("fechaBuscar").trim() : "";
    	String interiorStr = request.getParameter("interior") != null ? request.getParameter("interior").trim() : "false";
        
    	boolean interior = Boolean.parseBoolean(interiorStr);
    	java.util.Date fechaBuscar = null;
        if (!fechaBuscarStr.isEmpty()) {
            try {
                // Ajustar el formato de la fecha al formato devuelto por un input datetime-local
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                fechaBuscar = sdf.parse(fechaBuscarStr);
            } catch (ParseException e) {
                request.setAttribute("error", "El formato de la fecha inicial es incorrecto.");
                request.getRequestDispatcher("/MVC/Views/user/buscarpistas.jsp").forward(request, response);
                return;
            }
        }
    	try {
            // Crear DAO y Vector para almacenar las pistas
        	GestorReservas gestor = new GestorReservas();
        	
            Vector<PistaDTO> todasLasPistas = new Vector<>();

            // Llamar al método listarPistas y obtener el código de resultado
            int resultado = gestor.buscarPistas(todasLasPistas,interior,fechaBuscar);

            // Manejar resultados según el código devuelto
            if (resultado == 0) {
                // Éxito: Pasar la lista de pistas a la vista
                request.setAttribute("pistas", todasLasPistas);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/user/buscarPista.jsp");
                dispatcher.forward(request, response);
            } else if (resultado == -1) {
                // Error: Vector proporcionado es nulo
            	request.setAttribute("error", "Se produjo un error al intentar buscar las soluciones");
                response.sendRedirect(request.getContextPath() + "/usermenu/buscarpistas");
            } else if (resultado == -2) {
                // Error: Datos inválidos en la base de datos
            	request.setAttribute("error", "Se produjo un error al intentar buscar las soluciones");
                response.sendRedirect(request.getContextPath() + "/usermenu/buscarpistas");
            } else if (resultado == -3) {
                // No se encontraron pistas
            	request.setAttribute("error", "No existe ninguna pista disponible con los datos especificados.");
                response.sendRedirect(request.getContextPath() + "/usermenu/buscarpistas");
            } else if (resultado == -4) {
                // Error en la consulta SQL
            	request.setAttribute("error", "Se produjo un error en la consulta de la base de datos");
                response.sendRedirect(request.getContextPath() + "/usermenu/buscarpistas");
            } else {
                // Error desconocido
                response.sendRedirect(request.getContextPath() + "/usermenu/buscarpistas");
            }
        } catch (Exception e) {
        	e.printStackTrace();
            request.setAttribute("error", "Error en el servidor. Inténtelo más tarde.");
            request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
        }
    }
    
}