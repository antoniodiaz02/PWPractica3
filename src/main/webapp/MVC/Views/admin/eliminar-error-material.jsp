<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error en la Eliminación</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>
<body>
    <div class="container">
        <div class="message-container">
            <h1>Error en la Eliminación</h1>
            <% 
                String error = request.getParameter("error");
                if (error != null) {
                    String errorMessage = "";
                    if ("id-material-invalido".equals(error)) {
                        errorMessage = "El ID del material no es válido.";
                    } else if ("material-no-existe".equals(error)) {
                        errorMessage = "No existe un material con este ID.";
                    } else if ("eliminar-fallido".equals(error)) {
                        errorMessage = "No se pudo eliminar el material. Intente nuevamente.";
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
                <p>Hubo un problema al intentar eliminar el material. Por favor, inténtalo nuevamente.</p>
            <% 
                }
            %>
            <a href="<%= request.getContextPath() %>/adminmenu/eliminarMateriales" class="button">Volver a la Eliminación</a>
        </div>
    </div>
</body>
</html>
