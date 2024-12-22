<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Actualización Exitosa</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>
<body>
    <div class="container">
        <div class="message-container">
            <h1>¡Actualización Exitosa!</h1>
            <p>La pista ha sido actualizada correctamente.</p>
            <a href="<%= request.getContextPath() %>/listarPistas" class="button">Ver Lista de Pistas</a>
            <a href="<%= request.getContextPath() %>/adminmenu/updatePista" class="button">Actualizar otra pista</a>
            <a href="<%= request.getContextPath() %>/adminmenu" class="button">Volver al inicio</a>
        </div>
    </div>
</body>
</html>