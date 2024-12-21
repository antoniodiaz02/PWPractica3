package es.uco.pw.servlets.admin;

import es.uco.pw.business.Gestores.GestorPistas;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class AsociarMaterialAPistaController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de asociación de material a pista
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/asociarMaterialAPista.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Capturar los parámetros del formulario
        String nombrePista = request.getParameter("nombre") != null ? request.getParameter("nombre").trim() : "";
        String idMaterialStr = request.getParameter("idMaterial") != null ? request.getParameter("idMaterial").trim() : "";

        // Validar campos obligatorios
        if (nombrePista.isEmpty() || idMaterialStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-asociar.jsp?error=campos-obligatorios");
            return;
        }

        int idMaterial;

        try {
            // Validar ID del material como entero positivo
            idMaterial = Integer.parseInt(idMaterialStr);
            if (idMaterial <= 0) {
                response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-asociar.jsp?error=id-material");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-asociar.jsp?error=id-material");
            return;
        }

        try {
            // Usar DAO para asociar el material a la pista
            GestorPistas gestor = new GestorPistas();
            int resultado = gestor.asociarMaterialAPista(nombrePista, idMaterial);

            switch (resultado) {
                case 0:
                    // Asociación exitosa
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-exito-asociar.jsp");
                    break;
                case -1:
                    // Error: Nombre de la pista inválido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-asociar.jsp?error=nombre-pista");
                    break;
                case -2:
                    // Error: ID del material inválido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-asociar.jsp?error=id-material-invalido");
                    break;
                case -3:
                    // Error: La pista no existe
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-asociar.jsp?error=pista-no-existe");
                    break;
                case -4:
                    // Error: El material no existe
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-asociar.jsp?error=material-no-existe");
                    break;
                case -5:
                    // Error: El material no está disponible (reservado o mal estado)
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-asociar.jsp?error=material-no-disponible");
                    break;
                case -6:
                    // Error: Tipos incompatibles
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-asociar.jsp?error=tipos-incompatibles");
                    break;
                case -7:
                    // Error: Sin cambios realizados
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-asociar.jsp?error=sin-cambios");
                    break;
                case -8:
                    // Error: Fallo en la consulta SQL
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-asociar.jsp?error=sql-excepcion");
                    break;
                default:
                    // Error desconocido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-asociar.jsp?error=desconocido");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-asociar.jsp?error=desconocido");
        }
    }
}
