package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.bson.Document;
import org.junit.Before;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

import aadd.bean.Activity;
import dao.ActivityDAO;

public class Test {

	@Before
	public void setUp() {
		
		if (!ActivityDAO.getActivityDAO().checkActivityExists("prueba1")) {

			Activity a1 = new Activity("prueba1", LocalDateTime.now().plusMonths(1), 2, "actividad1", "descripcion",
					"artesania");
			ActivityDAO.getActivityDAO().crearActividad(a1);

		}
		
		if (!ActivityDAO.getActivityDAO().checkActivityExists("prueba2")) {

			Activity a2 = new Activity("prueba2", LocalDateTime.now().plusMonths(1), 0, "actividad2", "descripcion",
					"artesania");
			ActivityDAO.getActivityDAO().crearActividad(a2);

		}
		
		if (!ActivityDAO.getActivityDAO().checkActivityExists("prueba3")) {

			Activity a3 = new Activity("prueba3", LocalDateTime.now().minusMonths(1), 2, "actividad3", "descripcion",
					"cine");
			ActivityDAO.getActivityDAO().crearActividad(a3);

		}
		
		
		
	}

	@org.junit.Test
	public void checkAvailableActivities() {

		ArrayList<Activity> l = new ArrayList<>();

		MongoCursor<Activity> cursor = ActivityDAO.getActivityDAO().getAvailableActivities();

		if (cursor.hasNext()) {

			try {
				while (cursor.hasNext()) {

					Activity activity = cursor.next();
					System.out.println(activity);

					l.add(activity);

				}
			} finally {
				cursor.close();
			}

		}
		
		assertTrue(l.size()==1 && l.get(0).getCode().equals("prueba1"));
		
	}
	
	@org.junit.Test
	public void checkCreatedActivities() {
		
		
	}
	
	
	@org.junit.Test
	public void check() {
		
		AggregateIterable<Document> resp= ActivityDAO.getActivityDAO().getSpotsByMonthActivityType();
		
		for(Document d: resp) {
			
			System.out.println(d);
			
			if(( (Document)d.get("_id") ).get("month").equals(LocalDateTime.now().getMonth().minus(1))) {
				
				assertEquals("cine", ((Document)d.get("_id") ).get("type"));
				assertEquals(d.get("availableSpots"), 2);
				assertEquals(d.get("numActivities"), 2);
				
				
				
				assertTrue(((ArrayList)d.get("nameActivities")).contains("actividad2"));
				assertTrue(((ArrayList)d.get("nameActivities")).contains("actividad1"));

				
			}
			
			else if(( (Document)d.get("_id") ).get("month").equals(LocalDateTime.now().getMonth().plus(1))) {
				
				assertEquals("artesania", ((Document)d.get("_id") ).get("type"));
				assertEquals(d.get("availableSpots"), 2);
				assertEquals(d.get("numActivities"), 1);

				assertTrue(((ArrayList)d.get("nameActivities")).contains("actividad3"));

				
			}
			
			
			//System.out.println(  ( (Document)d.get("_id") ).get("month")  );
			//System.out.println(d.get("availableSpots"));
		}
		
		
	}

	
}
