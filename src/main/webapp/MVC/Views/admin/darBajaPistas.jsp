<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Eliminar Pista</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h1>Eliminar Pista</h1>

            <form action="<%= request.getContextPath() %>/adminmenu/eliminarPistas" method="POST">
                <!-- Nombre de la Pista -->
                <div class="form-group">
                    <label for="nombre">Nombre de la Pista:</label>
                    <input type="text" id="nombre" name="nombre" required minlength="3">
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
