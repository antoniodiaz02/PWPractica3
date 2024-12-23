package es.uco.pw.servlets.admin;

import es.uco.pw.business.Gestores.GestorMateriales;
import es.uco.pw.business.DTOs.MaterialDTO;

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
 * Controlador que gestiona la funcionalidad de actualizar el material.
 */
public class UpdateMaterialController extends HttpServlet {
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
        // Redirigir a la página de actualización de materiales
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/updateMaterial.jsp");
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
        String idMaterialStr = request.getParameter("idMaterial") != null ? request.getParameter("idMaterial").trim() : "";
        String tipoMaterial = request.getParameter("tipoMaterial") != null ? request.getParameter("tipoMaterial").trim() : "";
        String usoInteriorStr = request.getParameter("usoInterior") != null ? request.getParameter("usoInterior").trim() : "false";
        String estadoMaterial = request.getParameter("estadoMaterial") != null ? request.getParameter("estadoMaterial").trim() : "";

        // Validar campos obligatorios
        if (idMaterialStr.isEmpty() || tipoMaterial.isEmpty() || estadoMaterial.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-material.jsp?error=campos-obligatorios");
            return;
        }

        boolean usoInterior;
        int idMaterial;

        try {
            // Validar ID del material como entero
            idMaterial = Integer.parseInt(idMaterialStr);
            if (idMaterial <= 0) {
                response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-material.jsp?error=id-invalido");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-material.jsp?error=id-invalido");
            return;
        }

        // Validar booleano
        usoInterior = Boolean.parseBoolean(usoInteriorStr);

        try {
            // Crear objeto MaterialDTO
            MaterialDTO material = new MaterialDTO(
                idMaterial,
                MaterialDTO.TipoMaterial.valueOf(tipoMaterial.toUpperCase()),
                usoInterior,
                MaterialDTO.EstadoMaterial.valueOf(estadoMaterial.toUpperCase())
            );

            // Usar DAO para actualizar el material
            GestorMateriales gestor = new GestorMateriales();

            int resultado = gestor.updateMaterial(material);

            switch (resultado) {
                case 0:
                    // Redirigir a página de éxito
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-exito-material.jsp");
                    break;
                case -2:
                    // Error: Material no proporcionado
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-material.jsp?error=material-null");
                    break;
                case -3:
                    // Error: Tipo de material no especificado
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-material.jsp?error=tipo-material-invalido");
                    break;
                case -4:
                    // Error: Estado del material no especificado
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-material.jsp?error=estado-material-invalido");
                    break;
                case -5:
                    // Error: El material no existe
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-material.jsp?error=no-existe");
                    break;
                case -6:
                    // Error: No se pudo actualizar el material
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-material.jsp?error=actualizar-fallido");
                    break;
                case -7:
                    // Error: Excepción SQL
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-material.jsp?error=sql-excepcion");
                    break;
                default:
                    // Error desconocido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-material.jsp?error=desconocido");
                    break;
            }
        } catch (IllegalArgumentException e) {
            // Error en el valor del tipo o estado del material
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-material.jsp?error=valor-invalido");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/actualizar-error-material.jsp?error=desconocido");
        }
    }
}
