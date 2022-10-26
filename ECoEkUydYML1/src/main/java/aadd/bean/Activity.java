package aadd.bean;

import java.util.Date;
import java.util.HashSet;

public class Activity {
	
	private String code;
	private Date startDate;
	private int availableSpots;
	private String activityName;
	private String description;
	private String type;
	private HashSet<User> users;
	
	public Activity(String code,Date startDate,int availableSpots,String activityName,String description,String type) {
		
		this.code=code;
		this.startDate=startDate;
		this.availableSpots=availableSpots;
		this.activityName=activityName;
		this.description=description;
		this.type=type;
		
		users=new HashSet<>();
		
	}
	
	
	public String getCode() {
		return code;
	}

	public Date getStartDate() {
		return startDate;
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
	


	public HashSet<User> getUsers() {
		return new HashSet<>(users);
	}

	public void addUser(User u) {
		
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
