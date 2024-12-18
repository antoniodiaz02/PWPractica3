<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Pista</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h1>Registro de Pista</h1>

            <!-- Mostrar mensaje de error si existe -->
            <% 
                String error = request.getParameter("error");
                if (error != null) {
                    String errorMessage = "";
                    if ("campos-obligatorios".equals(error)) {
                        errorMessage = "Todos los campos son obligatorios.";
                    } else if ("max-jugadores".equals(error)) {
                        errorMessage = "El n�mero m�ximo de jugadores debe ser mayor a 0.";
                    } else if ("tamano-pista".equals(error)) {
                        errorMessage = "El tama�o de la pista no es v�lido.";
                    } else if ("nombre-invalido".equals(error)) {
                        errorMessage = "El nombre de la pista no es v�lido.";
                    } else if ("nombre-existente".equals(error)) {
                        errorMessage = "Ya existe una pista con este nombre.";
                    } else if ("insertar-fallido".equals(error)) {
                        errorMessage = "No se pudo registrar la pista. Intente nuevamente.";
                    } else if ("sql-excepcion".equals(error)) {
                        errorMessage = "Ocurri� un error con la base de datos. Consulte al administrador.";
                    } else {
                        errorMessage = "Ocurri� un error desconocido.";
                    }
            %>
                <p class="error"><%= errorMessage %></p>
            <% 
                }
            %>

            <form action="<%= request.getContextPath() %>/registerPista" method="POST">
                <!-- Nombre de la Pista -->
                <div class="form-group">
                    <label for="nombre">Nombre de la Pista:</label>
                    <input type="text" id="nombre" name="nombre" required minlength="3">
                </div>

                <!-- Disponible -->
                <div class="form-group">
                    <label for="disponible">Disponible:</label>
                    <select id="disponible" name="disponible" required>
                        <option value="true">S�</option>
                        <option value="false">No</option>
                    </select>
                </div>

                <!-- Interior -->
                <div class="form-group">
                    <label for="interior">�Es Interior?:</label>
                    <select id="interior" name="interior" required>
                        <option value="true">S�</option>
                        <option value="false">No</option>
                    </select>
                </div>

                <!-- Tama�o -->
                <div class="form-group">
                    <label for="tamano">Tama�o:</label>
                    <select id="tamano" name="tamano" required>
                        <option value="MINIBASKET">Minibasket</option>
                        <option value="ADULTOS">Adultos</option>
                        <option value="TRES_VS_TRES">Tres vs Tres</option>
                    </select>
                </div>

                <!-- N�mero M�ximo de Jugadores -->
                <div class="form-group">
                    <label for="maxJugadores">N�mero M�ximo de Jugadores:</label>
                    <input type="number" id="maxJugadores" name="maxJugadores" min="1" required>
                </div>

                <!-- Bot�n de env�o -->
                <div class="form-group">
                    <input type="submit" value="Registrar">
                </div>
            </form>
        </div>
    </div>
</body>
</html>
