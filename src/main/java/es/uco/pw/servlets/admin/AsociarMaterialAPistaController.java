package es.uco.pw.servlets.admin;

import es.uco.pw.business.Gestores.GestorPistas;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class AsociarMaterialAPistaController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener los parámetros del formulario
            String nombrePista = request.getParameter("nombre");
            String idMaterialStr = request.getParameter("idMaterial");

            // Validar parámetros
            if (nombrePista == null || nombrePista.isEmpty() || idMaterialStr == null || idMaterialStr.isEmpty()) {
                request.setAttribute("mensajeError", "Error: Parámetros inválidos.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/asociarMaterialAPista.jsp");
                dispatcher.forward(request, response);
                return;
            }

            int idMaterial;
            try {
                idMaterial = Integer.parseInt(idMaterialStr);
            } catch (NumberFormatException e) {
                request.setAttribute("mensajeError", "Error: ID del material inválido.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/asociarMaterialAPista.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Crear instancia del gestor
            GestorPistas gestor = new GestorPistas();

            // Llamar al método asociarMaterialAPista y obtener el código de resultado
            int resultado = gestor.asociarMaterialAPista(nombrePista, idMaterial);

            // Manejar resultados según el código devuelto
            String mensajeError = null;
            if (resultado == 0) {
                // Éxito: Pasar mensaje de éxito
                request.setAttribute("mensajeExito", "Material asociado exitosamente a la pista.");
            } else if (resultado == -1) {
                mensajeError = "Error: Nombre de la pista inválido.";
            } else if (resultado == -2) {
                mensajeError = "Error: ID del material inválido.";
            } else if (resultado == -3) {
                mensajeError = "Error: La pista no existe.";
            } else if (resultado == -4) {
                mensajeError = "Error: El material no existe.";
            } else if (resultado == -5) {
                mensajeError = "Error: Tipos incompatibles.";
            } else if (resultado == -6) {
                mensajeError = "Error: No se afectaron filas.";
            } else if (resultado == -7) {
                mensajeError = "Error: Excepción SQL.";
            } else {
                mensajeError = "Error desconocido.";
            }

            // Si hubo un error, pasar el mensaje de error a la vista
            if (mensajeError != null) {
                request.setAttribute("mensajeError", mensajeError);
            }

            // Redirigir a la página asociarMaterialAPista.jsp
            RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/asociarMaterialAPista.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // Pasar mensaje de error general en caso de excepción
            request.setAttribute("mensajeError", "Error: Se produjo una excepción en el servidor.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/asociarMaterialAPista.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a una página de error ya que esta operación debe ser vía POST
        request.setAttribute("mensajeError", "Error: La operación debe ser realizada vía POST.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/asociarMaterialAPista.jsp");
        dispatcher.forward(request, response);
    }
}
