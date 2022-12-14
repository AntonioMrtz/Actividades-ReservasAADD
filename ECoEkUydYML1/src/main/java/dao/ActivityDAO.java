package dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.BsonField;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;

import aadd.bean.Activity;

public class ActivityDAO extends MongoCodecDAO<Activity> {

    private static ActivityDAO actividadDAO;

    public static ActivityDAO getActivityDAO() {
        if (actividadDAO == null) {
        	actividadDAO = new ActivityDAO();
        }
        return actividadDAO;
    }

    @Override
    public void createCollection() {
        collection = db.getCollection("activity", Activity.class).withCodecRegistry(defaultCodecRegistry);
        collection_nocodec = db.getCollection("activity");
    }
    
    public boolean checkActivityExists(String code) {
    	
    	Bson query=Filters.eq("_id",code);
    	
    	long resultados  = collection.countDocuments(query);
    	    	
    	return resultados>0;
    	
    }
    
    public boolean crearActividad(Activity actividad) {
    	
    	if(checkActivityExists(actividad.getCode())) {
    		    		
    		return false;
    	}
    	
    	collection.insertOne(actividad);
    	    	
    	return true;
    }
    
    
    public boolean checkCanReservar(String code,Integer user_code) {
    	    	
    	Bson query_id=Filters.eq("_id",code);
    	Bson query_plazas=Filters.gt("availableSpots", 0);
    	Bson query_user=Filters.nin("users",user_code);
    	
    	    	
    	Bson query= Filters.and(query_id,query_plazas,query_user);
    	
    	long resultados  = collection.countDocuments(query);
    	
    	return resultados>0;
    	
    }
    
    public boolean reservar(String code,Integer user_code) {
    	
    	if(checkActivityExists(code) && checkCanReservar(code,user_code)) {
    		
    		Bson query_id=Filters.eq("_id",code);
    		
    		Bson query_user= Updates.push("users",user_code);
    		Bson query_spots= Updates.inc("availableSpots", -1);
    		
    		Bson query = Updates.combine(query_user,query_spots);
    	
    		
    		collection.findOneAndUpdate(query_id, query);

    		
    		return true;
    	}

    	
    	return false;
    }
    
    public MongoCursor<Activity> checkReservas(Integer user_code) {
    	
    	Bson query_id=Filters.in("users",user_code);
    	Bson query_fields=Projections.fields(Projections.include("activityName","description"));
    	
    	//collection.find(query_id).projection(query_fields);
    	
    	return collection.find(query_id).projection(query_fields).iterator();
    	
    	//return collection.find(Filters.eq("_id","12")).iterator();
    	//return collection.find().iterator();
    	
    }

	public MongoCursor<Activity> checkActivities(Integer year,Integer month) {
		
		LocalDateTime l1 = LocalDateTime.of(year,month, 1, 0, 0);
		
		LocalDateTime l2;
		
		if(month==12) {
			l2 = LocalDateTime.of(year+1, 1, 1, 0, 0);			
		}
		else {
			
			l2 = LocalDateTime.of(year, month+1, 1, 0, 0);
			
		}
		
		
		Bson query_date_start=Filters.gte("startDate", l1);
		Bson query_date_end=Filters.lt("startDate", l2);
		
		Bson query=Filters.and(query_date_end,query_date_start);
		
		return collection.find(query).iterator();
		

		
	}
	
	public MongoCursor<Activity> getAvailableActivities() {
		
		
		LocalDateTime current_date= LocalDateTime.now();
		
		Bson query_date=Filters.gt("startDate",current_date);
		Bson query_spots=Filters.gt("availableSpots",0);
		
		Bson query=Filters.and(query_date,query_spots);
		
		return collection.find(query).iterator();
		
	}
	
	
	public AggregateIterable<Document> getSpotsByMonthActivityType() {
		

		Bson project =Aggregates.project(new Document("activityName",1).append("availableSpots", 1).append("type",1).append("year",new Document("$year","$startDate")).append("month",new Document("$month","$startDate")));
		Bson matchStage = Aggregates.match(Filters.eq("year",2022));
		List<BsonField> acumuladores = new ArrayList<BsonField>();
		acumuladores.add(Accumulators.sum("availableSpots","$availableSpots"));
		acumuladores.add(Accumulators.sum("numActivities",1));	
		acumuladores.add(Accumulators.addToSet("nameActivities","$activityName"));
		Document fields = new Document("month","$month").append("type", "$type");
	    Bson groupStage = Aggregates.group(fields, acumuladores);
		
		
		return collection_nocodec.aggregate(Arrays.asList(project,matchStage,groupStage));
		
		
	}
	
	public List<Activity> getAllActivities(){
		
		ArrayList<Activity> actividades= new ArrayList<>();
		
		
		MongoCursor<Activity> cursor = ActivityDAO.getActivityDAO().getAvailableActivities();
		
		if (cursor.hasNext()) {

			try {
				while (cursor.hasNext()) {

					Activity activity = cursor.next();
					System.out.println(activity);

					actividades.add(activity);

				}
			} finally {
				cursor.close();
			}

		}
		
		return actividades;
	}
	
	


}