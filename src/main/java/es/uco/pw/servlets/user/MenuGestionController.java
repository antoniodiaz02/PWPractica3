package es.uco.pw.servlets.user;

import javax.servlet.*;
import javax.servlet.http.*;

import es.uco.pw.business.DTOs.ReservaDTO;
import es.uco.pw.business.Gestores.GestorReservas;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class MenuGestionController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de gestión de bonos.
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/user/gestionReservas.jsp");
        dispatcher.forward(request, response);
    }
}