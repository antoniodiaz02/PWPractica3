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
</head>
<body>
    <div class="header">
        <div class="container">
            <h1>Lista de Usuarios</h1>
        </div>
    </div>
    <div class="main-content">
        <div class="container">
            <table class="tabla-pistas">
                <thead>
                    <tr>
                        <th>Nombre Completo</th>
                        <th>Fecha de Nacimiento</th>
                        <th>Fecha de Inscripción</th>
                        <th>Correo Electrónico</th>
                        <th>Tipo de Usuario</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        Vector<JugadorDTO> usuarios = (Vector<JugadorDTO>) request.getAttribute("clientes");
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
