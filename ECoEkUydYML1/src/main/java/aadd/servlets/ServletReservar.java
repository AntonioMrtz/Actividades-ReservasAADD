package aadd.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mongodb.client.MongoCursor;

import aadd.bean.Activity;
import aadd.bean.User;
import dao.ActivityDAO;


@WebServlet("/reservar")
public class ServletReservar extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private int current_user=0;

    public ServletReservar() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		HttpSession sesion = request.getSession(false);
		
		if (sesion == null) {
		    // Session is not created.
			
			synchronized (this) {
				
				//out.println("entro "+current_user+1);
				current_user+=1;
				sesion = request.getSession();
				sesion.setAttribute("user", new User(current_user));

			}
			
				
		}
		
		else { // aqui se entra en caso de que la sesion se haya creado en la seccion de calendario por lo que no se inicializó el id
			
			if(sesion.getAttribute("user")==null) {
				
				current_user+=1;
				sesion = request.getSession();
				sesion.setAttribute("user", new User(current_user));
			}
		}
		
		User u = (User) sesion.getAttribute("user");
		
		
		
		// En caso de no parametros -> pantalla donde elegir reservar o ver reservas
		
		if(request.getParameterMap().containsKey("actividad")==false && request.getParameterMap().containsKey("ver_reservas")==false) {
			
			request.getRequestDispatcher("reservar_actividad.html").include(request, response);
			
		}
	
		else if(request.getParameterMap().containsKey("ver_reservas")) { //mostrar reservas usuario
			
			
			
			MongoCursor<Activity> cursor = ActivityDAO.getActivityDAO().checkReservas(u.getCode());

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

				out.println("No hay reservas");

			}
			
		
		}
		else{ 	// pantalla para reservar
		
			
			
			String code_actividad=request.getParameter("actividad");
			
			if(!ActivityDAO.getActivityDAO().reservar(code_actividad,u.getCode())) {
				
				
				out.println("Reserva no disponible");
				
			}
			
			else {
				
				out.println("Reserva hecha");
			}
			
		
			
		}
	
		
		
	
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
