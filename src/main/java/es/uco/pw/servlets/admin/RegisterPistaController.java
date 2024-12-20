package es.uco.pw.servlets.admin;

import es.uco.pw.business.Gestores.GestorPistas;
import es.uco.pw.business.DTOs.PistaDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class RegisterPistaController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de registro de pistas
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/darAltaPistas.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Capturar los parámetros del formulario
        String nombre = request.getParameter("nombre") != null ? request.getParameter("nombre").trim() : "";
        String disponibleStr = request.getParameter("disponible") != null ? request.getParameter("disponible").trim() : "false";
        String interiorStr = request.getParameter("interior") != null ? request.getParameter("interior").trim() : "false";
        String tamano = request.getParameter("tamano") != null ? request.getParameter("tamano").trim() : "";
        String maxJugadoresStr = request.getParameter("maxJugadores") != null ? request.getParameter("maxJugadores").trim() : "0";

        // Validar campos obligatorios
        if (nombre.isEmpty() || tamano.isEmpty() || maxJugadoresStr.isEmpty() || disponibleStr.isEmpty() || interiorStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error.jsp?error=campos-obligatorios");
            return;
        }

        boolean disponible = Boolean.parseBoolean(disponibleStr);
        boolean interior = Boolean.parseBoolean(interiorStr);
        int maxJugadores;

        try {
            // Validar número máximo de jugadores
            maxJugadores = Integer.parseInt(maxJugadoresStr);
            if (maxJugadores <= 0) {
                response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error.jsp?error=max-jugadores");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error.jsp?error=max-jugadores");
            return;
        }

        try {
            // Crear objeto PistaDTO
            PistaDTO pista = new PistaDTO(nombre, disponible, interior, PistaDTO.TamanoPista.valueOf(tamano.toUpperCase()), maxJugadores);

            // Usar DAO para registrar la pista
            
            GestorPistas gestor = new GestorPistas();
     
            int resultado = gestor.insertPista(pista);

            switch (resultado) {
                case 0:
                    // Redirigir a página de éxito
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-exito.jsp");
                    break;
                case -2:
                    // Error: Pista no proporcionada
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error.jsp?error=pista-null");
                    break;
                case -3:
                    // Error: Nombre no válido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error.jsp?error=nombre-invalido");
                    break;
                case -4:
                    // Error: Número máximo de jugadores no válido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error.jsp?error=max-jugadores");
                    break;
                case -5:
                    // Error: Tamaño de pista no especificado
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error.jsp?error=tamano-pista");
                    break;
                case -6:
                    // Error: Ya existe una pista con este nombre
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error.jsp?error=nombre-existente");
                    break;
                case -7:
                    // Error: Inserción fallida
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error.jsp?error=insertar-fallido");
                    break;
                case -8:
                    // Error: Excepción SQL
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error.jsp?error=sql-excepcion");
                    break;
                default:
                    // Error desconocido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error.jsp?error=desconocido");
                    break;
            }
        } catch (IllegalArgumentException e) {
            // Error en el valor del tamaño de la pista
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error.jsp?error=tamano-invalido");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error.jsp?error=desconocido");
        }
    }
}
