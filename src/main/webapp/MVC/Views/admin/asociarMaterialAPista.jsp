<%@ page import="es.uco.pw.business.DTOs.PistaDTO" %>
<%@ page import="es.uco.pw.business.DTOs.MaterialDTO" %>
<%@ page import="java.util.Vector" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Asociar Material a Pista</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/tabla.css">
    <style>
        .error {
            color: red;
            font-weight: bold;
            margin: 20px 0;
        }
        .success {
            color: green;
            font-weight: bold;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="container">
            <h1>Asociar Material a Pista</h1>
        </div>
    </div>

    <div class="main-content">
        <div class="container">
            <!-- Mostrar mensaje de error si existe -->
            <%
                String mensajeError = (String) request.getAttribute("mensajeError");
                String mensajeExito = (String) request.getAttribute("mensajeExito");
                if (mensajeError != null) {
            %>
            <p class="error"><%= mensajeError %></p>
            <% } else if (mensajeExito != null) { %>
            <p class="success"><%= mensajeExito %></p>
            <% } %>

            <!-- Formulario para asociar material a pista -->
            <form action="<%= request.getContextPath() %>/adminmenu/AsociarMaterialAPistaController" method="post">
                <!-- Selección de pista -->
                <div class="form-group">
                    <label for="nombrePista">Seleccionar Pista:</label>
                    <select name="nombrePista" id="nombrePista" required>
                        <%
                            Vector<PistaDTO> pistas = (Vector<PistaDTO>) request.getAttribute("pistas");
                            if (pistas != null && !pistas.isEmpty()) {
                                for (PistaDTO pista : pistas) {
                        %>
                        <option value="<%= pista.getNombre() %>">
                            <%= pista.getNombre() %> (<%= pista.isInterior() ? "Interior" : "Exterior" %>)
                        </option>
                        <%
                                }
                            } else {
                        %>
                        <option value="" disabled>No hay pistas disponibles</option>
                        <% } %>
                    </select>
                </div>

                <!-- Selección de material -->
                <div class="form-group">
                    <label for="idMaterial">Seleccionar Material:</label>
                    <select name="idMaterial" id="idMaterial" required>
                        <%
                            Vector<MaterialDTO> materiales = (Vector<MaterialDTO>) request.getAttribute("materiales");
                            if (materiales != null && !materiales.isEmpty()) {
                                for (MaterialDTO material : materiales) {
                        %>
                        <option value="<%= material.getIdMaterial() %>">
                            ID: <%= material.getIdMaterial() %> - <%= material.getTipoMaterial() %> 
                            (<%= material.getUsoInterior() ? "Interior" : "Exterior" %>) 
                            - Estado: <%= material.getEstadoMaterial() %>
                        </option>
                        <%
                                }
                            } else {
                        %>
                        <option value="" disabled>No hay materiales disponibles</option>
                        <% } %>
                    </select>
                </div>

                <!-- Botones de acción -->
                <div class="form-group">
                    <button type="submit" class="button">Asociar</button>
                    <a href="<%= request.getContextPath() %>/MVC/Views/admin/adminmenu.jsp" class="button">Cancelar</a>
                </div>
            </form>
        </div>
    </div>

    <div class="footer">
        <div class="container">
            <p>Aplicación de Gestión Deportiva - Práctica de Programación Web</p>
        </div>
    </div>
</body>
</html>
