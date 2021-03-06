package com.zoy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Timetable is the main evaluation class for the class scheduler GA.
 * 
 * A timetable represents a potential solution in human-readable form, unlike an
 * Individual or a chromosome. This timetable class, then, can read a chromosome
 * and develop a timetable from it, and ultimately can evaluate the timetable
 * for its fitness and number of scheduling clashes.
 * 
 * The most important methods in this class are createClasses and calcClashes.
 * 
 * The createClasses method accepts an Individual (really, a chromosome),
 * unpacks its chromosome, and creates Class objects from the genetic
 * information. Class objects are lightweight; they're just containers for
 * information with getters and setters, but it's more convenient to work with
 * them than with the chromosome directly.
 * 
 * The calcClashes method is used by GeneticAlgorithm.calcFitness, and requires
 * that createClasses has been run first. calcClashes looks at the Class objects
 * created by createClasses, and figures out how many hard constraints have been
 * violated.
 * 
 */
public class Timetable {
	private final HashMap<Integer, Teacher> teachers;
	private final HashMap<Integer, Timeslot> timeslots;
	private Class classes[];

	private int numClasses = 0;

	/**
	 * Initialize new Timetable
	 */
	public Timetable() {
		this.teachers = new LinkedHashMap<Integer, Teacher>();
		this.timeslots = new HashMap<Integer, Timeslot>();
	}

	/**
	 * "Clone" a timetable. We use this before evaluating a timetable so we have
	 * a unique container for each set of classes created by "createClasses".
	 * Truthfully, that's not entirely necessary (no big deal if we wipe out and
	 * reuse the .classes property here), but Chapter 6 discusses
	 * multi-threading for fitness calculations, and in order to do that we need
	 * separate objects so that one thread doesn't step on another thread's
	 * toes. So this constructor isn't _entirely_ necessary for Chapter 5, but
	 * you'll see it in action in Chapter 6.
	 * 
	 * @param cloneable
	 */
	public Timetable(Timetable cloneable) {
		this.teachers = cloneable.getTeachers();
		this.timeslots = cloneable.getTimeslots();
	}

	private HashMap<Integer, Timeslot> getTimeslots() {
		return this.timeslots;
	}

	public HashMap<Integer, Teacher> getTeachers() {
		return this.teachers;
	}

	public void addTeacher(int id, Teacher teacher) {
		this.teachers.put(id, teacher);
	}

	/**
	 * Add new timeslot
	 * 
	 * @param timeslotId
	 * @param timeslot
	 */
	public void addTimeslot(int timeslotId, String timeslot) {
		this.timeslots.put(timeslotId, new Timeslot(timeslotId, timeslot));
	}

	/**
	 * Create classes using individual's chromosome
	 * 
	 * One of the two important methods in this class; given a chromosome,
	 * unpack it and turn it into an array of Class (with a capital C) objects.
	 * These Class objects will later be evaluated by the calcClashes method,
	 * which will loop through the Classes and calculate the number of
	 * conflicting timeslots, rooms, professors, etc.
	 * 
	 * While this method is important, it's not really difficult or confusing.
	 * Just loop through the chromosome and create Class objects and store them.
	 * 
	 * @param individual
	 */
	public void createClasses(Individual individual) {
		// Init classes
		Class classes[] = new Class[this.getNumClasses()];

		// Get individual's chromosome
		int chromosome[] = individual.getChromosome();
		int chromosomePos = 0;
		int classIndex = 0;
		
		Timetable timetable = TimetableGA.initializeTimetable();
		Map<Integer, Teacher> teachers = timetable.getTeachers();

		for (int i = 0; i < this.getNumClasses(); i++) {
			int clazzId = teachers.get(chromosome[chromosomePos]).getClazzId();
			classes[classIndex] = new Class(classIndex);
			
			classes[classIndex].setClazzId(clazzId);
			// Add teacher
			classes[classIndex].setTeacherId(chromosome[chromosomePos]);
			chromosomePos++;

			// Add timeslot
			classes[classIndex].setTimeslotId(chromosome[chromosomePos]);
			chromosomePos++;

			classIndex++;
		}
		
		this.classes = classes;
	}

	/**
	 * Get timeslot by timeslotId
	 * 
	 * @param timeslotId
	 * @return timeslot
	 */
	public Timeslot getTimeslot(int timeslotId) {
		return (Timeslot) this.timeslots.get(timeslotId);
	}

