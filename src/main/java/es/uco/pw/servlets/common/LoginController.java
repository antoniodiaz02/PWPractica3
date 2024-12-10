package es.uco.pw.servlets.common;

import es.uco.pw.data.DAOs.JugadorDAO;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contraseña");

        try {
            JugadorDAO jugadorDAO = new JugadorDAO();
            boolean esValido = jugadorDAO.validarUsuario(correo, contraseña);

            if (esValido) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", correo);
                response.sendRedirect("/MVC/Views/common/inicio.jsp");
            } else {
                request.setAttribute("error", "Correo o contraseña incorrectos.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor.");
            request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
        }
    }
}
