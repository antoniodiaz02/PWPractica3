package es.uco.pw.servlets.admin;

import es.uco.pw.business.Gestores.GestorUsuarios;

import es.uco.pw.business.DTOs.JugadorDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Vector;

public class ListarUsuariosController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Crear DAO y Vector para almacenar los clientes
        	GestorUsuarios gestor = new GestorUsuarios();
      
            Vector<JugadorDTO> todosLosUsuarios = new Vector<>();

            // Llamar al método listarClientes y obtener el código de resultado
            int resultado = gestor.listarUsuarios(todosLosUsuarios);

            // Manejar resultados según el código devuelto
            if (resultado == 0) {
                // Éxito: Pasar la lista de clientes a la vista
                request.setAttribute("clientes", todosLosUsuarios);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/admin/listarUsuarios.jsp");
                dispatcher.forward(request, response);
            } else if (resultado == -1) {
                // Error: Vector proporcionado es nulo
                response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/error-listar-usuarios.jsp?error=vector-nulo");
            } else if (resultado == -2) {
                // Error: Datos inválidos en la base de datos
                response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/error-listar-usuarios.jsp?error=datos-invalidos");
            } else if (resultado == -3) {
                // No se encontraron clientes
                response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/lista-vacia-usuarios.jsp");
            } else if (resultado == -4) {
                // Error en la consulta SQL
                response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/error-listar-usuarios.jsp?error=sql");
            } else {
                // Error desconocido
                response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/error-listar-usuarios.jsp?error=desconocido");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Redirigir a página de error en caso de excepción general
            response.sendRedirect(request.getContextPath() + "/MVC/Views/admin/error-listar-usuarios.jsp?error=excepcion");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a doGet para manejar la lógica de listado
        doGet(request, response);
    }
}
