<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Asociación Exitosa</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
    <link rel="icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/x-icon">
</head>
<body>
    <div class="container">
        <div class="message-container">
            <h1>ˇAsociación Exitosa!</h1>
            <p>El material ha sido asociado correctamente a la pista.</p>
            <a href="<%= request.getContextPath() %>/listarPistas" class="button">Ver Lista de Pistas</a>
            <a href="<%= request.getContextPath() %>/adminmenu/asociarMaterialAPista" class="button">Asociar otro material</a>
            <a href="<%= request.getContextPath() %>/adminmenu" class="button">Volver al inicio</a>
        </div>
    </div>
</body>
</html>
