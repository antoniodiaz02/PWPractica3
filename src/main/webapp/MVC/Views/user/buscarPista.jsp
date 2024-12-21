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
    <title>Buscar pista</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/buscar.css">
</head>
<body>
    <header class="header">
	    <div class="container">
	        <h1>Buscar pistas disponibles</h1>
	        <p>Aquí podrás visualizar las pistas disponibles según el tipo de pista y la fecha</p>
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
	        <h2>Buscar</h2>
	        
	        <form action="<%= request.getContextPath() %>/usermenu/buscarpista" method="POST">
	        	
	        		 <div class="form-group">
	                    <label for="interior">Tipo de pista:</label>
	                    <select id="interior" name="interior" required>
	                        <option value="true">Interior</option>
	                        <option value="false">Exterior</option>
	                    </select>
	                </div>
	                
	                <div class="form-group">
	                    <label for="fechaBuscar">Fecha a buscar:</label>
	                    <input type="datetime-local" id="fechaBuscar" name="fechaBuscar" required>
	                </div>   
	                
	                <div class="form-group">
	                    <input type="submit" value="Buscar Pistas">
	                </div>    
	        </form>
	        
	         <% 
                String error = (String) request.getAttribute("error");
                if (error != null) {
            %>
                <p class="error"><%= error %></p>
            <% 
                }
            %>
	    </div>
        <hr>
        <div class="main-content">
	        <div class="container">
	            <table class="tabla-reservas">
				    <thead>
				        <tr>
				            <th>Nombre</th>
	                        <th>Tipo</th>
	                        <th>Tamaño</th>
	                        <th>Número Máximo de Jugadores</th>
				        </tr>
				    </thead>
                <tbody>
                    <% 
                        Vector<PistaDTO> pistas = (Vector<PistaDTO>) request.getAttribute("pistas");
                        if (pistas != null && !pistas.isEmpty()) {
                            for (PistaDTO pista : pistas) {
                    %>
                    <tr>
                        <td><%= pista.getNombre() %></td>
                        <td><%= pista.isInterior() ? "Interior" : "Exterior" %></td>
                        <td><%= pista.getTamanoPista().toString() %></td>
                        <td><%= pista.getMaxJugadores() %></td>
                    </tr>
                    <% 
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="4">No hay pistas disponibles.</td>
                    </tr>
                    <% 
                        } 
                    %>
                </tbody>
            </table>
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
