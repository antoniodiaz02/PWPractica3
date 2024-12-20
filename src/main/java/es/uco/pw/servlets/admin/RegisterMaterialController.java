package es.uco.pw.servlets.admin;

import es.uco.pw.data.DAOs.MaterialDAO;
import es.uco.pw.business.DTOs.MaterialDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class RegisterMaterialController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de registro de materiales
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/darAltaMateriales.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Capturar los parámetros del formulario
        String idMaterialStr = request.getParameter("idMaterial") != null ? request.getParameter("idMaterial").trim() : "";
        String tipoMaterial = request.getParameter("tipoMaterial") != null ? request.getParameter("tipoMaterial").trim() : "";
        String usoInteriorStr = request.getParameter("usoInterior") != null ? request.getParameter("usoInterior").trim() : "false";
        String estadoMaterial = request.getParameter("estadoMaterial") != null ? request.getParameter("estadoMaterial").trim() : "";

        // Validar campos obligatorios
        if (idMaterialStr.isEmpty() || tipoMaterial.isEmpty() || usoInteriorStr.isEmpty() || estadoMaterial.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-material.jsp?error=campos-obligatorios");
            return;
        }

        int idMaterial;
        boolean usoInterior;

        try {
            // Validar ID del material
            idMaterial = Integer.parseInt(idMaterialStr);
            if (idMaterial <= 0) {
                response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-material.jsp?error=id-material");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-material.jsp?error=id-material");
            return;
        }

        // Validar uso interior como booleano
        usoInterior = Boolean.parseBoolean(usoInteriorStr);

        try {
            // Crear objeto MaterialDTO
            MaterialDTO material = new MaterialDTO(
                idMaterial,
                MaterialDTO.TipoMaterial.valueOf(tipoMaterial.toUpperCase()),
                usoInterior,
                MaterialDTO.EstadoMaterial.valueOf(estadoMaterial.toUpperCase())
            );

            // Usar DAO para registrar el material
            MaterialDAO materialDAO = new MaterialDAO();
            int resultado = materialDAO.insertMaterial(material);

            switch (resultado) {
                case 0:
                    // Redirigir a página de éxito
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-exito-material.jsp");
                    break;
                case -2:
                    // Error: Material no proporcionado
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-material.jsp?error=material-null");
                    break;
                case -3:
                    // Error: Tipo de material no válido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-material.jsp?error=tipo-material");
                    break;
                case -4:
                    // Error: Estado del material no válido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-material.jsp?error=estado-material");
                    break;
                case -5:
                    // Error: Ya existe un material con este ID
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-material.jsp?error=id-existente");
                    break;
                case -7:
                    // Error: Inserción fallida
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-material.jsp?error=insertar-fallido");
                    break;
                case -6:
                    // Error: Excepción SQL
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-material.jsp?error=sql-excepcion");
                    break;
                default:
                    // Error desconocido
                    response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-material.jsp?error=desconocido");
                    break;
            }
        } catch (IllegalArgumentException e) {
            // Error en el valor del tipo o estado del material
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-material.jsp?error=valor-invalido");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/registro-error-material.jsp?error=desconocido");
        }
    }
}