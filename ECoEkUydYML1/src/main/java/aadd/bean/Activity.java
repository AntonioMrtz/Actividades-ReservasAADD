package aadd.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonId;

public class Activity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@BsonId
	private String code;
	private LocalDateTime startDate;
	private int availableSpots;
	private String activityName;
	private String description;
	private String type;
	private List<Integer> users;
	
	public Activity() {
		
		
		
	}
	
	public Activity(String code,LocalDateTime startDate,int availableSpots,String activityName,String description,String type) {
		
		
		this.code=code;
		this.startDate=startDate;
		this.availableSpots=availableSpots;
		this.activityName=activityName;
		this.description=description;
		this.type=type;
		
		users=new ArrayList<>();
		
	}
	
	
	public void addUser(Integer u) {
		
		users.add(u);
		
	}
	
	public void decreaseAvailableSpots() {
		
		availableSpots-=1;
	}
	
	
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public int getAvailableSpots() {
		return availableSpots;
	}

	public void setAvailableSpots(int availableSpots) {
		this.availableSpots = availableSpots;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Integer> getUsers() {
		return users;
	}

	public void setUsers(List<Integer> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Activity [code=" + code + ", startDate=" + startDate + ", availableSpots=" + availableSpots
				+ ", activityName=" + activityName + ", description=" + description + ", type=" + type + ", users="
				+ users + "]";
	}
	
}
	