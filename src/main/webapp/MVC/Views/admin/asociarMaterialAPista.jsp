<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Asociar Material a Pista</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/tabla.css">
    <link rel="icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/x-icon">
</head>
<body>
    <!-- Encabezado -->
    <div class="header">
        <div class="container">
            <h1>Asociar Material a Pista</h1>
        </div>
    </div>

    <!-- Contenedor principal -->
    <div class="main-content">
        <div class="container">
            <div class="form-container">
                <form action="<%= request.getContextPath() %>/adminmenu/asociarMaterialAPista" method="POST">
                    <!-- Nombre de la Pista -->
                    <div class="form-group">
                        <label for="nombre">Nombre de la Pista:</label>
                        <input type="text" id="nombre" name="nombre" required>
                    </div>

                    <!-- ID del Material -->
                    <div class="form-group">
                        <label for="idMaterial">ID del Material:</label>
                        <input type="number" id="idMaterial" name="idMaterial" required min="1">
                    </div>

                    <!-- Botón de envío -->
                    <div class="form-group">
                        <input type="submit" value="Asociar" class="button">
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Pie de página -->
    <div class="footer">
        <div class="container">
            <p>Aplicación de Gestión Deportiva - Práctica de Programación Web.</p>
        </div>
    </div>
</body>
</html>