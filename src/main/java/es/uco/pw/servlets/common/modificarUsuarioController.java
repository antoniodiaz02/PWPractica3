package es.uco.pw.servlets.common;

import es.uco.pw.business.DTOs.JugadorDTO;
import es.uco.pw.business.Gestores.GestorUsuarios;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

/**
 * Controlador que gestiona la funcionalidad de modificar datos de usuario.
 */
@WebServlet("/modificarUsuario")
public class modificarUsuarioController extends HttpServlet {
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
        // Redirigir a la página de modificación de usuario
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/common/modificarUsuario.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Maneja las solicitudes HTTP POST.
     * @param request el objeto {@link HttpServletRequest} que contiene la solicitud del cliente.
     * @param response el objeto {@link HttpServletResponse} que contiene la respuesta al cliente.
     * @throws ServletException si ocurre un error en el despacho de la solicitud.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Capturar los parámetros del formulario
        String nombreCompleto = request.getParameter("nombreCompleto") != null ? request.getParameter("nombreCompleto").trim() : null;
        
        // String apellidos = request.getParameter("apellidos") != null ? request.getParameter("apellidos").trim() : null;
        String fechaNacimientoStr = request.getParameter("fechaNacimiento") != null ? request.getParameter("fechaNacimiento").trim() : null;
        String nuevaContrasena = request.getParameter("nuevaContrasena") != null ? request.getParameter("nuevaContrasena").trim() : null;
        String correo = request.getParameter("correo") != null ? request.getParameter("correo").trim() : "";

        // Validar que el correo no esté vacío
        if (correo.isEmpty()) {
            request.setAttribute("error", "El correo es obligatorio para identificar al usuario.");
            request.getRequestDispatcher("/MVC/Views/common/modificarUsuario.jsp").forward(request, response);
            return;
        }

        // Validar la fecha de nacimiento (si se proporciona)
        Date fechaNacimiento = null;
        if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                fechaNacimiento = sdf.parse(fechaNacimientoStr);
            } catch (ParseException e) {
                request.setAttribute("error", "El formato de la fecha de nacimiento es incorrecto. Debe ser yyyy-MM-dd.");
                request.getRequestDispatcher("/MVC/Views/common/modificarUsuario.jsp").forward(request, response);
                return;
            }
        }

        try {
            // Crear objeto JugadorDTO con los campos proporcionados
            JugadorDTO jugador = new JugadorDTO();
            
            // Solo asignar el nombre completo si no es vacío
            if (nombreCompleto != null && !nombreCompleto.isEmpty()) {
                jugador.setNombreCompleto(nombreCompleto);
            }
            
            // Solo asignar la fecha de nacimiento si no es nula
            if (fechaNacimiento != null) {
                jugador.setFechaNacimiento(fechaNacimiento);
            }

            // Solo asignar la nueva contraseña si no es vacía
            if (nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
                jugador.setContraseña(nuevaContrasena);
            }

            // Llamar al gestor para modificar el usuario
            GestorUsuarios gestor = new GestorUsuarios();
            int resultado = gestor.modificarUsuario(jugador, correo);

            if (resultado == 1) {
            	
                // Éxito al modificar el usuario
                HttpSession session = request.getSession();
                session.setAttribute("mensaje", "Usuario modificado correctamente.");
                response.sendRedirect(request.getContextPath() + "/MVC/Views/common/modificarUsuario.jsp");
                
                JugadorDTO jugadorActualizado = gestor.obtenerJugadorPorCorreo(correo);

                // Actualizar la sesión con los datos del usuario actualizados
                session = request.getSession();
                session.setAttribute("jugador", jugadorActualizado);
                
            } else if (resultado == 0) {
                request.setAttribute("error", "No se encontró ningún usuario con el correo proporcionado.");
                request.getRequestDispatcher("/MVC/Views/common/modificarUsuario.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Error desconocido al modificar el usuario. Inténtelo más tarde.");
                request.getRequestDispatcher("/MVC/Views/common/modificarUsuario.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor. Inténtelo más tarde.");
            request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
        }
    }
}

