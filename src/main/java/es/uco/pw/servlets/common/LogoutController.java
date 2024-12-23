package es.uco.pw.servlets.common;

import javax.servlet.*;
import javax.servlet.http.*;
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
 * Controlador que gestiona la funcionalidad cerrar sesión.
 */
public class LogoutController extends HttpServlet {
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
        // Obtiene la sesión actual, si existe
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Invalidar la sesión
        }

        // Establece un mensaje opcional para mostrar en logout.jsp
        request.setAttribute("mensaje", "Has cerrado sesión exitosamente.");

        // Despacha la solicitud a logout.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/common/logout.jsp");
        dispatcher.forward(request, response);
    }
}
