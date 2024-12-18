package es.uco.pw.servlets.common;

import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.business.Gestores.GestorUsuarios;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de registro
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/common/registro.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Capturar los parámetros del formulario
        String nombreCompleto = request.getParameter("nombreCompleto") != null ? request.getParameter("nombreCompleto").trim() : "";
        String correo = request.getParameter("correo") != null ? request.getParameter("correo").trim() : "";
        String contraseña = request.getParameter("contraseña") != null ? request.getParameter("contraseña").trim() : "";
        String tipoUsuario = request.getParameter("tipoUsuario") != null ? request.getParameter("tipoUsuario").trim() : "";
        String fechaNacimientoStr = request.getParameter("fechaNacimiento") != null ? request.getParameter("fechaNacimiento").trim() : "";

        // Validar los campos obligatorios
        if (nombreCompleto.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || tipoUsuario.isEmpty()) {
            request.setAttribute("error", "Todos los campos obligatorios deben completarse.");
            request.getRequestDispatcher("/MVC/Views/common/registro.jsp").forward(request, response);
            return;
        }

        // Validar la fecha de nacimiento (si se proporciona)
        java.util.Date fechaNacimiento = null;
        if (!fechaNacimientoStr.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                fechaNacimiento = sdf.parse(fechaNacimientoStr);
            } catch (ParseException e) {
                request.setAttribute("error", "El formato de la fecha de nacimiento es incorrecto. Debe ser yyyy-MM-dd.");
                request.getRequestDispatcher("/MVC/Views/common/registro.jsp").forward(request, response);
                return;
            }
        }

        try {
            // Crear objeto JugadorDTO (fecha por defecto si es null)
            if (fechaNacimiento == null) {
                // Establecemos una fecha por defecto si no se proporcionó
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                fechaNacimiento = sdf.parse("2000-01-01");
            }

            JugadorDTO jugador = new JugadorDTO(nombreCompleto, fechaNacimiento, correo, contraseña, tipoUsuario);
            
            // Llamar al gestor para registrar el jugador
            GestorUsuarios gestor = new GestorUsuarios();
            
            int resultado = gestor.insertarUsuario(jugador);

            if (resultado == 1) {
                // Crear la sesión e iniciar la sesión para el jugador registrado
                HttpSession session = request.getSession();
                session.setAttribute("jugador", jugador); // Jugador disponible en toda la sesión

                String destino = "administrador".equalsIgnoreCase(tipoUsuario) 
                    ? "/MVC/Views/admin/adminmenu.jsp" 
                    : "/MVC/Views/user/usermenu.jsp";

                response.sendRedirect(request.getContextPath() + destino);
            } else if (resultado == -2) {
                request.setAttribute("error", "El correo ya está registrado.");
                request.getRequestDispatcher("/MVC/Views/common/registro.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Error desconocido al registrar. Inténtelo más tarde.");
                request.getRequestDispatcher("/MVC/Views/common/registro.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor. Inténtelo más tarde.");
            request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
        }
    }
}
