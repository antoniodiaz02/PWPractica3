<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="refresh" content="3;url=index.jsp"> <!-- Redirección automática en 3 segundos -->
    <title>Cerrando sesión</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/logout.css">
    <link rel="icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/x-icon">
</head>
<body>
    <div class="logout-container">
        <h1>Cerrando sesión...</h1>
    </div>
</body>
</html>
