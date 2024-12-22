<%
    es.uco.pw.business.DTOs.JugadorDTO admin = (es.uco.pw.business.DTOs.JugadorDTO) session.getAttribute("jugador");

    if (admin == null) {
        response.sendRedirect("../../../index.jsp"); // Redirigir al login si no hay usuario en sesión
        return; // Detener la ejecución de la página
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Eliminar Reserva</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
    <link rel="icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/x-icon">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h1>Eliminar Reserva</h1>

            <!-- Mensaje de ayuda -->
            <p class="info-message">
                Puedes eliminar una reserva proporcionando el ID correspondiente.
            </p>

            <!-- Mostrar mensaje de éxito si existe -->
            <% 
                String mensaje = (String) request.getAttribute("mensaje");
                if (mensaje != null) {
            %>
                <div class="success"><%= mensaje %></div>
            <% 
                }
            %>

            <!-- Mostrar mensaje de error si existe -->
            <% 
                String error = (String) request.getAttribute("error");
                if (error != null) {
            %>
                <p class="error"><%= error %></p>
            <% 
                }
            %>

            <form action="<%= request.getContextPath() %>/adminmenu/eliminarReservas" method="POST">
                <!-- ID de la Reserva -->
                <div class="form-group">
                    <label for="idReserva">ID de la Reserva: <strong>*</strong></label>
                    <input type="number" id="idReserva" name="idReserva" min="1" required placeholder="Introduce el ID de la reserva a eliminar">
                </div>

                <!-- Botón de envío -->
                <div class="form-group">
                    <input type="submit" value="Eliminar">
                </div>
            </form>

            <!-- Botón para volver al panel admin -->
            <a href="<%= request.getContextPath() %>/adminmenu" class="back-button">Volver al Panel de Admin</a>
        </div>
    </div>
</body>
</html>
