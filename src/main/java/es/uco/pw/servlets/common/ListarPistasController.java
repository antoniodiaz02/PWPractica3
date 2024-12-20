package es.uco.pw.servlets.common;

import es.uco.pw.data.DAOs.PistaDAO;
import es.uco.pw.business.DTOs.PistaDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Vector;

public class ListarPistasController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Crear DAO y Vector para almacenar las pistas
            PistaDAO pistaDAO = new PistaDAO();
            Vector<PistaDTO> todasLasPistas = new Vector<>();

            // Llamar al método listarPistas y obtener el código de resultado
            int resultado = pistaDAO.listarPistas(todasLasPistas);

            // Manejar resultados según el código devuelto
            if (resultado == 0) {
                // Éxito: Pasar la lista de pistas a la vista
                request.setAttribute("pistas", todasLasPistas);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/common/listarPistas.jsp");
                dispatcher.forward(request, response);
            } else if (resultado == -1) {
                // Error: Vector proporcionado es nulo
                response.sendRedirect(request.getContextPath() + "/MVC/Views/common/error-listar-pistas.jsp?error=vector-nulo");
            } else if (resultado == -2) {
                // Error: Datos inválidos en la base de datos
                response.sendRedirect(request.getContextPath() + "/MVC/Views/common/error-listar-pistas.jsp?error=datos-invalidos");
            } else if (resultado == -3) {
                // No se encontraron pistas
                response.sendRedirect(request.getContextPath() + "/MVC/Views/common/lista-vacia-pistas.jsp");
            } else if (resultado == -4) {
                // Error en la consulta SQL
                response.sendRedirect(request.getContextPath() + "/MVC/Views/common/error-listar-pistas.jsp?error=sql");
            } else {
                // Error desconocido
                response.sendRedirect(request.getContextPath() + "/MVC/Views/common/error-listar-pistas.jsp?error=desconocido");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Redirigir a página de error en caso de excepción general
            response.sendRedirect(request.getContextPath() + "/MVC/Views/common/error-listar-pistas.jsp?error=excepcion");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a doGet para manejar la lógica de listado
        doGet(request, response);
    }
}