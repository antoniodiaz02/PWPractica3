<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error en el Registro</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
    <link rel="icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/x-icon">
</head>
<body>
    <div class="container">
        <div class="message-container">
            <h1>Error en el Registro</h1>
            <% 
                String error = request.getParameter("error");
                if (error != null) {
                    String errorMessage = "";
                    if ("campos-obligatorios".equals(error)) {
                        errorMessage = "Todos los campos son obligatorios.";
                    } else if ("max-jugadores".equals(error)) {
                        errorMessage = "El número máximo de jugadores debe ser mayor a 0.";
                    } else if ("tamano-pista".equals(error)) {
                        errorMessage = "El tamaño de la pista no es válido.";
                    } else if ("nombre-invalido".equals(error)) {
                        errorMessage = "El nombre de la pista no es válido.";
                    } else if ("nombre-existente".equals(error)) {
                        errorMessage = "Ya existe una pista con este nombre.";
                    } else if ("insertar-fallido".equals(error)) {
                        errorMessage = "No se pudo registrar la pista. Intente nuevamente.";
                    } else if ("sql-excepcion".equals(error)) {
                        errorMessage = "Ocurrió un error con la base de datos. Consulte al administrador.";
                    } else {
                        errorMessage = "Ocurrió un error desconocido.";
                    }
            %>
                <p><%= errorMessage %></p>
            <% 
                } else {
            %>
                <p>Hubo un problema al intentar registrar la pista. Por favor, inténtalo nuevamente.</p>
            <% 
                }
            %>
            <a href="<%= request.getContextPath() %>/adminmenu/registerPista" class="button">Volver al Registro</a>
        </div>
    </div>
</body>
</html>
