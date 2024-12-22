<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Pistas de Baloncesto</title>
    <!-- Ruta al archivo CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/index.css">
    <link rel="icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/x-icon">
    
</head>
<body>
    <section class="vh-100 gradient-custom">
        <div class="container py-5 h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                    <div class="card">
                        <div class="card-body text-center">
                            <div class="mb-md-5 mt-md-4 pb-5">
                                <!-- Imagen de fondo con baja opacidad -->
                                <div class="background-logo"></div>
                                <h2 class="fw-bold mb-2 text-uppercase">Gestión de Pistas</h2>
                                <p class="text-white-50 mb-5">Accede a tu cuenta para gestionar tus reservas</p>

                                <!-- Formulario de login -->
                                <form method="POST" action="<%= request.getContextPath() %>/login">
                                    <div class="form-outline form-white">
                                        <input type="email" id="correo" name="correo" class="form-control form-control-lg" required autocomplete="off"/>
                                        <label class="form-label" for="correo">Correo Electrónico</label>
                                    </div>

                                    <div class="form-outline form-white">
                                        <input type="password" id="contraseña" name="contraseña" class="form-control form-control-lg" required autocomplete="off"/>
                                        <label class="form-label" for="contraseña">Contraseña</label>
                                    </div>

                                    <button class="btn-custom btn-lg px-5" type="submit">Iniciar Sesión</button>
                                </form>

                                <!-- Mostrar mensajes de error -->
                                <% if (request.getAttribute("error") != null) { %>
                                    <div class="alert alert-danger mt-3" role="alert">
                                        <%= request.getAttribute("error") %>
                                    </div>
                                <% } %>
                            </div>

                            <div class="footer">
                                <!-- Enlace a la página de registro -->
                                <p class="mb-0">¿Nuevo en el sistema? 
                                    <a href="<%= request.getContextPath() %>/register" class="fw-bold">Regístrate aquí</a>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Scripts de Bootstrap -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
