package es.uco.pw.servlets.admin;

import es.uco.pw.business.Gestores.GestorReservas;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

/**
 * Controlador que gestiona la funcionalidad de EliminarReservas.
 */
public class EliminarReservasController extends HttpServlet {
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
        // Redirigir a la página de eliminación de reservas
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/eliminarReservas.jsp");
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
        String idReservaStr = request.getParameter("idReserva") != null ? request.getParameter("idReserva").trim() : "";

        // Validar campos obligatorios
        if (idReservaStr.isEmpty()) {
            request.setAttribute("error", "El ID de la reserva es obligatorio.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/eliminarReservas.jsp");
            dispatcher.forward(request, response);
            return;
        }

        int idReserva;

        try {
            // Validar que el ID de la reserva sea un número entero
            idReserva = Integer.parseInt(idReservaStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "El ID de la reserva debe ser un número entero válido.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/eliminarReservas.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            // Usar DAO para eliminar la reserva
            GestorReservas gestor = new GestorReservas();
            int resultado = gestor.eliminarReserva(idReserva);

            switch (resultado) {
                case 0:
                    // Éxito al eliminar la reserva
                    request.setAttribute("mensaje", "Reserva eliminada exitosamente.");
                    break;
                case -1:
                    // Error: Reserva no encontrada
                    request.setAttribute("error", "No se encontró la reserva con el ID proporcionado.");
                    break;
                case -2:
                    // Error: Excepción SQL
                    request.setAttribute("error", "Hubo un error al eliminar la reserva. Inténtelo más tarde.");
                    break;
                default:
                    // Error desconocido
                    request.setAttribute("error", "Error desconocido al eliminar la reserva.");
                    break;
            }
            // Redirigir a la misma página de eliminar reservas con los mensajes de error o éxito
            RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/eliminarReservas.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor. Inténtelo más tarde.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/eliminarReservas.jsp");
            dispatcher.forward(request, response);
        }
    }
}
