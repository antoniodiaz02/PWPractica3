<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro</title>
</head>
<body>
    <h1>Registro de Usuario</h1>
	<form action="/gestionbaloncestopistas/register" method="POST">
        <label for="nombreCompleto">Nombre Completo:</label>
        <input type="text" id="nombreCompleto" name="nombreCompleto" required><br><br>

        <label for="correo">Correo Electrónico:</label>
        <input type="email" id="correo" name="correo" required><br><br>

        <label for="contraseña">Contraseña:</label>
        <input type="password" id="contraseña" name="contraseña" required><br><br>

        <label for="userType">Tipo de usuario:</label>
        <select id="userType" name="tipoUsuario">
            <option value="cliente">Cliente</option>
            <option value="administrador">Administrador</option>
        </select><br><br>

        <input type="submit" value="Registrar">
    </form>
</body>
</html>
