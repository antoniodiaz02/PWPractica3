<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    es.uco.pw.business.DTOs.JugadorDTO jugador = (es.uco.pw.business.DTOs.JugadorDTO) session.getAttribute("jugador");

    if (jugador == null) {
        response.sendRedirect(request.getContextPath() + "/"); // Redireccionar al login si no hay usuario en sesión
        return; // Detener la ejecución de la página
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión Reservas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/bonos.css">
    <link rel="icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/x-icon">
</head>
<body>

<header class="header">
    <div class="container">
        <h1>Gestión de Reservas</h1>
        <p>Modificación y cancelación de tus reservas.</p>
    </div>
</header>

<nav class="navbar">
    <div class="container">
        <ul>
            <li><a href="<%= request.getContextPath() %>/usermenu">Volver al menú principal</a></li>
            <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-danger">Cerrar sesión</a></li>
        </ul>
    </div>
</nav>

<main class="main-content">
    
    <div class="container">
        <h2>Modificar una reserva</h2>
        <p>En esta opción podrás editar varios detalles de una reserva que hayas realizado previamente.
           Las modificaciones no se podrán realizar 24 horas antes de la reserva.
           Podrás editar el número de participantes, el tipo de pista, la duración de la reserva y la fecha de la reserva.</p>
        <!-- Enlace para Modificar una Reserva -->
        <a href="<%= request.getContextPath() %>/usermenu/gestionreservas/modificar">Modificar una Reserva</a>
    </div>
    
    <div class="container">
        <h2>Cancelar una reserva</h2>
        <p>Cualquier reserva que hayas solicitado la puedes cancelar desde esta opción.
           No se podrá cancelar una reserva 24 horas antes de la fecha de la reserva.</p>
        <!-- Enlace para Cancelar una Reserva -->
        <a href="<%= request.getContextPath() %>/usermenu/gestionreservas/cancelar">Realizar Reserva</a>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>Aplicación de Gestión Deportiva - Práctica de Programación Web</p>
    </div>
</footer>

</body>
</html>
