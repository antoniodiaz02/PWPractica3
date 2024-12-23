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
    <title>Página Principal - Usuario</title>
    <link rel="icon" href="images/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon">
    
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
            <li><a href="<%= request.getContextPath() %>/usermenu/consultareservas">Consultar Reservas</a></li>
            <li><a href="<%= request.getContextPath() %>/usermenu/buscarpista">Buscar Pista</a></li>
            <li><a href="<%= request.getContextPath() %>/usermenu/reservar">Realizar Reserva</a></li>
            <li><a href="<%= request.getContextPath() %>/usermenu/bonos">Gestionar Bonos</a></li>
            <li><a href="<%= request.getContextPath() %>/usermenu/gestionreservas">Modificar/Cancelar Reserva</a></li>
            <li><a href="<%= request.getContextPath() %>/modificarUsuario" class="btn btn-danger">Modificar Datos</a></li>
            <li><a href="<%= request.getContextPath() %>/logout" class="btn btn-danger">Cerrar sesión</a></li>
        </ul>
    </div>
</nav>

<main class="main-content">
    <div class="container">
        <h2>Información General</h2>
        <p>
            Bienvenido a nuestra plataforma de gestión de reservas para pistas de baloncesto. Aquí puedes realizar reservas de nuestras pistas deportivas, gestionar tus datos personales, y aprovechar las diversas ventajas que ofrecemos a nuestros usuarios. Disfruta de una experiencia completa con la mejor infraestructura deportiva disponible para todos los niveles de juego.
        </p>
        
        <h3>Nuestras Pistas</h3>
        <p>
            Contamos con una variedad de pistas que se ajustan a todas las necesidades: desde pistas especializadas para <strong>mini-basket</strong>, ideales para jugadores jóvenes y principiantes, hasta pistas para juegos de <strong>tres contra tres</strong> o tamaño completo para <strong>jugadores adultos</strong>. Cada pista está diseñada para ofrecerte la mejor experiencia de juego, tanto si prefieres entrenamientos intensos como encuentros casuales con amigos.
        </p>
        
        <div class="gallery">
            <h4>Galería de Nuestras Pistas</h4>
            <div class="row">
                <div class="column">
                    <img src="<%= request.getContextPath() %>/images/pista1.jpg" alt="Pista de Minibasket" style="width:100%">
                    <p>Pista de Minibasket</p>
                </div>
                <div class="column">
                    <img src="<%= request.getContextPath() %>/images/pista2.jpg" alt="Pista Adultos" style="width:100%">
                    <p>Pista para Adultos</p>
                </div>
                <div class="column">
                    <img src="<%= request.getContextPath() %>/images/pista3.jpg" alt="Pista 3 vs 3" style="width:100%">
                    <p>Pista de 3 contra 3</p>
                </div>
            </div>
        </div>

        <p>
            ¿Buscas ahorrar en tus reservas? Te ofrecemos la posibilidad de adquirir <strong>bonos</strong> con los cuales puedes obtener un <strong>descuento del 5%</strong> en tus próximas reservas. ¡Es una excelente opción para quienes disfrutan de un juego regular y quieren maximizar su tiempo en la cancha!
        </p>
        
        <p>
            Además, si decides unirte a nuestra comunidad de jugadores con una <strong>inscripción anual</strong>, tendrás acceso a <strong>descuentos adicionales y exclusivos</strong>. A lo largo del año, disfrutarás de promociones especiales que te permitirán obtener mayores ahorros y ventajas personalizadas, haciendo de tu experiencia deportiva algo aún más gratificante.
        </p>
        
        <p>
            Explora nuestras distintas categorías de pistas, y asegúrate de reservar con tiempo para disfrutar de nuestras instalaciones al máximo. Cada una de nuestras pistas está diseñada para garantizar que tu experiencia sea óptima y llena de emoción. ¡No te pierdas la oportunidad de ser parte de nuestra comunidad deportiva!
        </p>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>Aplicación de Gestión Deportiva - Práctica de Programación Web</p>
    </div>
</footer>

</body>
</html>
