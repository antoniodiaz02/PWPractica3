<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error en la Actualización de Material</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>
<body>
    <div class="container">
        <div class="message-container">
            <h1>Error en la Actualización de Material</h1>
            <% 
                String error = request.getParameter("error");
                if (error != null) {
                    String errorMessage = "";
                    if ("campos-obligatorios".equals(error)) {
                        errorMessage = "Por favor, complete todos los campos obligatorios.";
                    } else if ("id-invalido".equals(error)) {
                        errorMessage = "El ID del material no es válido.";
                    } else if ("material-null".equals(error)) {
                        errorMessage = "El material proporcionado es nulo.";
                    } else if ("tipo-material-invalido".equals(error)) {
                        errorMessage = "El tipo de material especificado no es válido.";
                    } else if ("estado-material-invalido".equals(error)) {
                        errorMessage = "El estado del material especificado no es válido.";
                    } else if ("no-existe".equals(error)) {
                        errorMessage = "El material con el ID proporcionado no existe en el sistema.";
                    } else if ("actualizar-fallido".equals(error)) {
                        errorMessage = "No se pudo actualizar el material. Intente nuevamente.";
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
                <p>Hubo un problema al intentar actualizar el material. Por favor, inténtalo nuevamente.</p>
            <% 
                }
            %>
            <a href="<%= request.getContextPath() %>/adminmenu/updateMaterial" class="button">Volver a la Actualización</a>
        </div>
    </div>
</body>
</html>