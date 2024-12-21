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

            String mensajeError = null;
            if (resultado == 0) {
                // Crear un vector de materiales para cada pista
                Vector<Vector<MaterialDTO>> materialesPorPista = new Vector<>();

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
            } else if (resultado == -1) {
                // Error: El vector proporcionado es nulo
                mensajeError = "Error: El vector proporcionado es nulo.";
            } else if (resultado == -2) {
                // Error: Datos inválidos en la base de datos
                mensajeError = "Error: Datos inválidos en la base de datos.";
            } else if (resultado == -3) {
                // No se encontraron pistas disponibles
                mensajeError = "No se encontraron pistas disponibles.";
            } else if (resultado == -4) {
                // Error en la consulta SQL
                mensajeError = "Error: Fallo en la consulta SQL.";
            } else {
                // Error desconocido
                mensajeError = "Error desconocido.";
            }

            // Si hubo un error, pasar el mensaje de error a la vista
            if (mensajeError != null) {
                request.setAttribute("mensajeError", mensajeError);
            }

            // Redirigir a la vista de listar pistas
            RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/common/listarPistas.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // Pasar mensaje de error general en caso de excepción
            request.setAttribute("mensajeError", "Error: Se produjo una excepción en el servidor.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/common/listarPistas.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
