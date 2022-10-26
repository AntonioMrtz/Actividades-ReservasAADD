package aadd.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonId;

public class Activity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@BsonId
	private String code;
	private Date startDate;
	private int availableSpots;
	private String activityName;
	private String description;
	private String type;
	private List<Integer> users;
	
	public Activity() {
		
		
		
	}
	
	public Activity(String code,Date startDate,int availableSpots,String activityName,String description,String type) {
		
		
		this.code=code;
		this.startDate=startDate;
		this.availableSpots=availableSpots;
		this.activityName=activityName;
		this.description=description;
		this.type=type;
		
		users=new ArrayList<>();
		
	}
	
	
	public String getCode() {
		return code;
	}


	public int getAvailableSpots() {
		return availableSpots;
	}

	public String getActivityName() {
		return activityName;
	}

	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}
	


	public ArrayList<Integer> getUsers() {
		return new ArrayList<>(users);
	}

	public void addUser(Integer u) {
		
		users.add(u);
		
	}
	
	public void decreaseAvailableSpots() {
		
		availableSpots-=1;
	}
	
	
	
	@Override
	public String toString() {
		return "Activity [code=" + code + ", startDate=" + startDate + ", availableSpots=" + availableSpots
				+ ", activityName=" + activityName + ", description=" + description + ", type=" + type + ", users="
				+ users + "]";
	}
	
}
	