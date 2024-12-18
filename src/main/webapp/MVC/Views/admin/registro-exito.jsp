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
            <h1>¡Registro Exitoso!</h1>
            <p>La pista ha sido registrada correctamente.</p>
			<a href="<%= request.getContextPath() %>/listaPistas" class="button">Ver Lista de Pistas</a>
			<a href="<%= request.getContextPath() %>/MVC/Views/admin/darAltaPistas.jsp" class="button">Registrar Otra Pista</a>
        </div>
    </div>
</body>
</html>
