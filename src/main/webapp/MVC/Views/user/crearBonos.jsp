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
    <title>Solicitar Bono</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>

<body>
    <div class="container">
    	<div class="form-container">
    		<h1>Solicitar Nuevo Bono</h1>
    	
       		<!-- Mostrar mensaje de error si existe -->
            <% 
                String error = (String) request.getAttribute("error");
                if (error != null) {
            %>
                <p class="error"><%= error %></p>
            <% 
                }
            %>
            
	        <form action="<%= request.getContextPath() %>/usermenu/bonos/nuevobono" method="POST">
	        
	        	<input type="hidden" id="correoUser" name="correoUser" value="<%= jugador.getCorreoElectronico() %>">
	
	                <!-- Tipo de bono -->
	                <div class="form-group">
	                    <label for="tipoBono">Tipo de reserva</label>
	                    <select id="tipoBono" name="tipoBono" required>
	                        <option value="MINIBASKET">Minibasket</option>
	                        <option value="ADULTOS">Adultos</option>
	                        <option value="TRES_VS_TRES">Tres vs Tres</option>
	                    </select>
	                </div>
    
	                <!-- Botón de envío -->
	                <div class="form-group">
	                    <input type="submit" value="Solicitar Nuevo Bono">
	                </div>
	            </form>
		</div>
	</div>
</body>
</html>