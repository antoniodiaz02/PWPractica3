<%
    es.uco.pw.business.DTOs.JugadorDTO admin = (es.uco.pw.business.DTOs.JugadorDTO) session.getAttribute("jugador");

    if (admin == null) {
        response.sendRedirect("../../../index.jsp"); // Redireccionar al login si no hay usuario en sesión
        return; // Detener la ejecución de la página
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modificar Usuario</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/modificarUsuario.css">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h1>Modificar Usuario</h1>

            <!-- Mensaje de ayuda -->
            <p class="info-message">
                Puedes modificar el campo que desees cambiar.
            </p>
            <p class="info-message-asterisk">
                (*) Campo obligatorio.
            </p>

            <!-- Mostrar mensaje de éxito si existe -->
            <% 
                String mensaje = (String) session.getAttribute("mensaje");
                if (mensaje != null) {
                    session.removeAttribute("mensaje"); // Eliminar el mensaje de la sesión después de mostrarlo
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

            <form action="<%= request.getContextPath() %>/modificarUsuario" method="POST">
                <!-- Nombre Completo -->
                <div class="form-group">
                    <label for="nombreCompleto">Nombre Completo:</label>
                    <input type="text" id="nombreCompleto" name="nombreCompleto">
                </div>

                <!-- Fecha de Nacimiento -->
                <div class="form-group">
                    <label for="fechaNacimiento">Fecha de Nacimiento:</label>
                    <input type="date" id="fechaNacimiento" name="fechaNacimiento">
                </div>

                <!-- Nueva Contraseña -->
                <div class="form-group">
                    <label for="nuevaContrasena">Nueva Contraseña:</label>
                    <input type="password" id="nuevaContrasena" name="nuevaContrasena">
                </div>

                <!-- Correo Electrónico (identificador) -->
                <div class="form-group">
                    <label for="correo">Correo Electrónico: <strong>*</strong></label>
                    <% 
                        if ("administrador".equals(admin.getTipoUsuario())) {
                    %>
                        <!-- El administrador puede modificar el correo -->
                        <input type="email" id="correo" name="correo" required>
                    <% 
                        } else {
                    %>
                        <!-- El cliente no puede modificar su correo -->
                        <input type="email" id="correo" name="correo" value="<%= admin.getCorreoElectronico() %>" readonly>
                    <% 
                        }
                    %>
                </div>

                <!-- Botón de envío -->
                <div class="form-group">
                    <input type="submit" value="Modificar">
                </div>
            </form>

            <!-- Botón para volver al panel admin -->
            <a href="<%= "administrador".equals(admin.getTipoUsuario()) ? request.getContextPath() + "/adminmenu" : request.getContextPath() + "/usermenu" %>" class="back-button">Volver al Panel</a>
        </div>
    </div>
</body>
</html>
