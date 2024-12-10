<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Gestión de Pistas de Baloncesto</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/index.css">
<style>
  body {
    background: linear-gradient(to bottom right, #1d3557, #457b9d);
    color: white;
  }
  .gradient-custom {
    background: linear-gradient(to bottom right, #457b9d, #1d3557);
  }
  .logo {
    max-width: 100px;
    margin-bottom: 15px;
  }
</style>
</head>
<body>
<section class="vh-100 gradient-custom">
  <div class="container py-5 h-100">
    <div class="row d-flex justify-content-center align-items-center h-100">
      <div class="col-12 col-md-8 col-lg-6 col-xl-5">
        <div class="card bg-dark text-white" style="border-radius: 1rem;">
          <div class="card-body p-5 text-center">

            <div class="mb-md-5 mt-md-4 pb-5">
              <img src="<%=request.getContextPath()%>/images/basketball_logo.jpg" alt="Basketball Logo" class="logo">
              <h2 class="fw-bold mb-2 text-uppercase">Gestión de Pistas</h2>
              <p class="text-white-50 mb-5">Accede a tu cuenta para gestionar tus reservas</p>

              <form method="POST" action="MVC/controllers/common/logInController.jsp">
                <div class="form-outline form-white mb-4">
                  <input type="email" name="typeEmailX" class="form-control form-control-lg" required/>
                  <label class="form-label" for="typeEmailX">Correo Electrónico</label>
                </div>

                <div class="form-outline form-white mb-4">
                  <input type="password" name="typePasswordX" class="form-control form-control-lg" required/>
                  <label class="form-label" for="typePasswordX">Contraseña</label>
                </div>

                <button class="btn btn-outline-light btn-lg px-5" type="submit">Iniciar Sesión</button>
              </form>

              <% if (request.getParameter("message") != null && !request.getParameter("message").isEmpty()) { %>
                <p style="color: red; margin-top: 15px;">Error: <%= request.getParameter("message") %></p>
              <% } %>

            </div>

            <div>
              <p class="mb-0">¿Nuevo en el sistema? <a href="<%=request.getContextPath()%><%=application.getInitParameter("register")%>" class="text-white-50 fw-bold">Regístrate aquí</a></p>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</section>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
