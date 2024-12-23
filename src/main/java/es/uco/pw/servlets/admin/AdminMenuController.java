package es.uco.pw.servlets.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

import es.uco.pw.business.DTOs.JugadorDTO;

/**
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

/**
 * Controlador que gestiona el acceso a el menu /adminmenu
 */
public class AdminMenuController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Maneja las solicitudes HTTP GET para redirigir al menú de administración.
     *
     * @param request el objeto {@link HttpServletRequest} que contiene la solicitud del cliente.
     * @param response el objeto {@link HttpServletResponse} que contiene la respuesta al cliente.
     * @throws ServletException si ocurre un error en el proceso de despacho.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de gestión de bonos.
    	
    	 // Obtener el usuario de la sesión
        HttpSession session = request.getSession();
        JugadorDTO admin = (JugadorDTO) session.getAttribute("jugador");

        // Validar que el usuario esté en sesión y sea un administrador
        if (admin == null || !"administrador".equals(admin.getTipoUsuario())) {
        	response.sendRedirect("../../../index.jsp"); 
            return; // Detener la ejecución
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/adminmenu.jsp");
        dispatcher.forward(request, response);
    }
}
