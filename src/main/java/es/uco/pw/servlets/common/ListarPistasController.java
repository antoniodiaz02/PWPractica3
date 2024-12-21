package es.uco.pw.servlets.common;

import es.uco.pw.business.Gestores.GestorPistas;
import es.uco.pw.business.DTOs.PistaDTO;
import es.uco.pw.business.DTOs.MaterialDTO;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Vector;
import java.util.*;


public class ListarPistasController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            GestorPistas gestor = new GestorPistas();
            Vector<PistaDTO> todasLasPistas = new Vector<>();

            int resultado = gestor.listarPistas(todasLasPistas);

            if (resultado == 0) {
                // Crear un vector de materiales para cada pista
                Vector<Vector<MaterialDTO>> materialesPorPista = new Vector<>();
                
                // Obtener materiales asociados a cada pista
             // Obtener materiales asociados a cada pista
                for (PistaDTO pista : todasLasPistas) {
                    Vector<MaterialDTO> materiales = new Vector<>();
                    // Fetch the list of materials for this track
                    List<MaterialDTO> materialesDePista = gestor.obtenerMaterialesDePista(pista.getNombre());
                    // Add the materials to the Vector
                    materiales.addAll(materialesDePista);
                    // Add the materials vector to the list
                    materialesPorPista.add(materiales);
                }


                // Pasar las pistas y los materiales al JSP
                request.setAttribute("pistas", todasLasPistas);
                request.setAttribute("materialesPorPista", materialesPorPista);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/common/listarPistas.jsp");
                dispatcher.forward(request, response);
            } else {
                // Manejo de errores según el código de resultado
                // ... tu código actual de manejo de errores
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/MVC/Views/common/error-listar-pistas.jsp?error=excepcion");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
