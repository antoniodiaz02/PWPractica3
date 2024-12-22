package es.uco.pw.servlets.admin;

import es.uco.pw.business.Gestores.GestorPistas;
import es.uco.pw.business.DTOs.PistaDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class UpdatePistaController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de actualización de pistas
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/updatePistas.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Capturar los parámetros del formulario
        String nombrePista = request.getParameter("nombrePista") != null ? request.getParameter("nombrePista").trim() : "";
        String disponibleStr = request.getParameter("disponible") != null ? request.getParameter("disponible").trim() : "false";
        String interiorStr = request.getParameter("interior") != null ? request.getParameter("interior").trim() : "false";
        String tamanoPista = request.getParameter("tamanoPista") != null ? request.getParameter("tamanoPista").trim() : "";
        String maxJugadoresStr = request.getParameter("maxJugadores") != null ? request.getParameter("maxJugadores").trim() : "";

        // Validar campos obligatorios
        if (nombrePista.isEmpty() || tamanoPista.isEmpty() || maxJugadoresStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-pista.jsp?error=campos-obligatorios");
            return;
        }

        boolean disponible;
        boolean interior;
        int maxJugadores;

        try {
            // Validar máximo de jugadores como entero
            maxJugadores = Integer.parseInt(maxJugadoresStr);
            if (maxJugadores <= 0) {
                response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-pista.jsp?error=max-jugadores");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-pista.jsp?error=max-jugadores");
            return;
        }

        // Validar booleanos
        disponible = Boolean.parseBoolean(disponibleStr);
        interior = Boolean.parseBoolean(interiorStr);

        try {
            // Crear objeto PistaDTO
            PistaDTO pista = new PistaDTO(
                nombrePista,
                disponible,
                interior,
                PistaDTO.TamanoPista.valueOf(tamanoPista.toUpperCase()),
                maxJugadores
            );

            // Usar DAO para actualizar la pista
            GestorPistas gestor = new GestorPistas();

            int resultado = gestor.updatePista(pista);

            switch (resultado) {
                case 0:
                    // Redirigir a página de éxito
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-exito-pista.jsp");
                    break;
                case -2:
                    // Error: Pista no proporcionada
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-pista.jsp?error=pista-null");
                    break;
                case -3:
                    // Error: Nombre de la pista no válido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-pista.jsp?error=nombre-invalido");
                    break;
                case -4:
                    // Error: Número máximo de jugadores no válido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-pista.jsp?error=max-jugadores");
                    break;
                case -5:
                    // Error: Tamaño de la pista no especificado
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-pista.jsp?error=tamano-invalido");
                    break;
                case -6:
                    // Error: La pista no existe
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-pista.jsp?error=no-existe");
                    break;
                case -7:
                    // Error: Actualización fallida
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-pista.jsp?error=actualizar-fallido");
                    break;
                case -8:
                    // Error: Excepción SQL
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-pista.jsp?error=sql-excepcion");
                    break;
                default:
                    // Error desconocido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-pista.jsp?error=desconocido");
                    break;
            }
        } catch (IllegalArgumentException e) {
            // Error en el valor del tamaño de la pista
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-pista.jsp?error=valor-invalido");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-pista.jsp?error=desconocido");
        }
    }
}
