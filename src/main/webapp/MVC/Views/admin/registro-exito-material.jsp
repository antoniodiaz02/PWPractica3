<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro Exitoso</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
</head>
<body>
    <div class="container">
        <div class="message-container">
            <h1>íRegistro Exitoso!</h1>
            <p>El material ha sido registrado correctamente.</p>
            <a href="<%= request.getContextPath() %>/listaMateriales" class="button">Ver Lista de Materiales    </a>
            <a href="<%= request.getContextPath() %>/MVC/Views/admin/darAltaMateriales.jsp" class="button">Registrar Otro Material</a>
        </div>
    </div>
</body>
</html>