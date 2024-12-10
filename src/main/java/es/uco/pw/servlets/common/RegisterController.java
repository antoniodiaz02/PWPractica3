package es.uco.pw.servlets.common;

import es.uco.pw.data.DAOs.JugadorDAO;
import es.uco.pw.business.DTOs.JugadorDTO;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de registro (puedes cambiar a una JSP si quieres)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/common/registro.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombreCompleto = request.getParameter("nombreCompleto");
        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contraseña");
        String tipoUsuario = request.getParameter("tipoUsuario");

        try {
            // Validar datos
            if (nombreCompleto == null || correo == null || contraseña == null) {
                request.setAttribute("error", "Todos los campos son obligatorios.");
                request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
                return;
            }

            // Crear objeto JugadorDTO
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            JugadorDTO jugador = new JugadorDTO(nombreCompleto, sdf.parse("2000-01-01"), correo, contraseña, tipoUsuario);
            jugador.setContraseña(contraseña); // Establecer contraseña
            jugador.setTipoUsuario(tipoUsuario);

            // Usar DAO para registrar
            JugadorDAO jugadorDAO = new JugadorDAO();
            int resultado = jugadorDAO.insertJugador(jugador);

            if (resultado == 1) {
                response.sendRedirect("success.jsp");
            } else if (resultado == -2) {
                request.setAttribute("error", "El correo ya está registrado.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Error desconocido al registrar.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
