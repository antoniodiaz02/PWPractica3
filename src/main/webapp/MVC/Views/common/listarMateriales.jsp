<%@ page import="es.uco.pw.business.DTOs.MaterialDTO" %>
<%@ page import="java.util.Vector" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Materiales</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/tabla.css">
    <style>
        .error-message {
            color: red;
            font-weight: bold;
            margin: 20px 0;
            padding: 10px;
            border: 2px solid red;
            border-radius: 5px;
            background-color: #ffe6e6;
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="container">
            <h1>Lista de Materiales</h1>
        </div>
    </div>
    <div class="main-content">
        <div class="container">
            <% 
                String mensajeError = (String) request.getAttribute("mensajeError");
                if (mensajeError != null) { 
            %>
                <div class="error-message">
                    <p><%= mensajeError %></p>
                </div>
            <% } else { %>
                <table class="tabla-pistas">
                    <thead>
                        <tr>
                            <th>ID Material</th>
                            <th>Tipo de Material</th>
                            <th>Uso Interior</th>
                            <th>Estado del Material</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            Vector<MaterialDTO> materiales = (Vector<MaterialDTO>) request.getAttribute("materiales");
                            if (materiales != null && !materiales.isEmpty()) {
                                for (MaterialDTO material : materiales) {
                        %>
                        <tr>
                            <td><%= material.getIdMaterial() %></td>
                            <td><%= material.getTipoMaterial().toString() %></td>
                            <td><%= material.getUsoInterior() ? "Sí" : "No" %></td>
                            <td><%= material.getEstadoMaterial().toString() %></td>
                        </tr>
                        <% 
                                }
                            } else { 
                        %>
                        <tr>
                            <td colspan="4">No hay datos disponibles.</td>
                        </tr>
                        <% 
                            } 
                        %>
                    </tbody>
                </table>
            <% } %>
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
