<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error al Asociar Material a Pista</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>
<body>
    <div class="container">
        <div class="message-container">
            <h1>Error al Asociar Material a Pista</h1>
            <% 
                String error = request.getParameter("error");
                if (error != null) {
                    String errorMessage = "";
                    if ("campos-obligatorios".equals(error)) {
                        errorMessage = "Todos los campos son obligatorios.";
                    } else if ("id-material".equals(error)) {
                        errorMessage = "El ID del material debe ser un número positivo.";
                    } else if ("nombre-pista".equals(error)) {
                        errorMessage = "El nombre de la pista no es válido.";
                    } else if ("pista-no-existe".equals(error)) {
                        errorMessage = "La pista especificada no existe.";
                    } else if ("material-no-existe".equals(error)) {
                        errorMessage = "El material especificado no existe.";
                    } else if ("tipos-incompatibles".equals(error)) {
                        errorMessage = "El tipo del material es incompatible con la pista.";
                    } else if ("sin-cambios".equals(error)) {
                        errorMessage = "No se realizó ningún cambio. Intente nuevamente.";
                    } else if ("sql-excepcion".equals(error)) {
                        errorMessage = "Ocurrió un error con la base de datos. Consulte al administrador.";
                    } else if ("metodo-no-permitido".equals(error)) {
                        errorMessage = "El método solicitado no está permitido.";
                    } else if ("material-no-disponible".equals(error)) {
                        errorMessage = "El material está en estado reservado o en mal estado y no puede ser asociado.";
                    } else {
                        errorMessage = "Ocurrió un error desconocido.";
                    }
            %>
                <p><%= errorMessage %></p>
            <% 
                } else {
            %>
                <p>Hubo un problema al intentar asociar el material a la pista. Por favor, inténtalo nuevamente.</p>
            <% 
                }
            %>
            <a href="<%= request.getContextPath() %>/adminmenu/asociarMaterialAPista" class="button">Volver a Asociar Material</a>
        </div>
    </div>
</body>
</html>
