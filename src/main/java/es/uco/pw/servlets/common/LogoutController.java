package es.uco.pw.servlets.common;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LogoutController extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
