package es.uco.pw.servlets.user;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

import es.uco.pw.business.DTOs.JugadorDTO;

public class UserMenuController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de gestión de bonos.
    	
    	 // Obtener el usuario de la sesión
        HttpSession session = request.getSession();
        JugadorDTO admin = (JugadorDTO) session.getAttribute("jugador");

        // Validar que el usuario esté en sesión y sea un administrador
        if (admin == null || !"cliente".equals(admin.getTipoUsuario())) {
        	response.sendRedirect("../../../index.jsp"); 
            return; // Detener la ejecución
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/user/usermenu.jsp");
        dispatcher.forward(request, response);
    }
}