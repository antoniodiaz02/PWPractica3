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
    <title>Adquirir Bono</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/usermenu.css">
</head>
<body>

<header class="header">
    <div class="container">
        <h1>Adquirir nuevos Bonos y realizar reservas con Bonos</h1>
    </div>
</header>

<nav class="navbar">
    <div class="container">
        <ul>
            <li><a href="usermenu.jsp">Volver al menú principal</a></li>
            <li><a href="logout.jsp">Desconectar</a></li>
        </ul>
    </div>
</nav>

<main class="main-content">
    <div class="container">
        <h2>Adquirir nuevo bono</h2>
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