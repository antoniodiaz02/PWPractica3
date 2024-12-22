<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/x-icon">
    <title>Inicio</title>
</head>
<body>
    <h1>Bienvenido, ${sessionScope.user.username}</h1>
    <a href="/MVC/Controllers/common/LogoutController">Cerrar sesión</a>
</body>
</html>
