package es.uco.pw.data.DAOs;

import es.uco.pw.business.DTOs.PistaDTO;
import es.uco.pw.business.DTOs.ReservaAdultosDTO;
import es.uco.pw.business.DTOs.ReservaDTO;
import es.uco.pw.business.DTOs.ReservaFamiliarDTO;
import es.uco.pw.business.DTOs.ReservaInfantilDTO;
import es.uco.pw.business.Gestores.GestorPistas;
import es.uco.pw.business.DTOs.PistaDTO.TamanoPista;
import es.uco.pw.common.DBConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/**
 * Clase que gestiona las reservas en la base de datos.
 */
public class ReservaDAO {

	/**
     * Objeto connection para crear la conexión con la base de datos.
     */
    private Connection connection;
    
    /**
     * Objeto properties encargado de las sentencias SQL.
     */
    private Properties properties;
    
    /**
     * Constructor que inicializa la conexión con base de datos.
     */
    public ReservaDAO() {
    	properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("sql.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new FileNotFoundException("Properties file 'sql.properties' not found in classpath");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Realiza una reserva individual.
     * 
     * @param correoUsuario Correo del usuario a realizar la reserva.
     * @param nombrePista Nombre de la pista a reservar.
     * @param fechaHora Fecha de la reserva.
     * @param duracion Duración de la reserva.
     * @param numeroAdultos Número de adultos a participar.
     * @param numeroNinos Numero de ninos a participar.
     * @return respuesta Código de respuesta.
     */
    public int hacerReservaIndividual(String correoUsuario, String nombrePista, Date fechaHora, int duracion, int numeroAdultos, int numeroNinos, Class<? extends ReservaDTO> tipoReserva) {
        int respuesta = -10; //Codigo de error por defecto.
    	int jugador = buscarIdJugador(correoUsuario); // Obtener el ID del jugador
        int pistaId = buscarIdPista(nombrePista); // Obtener el ID de la pista

        // Comprobación adicional para evitar reservas en la misma pista y hora
        if (existeReservaParaPistaYHora(nombrePista, fechaHora)) {
        	respuesta = -1;
            return respuesta;
        }

        // Validaciones de existencia y disponibilidad
        if (jugador == -1) {
        	respuesta = -2;
            return respuesta;
        }
        PistaDTO pista = buscarPista(nombrePista);
        if (pista == null) {
        	respuesta = -3;
            return respuesta;
        }
        if (!pista.isDisponible()) {
        	respuesta = -4;
            return respuesta;
        }
        if (plazoExcedido(fechaHora)) {
        	respuesta = -5;
            return respuesta;
        }
        
        if(pista.getMaxJugadores() < numeroAdultos + numeroNinos) {
        	respuesta = -7;
        	return respuesta;
        }

        // Calcular precio y descuento
        float precio = calcularPrecio(duracion);
        float descuento = calcularAntiguedadJugador(correoUsuario) > 2 ? precio * 0.1f : 0;

        String tipoReservaString = null;

        // Determinar el tipo de reserva
        if (tipoReserva == ReservaInfantilDTO.class && pista.getTamanoPista() == PistaDTO.TamanoPista.MINIBASKET) {
            tipoReservaString = "INFANTIL";
        } else if (tipoReserva == ReservaFamiliarDTO.class && 
                   (pista.getTamanoPista() == PistaDTO.TamanoPista.MINIBASKET || pista.getTamanoPista() == PistaDTO.TamanoPista.TRES_VS_TRES)) {
            tipoReservaString = "FAMILIAR";
        } else if (tipoReserva == ReservaAdultosDTO.class) {
            tipoReservaString = "ADULTOS";
        } else {
        	respuesta = -6;
            return respuesta;
        }

        // Inserción en la base de datos
        String query = properties.getProperty("insert_reserva");
        respuesta = -8;

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, jugador);
            statement.setTimestamp(2, new java.sql.Timestamp(fechaHora.getTime()));
            statement.setInt(3, duracion);
            statement.setInt(4, pistaId);
            statement.setFloat(5, precio);
            statement.setFloat(6, descuento); // True si hay descuento
            statement.setString(7, tipoReservaString);
            statement.setObject(8, numeroNinos > 0 ? numeroNinos : null); // NULL si no aplica
            statement.setObject(9, numeroAdultos > 0 ? numeroAdultos : null); // NULL si no aplica

            int rowsInserted = statement.executeUpdate();
            if(rowsInserted > 0) {
            	respuesta = 0;        	
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        	respuesta = -9;
            return respuesta;
        } finally {
            db.closeConnection();
        }

        return respuesta;
    }

    
    /**
	 * Realiza una reserva con bono.
	 * @param correoUsuario Correo del usuario que realiza la reserva.
	 * @param nombrePista Nombre de la pista a reservar.
	 * @param fechaHora Día y hora de la reserva de la pista.
	 * @param duracion Tiempo de duración de la reserva (30, 60 ó 120 mins).
	 * @param numeroAdultos Número de adultos que acuden.
	 * @param numeroNinos Número de niños que acuden.
	 * @param tipoReserva Clase Reserva que tiene más cantidad de detalles de la reserva como el tipo de reserva, el tamaño de la pista...
	 * @param bonoId El identificador del bono con el que se va a realizar la reserva.
	 * @return respuesta Código de respuesta.
	 */
    public int hacerReservaBono(String correoUsuario, String nombrePista, Date fechaHora, int duracion, int numeroAdultos, int numeroNinos, Class<? extends ReservaDTO> tipoReserva, int bonoId) {
        int respuesta = -15;
    	int jugador = buscarIdJugador(correoUsuario); // Obtener ID del jugador
        int pistaId = buscarIdPista(nombrePista); // Obtener ID de la pista

        // Comprobación adicional para evitar reservas en la misma pista y hora
        if (existeReservaParaPistaYHora(nombrePista, fechaHora)) {
        	respuesta = -1;
            return respuesta;
        }

        // Validaciones de existencia y disponibilidad
        if (jugador == -1) {
        	respuesta = -2;
            return respuesta;
        }
        PistaDTO pista = buscarPista(nombrePista);
        if (pista == null) {
        	respuesta = -3;
            return respuesta;
        }
        if (!pista.isDisponible()) {
        	respuesta = -4;
            return respuesta;
        }
        if (plazoExcedido(fechaHora)) {
        	respuesta = -5;
            return respuesta;
        }

        if(pista.getMaxJugadores() < numeroAdultos + numeroNinos) {
        	respuesta = -6;
        	return respuesta;
        }

        // Comprobación del bono
        int error= comprobarBono(bonoId, correoUsuario, pista.getTamanoPista());
        if(error !=0) {
            return error;
        }

        int sesionesRestantes = obtenerSesionesRestantes(bonoId);

        // Validar que queden sesiones disponibles
        if (sesionesRestantes <= 0) {
        	respuesta = -10;
            return respuesta;
        }

        float precio = calcularPrecio(duracion);
        float descuento = 0.05f; // Descuento fijo del bono

        String tipoReservaString = null;

        // Determinar el tipo de reserva
        if (tipoReserva == ReservaInfantilDTO.class && pista.getTamanoPista() == PistaDTO.TamanoPista.MINIBASKET) {
            tipoReservaString = "INFANTIL";
        } else if (tipoReserva == ReservaFamiliarDTO.class && 
                   (pista.getTamanoPista() == PistaDTO.TamanoPista.MINIBASKET || pista.getTamanoPista() == PistaDTO.TamanoPista.TRES_VS_TRES)) {
            tipoReservaString = "FAMILIAR";
        } else if (tipoReserva == ReservaAdultosDTO.class) {
            tipoReservaString = "ADULTOS";
        } else {
        	respuesta = -7;
            return respuesta;
        }

        // Inserción en la base de datos
        String queryInsertReserva = properties.getProperty("insert_reserva");
        respuesta = 0;

        DBConnection db = new DBConnection();
        connection = db.getConnection();

        try {
            // Usar transacción para asegurarse de que ambas operaciones (reserva y actualización del bono) sean atómicas
            connection.setAutoCommit(false);

            // Insertar reserva
            try (PreparedStatement statement = connection.prepareStatement(queryInsertReserva)) {
                statement.setInt(1, jugador);
                statement.setTimestamp(2, new java.sql.Timestamp(fechaHora.getTime()));
                statement.setInt(3, duracion);
                statement.setInt(4, pistaId);
                statement.setFloat(5, precio);
                statement.setFloat(6, descuento); // Descuento aplicado siempre para bonos
                statement.setString(7, tipoReservaString);
                statement.setObject(8, numeroNinos > 0 ? numeroNinos : null); // NULL si no aplica
                statement.setObject(9, numeroAdultos > 0 ? numeroAdultos : null); // NULL si no aplica

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted <= 0) {
                    System.out.println(" ERROR! No se pudo insertar la reserva.");
                    respuesta = -13;
                    return respuesta;
                }
            }

            // Actualizar el bono
            actualizarSesionesBono(bonoId);

            // Confirmar transacción
            respuesta= 0;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback(); // Revertir cambios en caso de error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            db.closeConnection();
        }

        return respuesta;
    }
   
    
    /**
	 * Calcula la antiguedad del jugador.
	 * 
	 * @param correoElectronico Correo del usuario que pide el bono.
	 * @return Respuesta Devuelve el codigo de error del proceso.
	 */
    public int calcularAntiguedadJugador(String correoElectronico) {
    	
    	int respuesta = -15;
    	String queryBuscar = properties.getProperty("buscar_por_correo");
        DBConnection db = new DBConnection();
        connection = db.getConnection();
        java.sql.Date fechaInscripcion;

        try (PreparedStatement statementBuscar = connection.prepareStatement(queryBuscar)) {

            // Comprobar si el usuario ya existe mediante el correo electrónico.
            statementBuscar.setString(1, correoElectronico);
            ResultSet rs = statementBuscar.executeQuery();

            if (rs.next()) {
            	fechaInscripcion= rs.getDate("fechaInscripcion");
            	if (fechaInscripcion == null) {
            		respuesta = 0;
            		return respuesta;
            	}
                long diffInMillis = new Date().getTime() - fechaInscripcion.getTime();
                return (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) / 365;
            	
            }
	    } catch (SQLException e) {
	    	respuesta = -1;
	        return respuesta; // Código para indicar error general de base de datos.
	    } finally {
	        db.closeConnection();
	    }
        respuesta = -1;
        return respuesta;
    }
    
