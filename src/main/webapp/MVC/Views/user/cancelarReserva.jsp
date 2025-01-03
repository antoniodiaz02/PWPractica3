<%@ page import="es.uco.pw.business.DTOs.ReservaDTO" %>
<%@ page import="es.uco.pw.business.DTOs.PistaDTO" %>
<%@ page import="es.uco.pw.business.DTOs.ReservaInfantilDTO" %>
<%@ page import="es.uco.pw.business.DTOs.ReservaFamiliarDTO" %>
<%@ page import="es.uco.pw.business.DTOs.ReservaAdultosDTO" %>
<%@ page import="es.uco.pw.business.Gestores.GestorReservas" %>
<%@ page import="java.util.Vector" %>
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
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cancelar Reserva</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/buscar.css">
    <link rel="icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/x-icon">
</head>
<body>
    <header class="header">
	    <div class="container">
	        <h1>Modificación de reserva</h1>
	        <p>Se incluye la búsqueda de una reserva por fecha y pista para su posterior modificación</p>
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
        <h2>Buscar Reserva</h2>
        
        <form action="<%= request.getContextPath() %>/usermenu/gestionreservas/cancelar" method="POST">
            <input type="hidden" id="correoUser" name="correoUser" value="<%= jugador.getCorreoElectronico() %>">
       		<input type="hidden" name="action" value="buscar">
            
            <div class="form-group">
                <label for="nombrePista">Nombre de la pista</label>
                <input type="text" id="nombrePista" name="nombrePista" required>
            </div>
            
            <div class="form-group">
                <label for="fechaBuscar">Fecha de la reserva</label>
                <input type="datetime-local" id="fechaBuscar" name="fechaBuscar" required>
            </div>   
            
            <div class="form-group">
                <input type="submit" value="Buscar Reserva">
            </div>    
        </form>
        
        <% 
            String error = (String) request.getAttribute("error-buscar");
            if (error != null) {
        %>
            <p class="error"><%= error %></p>
        <% 
            }
        %>
    </div>
    <% if ("POST".equalsIgnoreCase(request.getMethod()) && request.getAttribute("error-buscar") == null) { %>
    <hr>
    <div class="main-content">
        <div class="container">
            <table class="tabla-reservas">
				    <thead>
				        <tr>
				            <th>Pista</th>
				            <th>Tipo de reserva</th>
				            <th>Día y hora</th>
				            <th>Duración</th>
				            <th>Precio</th>
				            <th>Descuento</th>
				            <th>Nº de adultos</th>
				            <th>Nº de niños</th>
				        </tr>
				    </thead>
				    <tbody>
				        <% 
				            Vector<ReservaDTO> reservas = (Vector<ReservaDTO>) request.getAttribute("reservas");
				            GestorReservas gestor = new GestorReservas();
				            if (reservas != null && !reservas.isEmpty()) {
				                for (ReservaDTO reserva : reservas) {
				                    String tipo = ""; // Declarar la variable 'tipo' para cada reserva
				                    
				                    // Declarar variables null por fuera
				                    ReservaInfantilDTO infantil = null;
				                    ReservaFamiliarDTO familiar = null;
				                    ReservaAdultosDTO adultos = null;
				
				                    // Realizar los castings según el tipo de reserva
				                    if (reserva instanceof ReservaInfantilDTO) {
				                        tipo = "Infantil";
				                        infantil = (ReservaInfantilDTO) reserva;
				                    } else if (reserva instanceof ReservaFamiliarDTO) {
				                        tipo = "Familiar";
				                        familiar = (ReservaFamiliarDTO) reserva;
				                    } else {
				                        tipo = "Adultos";
				                        adultos = (ReservaAdultosDTO) reserva;
				                    }
				        %>
				        
				        <tr>
				            <td><%= request.getAttribute("nombre") %></td>
				            <td><%= tipo %></td>
				            <td><%= reserva.getFechaHora() %></td>
				            <td><%= reserva.getDuracion() %></td>
				            <td><%= reserva.getPrecio() %></td>
				            <td><%= reserva.getDescuento() %></td>
				
				            <!-- Condicional para imprimir numAdultos según el tipo -->
				            <td>
				                <% if ("Adultos".equals(tipo) || "Familiar".equals(tipo)) { %>
				                    <%= (adultos != null) ? adultos.getNumAdultos() : familiar.getNumAdultos() %>
				                <% } %>
				            </td>
				
				            <!-- Condicional para imprimir numNinos según el tipo -->
				            <td>
				                <% if ("Infantil".equals(tipo) || "Familiar".equals(tipo)) { %>
				                    <%= (infantil != null) ? infantil.getNumNinos() : familiar.getNumNinos() %>
				                <% } %>
				            </td>
				        </tr>
				        <% 
				                }
				            } else {
				        %>
				        <tr>
				            <td colspan="8">No tienes reservas según los datos especificados.</td>
				        </tr>
				        <% 
				            } 
				        %>
				    </tbody>
			</table>
        </div>
    <div class="container">
        <h2>Cancelar Reserva</h2>
        	<p>¿Estas seguro de cancelar la reserva? Si pulsas el siguiente botón la reserva se eliminará</p>
        	<form action="<%= request.getContextPath() %>/usermenu/gestionreservas/cancelar" method="POST">
        		<input type="hidden" name="action" value="cancelar">
	        	<input type="hidden" id="idReservaAntigua" name="idReservaAntigua" value="<%= session.getAttribute("idReserva") != null ? session.getAttribute("idReserva").toString() : "" %>">
        	
	        	<div class="form-group">
	                <input type="submit" value="Cancelar Reserva">
	            </div>    
	        </form>
	        <% 
            String errorCan = (String) request.getAttribute("error-cancelar");
            if (errorCan != null) {
	        %>
	            <p class="error"><%= errorCan %></p>
	        <% 
	            }
	        %>
        <% } %>
        
       </div> 
    </div>
        </main>
    
    <div class="footer">
        <div class="container">
            <p>Aplicación de Gestión Deportiva - Práctica de Programación Web</p>
        </div>
    </div>
</body>
</html>