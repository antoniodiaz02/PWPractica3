package es.uco.pw.servlets.common;

import es.uco.pw.data.DAOs.JugadorDAO;
import es.uco.pw.business.DTOs.JugadorDTO;
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
            // Usamos el método que obtiene el jugador por correo y contraseña
            JugadorDTO jugador = jugadorDAO.obtenerJugadorPorCorreoYContraseña(correo, contraseña);

            if (jugador != null) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", jugador);  // Guardamos el objeto completo del jugador en la sesión

                // Verificamos el tipo de usuario
                if ("administrador".equalsIgnoreCase(jugador.getTipoUsuario())) {
                    // Si el usuario es administrador, redirigir al menú de administrador
                    response.sendRedirect("/MVC/Views/admin/adminmenu.jsp");
                } else {
                    // Si el usuario es normal, redirigir al menú de usuario
                    response.sendRedirect("/MVC/Views/user/usermenu.jsp");
                }
            } else {
                // Si las credenciales son incorrectas
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
