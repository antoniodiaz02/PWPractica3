# Proyecto PW - Sistema de Gestión de Reservas de Pistas de Baloncesto

Este programa permite la gestión de un sistema de reservas con tres módulos principales:  
**Menú pistas**, **Menú usuarios** y **Menú reservas**.  

La aplicación está programada en Java siguiendo el patrón Modelo Vista Controlador (MVC) y utiliza tecnologías como Servlets, JSP, y JavaBeans para una separación adecuada de la lógica de negocio y la presentación. Se entrega como un archivo `.war` listo para ser desplegado en un servidor Apache Tomcat.

## Requisitos para la ejecución

- **Servidor de aplicaciones Apache Tomcat 8.5** (standalone).  
- **Java Runtime Environment (JRE)** versión 8 o superior.  
- Archivo WAR proporcionado: `GestorReservas.war`.  

## Instalación y ejecución

1. **Descarga del archivo WAR**  
   Clona el repositorio o descarga el archivo WAR desde el siguiente enlace:  
   [Repositorio GitHub](https://github.com/antoniodiaz02/PWPractica3)  

2. **Copia del archivo WAR al servidor Tomcat**  
   Copia el archivo `GestorReservas.war` a la carpeta `webapps` de tu instalación de Tomcat.  

3. **Inicia el servidor Tomcat**  
   Asegúrate de que el servidor esté correctamente configurado y ejecuta el script de inicio:

   ```bash
   /path/to/tomcat/bin/startup.sh

4. **Abre un navegador web y accede a la URL correspondiente a tu servidor:**

```bash
http://<tu-servidor>:<puerto>/GestorReservas
````

Por defecto, el puerto suele ser 8080.


# Estructura del Programa

El sistema se compone de un flujo de navegación que incluye las siguientes vistas y funcionalidades:

## Página de Login/Registro
- Permite que los usuarios existentes inicien sesión y los nuevos se registren.

## Menú Usuario
- Gestión de datos personales.
- Visualización de pistas disponibles.
- Reserva de pistas.

## Menú Administrador
- Gestión de usuarios registrados.
- Administración de las pistas.
- Visualización y control de las reservas realizadas.

Cada menú y funcionalidad está implementado mediante **Servlets** y **JSP**, asegurando una experiencia interactiva y eficiente para el usuario final.

---

# Tecnologías Utilizadas

- **Java (J2EE):** Desarrollo de la lógica de negocio y controladores.
- **Servlets:** Implementación de las funcionalidades del lado del servidor.
- **JSP (Java Server Pages):** Creación de las vistas dinámicas en la aplicación.
- **JavaBeans:** Separación de funcionalidades entre cliente y administrador.
- **CSS:** Mejoras visuales en la interfaz de usuario.
- **Apache Tomcat 8.5:** Servidor de aplicaciones para el despliegue del archivo WAR.

---

# Equipo de Desarrollo

- **Antonio Diaz Barbancho**  
  _GitHub: [antoniodiaz02](https://github.com/antoniodiaz02)_

- **Carlos Marín Rodríguez**  
  _GitHub: [k3sero](https://github.com/k3sero)_

- **Carlos De la Torre Frias**  
  _GitHub: [CarlosDT191](https://github.com/CarlosDT191)_

- **Daniel Grande Rubio**  
  _GitHub: [Dani93414](https://github.com/Dani93414)_

