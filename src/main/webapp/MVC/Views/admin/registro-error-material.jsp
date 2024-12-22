<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error en el Registro de Material</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
    <link rel="icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/x-icon">
</head>
<body>
    <div class="container">
        <div class="message-container">
            <h1>Error en el Registro de Material</h1>
            <% 
                String error = request.getParameter("error");
                if (error != null) {
                    String errorMessage = "";
                    if ("campos-obligatorios".equals(error)) {
                        errorMessage = "Todos los campos son obligatorios.";
                    } else if ("id-material".equals(error)) {
                        errorMessage = "El ID del material debe ser un número positivo.";
                    } else if ("tipo-material".equals(error)) {
                        errorMessage = "El tipo de material no es válido.";
                    } else if ("estado-material".equals(error)) {
                        errorMessage = "El estado del material no es válido.";
                    } else if ("id-existente".equals(error)) {
                        errorMessage = "Ya existe un material con este ID.";
                    } else if ("insertar-fallido".equals(error)) {
                        errorMessage = "No se pudo registrar el material. Intente nuevamente.";
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
                <p>Hubo un problema al intentar registrar el material. Por favor, inténtalo nuevamente.</p>
            <% 
                }
            %>
            <a href="<%= request.getContextPath() %>/adminmenu/registerMaterial" class="button">Volver al Registro</a>
        </div>
    </div>
</body>
</html>