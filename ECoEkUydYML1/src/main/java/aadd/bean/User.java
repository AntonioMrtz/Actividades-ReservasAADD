package aadd.bean;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
