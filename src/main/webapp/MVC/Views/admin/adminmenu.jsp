<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    es.uco.pw.business.DTOs.JugadorDTO admin = (es.uco.pw.business.DTOs.JugadorDTO) session.getAttribute("jugador");

    if (admin == null) {
        response.sendRedirect("../../../index.jsp"); // Redireccionar al login si no hay usuario en sesión
        return; // Detener la ejecución de la página
    }
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
            <li><a href="listarClientes.jsp">Listar Clientes</a></li>
            <li><a href="darAltaMateriales.jsp">Dar de Alta Materiales</a></li>
            <li><a href="darAltaPistas.jsp">Dar de Alta Pistas</a></li>
            <li><a href="asociarMaterialesPistas.jsp">Asociar Materiales a Pistas</a></li>
            <li><a href="modificarEstadoMateriales.jsp">Modificar Estado de Materiales</a></li>
            <li><a href="modificarEstadoPistas.jsp">Modificar Estado de Pistas</a></li>
            <li><a href="eliminarReservas.jsp">Eliminar Reservas</a></li>
            <li><li><a href="<%= request.getContextPath() %>/logout" class="btn btn-danger">Cerrar sesión</a></li></li>
        </ul>
    </div>
</nav>

<main class="main-content">
    <div class="container">
        <h2>Lista de Clientes</h2>
        
        <%
            es.uco.pw.data.DAOs.JugadorDAO jugadorDAO = new es.uco.pw.data.DAOs.JugadorDAO();
            int codigo = jugadorDAO.listarUsuarios();
            
            if (codigo == 2) {
        %>
            <p>No se encontraron usuarios registrados.</p>
        <%
            } else if (codigo == -1) {
        %>
            <p>Ocurrió un error al obtener la lista de usuarios.</p>
        <%
            }
        %>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>Aplicación de Gestión Deportiva - Práctica de Programación Web</p>
    </div>
</footer>

</body>
</html>
