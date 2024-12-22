<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error en la Eliminaci�n</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>
<body>
    <div class="container">
        <div class="message-container">
            <h1>Error en la Eliminaci�n</h1>
            <% 
                String error = request.getParameter("error");
                if (error != null) {
                    String errorMessage = "";
                    if ("campos-obligatorios".equals(error)) {
                        errorMessage = "El nombre de la pista es obligatorio.";
                    } else if ("nombre-invalido".equals(error)) {
                        errorMessage = "El nombre de la pista no es v�lido.";
                    } else if ("pista-no-existe".equals(error)) {
                        errorMessage = "No existe una pista con este nombre.";
                    } else if ("eliminar-fallido".equals(error)) {
                        errorMessage = "No se pudo eliminar la pista. Intente nuevamente.";
                    } else if ("sql-excepcion".equals(error)) {
                        errorMessage = "Ocurri� un error con la base de datos. Consulte al administrador.";
                    } else {
                        errorMessage = "Ocurri� un error desconocido.";
                    }
            %>
                <p><%= errorMessage %></p>
            <% 
                } else {
            %>
                <p>Hubo un problema al intentar eliminar la pista. Por favor, int�ntalo nuevamente.</p>
            <% 
                }
            %>
            <a href="<%= request.getContextPath() %>/adminmenu/eliminarPistas" class="button">Volver a la Eliminaci�n</a>
        </div>
    </div>
</body>
</html>
