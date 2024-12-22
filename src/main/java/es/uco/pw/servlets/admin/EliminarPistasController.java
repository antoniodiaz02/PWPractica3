package es.uco.pw.servlets.admin;

import es.uco.pw.business.Gestores.GestorPistas;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class EliminarPistasController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de eliminación de pistas
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/darBajaPistas.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Capturar el parámetro del formulario
        String nombre = request.getParameter("nombre") != null ? request.getParameter("nombre").trim() : "";

        // Validar el campo obligatorio
        if (nombre.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-error.jsp?error=campos-obligatorios");
            return;
        }

        try {
            // Usar GestorPistas para eliminar la pista
            GestorPistas gestor = new GestorPistas();

            int resultado = gestor.eliminarPista(nombre);

            switch (resultado) {
                case 0:
                    // Redirigir a página de éxito
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-exito.jsp");
                    break;
                case -2:
                    // Error: Nombre no proporcionado o vacío
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-error.jsp?error=nombre-invalido");
                    break;
                case -3:
                    // Error: No existe una pista con este nombre
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-error.jsp?error=pista-no-existe");
                    break;
                case -4:
                    // Error: No se pudo eliminar la pista
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-error.jsp?error=eliminar-fallido");
                    break;
                case -5:
                    // Error: Excepción SQL
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-error.jsp?error=sql-excepcion");
                    break;
                default:
                    // Error desconocido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-error.jsp?error=desconocido");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-error.jsp?error=desconocido");
        }
    }
}