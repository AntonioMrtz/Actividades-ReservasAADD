package aadd.bean;

public class User {

	private int code;
	
	public User(int code) {
		
		this.code=code;
		
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "User [code=" + code + "]";
	}
	
}
