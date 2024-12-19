package es.uco.pw.servlets.common;

import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.business.Gestores.GestorUsuarios;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;



public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo").trim();
        String contraseña = request.getParameter("contraseña").trim();

        try {
            if (correo.isEmpty() || contraseña.isEmpty()) {
                request.setAttribute("error", "Los campos de correo y contraseña son obligatorios.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                return;
            }

            GestorUsuarios gestor = new GestorUsuarios();
            
            // Validar si las credenciales son correctas
            boolean credencialesValidas = gestor.validarCredenciales(correo, contraseña);
            
            if (credencialesValidas) {
                // Obtener la información del jugador
                JugadorDTO jugador = gestor.obtenerJugadorPorCorreo(correo);

                // Crear la sesión e iniciar la sesión para el jugador
                HttpSession session = request.getSession();
                session.invalidate(); // Evita la fijación de sesión
                session = request.getSession(true); // Inicia una nueva sesión
                session.setAttribute("jugador", jugador); // El jugador estará disponible en toda la sesión

                // Determina el destino según el tipo de usuario
                String destino = "administrador".equalsIgnoreCase(jugador.getTipoUsuario())
                        ? "/MVC/Views/admin/adminmenu.jsp"
                        : "/MVC/Views/user/usermenu.jsp";

                // Redirige a la página correspondiente
                response.sendRedirect(request.getContextPath() + destino);
            } else {
                request.setAttribute("error", "Correo o contraseña incorrectos.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error interno del servidor. Inténtelo más tarde.");
            request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
        }
    }
}
