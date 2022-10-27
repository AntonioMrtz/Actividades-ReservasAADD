package dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
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
    
 

}