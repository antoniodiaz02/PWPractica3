<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %> <!-- Importar List -->

<%
	// Sanitización correcta realizada en el serverlet.
    es.uco.pw.business.DTOs.JugadorDTO admin = (es.uco.pw.business.DTOs.JugadorDTO) session.getAttribute("jugador");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel de Control - Administrador</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/adminmenu.css">
</head>
<body>

<header class="header">
    <div class="container">
        <h1>Panel de Administrador</h1>
        <p>Bienvenido, <%= admin.getNombre() %>!</p>
        <p>Fecha de Ingreso: <%= admin.getFechaInscripcion() %></p>
        <p>Correo: <%= admin.getCorreoElectronico() %></p>
    </div>
</header>

<nav class="navbar">
    <div class="container">
        <ul>
            <li><a href="<%= request.getContextPath() %>/listarUsuarios">Listar Usuarios</a></li>
            <li><a href="<%= request.getContextPath() %>/listarPistas">Listar Pistas</a></li>
            <li><a href="<%= request.getContextPath() %>/listarMateriales">Listar Materiales</a></li>
            <li><a href="<%= request.getContextPath() %>/adminmenu/registerMaterial">Dar de Alta Materiales</a></li>
            <li><a href="<%= request.getContextPath() %>/adminmenu/registerPista">Dar de Alta Pistas</a></li>
            <li><a href="<%= request.getContextPath() %>/adminmenu/eliminarPistas">Dar de Baja Pistas</a></li>
            <li><a href="<%= request.getContextPath() %>/adminmenu/eliminarMateriales">Dar de Baja Materiales</a></li>
            <li><a href="<%= request.getContextPath() %>/adminmenu/asociarMaterialAPista">Asociar Materiales a Pistas</a></li>
            <li><a href="<%= request.getContextPath() %>/adminmenu/updatePista">Modificar estado de pistas</a></li>
            <li><a href="<%= request.getContextPath() %>/adminmenu/updateMaterial">Modificar estado de materiales</a></li>
            <li><a href="eliminarReservas.jsp">Eliminar Reservas</a></li>
            <li><a href="<%= request.getContextPath() %>/modificarUsuario" class="btn btn-danger">Modificar Datos de Usuarios</a></li>
            <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-danger">Cerrar sesión</a></li>
        </ul>
    </div>
</nav>
<%-- 
<main class="main-content">
    <div class="container">
        <h2>Lista de Clientes</h2>

        <% 
            if (usuarios == null || usuarios.isEmpty()) {
        %>
            <p>No se encontraron usuarios registrados.</p>
        <% 
            } else {
        %>
            <table>
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Apellidos</th>
                        <th>Fecha de Nacimiento</th>
                        <th>Correo Electrónico</th>
                        <th>Tipo de Usuario</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (es.uco.pw.business.DTOs.JugadorDTO usuario : usuarios) { %>
                        <tr>
                            <td><%= usuario.getNombre() %></td>
                            <td><%= usuario.getApellidos() %></td>
                            <td><%= usuario.getFechaNacimiento() %></td>
                            <td><%= usuario.getCorreoElectronico() %></td>
                            <td><%= usuario.getTipoUsuario() %></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% 
            }
        %>
    </div>
</main>
--%>

<footer class="footer">
    <div class="container">
        <p>Aplicación de Gestión Deportiva - Práctica de Programación Web</p>
    </div>
</footer>

</body>
</html>
