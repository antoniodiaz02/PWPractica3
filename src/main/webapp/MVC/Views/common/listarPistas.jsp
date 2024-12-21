<%@ page import="es.uco.pw.business.DTOs.PistaDTO" %>
<%@ page import="es.uco.pw.business.DTOs.MaterialDTO" %>
<%@ page import="java.util.Vector" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Pistas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/tabla.css">
</head>
<body>
    <div class="header">
        <div class="container">
            <h1>Lista de Pistas</h1>
        </div>
    </div>
    <div class="main-content">
        <div class="container">
            <table class="tabla-pistas">
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Estado</th>
                        <th>Tipo</th>
                        <th>Tamaño</th>
                        <th>Número Máximo de Jugadores</th>
                        <th>Materiales Asociados</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        Vector<PistaDTO> pistas = (Vector<PistaDTO>) request.getAttribute("pistas");
                        Vector<Vector<MaterialDTO>> materialesPorPista = (Vector<Vector<MaterialDTO>>) request.getAttribute("materialesPorPista");
                        if (pistas != null && !pistas.isEmpty()) {
                            for (int i = 0; i < pistas.size(); i++) {
                                PistaDTO pista = pistas.get(i);
                                Vector<MaterialDTO> materiales = materialesPorPista.get(i);  // Obtener los materiales para esta pista
                    %>
                    <tr>
                        <td><%= pista.getNombre() %></td>
                        <td><%= pista.isDisponible() ? "Disponible" : "No Disponible" %></td>
                        <td><%= pista.isInterior() ? "Interior" : "Exterior" %></td>
                        <td><%= pista.getTamanoPista().toString() %></td>
                        <td><%= pista.getMaxJugadores() %></td>
                        <td>
                            <% if (materiales != null && !materiales.isEmpty()) { %>
                                <ul>
                                    <% for (MaterialDTO material : materiales) { %>
                                        <li>
                                            <strong>ID Material:</strong> <%= material.getIdMaterial() %><br>
                                            <strong>Tipo de Material:</strong> <%= material.getTipoMaterial().name() %><br>
                                            <strong>Uso Interior:</strong> <%= material.getUsoInterior() ? "Interior" : "Exterior" %><br>
                                            <strong>Estado del Material:</strong> <%= material.getEstadoMaterial().name() %><br>
                                        </li>
                                        <hr>
                                    <% } %>
                                </ul>
                            <% } else { %>
                                No hay materiales disponibles
                            <% } %>
                        </td>
                    </tr>
                    <% 
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="6">No hay pistas disponibles.</td>
                    </tr>
                    <% 
                        } 
                    %>
                </tbody>
            </table>
            <a href="<%= request.getContextPath() %>/MVC/Views/admin/adminmenu.jsp" class="button">Volver al Menú</a>
        </div>
    </div>
    <div class="footer">
        <div class="container">
            <p>Aplicación de Gestión Deportiva - Práctica de Programación Web</p>
        </div>
    </div>
</body>
</html>