    /**
	 * Genera un nuevo bono de usuario.
	 * @param correoUsuario Correo del usuario que pide el bono.
	 * @param tamano Tipo de pista a la que asignar el bono.
	 * @return Devuelve 0 si el procedimiento de creacion del bono se ha hecho de manera correcta, y un valor negativo si hay algo que falla.
	 */
    public int hacerNuevoBono(String correoUsuario, TamanoPista tamano){
    	    	
    	JugadorDAO jugador= new JugadorDAO();
    	int idUsuario= -1;
    	
    	String queryInsert = properties.getProperty("insert_bono");   
    	String queryJugador= properties.getProperty("buscar_por_correo");
    	
    	DBConnection dbConnection = new DBConnection();
    	connection = dbConnection.getConnection();
 	
        if(jugador.buscarUsuarioPorCorreo(correoUsuario) != 1) {
        	return -1;
        }
  
    	// Valor por defecto para las sesiones de un bono nuevo
    	int sesiones = 5;
    	
    	try (PreparedStatement stmtJugador = connection.prepareStatement(queryJugador)) {
    		stmtJugador.setString(1, correoUsuario);
    		
    		ResultSet rs = stmtJugador.executeQuery();

            // Si la consulta devuelve un resultado
            if (rs.next()) {
                // Obtener el valor de id_usuario del ResultSet
                idUsuario = rs.getInt("idUsuario"); // Asegúrate de que "id_usuario" es el nombre correcto de la columna en tu base de datos
            }
            try (PreparedStatement stmtInsert = connection.prepareStatement(queryInsert)) {
            	// Establecer los parámetros de la consulta
            	stmtInsert.setInt(1, idUsuario);                // ID del usuario
            	stmtInsert.setInt(2, sesiones);                 // Número de sesiones
            	stmtInsert.setString(3, tamano.toString());     // Tamaño de la pista (usamos toString() para convertir a texto)
            	
            	// Ejecutar la consulta
            	stmtInsert.executeUpdate();
            } catch (SQLException e) {
            	e.printStackTrace();
            	return -2;
            }
    		
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return -3;
    	} finally {		
    		// Cerrar la conexión a la base de datos
    		dbConnection.closeConnection();
    	}
    	// Si todo fue bien, retornamos true
    	return 0;
        	
    }

    
    /**
	 * Función que calcula el precio de reserva respecto al tiempo que se quiere reservar.
	 * @param duracion Tiempo de duración de la reserva.
	 * @return Devuelve el precio total de reserva sin descuento respecto al tiempo de reserva.
	 */
    private float calcularPrecio(int duracion) {
        switch (duracion) {
            case 60:
                return 20.0f;
            case 90:
                return 30.0f;
            case 120:
                return 40.0f;
            default:
                throw new IllegalArgumentException(" ERROR! Duración no permitida. Use 60, 90 o 120 minutos.");
        }
    }
    
    
    /**
     * Busca y devuelve el identificador del jugador con el correo descrito.
     *
     * @param correoElectronico Correo electrónico del jugador a buscar.
     * @return El valor de su identificador.
     */
    public int buscarIdJugador(String correoElectronico) {
        String queryBuscar = properties.getProperty("buscar_por_correo");
        DBConnection db = new DBConnection();
        connection = db.getConnection();
        int idJugador= -1;

        try (PreparedStatement statementBuscar = connection.prepareStatement(queryBuscar)) {

            // Comprobar si el usuario ya existe mediante el correo electrónico.
            statementBuscar.setString(1, correoElectronico);
            ResultSet rs = statementBuscar.executeQuery();

            if (rs.next()) {
            	idJugador= rs.getInt("idUsuario");
            }
	    } catch (SQLException e) {
	        System.err.println("Error al buscar el usuario en la base de datos: " + e.getMessage());
	        return -1; // Código para indicar error general de base de datos.
	    } finally {
	        db.closeConnection();
	    }
        return idJugador;
    }
    
    
    /**
     * Busca y devuelve el identificador de la pista con el nombre a buscar.
     *
     * @param nombre Nombre de la pista a buscar.
     * @return El identificador de la pista con el nombre de pista nombre.
     */
    public int buscarIdPista(String nombre) {
    	String queryBuscar = properties.getProperty("find_pista_by_nombre");
        DBConnection db = new DBConnection();
        connection = db.getConnection();
        int idPista= -1;

        try (PreparedStatement statementBuscar = connection.prepareStatement(queryBuscar)) {

            // Comprobar si el usuario ya existe mediante el correo electrónico.
            statementBuscar.setString(1, nombre);
            ResultSet rs = statementBuscar.executeQuery();

            if (rs.next()) {
            	idPista= rs.getInt("idPista");
            }
	    } catch (SQLException e) {
	        System.err.println("Error al buscar el usuario en la base de datos: " + e.getMessage());
	        return -1; // Código para indicar error general de base de datos.
	    } finally {
	        db.closeConnection();
	    }
        return idPista;
    }
    
    
    /**
     * Busca y devuelve un objeto Pista a partir de su nombre.
     * Lee el archivo de pistas línea por línea hasta encontrar el nombre de pista solicitado.
     * Luego, convierte los datos en un objeto Pista.
     *
     * @param nombre Nombre de la pista a buscar.
     * @return Un objeto Pista si el nombre existe en el archivo, o null si no se encuentra
     *         o si ocurre algún error.
     */
    public PistaDTO buscarPista(String nombre) {
    	PistaDAO pista = new PistaDAO();
    	return pista.findPistaByNombre(nombre);
    }
 
    
    /**
	 * Comprueba si el bono tiene sesiones disponibles, si no está caducado, si el bono es de la persona que intenta aceder a él 
	 * y si la reserva que se quiere hacer con el bono, es a una pista del mismo tamaño que del bono.
	 * @param bonoId Identificador único del bono.
	 * @param correoUsuario Correo del usuario
	 * @param tamano Indica el tamaño de pista.
	 * @return Si se ha realizado el procedimiento correctamente devuelve true, y devuelve false si contradice una de las condiciones
	 * 		   o si ha habido un error.
	 */
    public int comprobarBono(int bonoId, String correoUsuario, TamanoPista tamano) {
    	String query = properties.getProperty("buscar_bono");
    	
    	DBConnection db = new DBConnection();
    	connection = db.getConnection();
    	
    	int sesiones = 0;
    	Date fechaBono = null;
    	int idPropietario = -1;
    	
    	try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bonoId);  // Asigna el id del bono al parámetro de la consulta
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Se encontró el bono
                idPropietario = rs.getInt("usuarioId");
                sesiones = rs.getInt("sesiones");
                fechaBono = rs.getTimestamp("fechaCaducidad");  // Obtener la fecha de la primera sesión como Timestamp
                
