<%@ page import="es.uco.pw.business.DTOs.JugadorDTO" %>
<%@ page import="java.util.Vector" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Usuarios</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/tabla.css">
    <link rel="icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/x-icon">
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
            <h1>Lista de Usuarios</h1>
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
                            <th>Nombre Completo</th>
                            <th>Fecha de Nacimiento</th>
                            <th>Fecha de Inscripci�n</th>
                            <th>Correo Electr�nico</th>
                            <th>Tipo de Usuario</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            Vector<JugadorDTO> usuarios = (Vector<JugadorDTO>) request.getAttribute("usuarios");
                            if (usuarios != null && !usuarios.isEmpty()) {
                                for (JugadorDTO usuario : usuarios) {
                        %>
                        <tr>
                            <td><%= usuario.getNombreCompleto() %></td>
                            <td><%= usuario.getFechaNacimiento() %></td>
                            <td><%= usuario.getFechaInscripcion() %></td>
                            <td><%= usuario.getCorreoElectronico() %></td>
                            <td><%= usuario.getTipoUsuario() %></td>
                        </tr>
                        <% 
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="5">No hay usuarios disponibles.</td>
                        </tr>
                        <% 
                            } 
                        %>
                    </tbody>
                </table>
            <% } %>
            <a href="<%= request.getContextPath() %>/adminmenu" class="button">Volver al Men�</a>
        </div>
    </div>
    <div class="footer">
        <div class="container">
            <p>Aplicaci�n de Gesti�n Deportiva - Pr�ctica de Programaci�n Web</p>
        </div>
    </div>
</body>
</html>
