package es.uco.pw.servlets.user;

import javax.servlet.*;
import javax.servlet.http.*;

import es.uco.pw.business.DTOs.ReservaDTO;
import es.uco.pw.business.DTOs.PistaDTO;
import es.uco.pw.business.DTOs.PistaDTO.TamanoPista;
import es.uco.pw.business.DTOs.ReservaInfantilDTO;
import es.uco.pw.business.DTOs.ReservaFamiliarDTO;
import es.uco.pw.business.DTOs.ReservaAdultosDTO;
import es.uco.pw.business.Gestores.GestorReservas;
import es.uco.pw.business.Gestores.GestorPistas;
import es.uco.pw.data.DAOs.ReservaDAO;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  @author Antonio Diaz Barbancho
 *  @author Carlos Marín Rodríguez 
 *  @author Carlos De la Torre Frias (GM2)
 *  @author Daniel Grande Rubio (GM2)
 *  @since 12-10-2024
 *  @version 1.0
 */

/**
 * Controlador que gestiona la funcionalidad de modificar reservas.
 */
public class ModificarReservaController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Maneja las solicitudes HTTP GET.
     * 
     * @param request el objeto {@link HttpServletRequest} que contiene la solicitud del cliente.
     * @param response el objeto {@link HttpServletResponse} que contiene la respuesta al cliente.
     * @throws ServletException si ocurre un error en el despacho de la solicitud.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirigir a la página de gestión de bonos.
        RequestDispatcher dispatcher = request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp");
        dispatcher.forward(request, response);
    }


    /**
     * Maneja las solicitudes HTTP POST.
     * @param request el objeto {@link HttpServletRequest} que contiene la solicitud del cliente.
     * @param response el objeto {@link HttpServletResponse} que contiene la respuesta al cliente.
     * @throws ServletException si ocurre un error en el despacho de la solicitud.
     * @throws IOException si ocurre un error de entrada/salida.
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        // Obtener los parámetros del formulario
			String action = request.getParameter("action");
			
			if (action == null) {
		        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no especificada.");
		        return;
		    }

		    switch (action) {
		        case "buscar":
		            buscarReserva(request, response);
		            break;
		        case "modificar":
		        	modificarReserva(request, response);
		            break;
		        default:
		            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida.");
		            break;
		    }
	}
	
	private void buscarReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String correoUser = request.getParameter("correoUser") != null ? request.getParameter("correoUser").trim() : "";
    	String nombrePista = request.getParameter("nombrePista") != null ? request.getParameter("nombrePista").trim() : "";
    	String fechaStr = request.getParameter("fechaBuscar") != null ? request.getParameter("fechaBuscar").trim() : "";
        
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        java.util.Date fechaHora = null;
        
        try {
            if (!fechaStr.isEmpty()) {
            	fechaHora = sdf.parse(fechaStr);
            }
        } catch (ParseException e) {
            request.setAttribute("error", "Formato de fecha incorrecto. Use el formato correcto: yyyy-MM-dd'T'HH:mm.");
            request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
            return;
        }

        try {
	        // Lógica de consulta a la base de datos (simulada aquí)
	        GestorReservas gestor = new GestorReservas();
	        Vector<ReservaDTO> vectorReservas = new Vector<>();
	        
	        AtomicInteger idReserva= new AtomicInteger(-1);
	        int resultado= gestor.listarReservasPorFechaYPista(vectorReservas, fechaHora, nombrePista, correoUser, idReserva);
	        
	
	        switch (resultado) {
                case 0: // Reservas encontradas
                    request.setAttribute("reservas", vectorReservas);
                    request.setAttribute("nombre", nombrePista);
                    HttpSession session = request.getSession();
                    session.setAttribute("idReserva", idReserva.get());
                    request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
                    break;
                case -1: // Se encuentra una reserva pero no es del usuario.
                    request.setAttribute("error-buscar", "No existe ninguna reserva tuya para esa fecha.");
                    request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
                    break;
                case -2: // No existe ninguna reserva para ese día
                    request.setAttribute("error-buscar", "No existe ninguna reserva tuya para esa fecha.");
                    request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
                    break;
                case -3: // Error en la base de datos
                    request.setAttribute("error-buscar", "Error al intentar a la base de datos.");
                    request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
                    break;
                default: // Error desconocido
                    request.setAttribute("error-buscar", "Ha ocurrido un error desconocido.");
                    request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
        }
	    
        } catch (Exception e) {
        	e.printStackTrace();
            request.setAttribute("error", "Error en el servidor. Inténtelo más tarde.");
            request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
        }
	}
	
	private void modificarReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String duracionStr = request.getParameter("nuevaDuracion") != null ? request.getParameter("nuevaDuracion").trim() : "";
	    String nuevaFechaStr = request.getParameter("NuevaFechaHora") != null ? request.getParameter("NuevaFechaHora").trim() : "";
	    String pistaName = request.getParameter("nuevoNombre") != null ? request.getParameter("nuevoNombre").trim() : "";
	    
	    String numNinosStr = request.getParameter("numNinos") != null ? request.getParameter("numNinos").trim() : "";
	    String numAdultosStr = request.getParameter("numAdultos") != null ? request.getParameter("numAdultos").trim() : "";
	    int idReserva = (int) request.getSession().getAttribute("idReserva");
	    
	    GestorReservas gestor = new GestorReservas();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
	    java.util.Date nuevaFecha = null;     
	    
	    ReservaDTO datosReserva = gestor.obtenerReservaPorId(idReserva);
	    
	    int solucion= 0;
	    GestorPistas gestorPista= new GestorPistas();
	    PistaDTO pista= new PistaDTO();
	    
	    if(!pistaName.isEmpty()) {
	    	pista= gestorPista.findPistaByNombre(pistaName);
	    	// Llamar al método getTamanoPista() y comparar con un String
	        TamanoPista tamano = pista.getTamanoPista();
	        String tamanoStr = tamano.name();
	        
	        // Convertir a String usando name()
	    	if (datosReserva instanceof ReservaInfantilDTO) {
                if(tamanoStr.equals("ADULTOS") || tamanoStr.equals("TRES_VS_TRES")) {
                	solucion= -5;
                }
                else{
                	datosReserva.setPistaId(gestorPista.idPistaByNombre(pista.getNombre()));
                }
                
            } else if (datosReserva instanceof ReservaFamiliarDTO) {
            	if(tamanoStr.equals("ADULTOS")) {
                	solucion= -5;
                }
				else{
					datosReserva.setPistaId(gestorPista.idPistaByNombre(pista.getNombre()));
                }
            	
            } else {
            	if(tamanoStr.equals("MINIBASKET") || tamanoStr.equals("TRES_VS_TRES")) {
                	solucion= -5;
                }
    			else{
    				datosReserva.setPistaId(gestorPista.idPistaByNombre(pista.getNombre()));
                }
            }
	    }
	    
	    else {
	    	pista= gestorPista.findPistaByNombre(gestorPista.nombrePistas(datosReserva.getPistaId()));
	    }
	    
	    // Validación del número de niños
	    if (!numNinosStr.isEmpty() && (datosReserva instanceof ReservaInfantilDTO || datosReserva instanceof ReservaFamiliarDTO)) {
	        try {
	            int numNinos = Integer.parseInt(numNinosStr);
	            //Se comprueba si sobrepasa el límite de jugadores cuando se introduce una nueva cantidad de participantes.
	            if (datosReserva instanceof ReservaInfantilDTO) {
	            	if(numNinos > pista.getMaxJugadores()) {
	            		solucion= -6;
	            	}
	            	else {
	            		((ReservaInfantilDTO) datosReserva).setNumNinos(numNinos);	            		
	            	}
	            } else if (datosReserva instanceof ReservaFamiliarDTO) {
	                ((ReservaFamiliarDTO) datosReserva).setNumNinos(numNinos);
	            }
	        } catch (NumberFormatException e) {
	            request.setAttribute("error-modificar", "Número de niños no válido.");
	            request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
	            return;
	        }
	    }
	    

	    // Validación del número de adultos
	    if (!numAdultosStr.isEmpty() && (datosReserva instanceof ReservaAdultosDTO || datosReserva instanceof ReservaFamiliarDTO)) {
	        try {
	            int numAdultos = Integer.parseInt(numAdultosStr);
	            if (datosReserva instanceof ReservaAdultosDTO) {
	            	//Se comprueba si sobrepasa el límite de jugadores cuando se introduce una nueva cantidad de participantes.
	            	if(numAdultos > pista.getMaxJugadores()) {
	            		solucion= -6;
	            	}
	            	else {
	            		((ReservaAdultosDTO) datosReserva).setNumAdultos(numAdultos);	            		
	            	}
	            } else if (datosReserva instanceof ReservaFamiliarDTO) {
	            	//Se comprueba si sobrepasa el límite de jugadores cuando se introduce una nueva cantidad de participantes.
	            	if(numAdultos + ((ReservaFamiliarDTO) datosReserva).getNumNinos() > pista.getMaxJugadores()) {
	            		solucion= -6;
	            	}
	            	else {
	            		((ReservaFamiliarDTO) datosReserva).setNumAdultos(numAdultos);
	            	}
	            }
	        } catch (NumberFormatException e) {
	            request.setAttribute("error-modificar", "Número de adultos no válido.");
	            request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
	            return;
	        }
	    }
	    
	    //Validación del número de participantes SI SE CAMBIA LA PISTA PERO NO EL NÚMERO DE PARTICIPANTES
	    if (numAdultosStr.isEmpty() && datosReserva instanceof ReservaAdultosDTO) {
        	//Se comprueba si sobrepasa el límite de jugadores.
        	if(((ReservaAdultosDTO) datosReserva).getNumAdultos() > pista.getMaxJugadores()) {
        		solucion= -6;
        	}
        } else if (numAdultosStr.isEmpty() && numNinosStr.isEmpty() && datosReserva instanceof ReservaFamiliarDTO) {
        	//Se comprueba si sobrepasa el límite de jugadores.
        	if(((ReservaFamiliarDTO) datosReserva).getNumAdultos() + ((ReservaFamiliarDTO) datosReserva).getNumNinos() > pista.getMaxJugadores()) {
        		solucion= -6;
        	}
        }
        else if (numNinosStr.isEmpty() && datosReserva instanceof ReservaInfantilDTO) {
        	//Se comprueba si sobrepasa el límite de jugadores.
        	if(((ReservaInfantilDTO) datosReserva).getNumNinos() > pista.getMaxJugadores()) {
        		solucion= -6;
        	}
        }
	    
	    // Validación de la duración
	    if(!duracionStr.isEmpty()) {
	    	try {
	    		int duracion = Integer.parseInt(duracionStr);
	    		datosReserva.setDuracion(duracion);
	    	} catch (NumberFormatException e) {
	    		request.setAttribute("error-modificar", "Duración no válida.");
	    		request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
	    		return;
	    	}
	    }
	    // Validación de la nueva fecha
	    if(ReservaDAO.plazoExcedido(datosReserva.getFechaHora())) {
	    	solucion= -4;
	    }
	    else {
	    	if (!nuevaFechaStr.isEmpty()) {
	    		try {
	    			nuevaFecha = sdf.parse(nuevaFechaStr);
	    			datosReserva.setFechaHora(nuevaFecha);
	    		} catch (ParseException e) {
	    			request.setAttribute("error-modificar", "Formato de fecha incorrecto. Use el formato correcto: yyyy-MM-dd'T'HH:mm.");
	    			request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
	    			return;
	    		}
	    	}	    	
	    }

	    try {
	    	if(solucion== 0) {
	    		// Consultar reservas a través del Gestor
	    		solucion= gestor.modificarReserva(idReserva, datosReserva);	    		
	    	}

	        switch (solucion) {
	            case 0: // Reserva modificada
	                response.sendRedirect(request.getContextPath() + "/usermenu/gestionreservas/modificar/exito");
	                break;
	            case -1: // Error genérico
	                request.setAttribute("error-modificar", "No se puede aplicar una fecha pasada a la reserva.");
	                request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
	                break;
	            case -2: // Error en la base de datos
	                request.setAttribute("error-modificar", "No se puede cambiar la fecha de la reserva  un plazo menor de 24 horas.");
	                request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
	                break;
	            case -3: // Sin reservas
	                request.setAttribute("error-modificar", "No se ha encontrado la reserva.");
	                request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
	                break;
	            case -4: // Se agotó el plazo de modificación
	                request.setAttribute("error-modificar", "No se puede modificar una reserva 24 horas antes de su realización.");
	                request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
	                break;
	            case -5: // La pista no es del mismo tipo que la reserva
	                request.setAttribute("error-modificar", "El tipo de pista debe ser compatible con el tipo de reserva.");
	                request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
	                break;
	            case -6: // Se ha sobrepasado el límite de jugadores en la modificación
	                request.setAttribute("error-modificar", "El número de participantes no puede superar al del límite de la pista.");
	                request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
	                break;
	            default: // Error desconocido
	                request.setAttribute("error-modificar", "Ha ocurrido un error desconocido.");
	                request.getRequestDispatcher("/MVC/Views/user/modificarReserva.jsp").forward(request, response);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("error", "Error en el servidor. Inténtelo más tarde.");
	        request.getRequestDispatcher("/MVC/Views/common/error.jsp").forward(request, response);
	    }
	}

}