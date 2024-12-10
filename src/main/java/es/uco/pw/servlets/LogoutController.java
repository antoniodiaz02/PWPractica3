package es.uco.pw.servlets;


import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LogoutController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Invalidar la sesión
        }
        response.sendRedirect("index.jsp"); // Redirigir al login
    }
}
