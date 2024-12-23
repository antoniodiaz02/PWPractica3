package es.uco.pw.servlets.admin;

import es.uco.pw.business.Gestores.GestorUsuarios;
import es.uco.pw.business.DTOs.JugadorDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Vector;

/**
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

/**
 * Controlador que gestiona la funcionalidad de listar usuarios.
 */
public class ListarUsuariosController extends HttpServlet {
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
        try {
            GestorUsuarios gestor = new GestorUsuarios();
            Vector<JugadorDTO> todosLosUsuarios = new Vector<>();

            int resultado = gestor.listarUsuarios(todosLosUsuarios);

            String mensajeError = null;
            if (resultado == 0) {
                // Éxito: Pasar la lista de usuarios a la vista
                request.setAttribute("usuarios", todosLosUsuarios);
            } else if (resultado == -1) {
                // Error: El vector proporcionado es nulo
                mensajeError = "Error: El vector proporcionado es nulo.";
            } else if (resultado == -2) {
                // Error: Datos inválidos en la base de datos
                mensajeError = "Error: Datos inválidos en la base de datos.";
            } else if (resultado == -3) {
                // No se encontraron usuarios disponibles
                mensajeError = "No se encontraron usuarios disponibles.";
            } else if (resultado == -4) {
                // Error en la consulta SQL
                mensajeError = "Error: Fallo en la consulta SQL.";
            } else {
                // Error desconocido
                mensajeError = "Error desconocido.";
            }

            // Si hubo un error, pasar el mensaje de error a la vista
            if (mensajeError != null) {
                request.setAttribute("mensajeError", mensajeError);
            }

            // Redirigir a la vista de listar usuarios
            RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/listarUsuarios.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // Pasar mensaje de error general en caso de excepción
            request.setAttribute("mensajeError", "Error: Se produjo una excepción en el servidor.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/listarUsuarios.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
