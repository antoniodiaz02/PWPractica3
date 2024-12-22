<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error en la Actualización</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>
<body>
    <div class="container">
        <div class="message-container">
            <h1>Error en la Actualización</h1>
            <% 
                String error = request.getParameter("error");
                if (error != null) {
                    String errorMessage = "";
                    if ("campos-obligatorios".equals(error)) {
                        errorMessage = "Por favor, complete todos los campos obligatorios.";
                    } else if ("max-jugadores".equals(error)) {
                        errorMessage = "El número máximo de jugadores no es válido.";
                    } else if ("nombre-invalido".equals(error)) {
                        errorMessage = "El nombre de la pista no es válido.";
                    } else if ("tamano-invalido".equals(error)) {
                        errorMessage = "El tamaño de la pista especificado no es válido.";
                    } else if ("no-existe".equals(error)) {
                        errorMessage = "La pista no existe en el sistema.";
                    } else if ("actualizar-fallido".equals(error)) {
                        errorMessage = "No se pudo actualizar la pista. Intente nuevamente.";
                    } else if ("sql-excepcion".equals(error)) {
                        errorMessage = "Ocurrió un error con la base de datos. Consulte al administrador.";
                    } else if ("valor-invalido".equals(error)) {
                        errorMessage = "Uno o más valores proporcionados son inválidos.";
                    } else {
                        errorMessage = "Ocurrió un error desconocido.";
                    }
            %>
                <p><%= errorMessage %></p>
            <% 
                } else {
            %>
                <p>Hubo un problema al intentar actualizar la pista. Por favor, inténtalo nuevamente.</p>
            <% 
                }
            %>
            <a href="<%= request.getContextPath() %>/adminmenu/updatePista" class="button">Volver a la Actualización</a>
        </div>
    </div>
</body>
</html>