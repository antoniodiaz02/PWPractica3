package es.uco.pw.servlets.user;

import javax.servlet.*;
import javax.servlet.http.*;

import es.uco.pw.business.DTOs.PistaDTO;
import es.uco.pw.business.Gestores.GestorReservas;

import java.io.IOException;

/**
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

/**
 * Controlador que gestiona la funcionalidad de crear Bonos.
 */
public class crearBonosController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Maneja las solicitudes HTTP GET.
     * 
     * @param request el objeto {@link HttpServletRequest} que contiene la solicitud del cliente.
     * @param response el objeto {@link HttpServletResponse} que contiene la respuesta al cliente.
     * @throws ServletException si ocurre un error en el despacho de la solicitud.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de creación de bonos
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/user/crearBonos.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Maneja las solicitudes HTTP POST.
     * @param request el objeto {@link HttpServletRequest} que contiene la solicitud del cliente.
     * @param response el objeto {@link HttpServletResponse} que contiene la respuesta al cliente.
     * @throws ServletException si ocurre un error en el despacho de la solicitud.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String correoUser = request.getParameter("correoUser") != null ? request.getParameter("correoUser").trim() : "";
    	String tipoBono = request.getParameter("tipoBono") != null ? request.getParameter("tipoBono").trim() : "";
    	
    	// Validar los campos obligatorios
        if (correoUser.isEmpty() || tipoBono.isEmpty()) {
            request.setAttribute("error", "Todos los campos obligatorios deben completarse.");
            request.getRequestDispatcher("/MVC/Views/user/crearBonos.jsp").forward(request, response);
            return;
        }
        
        try {
            //Se realiza la reserva y se guarda el precio de la reserva.
            GestorReservas bono = new GestorReservas();
            int resultado;
            PistaDTO.TamanoPista tamano= PistaDTO.TamanoPista.valueOf(tipoBono.toUpperCase());
            resultado= bono.hacerNuevoBono(correoUser, tamano);
            
            
            //Si se realiza la reserva con éxto se redirecciona a la página de éxito con el precio.
            if (resultado == 0){
            	response.sendRedirect(request.getContextPath() + "/usermenu/bonos/nuevobono/exito");
            } 
            
            //Si hay algún error
            else if (resultado == -1) {
                request.setAttribute("error", "El usuario no existe.");
                request.getRequestDispatcher("/MVC/Views/user/crearBonos.jsp").forward(request, response);
            } else if (resultado == -2) {
                request.setAttribute("error", "No se pudo insertar el nuevo bono en la base de datos.");
                request.getRequestDispatcher("/MVC/Views/user/crearBonos.jsp").forward(request, response);
            } else if (resultado == -3) {
                request.setAttribute("error", "No se pudo acceder a la base de datos del usuario.");
                request.getRequestDispatcher("/MVC/Views/user/crearBonos.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Error desconocido al reservar. Inténtelo más tarde.");
                request.getRequestDispatcher("/MVC/Views/user/crearBonos.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor. Inténtelo más tarde.");
            request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
        }
    }
}
