<%@ page import="es.uco.pw.business.DTOs.ReservaDTO" %>
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
    <title>Consultar reservas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/consultas.css">
    <link rel="icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/x-icon">
</head>
<body>
    <header class="header">
	    <div class="container">
	        <h1>Constultar mis reservas</h1>
	        <p>Aquí podrás visualizar las reservas que has solicitado previamente</p>
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
	        <h2>Buscar</h2>
	        
	        <form action="<%= request.getContextPath() %>/usermenu/consultareservas" method="POST">
	        	<input type="hidden" id="correoUser" name="correoUser" value="<%= jugador.getCorreoElectronico() %>">
	        		 <div class="form-group">
	                    <label for="fechaInicio">Fecha de filtrado inicial</label>
	                    <input type="datetime-local" id="fechaInicio" name="fechaInicio" required>
	                </div>
	                
	                <div class="form-group">
	                    <label for="fechaFinal">Fecha de filtrado final</label>
	                    <input type="datetime-local" id="fechaFinal" name="fechaFinal" required>
	                </div>   
	                
	                <div class="form-group">
	                    <input type="submit" value="Buscar Reservas">
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
	    <% if ("POST".equalsIgnoreCase(request.getMethod())) { %>
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
		                	GestorReservas gestor= new GestorReservas();
		                    Vector<ReservaDTO> reservas = (Vector<ReservaDTO>) request.getAttribute("reservas");
		                    Vector<String> nombres = (Vector<String>) request.getAttribute("nombres");  // Asegúrate de que 'nombres' esté disponible
		                    if (reservas != null && !reservas.isEmpty()) {
		                        for (int i = 0; i < reservas.size(); i++) {  // Usamos un índice para acceder a los elementos
		                            ReservaDTO reserva = reservas.get(i);
		                            String tipo = "";
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
		                            
		                            String claseFila = gestor.esReservaFutura(reserva.getFechaHora()) ? "reserva-futura" : "reserva-pasada";
		                %>
		                <tr class="<%= claseFila %>">
		                    <!-- Imprimir el nombre de la pista desde el vector 'nombres' usando el índice i -->
		                    <td><%= nombres.get(i) %></td>  <!-- El nombre de la pista es el que está en el índice 'i' del vector nombres -->
		                    <td><%= tipo %></td>
		                    <td><%= reserva.getFechaHora() %></td>
		                    <td><%= reserva.getDuracion() %></td>
		                    <td><%= reserva.getPrecio() %></td>
		                    <td><%= reserva.getDescuento() %></td>
		
		                    <!-- Condicional para imprimir numAdultos según el tipo -->
		                    <td>
		                        <% 
		                            if ("Adultos".equals(tipo) || "Familiar".equals(tipo)) {
		                                int numAdultos = (adultos != null) ? adultos.getNumAdultos() : (familiar != null ? familiar.getNumAdultos() : 0);
		                                out.print(numAdultos); // Imprimir el número de adultos
		                            }
		                        %>
		                    </td>
		
		                    <!-- Condicional para imprimir numNinos según el tipo -->
		                    <td>
		                        <% 
		                            if ("Infantil".equals(tipo) || "Familiar".equals(tipo)) {
		                                int numNinos = (infantil != null) ? infantil.getNumNinos() : (familiar != null ? familiar.getNumNinos() : 0);
		                                out.print(numNinos); // Imprimir el número de niños
		                            }
		      
		                        %>
		                    </td>
		                </tr>
		                <% 
		                        }
		                    } else {
		                %>
		                <tr>
		                    <td colspan="8">No tienes reservas entre las fechas especificadas</td>
		                </tr>
		                <% 
		                    } 
		                %>
		            </tbody>
		        </table>
		    </div>
		    <div class="container">
			    <p>*(Las reservas que se muestran en rojo son aquellas que ya se han celebrado, y las que se muestran en verde son las que
			       faltan por celebrarse).</p>
			</div>
		</div>
	    <% } %>
    </main>
    
    <div class="footer">
        <div class="container">
            <p>Aplicación de Gestión Deportiva - Práctica de Programación Web</p>
        </div>
    </div>
</body>
</html>
