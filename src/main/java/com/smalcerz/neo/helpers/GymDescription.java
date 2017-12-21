package com.smalcerz.neo.helpers;

public class GymDescription {
	
	private String name;
	private String tagline;
	
	
	
	
	public GymDescription(String name, String tagline) {
		super();
		this.name = name;
		this.tagline = tagline;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTagline() {
		return tagline;
	}
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}
	
	
	 
}
