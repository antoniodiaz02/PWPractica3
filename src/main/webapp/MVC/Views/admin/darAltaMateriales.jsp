<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Material</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h1>Registro de Material</h1>

            <!-- Mostrar mensaje de error si existe -->
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
                <p class="error"><%= errorMessage %></p>
            <% 
                }
            %>

            <form action="<%= request.getContextPath() %>/registerMaterial" method="POST">
                <!-- ID del Material -->
                <div class="form-group">
                    <label for="idMaterial">ID del Material:</label>
                    <input type="number" id="idMaterial" name="idMaterial" required min="1">
                </div>

                <!-- Tipo de Material -->
                <div class="form-group">
                    <label for="tipoMaterial">Tipo de Material:</label>
                    <select id="tipoMaterial" name="tipoMaterial" required>
                        <option value="PELOTAS">Pelotas</option>
                        <option value="CONOS">Conos</option>
                        <option value="CANASTAS">Canastas</option>
                    </select>
                </div>

                <!-- Uso Interior -->
                <div class="form-group">
                    <label for="usoInterior">¿Es para Uso Interior?:</label>
                    <select id="usoInterior" name="usoInterior" required>
                        <option value="true">Sí</option>
                        <option value="false">No</option>
                    </select>
                </div>

                <!-- Estado del Material -->
                <div class="form-group">
                    <label for="estadoMaterial">Estado del Material:</label>
                    <select id="estadoMaterial" name="estadoMaterial" required>
                        <option value="DISPONIBLE">Disponible</option>
                        <option value="RESERVADO">Reservado</option>
                        <option value="MAL_ESTADO">Mal estado</option>
                    </select>
                </div>

                <!-- Botón de envío -->
                <div class="form-group">
                    <input type="submit" value="Registrar">
                </div>
            </form>
        </div>
    </div>
</body>
</html>
