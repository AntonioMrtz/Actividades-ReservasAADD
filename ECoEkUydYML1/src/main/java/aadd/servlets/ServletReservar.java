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

import aadd.bean.Activity;
import aadd.bean.User;


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
		ServletContext app = getServletConfig().getServletContext();
		
		
		
		// En caso de no parametros -> pantalla donde elegir reservar o ver reservas
		
		if(request.getParameterMap().containsKey("actividad")==false && request.getParameterMap().containsKey("ver_reservas")==false) {
			
			request.getRequestDispatcher("reservar_actividad.html").include(request, response);
			
		}
		
		
		
		// hay parametro ver_reservas -> ver reservas
		
		
		else if(request.getParameterMap().containsKey("ver_reservas")) { //mostrar reservas usuario
			
			// traemos la base de datos
			HashMap<Activity, Integer> database=(HashMap<Activity, Integer>) app.getAttribute("database");
			
			int flag=0; // para comprobar si no se obtiene ninguna actividad con la condicion indicada
			
			if(database!=null) {
				
				for(Activity a : database.keySet()) {
					
					for(User us:a.getUsers()) {
						
						if(us.getCode()==u.getCode()) {
							
							flag=1;
							
							if(a.getDescription()!="") {
								
								out.println(a.getActivityName()+" "+a.getDescription());
								
							}
							else {
								
								out.println(a.getActivityName());
							}
							
							
						}
					}
					
				}
				
				if(flag==0) {
					
					out.println("No hay reservas");
				}
				
			}else {
				
				out.println("Sin reservas ");
			}
			
			
		}
		else{ 	// pantalla para reservar
		
			
			HashMap<Activity, Integer> database=(HashMap<Activity, Integer>) app.getAttribute("database");
			
			String code_actividad=request.getParameter("actividad");
			//out.println(code_actividad);
			int flag=0;
			
			
			for(Activity a : database.keySet()) {
				
				if(a.getCode().equals(code_actividad) && a.getAvailableSpots()>0 && !a.getUsers().contains(u)) {
					
					a.addUser(u);
					a.decreaseAvailableSpots();
					
					
					database.put(a,a.getAvailableSpots());
					
					//database.remove(a);
					
					app.setAttribute("database", database);
					
					//out.println(database);
					
					out.println("Actividad reservada o modificada con éxito");
					flag=1;
					break;
				}
					
				
			}
			
			if(flag==0) {
				
				out.println("Reserva no disponible");
			}
			
			
		}
	
		
		
	
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
