package aadd.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import aadd.bean.Activity;
import aadd.bean.User;

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


	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletContext app = getServletConfig().getServletContext();
		PrintWriter out = response.getWriter();
		
	

		
		
		if(request.getParameterMap().containsKey("ver_reservas")) {
			
			request.getRequestDispatcher("ver_actividades.html").include(request, response);
		}
		
		
		else if (request.getParameterMap().containsKey("mes") && request.getParameterMap().containsKey("anyo")) { // CONSULTA
																										// ACTIVIDADES

			
			if(request.getParameter("anyo")=="") {
				
				request.getRequestDispatcher("ver_actividadeserror.html").include(request, response);
			}
			
			int anyo = Integer.parseInt(request.getParameter("anyo"));
			int mes = Integer.parseInt(request.getParameter("mes"))+1;

			
			//out.println(mes+" "+anyo); out.println();
			 

			database = (HashMap<Activity, Integer>) app.getAttribute("database");
			
			/*out.println(database);
			out.println(database.keySet());*/
			
			if(database!=null) {
				
				
				for (Activity a : database.keySet()) {
					
//					out.println(mes);
//					out.println(a.getStartDate().getMonth()+1);
//					out.println(a.getStartDate().getYear()+1900);
//					out.println(anyo);
//					out.println();
					
					//out.printl);
					//out.println(a.getStartDate().getYear()+1900);
					//out.println(a.getStartDate().getMonth()+1);
					
					if (a.getStartDate().getMonth() + 1 == mes ) {
						
						
						if(a.getStartDate().getYear() + 1900 == anyo) {
							
							//out.println(a.getActivityName() + ", code = " + a.getCode());
							out.println(a.getActivityName()+" | plazas "+a.getAvailableSpots());
							
							if (a.getDescription()!="") {
								out.println(a.getDescription());
								
							}
							
							//out.println(a.getAvailableSpots() + " plazas");
							
							out.println();
							
						}
						
						
					}
					
				}
				
			}
			
			else{

				out.println("Sin actividades");
				
			}

		}

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
					 //out.println(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}

			} else {
				error_flag += 1;

			}

			// tratamiento datos obligatorios

			if (request.getParameter("codigo").isEmpty() || request.getParameter("nombre").isEmpty()
					|| request.getParameter("plazas").isEmpty() || request.getParameter("tipo").isEmpty()) {

				// request.getRequestDispatcher("indexerror.html").include(request, response);
				error_flag += 1;
			}

			else {

				activity = new Activity(request.getParameter("codigo"), date,
						Integer.parseInt(request.getParameter("plazas")), request.getParameter("nombre"),
						request.getParameter("descripcion"), request.getParameter("tipo"));

				// out.println("actividad creada correctamente");
				// out.println(activity);

			}

			// reenviar a index_errores o guardar en DB

			if (error_flag > 0) {

				request.getRequestDispatcher("indexerror.html").include(request, response);
			} else {

				int activity_exists = 0;

				database = (HashMap<Activity, Integer>) app.getAttribute("database");

				if (database == null) {

					database = new HashMap<Activity, Integer>();
					app.setAttribute("database", database);
				}

				for (Activity a : database.keySet()) {

					if (a.getCode().equals(activity.getCode())) {

						activity_exists = 1;

					}

				}

				if (activity_exists == 0) {

					database.put(activity, activity.getAvailableSpots());
					app.setAttribute("database", database);
					//out.println(database);
					out.println("Actividad creada con código "+request.getParameter("codigo"));

				}

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
