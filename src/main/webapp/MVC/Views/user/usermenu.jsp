<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    es.uco.pw.business.DTOs.JugadorDTO jugador = (es.uco.pw.business.DTOs.JugadorDTO) session.getAttribute("jugador");

    if (jugador == null) {
        response.sendRedirect("../../../index.jsp"); // Redireccionar al login si no hay usuario en sesión
        return; // Detener la ejecución de la página
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Página Principal - Usuario</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/usermenu.css">
</head>
<body>

<header class="header">
    <div class="container">
        <h1>Bienvenido, <%= jugador.getNombre() %>!</h1>
        <p>Fecha de Inscripción: <%= jugador.getFechaInscripcion() %></p>
        <p>Correo: <%= jugador.getCorreoElectronico() %></p>
    </div>
</header>

<nav class="navbar">
    <div class="container">
        <ul>
            <li><a href="consultarReservas.jsp">Consultar Reservas</a></li>
            <li><a href="buscarPista.jsp">Buscar Pista</a></li>
            <li><a href="<%= request.getContextPath() %>/usermenu/reservar">Realizar Reserva</a></li>
            <li><a href="<%= request.getContextPath() %>/usermenu/bonos">Gestionar Bonos</a></li>
            <li><a href="modificarReserva.jsp">Modificar/Cancelar Reserva</a></li>
            <li><a href="modificarDatos.jsp">Modificar Datos</a></li>
            <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-danger">Cerrar sesión</a></li>
        </ul>
    </div>
</nav>

<main class="main-content">
    <div class="container">
        <h2>Información General</h2>
        <p>Aquí puedes gestionar tus reservas, modificar tus datos personales y disfrutar de nuestras pistas deportivas.</p>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>Aplicación de Gestión Deportiva - Práctica de Programación Web</p>
    </div>
</footer>

</body>
</html>
