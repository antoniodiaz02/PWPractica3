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
            <p>El material ha sido actualizado correctamente.</p>
            <a href="<%= request.getContextPath() %>/listarMateriales" class="button">Ver Lista de Materiales</a>
            <a href="<%= request.getContextPath() %>/adminmenu/updateMaterial" class="button">Actualizar otro material</a>
            <a href="<%= request.getContextPath() %>/adminmenu" class="button">Volver al inicio</a>
        </div>
    </div>
</body>
</html>