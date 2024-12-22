package es.uco.pw.servlets.user;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class ModificarReservaExitoController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de gestión de bonos.
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/user/modificarReservasExito.jsp");
        dispatcher.forward(request, response);
    }
}
