package dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import com.mongodb.client.model.Filters;
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
    	//Bson query= Filters.and(query_id,query_plazas);
    	
    	long resultados  = collection.countDocuments(query);
    	
    	return resultados>0;
    	
    }
    
    public boolean reservar(String code,Integer user_code) {
    	
    	if(checkActivityExists(code) && checkCanReservar(code,user_code)) {
    		
    		Bson query_id=Filters.eq("_id",code);
    		
    		Bson query_user= Updates.push("users",user_code);
    		Bson query_spots= Updates.inc("availableSpots", -1);

    		
    		collection.findOneAndUpdate(query_id, query_user);
    		collection.findOneAndUpdate(query_id, query_spots);

    		
    		return true;
    	}

    	
    	return false;
    }
    
 

}