	/**
	 * Get random timeslotId
	 * 
	 * @return timeslot
	 */
	public Timeslot getRandomTimeslot() {
		Random random = new Random();
		return this.timeslots.get(random.nextInt(this.timeslots.size()));
	}

	/**
	 * Get classes
	 * 
	 * @return classes
	 */
	public Class[] getClasses() {
		return this.classes;
	}

	/**
	 * 有多少老师的课程需要被安排
	 * 
	 * @return numClasses
	 */
	public int getNumClasses() {
		return this.teachers.size();
	}

	/**
	 * Calculate the number of clashes between Classes generated by a
	 * chromosome.
	 * 
	 * The most important method in this class; look at a candidate timetable
	 * and figure out how many constraints are violated.
	 * 
	 * Running this method requires that createClasses has been run first (in
	 * order to populate this.classes). The return value of this method is
	 * simply the number of constraint violations (conflicting professors,
	 * timeslots, or rooms), and that return value is used by the
	 * GeneticAlgorithm.calcFitness method.
	 * 
	 * There's nothing too difficult here either -- loop through this.classes,
	 * and check constraints against the rest of the this.classes.
	 * 
	 * The two inner `for` loops can be combined here as an optimization, but
	 * kept separate for clarity. For small values of this.classes.length it
	 * doesn't make a difference, but for larger values it certainly does.
	 * 
	 * @return numClashes
	 */
	public int calcClashes() {
		int clashes = 0;

		Timetable timetable = TimetableGA.initializeTimetable();
		Map<Integer, Teacher> teachers = timetable.getTeachers();
		int i = 0;
		for (Class classA : this.classes) {
			// 同一个老师同一个时间段不能教两个班级
			int j = 0;
			for (Class classB : this.classes) {
				if (i != j) {
					if (teachers.get(classA.getTeacherId()).getId() == teachers.get(classB.getTeacherId()).getId()
							&& classA.getTimeslotId() == classB.getTimeslotId()) {
						clashes++;
					}
				}
				j++;
			}

			// 同一个老师不能在同一时间段给同一个班上课
			j = 0;
			for (Class classB : this.classes) {
				if (i != j) {
					if (teachers.get(classA.getTeacherId()).getId() == teachers.get(classB.getTeacherId()).getId()
							&& classA.getTimeslotId() == classB.getTimeslotId()
							&& classA.getClazzId() == classB.getClazzId()) {
						clashes++;
					}
				}
				j++;
			}

			// 相同班级不能有相同时间段的课程
			j = 0;
			for (Class classB : this.classes) {
				if (i != j) {
					if (classA.getTimeslotId() == classB.getTimeslotId()
							&& classA.getClazzId() == classB.getClazzId()) {
						clashes++;
					}
				}
				j++;
			}
			
			i++;
		}
		
		// 每天的第一节课为语文或英语
		for (Class classB : this.classes) {
			Teacher teacher = teachers.get(classB.getTeacherId());
			if ((classB.getTimeslotId() == 0 || classB.getTimeslotId() == 6)
					&& (!teacher.getSubject().equals("语文") && !teacher.getSubject().equals("英语"))) {
				clashes++;
			}
		}
		
		// 限制每天科目的数量，平均分配
		Map<String, Integer> class1FirstDay = new HashMap<String, Integer>();
		Map<String, Integer> class1SecondDay = new HashMap<String, Integer>();
		Map<String, Integer> class2FirstDay = new HashMap<String, Integer>();
		Map<String, Integer> class2SecondDay = new HashMap<String, Integer>();
		for (Class classB : this.classes) {
			Teacher teacher = teachers.get(classB.getTeacherId());
			// 第一天
			if (classB.getTimeslotId() < 6) {
				if(classB.getClazzId()==1){
					Integer day1 = class1FirstDay.get(teacher.getSubject());
					if (day1 != null) {
						clashes++;
					}
					class1FirstDay.put(teacher.getSubject(), 1);
				} else {
					Integer day1 = class2FirstDay.get(teacher.getSubject());
					if (day1 != null) {
						clashes++;
					}
					class2FirstDay.put(teacher.getSubject(), 1);
				}
			} else {
				if (classB.getClazzId() == 1) {
					Integer day1 = class1SecondDay.get(teacher.getSubject());
					if (day1 != null) {
						clashes++;
					}
					class1SecondDay.put(teacher.getSubject(), 1);
				} else {
					Integer day1 = class2SecondDay.get(teacher.getSubject());
					if (day1 != null) {
						clashes++;
					}
					class2SecondDay.put(teacher.getSubject(), 1);
				}
			}
		}

		return clashes;
	}
}