package es.uco.pw.servlets.user;

import javax.servlet.*;
import javax.servlet.http.*;

import es.uco.pw.business.DTOs.ReservaDTO;
import es.uco.pw.business.Gestores.GestorReservas;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * Controlador que gestiona la funcionalidad de consultar reservas.
 */
public class ConsultarReservasController extends HttpServlet {
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
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/user/consultarReservas.jsp");
        dispatcher.forward(request, response);
    }
    
    /**
     * Maneja las solicitudes HTTP POST.
     * @param request el objeto {@link HttpServletRequest} que contiene la solicitud del cliente.
     * @param response el objeto {@link HttpServletResponse} que contiene la respuesta al cliente.
     * @throws ServletException si ocurre un error en el despacho de la solicitud.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parámetros del formulario
        String correoUser = request.getParameter("correoUser") != null ? request.getParameter("correoUser").trim() : "";
        String fechaInicioStr = request.getParameter("fechaInicio") != null ? request.getParameter("fechaInicio").trim() : "";
        String fechaFinalStr = request.getParameter("fechaFinal") != null ? request.getParameter("fechaFinal").trim() : "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        java.util.Date fechaInicio = null;
        java.util.Date fechaFinal = null;

        // Validar y parsear fechas
        try {
            if (!fechaInicioStr.isEmpty()) {
                fechaInicio = sdf.parse(fechaInicioStr);
            }
            if (!fechaFinalStr.isEmpty()) {
                fechaFinal = sdf.parse(fechaFinalStr);
            }
        } catch (ParseException e) {
            request.setAttribute("error", "Formato de fecha incorrecto. Use el formato correcto: yyyy-MM-dd'T'HH:mm.");
            request.getRequestDispatcher("/MVC/Views/user/consultarReservas.jsp").forward(request, response);
            return;
        }

        try {
            // Consultar reservas a través del Gestor
            GestorReservas gestor = new GestorReservas();
            Vector<ReservaDTO> vectorReservas = new Vector<>();
            Vector<String> vectorNombres = new Vector<>();
            int resultado= gestor.listarReservasEntreFechas(vectorReservas, vectorNombres, fechaInicio, fechaFinal, correoUser);

            switch (resultado) {
                case 0: // Reservas encontradas
                    request.setAttribute("reservas", vectorReservas);
                    request.setAttribute("nombres", vectorNombres);
                    request.getRequestDispatcher("/MVC/Views/user/consultarReservas.jsp").forward(request, response);
                    break;
                case -1: // Error genérico
                    request.setAttribute("error", "Se produjo un error al intentar buscar las reservas.");
                    request.getRequestDispatcher("/MVC/Views/user/consultarReservas.jsp").forward(request, response);
                    break;
                case -2: // Error en la base de datos
                    request.setAttribute("error", "Ocurrió un error al acceder a la base de datos.");
                    request.getRequestDispatcher("/MVC/Views/user/consultarReservas.jsp").forward(request, response);
                    break;
                case -3: // Sin reservas
                    request.setAttribute("error", "No existen reservas para las fechas indicadas.");
                    request.getRequestDispatcher("/MVC/Views/user/consultarReservas.jsp").forward(request, response);
                    break;
                default: // Error desconocido
                    request.setAttribute("error", "Ha ocurrido un error desconocido.");
                    request.getRequestDispatcher("/MVC/Views/user/consultarReservas.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error en el servidor. Inténtelo más tarde.");
            request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
        }
    }
}
