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
    <form action="/MVC/Controllers/common/RegisterController" method="POST">
        <label for="username">Nombre de usuario:</label>
        <input type="text" id="username" name="username" required><br><br>

        <label for="password">Contraseña:</label>
        <input type="password" id="password" name="password" required><br><br>

        <label for="userType">Tipo de usuario:</label>
        <select id="userType" name="userType">
            <option value="cliente">Cliente</option>
            <option value="administrador">Administrador</option>
        </select><br><br>

        <input type="submit" value="Registrar">
    </form>
</body>
</html>
