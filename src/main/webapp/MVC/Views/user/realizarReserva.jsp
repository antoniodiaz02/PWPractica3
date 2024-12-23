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
	<title>Realizar nueva reserva</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
	<link rel="icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/x-icon">
</head>

<body>
    <div class="container">
    	<div class="form-container">
    		<h1>Realizar una nueva reserva</h1>
    	
       		<!-- Mostrar mensaje de error si existe -->
            <% 
                String error = (String) request.getAttribute("error");
                if (error != null) {
            %>
                <p class="error"><%= error %></p>
            <% 
                }
            %>
            
	        <form action="<%= request.getContextPath() %>/usermenu/reservar" method="POST">
	        
	        	<input type="hidden" id="correoUser" name="correoUser" value="<%= jugador.getCorreoElectronico() %>">
	                
	                <!-- Nombre de la pista -->
	                <div class="form-group">
	                    <label for="nombrePista">Nombre de la pista</label>
	                    <input type="text" id="nombrePista" name="nombrePista" required>
	                </div>
	
	                <!-- Duracion -->
	                <div class="form-group">
	                    <label for="duracion">Duración</label>
	                    <select id="duracion" name="duracion" required>
	                        <option value="60">60 mins.</option>
	                        <option value="90">90 mins.</option>
	                        <option value="120">120 mins.</option>
	                    </select>
	                </div>
	
	
	                <!-- Fecha de Reserva -->
	                <div class="form-group">
	                    <label for="fechaHora">Fecha de Reserva</label>
	                    <input type="datetime-local" id="fechaHora" name="fechaHora" required>
	                </div>
	
	                <!-- Tipo de reserva -->
	                <div class="form-group">
	                    <label for="tipoReserva">Tipo de reserva</label>
	                    <select id="tipoReserva" name="tipoReserva" required onchange="actualizarFormulario(); recargarPagina()">

	                        <option value="INFANTIL">Infantil</option>
	                        <option value="FAMILIAR">Familiar</option>
	                        <option value="ADULTOS">Adultos</option>
	                    </select>
	                </div>
	                
	                <!-- Número de participantes (inicialmente solo un campo) -->
				    <div id="numeroParticipantesContainer">
				        <div class="form-group" id="participantes">
				            <label for="numParticipantes">Número de participantes</label>
				            <input type="number" id="numParticipantes" name="numParticipantes" min="0">
				        </div>
				    </div>
				
				    <!-- Número de niños y adultos (estos campos se agregan si se selecciona "Familiar") -->
				    <div id="numeroNiñosAdultos" style="display: none;">
					    <div class="form-group">
					        <label for="numNinos">Número de niños</label>
					        <input type="number" id="numNinos" name="numNinos" min="0">
					    </div>
					    <div class="form-group">
					        <label for="numAdultos">Número de adultos</label>
					        <input type="number" id="numAdultos" name="numAdultos" min="0">
					    </div>
					</div>
    
	                <!-- Botón de envío -->
	                <div class="form-group">
	                    <input type="submit" value="Realizar Reserva">
	                </div>
	            </form>
	            
	            <script>
		         	// Función para actualizar el formulario dependiendo de la opción seleccionada
	                function actualizarFormulario() {
					    var tipoReserva = document.getElementById("tipoReserva").value;
					    var numeroParticipantesContainer = document.getElementById("numeroParticipantesContainer");
					    var numeroNiñosAdultos = document.getElementById("numeroNiñosAdultos");
					    var numNiños = document.getElementById("numNiños");
					    var numAdultos = document.getElementById("numAdultos");
					    var numParticipantes = document.getElementById("numParticipantes");
					    
					    // Si se selecciona "FAMILIAR", mostrar los campos para número de niños y adultos
					    if (tipoReserva === "FAMILIAR") {
					        numeroParticipantesContainer.style.display = "none"; // Ocultar campo único de participantes
					        numeroNiñosAdultos.style.display = "block"; // Mostrar los campos para niños y adultos
					        numNiños.setAttribute('required', 'required');
					        numAdultos.setAttribute('required', 'required');
					        numParticipantes.removeAttribute('required');
					    } else {
					        numeroParticipantesContainer.style.display = "block"; // Mostrar campo único de participantes
					        numeroNiñosAdultos.style.display = "none"; // Ocultar los campos de niños y adultos
					        numNiños.removeAttribute('required');
					        numAdultos.removeAttribute('required');
					        numParticipantes.setAttribute('required', 'required');
					        // Establecer los valores de niños y adultos a 0 cuando no es "FAMILIAR"
					        numNiños.value = 0;
					        numAdultos.value = 0;
					    }
					}
	
	                // Llamar a la función para actualizar el formulario cuando la página se cargue
	                window.onload = function() {
	                    actualizarFormulario(); // Actualizar la vista del formulario dependiendo de la selección inicial
	                };
                </script>
		</div>
	</div>
</body>

</html>