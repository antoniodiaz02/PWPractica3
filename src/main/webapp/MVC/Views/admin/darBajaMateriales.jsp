<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Eliminar Material</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h1>Eliminar Material</h1>

            <form action="<%= request.getContextPath() %>/adminmenu/eliminarMateriales" method="POST">
                <!-- ID del Material -->
                <div class="form-group">
                    <label for="idMaterial">ID del Material:</label>
                    <input type="number" id="idMaterial" name="idMaterial" required min="1">
                </div>

                <!-- Botón de envío -->
                <div class="form-group">
                    <input type="submit" value="Eliminar">
                </div>
            </form>
        </div>
    </div>
</body>
</html>
