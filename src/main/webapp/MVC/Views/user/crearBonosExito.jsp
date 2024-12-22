<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    es.uco.pw.business.DTOs.JugadorDTO jugador = (es.uco.pw.business.DTOs.JugadorDTO) session.getAttribute("jugador");

    if (jugador == null) {
        response.sendRedirect(request.getContextPath() + "/"); // Redireccionar al login si no hay usuario en sesión
        return; // Detener la ejecución de la página
    }
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Nuevo bono exito</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>

<body>
    <div class="container">
    	<div class="form-container">
    		<h1>Solicitar Nuevo Bono</h1>
    		
    		<!-- Mensaje de éxito: "Reserva realizada con éxito" -->
            <p><strong>¡Bono creado correctamente!</strong></p>
    
            
	        <form action="<%= request.getContextPath() %>/usermenu/bonos/nuevobono/exito" method="POST">>
    
                <!-- Botón de regreso al menú -->
                <div class="form-group">
                    <a href="<%= request.getContextPath() %>/usermenu" class="fw-bold">Volver al menú principal</a>
                </div>
            </form>
	           
		</div>
	</div>
</body>

</html>