<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %> <!-- Importar List -->

<%
    es.uco.pw.business.DTOs.JugadorDTO admin = (es.uco.pw.business.DTOs.JugadorDTO) session.getAttribute("jugador");

    if (admin == null || !admin.getTipoUsuario().equals("administrador")) {
        response.sendRedirect("../../../index.jsp"); // Redireccionar al login si no hay usuario en sesión
        return; // Detener la ejecución de la página
    }

    // Obtener la lista de usuarios desde el DAO
    //es.uco.pw.data.DAOs.JugadorDAO jugadorDAO = new es.uco.pw.data.DAOs.JugadorDAO();
    //List<es.uco.pw.business.DTOs.JugadorDTO> usuarios = jugadorDAO.listarUsuarios();

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
            <li><a href="<%= request.getContextPath() %>/adminmenu/registerMaterial">Dar de Alta Materiales</a></li>
            <li><a href="<%= request.getContextPath() %>/adminmenu/registerPista">Dar de Alta Pistas</a></li>
            <li><a href="asociarMaterialesPistas.jsp">Asociar Materiales a Pistas</a></li>
            <li><a href="modificarEstadoMateriales.jsp">Modificar Estado de Materiales</a></li>
            <li><a href="modificarEstadoPistas.jsp">Modificar Estado de Pistas</a></li>
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
