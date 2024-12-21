package es.uco.pw.servlets.common;

import es.uco.pw.business.Gestores.GestorMateriales;
import es.uco.pw.business.DTOs.MaterialDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Vector;

public class ListarMaterialesController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Crear gestor y Vector para almacenar los materiales
            GestorMateriales gestor = new GestorMateriales();
            Vector<MaterialDTO> todosLosMateriales = new Vector<>();

            // Llamar al método listarMateriales y obtener el código de resultado
            int resultado = gestor.listarMateriales(todosLosMateriales);

            // Manejar resultados según el código devuelto
            if (resultado == 0) {
                // Éxito: Pasar la lista de materiales a la vista
                request.setAttribute("materiales", todosLosMateriales);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/common/listarMateriales.jsp");
                dispatcher.forward(request, response);
            } else if (resultado == -1) {
                // Error: Vector proporcionado es nulo
                response.sendRedirect(request.getContextPath() + "/MVC/Views/common/error-listar-materiales.jsp?error=vector-nulo");
            } else if (resultado == -2) {
                // Error: Datos inválidos en la base de datos
                response.sendRedirect(request.getContextPath() + "/MVC/Views/common/error-listar-materiales.jsp?error=datos-invalidos");
            } else if (resultado == -3) {
                // No se encontraron materiales
                response.sendRedirect(request.getContextPath() + "/MVC/Views/common/lista-vacia-materiales.jsp");
            } else if (resultado == -4) {
                // Error en la consulta SQL
                response.sendRedirect(request.getContextPath() + "/MVC/Views/common/error-listar-materiales.jsp?error=sql");
            } else {
                // Error desconocido
                response.sendRedirect(request.getContextPath() + "/MVC/Views/common/error-listar-materiales.jsp?error=desconocido");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Redirigir a página de error en caso de excepción general
            response.sendRedirect(request.getContextPath() + "/MVC/Views/common/error-listar-materiales.jsp?error=excepcion");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a doGet para manejar la lógica de listado
        doGet(request, response);
    }
}