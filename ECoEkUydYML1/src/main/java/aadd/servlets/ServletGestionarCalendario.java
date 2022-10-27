package aadd.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.client.MongoCursor;

import aadd.bean.Activity;
import dao.ActivityDAO;

@WebServlet(urlPatterns = { "/calendario" })
public class ServletGestionarCalendario extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private HashMap<Activity,Integer> database;
	
	
	
	public HashMap<Activity,Integer> getDatabase(){
		
		return new HashMap<>(database);
	}
	



    public ServletGestionarCalendario() {
        super();
        
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
	

		// VER RESERVAS DEL USUARIO
		
		if(request.getParameterMap().containsKey("ver_reservas")) {
			
			request.getRequestDispatcher("ver_actividades.html").include(request, response);
		}
		
		
		
		// CONSULTA ACTIVIDADES
		
		else if (request.getParameterMap().containsKey("mes") && request.getParameterMap().containsKey("anyo")) { 

			
			if(request.getParameter("anyo")=="") {
				
				request.getRequestDispatcher("ver_actividadeserror.html").include(request, response);
			}
			
			int anyo = Integer.parseInt(request.getParameter("anyo"));
			int mes = Integer.parseInt(request.getParameter("mes"))+1;

			
				
			//check actividades del mes
			

			MongoCursor<Activity> cursor = ActivityDAO.getActivityDAO().checkActivities(anyo,mes);

			if (cursor.hasNext()) {

				try {
					while (cursor.hasNext()) {
						
						Activity activity=cursor.next();
						
						if(!activity.getDescription().isEmpty()) {
							
							out.println(activity.getActivityName() + " | " + activity.getCode() + " | "+ activity.getDescription() + "\n");
							
						}
						else {
							
						out.println(activity.getActivityName() + " | " + activity.getCode() + "\n");
							
						}

					}
				} finally {
					cursor.close();
				}

			}

			else {

				out.println("Sin actividades");

			}
				
			
			
			


		}

		
		// REGISTRAR ACTIVIDAD
		
		else if(request.getParameterMap().containsKey("nombre")){

			int error_flag = 0;

			Activity activity = null;

			// tratamiento de fecha y hora

			String fecha = request.getParameter("fecha");

			String hora;
			String minutos;

			if (request.getParameter("hora").isEmpty()) {

				hora = "00";
				minutos = "00";
			} else {
				hora = request.getParameter("hora");
				minutos = hora.split(":")[1];
				hora = hora.split(":")[0];

			}

			Date date = null;
			

			if (!fecha.isEmpty()) {

				fecha += " " + hora + ":" + minutos;

				try {
					date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fecha);
				} catch (ParseException e) {
					e.printStackTrace();
				}

			} else {
				error_flag += 1;

			}
		
				
			LocalDateTime local_date_time =  LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDay()).atTime(Integer.parseInt(hora), Integer.parseInt(minutos));			
							

			// tratamiento datos obligatorios
			if (request.getParameter("codigo").isEmpty() || request.getParameter("nombre").isEmpty()
					|| request.getParameter("plazas").isEmpty() || request.getParameter("tipo").isEmpty()) {

				error_flag += 1;
			}

			else {

								
				activity = new Activity(request.getParameter("codigo"), local_date_time,
						Integer.parseInt(request.getParameter("plazas")), request.getParameter("nombre"),
						request.getParameter("descripcion"), request.getParameter("tipo"));	
			}


			if (error_flag > 0) {

				request.getRequestDispatcher("indexerror.html").include(request, response);
				
				
			} else if(ActivityDAO.getActivityDAO().crearActividad(activity)){

				out.println("Actividad creada | Codigo : "+activity.getCode());

			}

		}
		
		
		else {
			
			request.getRequestDispatcher("index.html").include(request, response);
		}

	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
