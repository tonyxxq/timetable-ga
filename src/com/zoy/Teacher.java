package com.zoy;

/**
 * Simple Teacher abstraction.
 */
public class Teacher {
	private int id;
	private String name;
	private int clazzId;
	private String subject;

	public Teacher(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Teacher(int id, String name, int clazzId, String subject) {
		super();
		this.id = id;
		this.name = name;
		this.clazzId = clazzId;
		this.subject = subject;
	}

	public int getClazzId() {
		return clazzId;
	}

	public void setClazzId(int clazzId) {
		this.clazzId = clazzId;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
}
