package es.uco.pw.servlets.admin;

import es.uco.pw.business.Gestores.GestorMateriales;

import javax.servlet.*;
import javax.servlet.http.*;
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
 * Controlador que gestiona la funcionalidad de EliminarMateriales.
 */
public class EliminarMaterialesController extends HttpServlet {
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
        // Redirigir a la página de eliminación de materiales
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/darBajaMateriales.jsp");
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
        // Capturar el parámetro del formulario
        String idMaterialParam = request.getParameter("idMaterial") != null ? request.getParameter("idMaterial").trim() : "";

        // Validar el campo obligatorio
        int idMaterial;
        try {
            idMaterial = Integer.parseInt(idMaterialParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-error-material.jsp?error=id-invalido");
            return;
        }

        try {
            // Usar GestorMateriales para eliminar el material
            GestorMateriales gestor = new GestorMateriales();

            int resultado = gestor.eliminarMaterial(idMaterial);

            switch (resultado) {
                case 0:
                    // Redirigir a página de éxito
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-exito-material.jsp");
                    break;
                case -2:
                    // Error: ID de material no válido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-error-material.jsp?error=id-material-invalido");
                    break;
                case -3:
                    // Error: No existe un material con este ID
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-error-material.jsp?error=material-no-existe");
                    break;
                case -4:
                    // Error: No se pudo eliminar el material
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-error-material.jsp?error=eliminar-fallido");
                    break;
                case -5:
                    // Error: Excepción SQL
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-error-material.jsp?error=sql-excepcion");
                    break;
                default:
                    // Error desconocido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-error-material.jsp?error=desconocido");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/eliminar-error-material.jsp?error=desconocido");
        }
    }
}
