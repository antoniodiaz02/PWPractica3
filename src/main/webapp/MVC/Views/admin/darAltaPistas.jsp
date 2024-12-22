<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Pista</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
    <link rel="icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/x-icon">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h1>Registro de Pista</h1>

            <form action="<%= request.getContextPath() %>/adminmenu/registerPista" method="POST">
                <!-- Nombre de la Pista -->
                <div class="form-group">
                    <label for="nombre">Nombre de la Pista:</label>
                    <input type="text" id="nombre" name="nombre" required minlength="3">
                </div>

                <!-- Disponible -->
                <div class="form-group">
                    <label for="disponible">Disponible:</label>
                    <select id="disponible" name="disponible" required>
                        <option value="true">Sí</option>
                        <option value="false">No</option>
                    </select>
                </div>

                <!-- Interior -->
                <div class="form-group">
                    <label for="interior">¿Es Interior?:</label>
                    <select id="interior" name="interior" required>
                        <option value="true">Sí</option>
                        <option value="false">No</option>
                    </select>
                </div>

                <!-- Tamaño -->
                <div class="form-group">
                    <label for="tamano">Tamaño:</label>
                    <select id="tamano" name="tamano" required>
                        <option value="MINIBASKET">Minibasket</option>
                        <option value="ADULTOS">Adultos</option>
                        <option value="TRES_VS_TRES">Tres vs Tres</option>
                    </select>
                </div>

                <!-- Número Máximo de Jugadores -->
                <div class="form-group">
                    <label for="maxJugadores">Número Máximo de Jugadores:</label>
                    <input type="number" id="maxJugadores" name="maxJugadores" min="1" required>
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
