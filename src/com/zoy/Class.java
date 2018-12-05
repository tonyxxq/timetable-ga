package com.zoy;

/**
 * A simple class abstraction -- basically a container for class, group, module,
 * professor, timeslot, and room IDs
 */
public class Class {

	private int id;

	private int timeslotId;
	private int teacherId;
	private int clazzId;

	public void setTimeslotId(int timeslotId) {
		this.timeslotId = timeslotId;
	}

	public int getClazzId() {
		return clazzId;
	}

	public void setClazzId(int clazzId) {
		this.clazzId = clazzId;
	}
	
	public Class(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTimeslotId() {
		return timeslotId;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
}
