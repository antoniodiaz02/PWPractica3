<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Actualizar Pista</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h1>Actualizar Pista</h1>

            <form action="<%= request.getContextPath() %>/adminmenu/updatePista" method="POST">
                <!-- Nombre de la Pista -->
                <div class="form-group">
                    <label for="nombrePista">Nombre de la Pista:</label>
                    <input type="text" id="nombrePista" name="nombrePista" required>
                </div>

                <!-- Tamaño de la Pista -->
                <div class="form-group">
                    <label for="tamanoPista">Tamaño de la Pista:</label>
                    <select id="tamanoPista" name="tamanoPista" required>
                        <option value="MINIBASKET">Minibasket</option>
                        <option value="TRES_VS_TRES">Tres vs Tres</option>
                        <option value="ADULTOS">Adultos</option>
                    </select>
                </div>

                <!-- Número Máximo de Jugadores -->
                <div class="form-group">
                    <label for="maxJugadores">Número Máximo de Jugadores:</label>
                    <input type="number" id="maxJugadores" name="maxJugadores" required min="1">
                </div>

                <!-- Disponibilidad -->
                <div class="form-group">
                    <label for="disponible">¿Está Disponible?:</label>
                    <select id="disponible" name="disponible" required>
                        <option value="true">Sí</option>
                        <option value="false">No</option>
                    </select>
                </div>

                <!-- Interior o Exterior -->
                <div class="form-group">
                    <label for="interior">¿Es Interior?:</label>
                    <select id="interior" name="interior" required>
                        <option value="true">Sí</option>
                        <option value="false">No</option>
                    </select>
                </div>

                <!-- Botón de envío -->
                <div class="form-group">
                    <input type="submit" value="Actualizar">
                </div>
            </form>
        </div>
    </div>
</body>
</html>