                String tamanoString = rs.getString("tipoPista");
                TamanoPista tamanoBono = TamanoPista.valueOf(tamanoString.toUpperCase());

                // Comprobar si el tamaño de la pista coincide con el del bono
                if (!tamanoBono.equals(tamano)) {
                    return -7;
                }
            } 
            
            else {
                return -8;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -9;
        } finally {
            db.closeConnection();
        }

        // Comprobar que el bono tiene sesiones disponibles
        if (sesiones == 0) {
            return -10;
        }

        int idComprobar= buscarIdJugador(correoUsuario);
        // Comprobar que el bono pertenece al usuario que intenta acceder a él
        if (idComprobar!=idPropietario) {
            return -11;
        }

        // Verificar que la fecha del bono no exceda en un año la fecha actual
        if(fechaBono!=null) {
        	if (plazoExcedido(fechaBono)) {
        		return -12;
        	}        	
        }
	    	
    	return 0; // El bono es válido
    }

    
    /**
	 * Función que decrementa el numero de sesiones del bono y que añade la fecha al final si es la primera reserva del bono.
	 * @param bonoId Identificador único del bono.
	 * @return Si se ha realizado el procedimiento correctamente devuelve true, y devuelve false si ha habido algun error.
	 */
	public boolean actualizarSesionesBono(int bonoId) {
		
		String queryBuscarBono = properties.getProperty("buscar_bono");
		String queryActualizarBono= properties.getProperty("update_bono");
	    
	    DBConnection db = new DBConnection();
	    connection = db.getConnection();
	    
	    try {
	        // Paso 1: Buscar el bono por su ID
	        try (PreparedStatement stmtBuscar = connection.prepareStatement(queryBuscarBono)) {
	            stmtBuscar.setInt(1, bonoId);
	            ResultSet rs = stmtBuscar.executeQuery();

	            if (rs.next()) {
	                int sesiones = rs.getInt("sesiones");
	                java.sql.Date fechaInicio = rs.getDate("fechaInicio");
	                java.sql.Date fechaCaducidad = rs.getDate("fechaCaducidad");

	                // Verificar si es la primera reserva (sesiones = 5)
	                
	                java.sql.Date nuevaFechaPrimeraSesion;
	                java.sql.Date nuevaFechaCaducidad;
	                if (sesiones == 5) {
	                	nuevaFechaPrimeraSesion = new java.sql.Date(new Date().getTime());
	                	
	                	Calendar calendar = Calendar.getInstance();
	                    calendar.setTime(nuevaFechaPrimeraSesion);
	                    calendar.add(Calendar.YEAR, 1);
	                    nuevaFechaCaducidad = new java.sql.Date(calendar.getTimeInMillis());
	                    
	                }
	                else {
	                	nuevaFechaPrimeraSesion= fechaInicio;
	                	nuevaFechaCaducidad= fechaCaducidad;
	                }

	                // Paso 2: Decrementar el número de sesiones
	                int nuevasSesiones = sesiones - 1;
	                if (nuevasSesiones < 0) {
	                    System.out.println(" ERROR! El bono no tiene más sesiones disponibles.");
	                    return false;
	                }

	                // Paso 3: Actualizar el bono en la base de datos
	                try (PreparedStatement stmtActualizar = connection.prepareStatement(queryActualizarBono)) {
	                    stmtActualizar.setInt(1, nuevasSesiones);
	                    stmtActualizar.setDate(2, nuevaFechaPrimeraSesion); // Actualizar la fecha de la primera sesión si es necesario
	                    stmtActualizar.setDate(3, nuevaFechaCaducidad);
	                    stmtActualizar.setInt(4, bonoId);

	                    int rowsUpdated = stmtActualizar.executeUpdate();
	                    if (rowsUpdated <= 0) {
	                        System.out.println(" ERROR! No se pudo actualizar el bono.");
	                        return false;
	                    }
	                }
	            } else {
	                System.out.println(" ERROR! No se encontró el bono con ID: " + bonoId);
	                return false;
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println(" ERROR! No se pudo actualizar las sesiones del bono: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    } finally {
	        db.closeConnection();
	    }

	    return true;
	}
	
	
	/**
	 * Función que calcula si se ha excedido el plazo de 24 horas anterior a la reservas.
	 * @param fechaRecibida Fecha de la reserva que se quiere comprobar.
	 * @return Devuelve true si se excedió el plazo, y devuelve false si no se ha excedido el plazo. 
	 */
	public static boolean plazoExcedido(Date fechaRecibida) {

		Date fechaActual = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaRecibida);

        cal.add(Calendar.HOUR_OF_DAY, -24);

        return fechaActual.after(cal.getTime());
	}
	
	
	/**
	 * Función que muestra todos los detalles de las reservas futuras.
	 * @return codigo Devuelve un numero distinto dependiendo del error que haya habido. 
	 */
	public int listarReservasFuturas() {
        int codigo = 0;
        String query = properties.getProperty("select_futuras");
        
        DBConnection db = new DBConnection();
        connection = db.getConnection();
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); // Formato de fecha requerido
            
            System.out.println("\n───────────────────────────────────────");
            System.out.println("---------- Lista de Reservas ----------");
            System.out.println("───────────────────────────────────────");

            // Iterar sobre los resultados de la consulta
            while (rs.next()) {
                String idReserva = rs.getString("idReserva");
                String tipoReserva= rs.getString("tipoReserva");
                int usuarioId = rs.getInt("usuarioId");
                int pistaId = rs.getInt("pistaId");
                java.util.Date fechaHora = rs.getTimestamp("fechaHora");
                int duracion = rs.getInt("duracion");
                float precio = rs.getFloat("precio");
                float descuento = rs.getFloat("descuento");
                
                int numAdultos = rs.getInt("numAdultos");
                int numNinos = rs.getInt("numNinos");

                // Imprimir los datos de la reserva
                System.out.println("ID Reserva: " + idReserva);
                System.out.println("Tipo de reserva: " + tipoReserva);
                System.out.println("Usuario ID: " + usuarioId);
                System.out.println("Pista ID: " + pistaId);
                System.out.println("Fecha Reserva: " + sdf.format(fechaHora));
                System.out.println("Duración: " + duracion + " minutos");
                System.out.println("Precio: " + precio + " €");
                System.out.println("Descuento: " + descuento);
                
                if(tipoReserva=="ADULTOS" || tipoReserva=="FAMILIAR") {
                	System.out.println("Numero de adultos: " + numAdultos);
                }
                else if(tipoReserva=="INFANTIL" || tipoReserva=="FAMILIAR"){
                	System.out.println("Numero de niños: " + numNinos);
                	
                }
                System.out.println("───────────────────────────────────────");
            }
        } catch (SQLException e) {
            System.out.println("ERROR! No se pudo obtener las reservas futuras: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }

        return codigo;
    }
	
	/**
	 * Función que muestra todos los detalles de las reservas de un usuario cuya fecha está entre las dos fechas.
	 * @param vectorReserva Vector que contiene todos los detalles de las reservas buscadas.
	 * @param fechaInicio Fecha inicial del filtro de la búsqueda.
	 * @param fechaFinal Fecha final del filtro de la búsqueda.
	 * @param correoUser Correo del reservante.
	 * @return codigo Devuelve un numero distinto dependiendo del error que haya habido. 
	 */
	public int listarReservasEntreFechas(Vector<ReservaDTO> vectorReserva, Vector<String> vectorNombres, Date fechaInicio, Date fechaFinal, String correoUser) {
	    String query = properties.getProperty("select_entre_fechas");
	    DBConnection db = null;
	    Connection connection = null;
	    int usuarioId = buscarIdJugador(correoUser);
	    GestorPistas gestor= new GestorPistas();

	    try {
	        db = new DBConnection();
	        connection = db.getConnection();
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setDate(1, new java.sql.Date(fechaInicio.getTime()));
	            stmt.setDate(2, new java.sql.Date(fechaFinal.getTime()));
	            stmt.setInt(3, usuarioId);

	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    try {
	                        String tipoReserva = rs.getString("tipoReserva");
	                        int pistaId= rs.getInt("pistaId");
	                        if (tipoReserva.equals("FAMILIAR")) {
	                            vectorReserva.add(new ReservaFamiliarDTO(
	                                rs.getInt("usuarioId"),
	                                rs.getTimestamp("fechaHora"),
	                                rs.getInt("duracion"),
	                                rs.getInt("pistaId"),
	                                rs.getFloat("precio"),
	                                rs.getFloat("descuento"),
	                                rs.getInt("numAdultos"),
	                                rs.getInt("numNinos")
	                            ));
	                      
	                        } else if (tipoReserva.equals("INFANTIL")) {
	                            vectorReserva.add(new ReservaInfantilDTO(
	                                rs.getInt("usuarioId"),
	                                rs.getTimestamp("fechaHora"),
	                                rs.getInt("duracion"),
	                                rs.getInt("pistaId"),
	                                rs.getFloat("precio"),
	                                rs.getFloat("descuento"),
	                                rs.getInt("numNinos")
	                            ));
	                            
	                        } else {
	                            vectorReserva.add(new ReservaAdultosDTO(
	                                rs.getInt("usuarioId"),
	                                rs.getTimestamp("fechaHora"),
	                                rs.getInt("duracion"),
	                                rs.getInt("pistaId"),
	                                rs.getFloat("precio"),
	                                rs.getFloat("descuento"),
	                                rs.getInt("numAdultos")
	                            ));
	                        }
	                        vectorNombres.add(gestor.nombrePistas(pistaId));
	                    } catch (IllegalArgumentException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -2;
	    } finally {
	        if (db != null) {
	            db.closeConnection();
	        }
	    }

	    if (vectorReserva.isEmpty()) {
	        return -3;
	    }

	    return 0;
	}
	/**
	 * Función que modifica una reserva buscada por identificador único.
	 * @param idReserva Identificador único de la reserva a modificar.
	 * @param nuevaReserva Clase Reserva con todos los nuevos detalles modificados.
	 * @return codigo Devuelve un número distinto dependiendo del error que haya habido. 
	 *               -1: Error en la fecha (pasada o fuera de plazo).
	 *                0: Reserva no encontrada.
	 *                1: Reserva modificada correctamente.
	 */
	public int modificarReserva(int idReserva, ReservaDTO nuevaReserva) {
	    String query = properties.getProperty("modificar_reserva");
	    int codigo = -1;

	    DBConnection db = new DBConnection();
	    connection = db.getConnection();

	    try {
	        // Comprobación de fecha futura y dentro del plazo permitido
	        if (!esReservaFutura(nuevaReserva.getFechaHora())) {
	            return -1;
	        }
	        if (plazoExcedido(nuevaReserva.getFechaHora())) {
	            return -2;
	        }

	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            // Determinar el tipo de reserva
	            String tipoReserva = nuevaReserva instanceof ReservaInfantilDTO ? "INFANTIL" :
	                                 nuevaReserva instanceof ReservaFamiliarDTO ? "FAMILIAR" : "ADULTOS";

	            // Inicializar los valores de adultos y niños
	            Integer numNinos = null;
	            Integer numAdultos = null;

	            if (nuevaReserva instanceof ReservaInfantilDTO) {
	                numNinos = ((ReservaInfantilDTO) nuevaReserva).getNumNinos();
	            } else if (nuevaReserva instanceof ReservaFamiliarDTO) {
	                numNinos = ((ReservaFamiliarDTO) nuevaReserva).getNumNinos();
	                numAdultos = ((ReservaFamiliarDTO) nuevaReserva).getNumAdultos();
	            } else if (nuevaReserva instanceof ReservaAdultosDTO) {
	                numAdultos = ((ReservaAdultosDTO) nuevaReserva).getNumAdultos();
	            }

	            // Establecer los parámetros de la consulta SQL
	            statement.setInt(1, nuevaReserva.getUsuarioId());
	            statement.setTimestamp(2, new java.sql.Timestamp(nuevaReserva.getFechaHora().getTime()));
	            statement.setInt(3, nuevaReserva.getDuracion());
	            statement.setInt(4, nuevaReserva.getPistaId());
	            statement.setFloat(5, calcularPrecio(nuevaReserva.getDuracion()));
	            statement.setFloat(6, nuevaReserva.getDescuento());
	            statement.setString(7, tipoReserva);
	            
	            // Manejo de valores nulos en numNinos y numAdultos
	            if (numNinos != null) {
	                statement.setInt(8, numNinos);
	            } else {
	                statement.setNull(8, java.sql.Types.INTEGER);
	            }

	            if (numAdultos != null) {
	                statement.setInt(9, numAdultos);
	            } else {
	                statement.setNull(9, java.sql.Types.INTEGER);
	            }

	            statement.setInt(10, idReserva); // ID de la reserva que se va a modificar

	            // Ejecutar la actualización
	            int rowsUpdated = statement.executeUpdate();

	            if (rowsUpdated > 0) {
	                codigo = 0; // Reserva modificada correctamente
	            } else {
	                codigo = -3; // Reserva no encontrada
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println(" ERROR! Error al modificar la reserva en la base de datos: " + e.getMessage());
	        e.printStackTrace();
	        codigo = -1; // Error durante la modificación
	    } finally {
	        db.closeConnection();
	    }

	    return codigo;
	}
	
	
	/**
	 * Función que calcula si muestra todos los detalles de las reservas con una fecha y pista exacta.
	 * @param reserva Se almacenan los datos de la reserva si la encuentra.
	 * @param fechaBuscada Fecha de la reserva a filtrar.
	 * @param nombrePista Nombre de la pista a filtrar.
	 * @param correoUser Es el correo del usuario que solicita la búsqueda de la reserva.
	 * @param idReserva Rellena el id de la reserva encontrada.
	 * @return Devuelve el código de error de la función.
	*/
	public int listarReservasPorFechaYPista(Vector<ReservaDTO> vectorReserva, Date fechaBuscada, String nombrePista, String correoUser, AtomicInteger idReserva) {
		
		int pistaId = buscarIdPista(nombrePista);
		int userId = buscarIdJugador(correoUser);
		String query = properties.getProperty("buscar_fecha_pista");
	    
	    DBConnection db = new DBConnection();
	    connection = db.getConnection();
	    int counter= 0;
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
        	stmt.setInt(1, pistaId);
        	stmt.setDate(2, new java.sql.Date(fechaBuscada.getTime()));

            // Iterar sobre los resultados de la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                	counter++;
                    try {
                        String tipoReserva = rs.getString("tipoReserva");
                        int usuarioDB= rs.getInt("usuarioId");
                        
                        if(usuarioDB== userId) {
                        	
	                        if (tipoReserva.equals("FAMILIAR")) {
	                        	idReserva.set(rs.getInt("idReserva"));
	                            vectorReserva.add(new ReservaFamiliarDTO(
	                                rs.getInt("usuarioId"),
	                                rs.getTimestamp("fechaHora"),
	                                rs.getInt("duracion"),
	                                rs.getInt("pistaId"),
	                                rs.getFloat("precio"),
	                                rs.getFloat("descuento"),
	                                rs.getInt("numAdultos"),
	                                rs.getInt("numNinos")
	                            ));
	                        } else if (tipoReserva.equals("INFANTIL")) {
	                        	idReserva.set(rs.getInt("idReserva"));
	                            vectorReserva.add(new ReservaInfantilDTO(
	                                rs.getInt("usuarioId"),
	                                rs.getTimestamp("fechaHora"),
	                                rs.getInt("duracion"),
	                                rs.getInt("pistaId"),
	                                rs.getFloat("precio"),
	                                rs.getFloat("descuento"),
	                                rs.getInt("numNinos")
	                            ));
	                        } else {
	                        	idReserva.set(rs.getInt("idReserva"));
	                            vectorReserva.add(new ReservaAdultosDTO(
	                                rs.getInt("usuarioId"),
	                                rs.getTimestamp("fechaHora"),
	                                rs.getInt("duracion"),
	                                rs.getInt("pistaId"),
	                                rs.getFloat("precio"),
	                                rs.getFloat("descuento"),
	                                rs.getInt("numAdultos")
	                            ));
	                        }
                        }
                        
                        else {
                        	return -1;
                        }
                    } catch (IllegalArgumentException e) {
	                        e.printStackTrace();
                    }
                }
            }
        }
         catch (SQLException e) {
        	e.printStackTrace();
            return -3;
        } finally {
            db.closeConnection();
        }
        
        if(counter== 0) {
        	return -2;
        }
        
    	return 0;
        
	}
	
	
	/**
	 * Función que cancela una reserva si no se ha excedido el plazo de 24 horas antes.
	 * @param idReserva Identificador único de la reserva a cancelar.
	 * @return Devuelve true si consiguió borrar la reserva del fichero correctamente, y devuelve false si hubo algún error.
	 */
	public boolean cancelarReserva(int idReserva) {
		boolean eliminada= false;
		
		String queryBuscarReserva = properties.getProperty("buscar_reserva"); // Consulta para buscar la reserva
	    String queryEliminarReserva = properties.getProperty("eliminar_reserva"); // Consulta para eliminar la reserva
	    
	    DBConnection db = new DBConnection();
	    connection = db.getConnection();

	    try (PreparedStatement stmtBuscar = connection.prepareStatement(queryBuscarReserva)) {
	        // Establecer el parámetro para la consulta de búsqueda de la reserva
	        stmtBuscar.setInt(1, idReserva);

	        // Ejecutar la consulta para obtener la reserva
	        ResultSet rs = stmtBuscar.executeQuery();

	        if (rs.next()) {
	            // Obtener la fecha de la reserva
	            Date fechaReserva = rs.getTimestamp("fechaHora");

	            // Verificar si el plazo de 24 horas no ha sido excedido
	            if (!plazoExcedido(fechaReserva)) {
	                // Si el plazo no ha sido excedido, procedemos con la eliminación de la reserva

	                try (PreparedStatement stmtEliminar = connection.prepareStatement(queryEliminarReserva)) {
	                    // Establecer el parámetro para la consulta de eliminación
	                    stmtEliminar.setInt(1, idReserva);

	                    // Ejecutar la eliminación
	                    int filasAfectadas = stmtEliminar.executeUpdate();
	                    if (filasAfectadas > 0) {
	                        eliminada = true;  // Si la eliminación fue exitosa, se marca como verdadera
	                    }
	                }
	            } else {
	                System.out.println("ERROR! No se puede eliminar la reserva porque el plazo de 24 horas ha sido excedido.");
	            }
	        } else {
	            System.out.println("ERROR! No se encontró una reserva con el id proporcionado.");
	        }

	    } catch (SQLException e) {
	        System.out.println("ERROR! Error al ejecutar la consulta: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        // Cerrar la conexión a la base de datos
	        db.closeConnection();
	    }
	    return eliminada;
	}
	
	
	/**
	 * Función que obtiene el numero de sesiones restantes de un bono.
	 * @param bonoId Identificador único del bono a buscar.
	 * @return sesionesRestantes Es la cantidad de sesiones que le quedan al bono.
	 */
	public int obtenerSesionesRestantes(int bonoId) {
		String query = properties.getProperty("buscar_bono");
		int sesionesRestantes=0;
		
		DBConnection db = new DBConnection();
        connection = db.getConnection();
		
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bonoId);
            
            ResultSet rs= statement.executeQuery();
            
            if (rs.next()) {
            	sesionesRestantes = rs.getInt("sesiones"); 
            }
            
            
        } catch (SQLException e) {
            System.err.println("Error inserting pista: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        
	    return sesionesRestantes;
	}
	
	
	/**
	 * Verifica si ya existe una reserva para la misma pista y horario.
	 * @param nombrePista Nombre de la pista a reservar.
	 * @param fechaHora Día y hora de la reserva de la pista.
	 * @return Devuelve true si ya existe una reserva para la misma pista y horario, y false si no existe.
	 */
	private boolean existeReservaParaPistaYHora(String nombrePista, Date fechaHora) {
		String query = properties.getProperty("buscar_reserva_existente");
	
		DBConnection db = new DBConnection();
		Connection connection = db.getConnection();
	
		try (PreparedStatement statement = connection.prepareStatement(query)) {
		     // Establecer los parámetros
		     statement.setString(1, nombrePista);
		     statement.setTimestamp(2, new Timestamp(fechaHora.getTime())); // Convertir Date a Timestamp
		
		     // Ejecutar la consulta
		     ResultSet resultSet = statement.executeQuery();
		     if (resultSet.next()) {
		         int count = resultSet.getInt(1);
		         return count > 0; // Si ya existe una reserva para la pista y la hora
		     }
		 } catch (SQLException e) {
		     System.out.println("ERROR! Error al comprobar reservas existentes en la base de datos: " + e.getMessage());
		 } finally {
		     db.closeConnection();
		 }
		return false; // No existe una reserva
	}
	
	
	/**
	 * Verifica si la fecha de la reserva es una fecha futura.
	 * @param fechaReserva Fecha de la reserva a verificar.
	 * @return Devuelve true si la fecha es futura, y false si la fecha ya ha pasado.
	 */
	public boolean esReservaFutura(Date fechaReserva) {
	    Date fechaActual = new Date();
	    return fechaReserva.after(fechaActual);
	}

	
	/**
	 * Obtiene el tamaño de pistas del bono. 
	 * @param bonoId Es el identificador de bono.
	 * @return Devuelve el string del tamaño del bono.
	 */
	public String obtenerTamanoBono(int bonoId) {
		String tamanoBono= " ERROR!";
		String query= properties.getProperty("buscar_bono");
		
		DBConnection db = new DBConnection();
		connection = db.getConnection();
	    
	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        // Establecer el parámetro de la consulta
	        stmt.setInt(1, bonoId);

	        // Ejecutar la consulta
	        ResultSet rs = stmt.executeQuery();

	        // Verificar si se encontró el bono
	        if (rs.next()) {
	            // Obtener el tamaño de la pista del bono
	            tamanoBono = rs.getString("tipoPista");
	        } else {
	            // Si no se encuentra el bono
	            tamanoBono = "ERROR! Bono no encontrado.";
	        }

	    } catch (SQLException e) {
	        System.out.println("ERROR! Error al ejecutar la consulta: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        // Cerrar la conexión a la base de datos
	        db.closeConnection();
	    }
	    return tamanoBono;
	}
	
	
	
	public ReservaDTO obtenerReservaPorId(int idReserva) {
		String query= properties.getProperty("buscar_reserva");
		
		DBConnection db = new DBConnection();
		connection = db.getConnection();
		ReservaDTO reserva= null;
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        // Establecer el parámetro de la consulta
	        stmt.setInt(1, idReserva);

	        // Ejecutar la consulta
	        ResultSet rs = stmt.executeQuery();

	        // Verificar si se encontró el bono
	        if (rs.next()) {
	            // Obtener el tamaño de la pista del bono
	        	int usuarioId = rs.getInt("usuarioId");
	            int duracion= rs.getInt("duracion");
	            java.sql.Timestamp fechaReserva = rs.getTimestamp("fechaHora");
	            int pistaId= rs.getInt("pistaId");
	            float precio = rs.getFloat("precio");
	            float descuento= rs.getFloat("descuento");
	            String tipoReserva= rs.getString("tipoReserva");
	            int numNinos= rs.getInt("numNinos");
	            int numAdultos= rs.getInt("numAdultos");
	            
                if(tipoReserva.equals("ADULTOS")) {
                	reserva = new ReservaAdultosDTO(usuarioId, fechaReserva, duracion, pistaId, precio, descuento, numAdultos);
                }
                
                else if(tipoReserva.equals("FAMILIAR")) {
                	reserva = new ReservaFamiliarDTO(usuarioId, fechaReserva, duracion, pistaId, precio, descuento, numAdultos, numNinos);
                }
                
                else {
                	reserva = new ReservaInfantilDTO(usuarioId, fechaReserva, duracion, pistaId, precio, descuento, numNinos);
                }
	            
	        }

	    } catch (SQLException e) {
	        System.out.println("ERROR! Error al ejecutar la consulta: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        // Cerrar la conexión a la base de datos
	        db.closeConnection();
	    }

        return reserva; // Devolver null si no se encontró la reserva
    }
	
	
	public boolean mostrarBonos(String correoUser) {
		int jugador = buscarIdJugador(correoUser); // Obtener ID del jugador
		String query= properties.getProperty("select_user_bono");
		DBConnection db = new DBConnection();
        connection = db.getConnection();
        
        int count=0;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
        	stmt.setInt(1, jugador);
            ResultSet resultSet = stmt.executeQuery();
		     if (resultSet.next()) {
		         count++;
		     }
        }
        catch (SQLException e) {
            System.out.println("ERROR! No se pudo obtener las reservas futuras: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        
        if(count==0) {
        	return false;
        }
        
        return true;
		
	}
	
	public int buscarPistas(Vector<PistaDTO> vectorPistas, boolean isInterior, Date fechaHora) {
		String query = properties.getProperty("select_pistas_disponibles");
        DBConnection db = new DBConnection();
        connection = db.getConnection();

        if (vectorPistas == null) {
            // Error: El vector proporcionado es null
            return -1;
        }

        try (PreparedStatement statement = connection.prepareStatement(query)){
    		statement.setInt(1, isInterior ? 1 : 0);
            ResultSet resultSet = statement.executeQuery();

            // Limpiar el vector antes de llenarlo
            vectorPistas.clear();

            // Iterar sobre los resultados y agregarlos al vector
            while (resultSet.next()) {
                try {
                	String pistaName= resultSet.getString("nombre");
                	if(!existeReservaParaPistaYHora(pistaName,fechaHora)) {
                		PistaDTO.TamanoPista tamanoPista = PistaDTO.TamanoPista.valueOf(resultSet.getString("tamano").toUpperCase());
                		PistaDTO pista = new PistaDTO(
                				pistaName,
                				resultSet.getBoolean("estado"),
                				resultSet.getBoolean("tipo"),
                				tamanoPista,
                				resultSet.getInt("numMaxJugadores")
                				);
                		vectorPistas.add(pista);                		
                	}
                } catch (IllegalArgumentException e) {
                    // Error: Formato de datos inválido en el ResultSet
                    System.err.println("Error processing pista data: " + e.getMessage());
                    return -2;
                }
            }

            if (vectorPistas.isEmpty()) {
                // No se encontraron pistas
                return -3;
            }

            // Operación exitosa
            return 0;

        } catch (SQLException e) {
            // Error en la consulta SQL
            System.err.println("Error listing pistas: " + e.getMessage());
            e.printStackTrace();
            return -4;
        } finally {
            db.closeConnection();
        }
	}

	
	/**
	 * Elimina una reserva específica de la base de datos. Esta función está diseñada para ser utilizada por un administrador.
	 *
	 * @param idReserva Identificador de la reserva a eliminar.
	 * @return boolean True si la eliminación fue exitosa, False en caso contrario.
	 */
	public int eliminarReserva(int idReserva) {
	    int resultado = -1;

	    // Consulta para eliminar la reserva (definida en el archivo de propiedades)
	    String queryEliminarReserva = properties.getProperty("eliminar_reserva");

	    DBConnection db = new DBConnection();
	    connection = db.getConnection();

	    try (PreparedStatement stmtEliminar = connection.prepareStatement(queryEliminarReserva)) {
	        // Establecer el parámetro para la consulta de eliminación
	        stmtEliminar.setInt(1, idReserva);

	        // Ejecutar la consulta de eliminación
	        int filasAfectadas = stmtEliminar.executeUpdate();
	        if (filasAfectadas > 0) {
	            resultado = 0; // Si la eliminación fue exitosa
	        } else {
	            resultado = -1; //No se ha eliminado la reserva.
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        resultado = -2;
	    } finally {
	        // Cerrar la conexión a la base de datos
	        db.closeConnection();
	    }

	    return resultado;
	}
	
	
	
}