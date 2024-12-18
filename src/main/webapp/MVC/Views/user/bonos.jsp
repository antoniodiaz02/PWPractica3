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
    <title>Gestión Bonos</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/bonos.css">
</head>
<body>

<header class="header">
    <div class="container">
        <h1>Gestión de bonos</h1>
        <p>Visualización de bonos de usuario, solicitud de nuevos bonos y realizar nuevas reservas con un bono solicitado</p>
    </div>
</header>

<nav class="navbar">
    <div class="container">
        <ul>
            <li><a href="<%= request.getContextPath() %>/MVC/Views/user/usermenu.jsp">Volver al menú principal</a></li>
            <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-danger">Cerrar sesión</a></li>
        </ul>
    </div>
</nav>

<main class="main-content">
    <div class="container">
        <h2>Tus bonos</h2>
        <p>Aquí puedes visualizar los bonos que has solicitado previamente:</p>
    </div>
    
    <div class="container">
        <h2>Solicitar un nuevo bono</h2>
        <p>Para realizar reservas a un precio rebajado puedes solicitar un bono.
           Este bono aplicará una rebaja del 5% a cada reserva que realices con el bono.</p>
        <!-- Botón para Solicitar un Bono -->
        <a href="<%= request.getContextPath() %>/usermenu/bonos/nuevobono">Solicitar Bono</a>
    </div>
    
    <div class="container">
        <h2>Realizar reserva con un bono</h2>
        <p>En esta opción podrás realizar reservas con algún bono que hayas solicitado.</p>
        <!-- Formulario para Reservar con el Bono -->
        <a href="<%= request.getContextPath() %>/usermenu/bonos/reservar">Realizar Reserva</a>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>Aplicación de Gestión Deportiva - Práctica de Programación Web</p>
    </div>
</footer>

</body>
</html>