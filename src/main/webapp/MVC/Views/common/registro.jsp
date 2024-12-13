<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro de Usuario</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>
<body>
    <div class="container">
        <div class="form-container">
            <h1>Registro de Usuario</h1>

            <!-- Mostrar mensaje de error si existe -->
            <% 
                String error = (String) request.getAttribute("error");
                if (error != null) {
            %>
                <p class="error"><%= error %></p>
            <% 
                }
            %>

            <form action="/gestionbaloncestopistas/register" method="POST">
                <!-- Nombre Completo -->
                <div class="form-group">
                    <label for="nombreCompleto">Nombre Completo:</label>
                    <input type="text" id="nombreCompleto" name="nombreCompleto" required>
                </div>

                <!-- Correo Electr�nico -->
                <div class="form-group">
                    <label for="correo">Correo Electr�nico:</label>
                    <input type="email" id="correo" name="correo" required>
                </div>

                <!-- Contrase�a -->
                <div class="form-group">
                    <label for="contrase�a">Contrase�a:</label>
                    <input type="password" id="contrase�a" name="contrase�a" required>
                </div>

                <!-- Fecha de Nacimiento (opcional) -->
                <div class="form-group">
                    <label for="fechaNacimiento">Fecha de Nacimiento:</label>
                    <input type="date" id="fechaNacimiento" name="fechaNacimiento">
                </div>

                <!-- Tipo de Usuario -->
                <div class="form-group">
                    <label for="userType">Tipo de Usuario:</label>
                    <select id="userType" name="tipoUsuario" required>
                        <option value="cliente">Cliente</option>
                        <option value="administrador">Administrador</option>
                    </select>
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
