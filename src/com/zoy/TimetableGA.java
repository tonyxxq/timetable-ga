package com.zoy;

/**
 * Don't be daunted by the number of classes in this chapter -- most of them are
 * just simple containers for information, and only have a handful of properties
 * with setters and getters.
 * 
 * The real stuff happens in the GeneticAlgorithm class and the Timetable class.
 * 
 * The Timetable class is what the genetic algorithm is expected to create a
 * valid version of -- meaning, after all is said and done, a chromosome is read
 * into a Timetable class, and the Timetable class creates a nicer, neater
 * representation of the chromosome by turning it into a proper list of Classes
 * with rooms and professors and whatnot.
 * 
 * The Timetable class also understands the problem's Hard Constraints (ie, a
 * professor can't be in two places simultaneously, or a room can't be used by
 * two classes simultaneously), and so is used by the GeneticAlgorithm's
 * calcFitness class as well.
 * 
 * Finally, we overload the Timetable class by entrusting it with the
 * "database information" generated here in initializeTimetable. Normally, that
 * information about what professors are employed and which classrooms the
 * university has would come from a database, but this isn't a book about
 * databases so we hardcode it.
 * 
 * @author bkanber
 *
 */
public class TimetableGA {

    public static void main(String[] args) {
    	// Get a Timetable object with all the available information.
        Timetable timetable = initializeTimetable();
        
        // Initialize GA
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.05, 0.95, 5, 10);
        
        // Initialize population
        Population population = ga.initPopulation(timetable);
        
        // Evaluate population
        ga.evalPopulation(population, timetable);
        
        // Keep track of current generation
        int generation = 1;
        
        // Start evolution loop
        while (ga.isTerminationConditionMet(generation, 5000) == false
            && ga.isTerminationConditionMet(population) == false) {
            // Print fitness
            System.out.println("G" + generation + " Best fitness: " + population.getFittest(0).getFitness());

            // Apply crossover
            population = ga.crossoverPopulation(population);

            // Apply mutation
            population = ga.mutatePopulation(population, timetable);

            // Evaluate population
            ga.evalPopulation(population, timetable);

            // Increment the current generation
            generation++;
        }

        // Print fitness
        timetable.createClasses(population.getFittest(0));
        System.out.println();
        System.out.println("Solution found in " + generation + " generations");
        System.out.println("Final solution fitness: " + population.getFittest(0).getFitness());
        System.out.println("Clashes: " + timetable.calcClashes());

        // Print classes
        System.out.println();
        Class classes[] = timetable.getClasses();
		for (Class bestClass : classes) {
			Teacher teacher = timetable.getTeachers().get(bestClass.getTeacherId());
			System.out.print("班级 ： " + teacher.getClazzId());
			System.out.print("  老师ID: " + teacher.getId());
			System.out.print("  科目: " + teacher.getSubject());
			System.out.print("  老师: " + teacher.getName());
			System.out.println("  时间: " + timetable.getTimeslot(bestClass.getTimeslotId()).getTimeslot());
		}
    }

    /**
     * Creates a Timetable with all the necessary course information.
     * 
     * Normally you'd get this info from a database.
     * 
     * @return
     */
	public static Timetable initializeTimetable() {
		Timetable timetable = new Timetable();

		// 时间段, 两天的课表
		timetable.addTimeslot(0, "星期一上午第一节");
		timetable.addTimeslot(1, "星期一上午第二节");
		timetable.addTimeslot(2, "星期一上午第三节");
		timetable.addTimeslot(3, "星期一下午第一节");
		timetable.addTimeslot(4, "星期一下午第二节");
		timetable.addTimeslot(5, "星期一下午第三节");
		timetable.addTimeslot(6, "星期二上午第一节");
		timetable.addTimeslot(7, "星期二上午第二节");
		timetable.addTimeslot(8, "星期二上午第三节");
		timetable.addTimeslot(9, "星期二下午第一节");
		timetable.addTimeslot(10, "星期二下午第二节");
		timetable.addTimeslot(11, "星期二下午第三节");
		
		// 一 班老师
		timetable.addTeacher(3, new Teacher(2, "小华", 1, "语文"));
		timetable.addTeacher(4, new Teacher(2, "小华", 1, "语文"));
		
		timetable.addTeacher(5, new Teacher(3, "小东", 1, "英语"));
		timetable.addTeacher(6, new Teacher(3, "小东", 1, "英语"));
		
		timetable.addTeacher(1, new Teacher(1, "小明", 1, "数学"));
		timetable.addTeacher(2, new Teacher(1, "小明", 1, "数学"));
		
		timetable.addTeacher(7, new Teacher(4, "小张", 1, "生物"));
		timetable.addTeacher(8, new Teacher(4, "小月", 1, "地理"));
		
		timetable.addTeacher(9, new Teacher(5, "王二", 1, "化学"));
		
		timetable.addTeacher(10, new Teacher(6, "张三", 1, "物理"));
		
		timetable.addTeacher(11, new Teacher(7, "李四", 1, "美术"));
		
		timetable.addTeacher(12, new Teacher(8, "王五", 1, "体育"));
		
		// 二班老师
		timetable.addTeacher(15, new Teacher(2, "小华", 2, "语文"));
		timetable.addTeacher(16, new Teacher(2, "小华", 2, "语文"));

		timetable.addTeacher(17, new Teacher(3, "小东", 2, "英语"));
		timetable.addTeacher(18, new Teacher(3, "小东", 2, "英语"));
		
		timetable.addTeacher(13, new Teacher(1, "小明", 2, "数学"));
		timetable.addTeacher(14, new Teacher(1, "小明", 2, "数学"));

		timetable.addTeacher(19, new Teacher(9, "小雪", 2, "生物"));
		timetable.addTeacher(20, new Teacher(9, "小狗", 2, "地理"));

		timetable.addTeacher(21, new Teacher(5, "王二", 2, "化学"));

		timetable.addTeacher(22, new Teacher(6, "张三", 2, "物理"));

		timetable.addTeacher(23, new Teacher(7, "李四", 2, "美术"));

		timetable.addTeacher(24, new Teacher(8, "王五", 2, "体育"));
		
		// 二班老师
		/*timetable.addTeacher(25, new Teacher(2, "小华", 2, "语文"));
		timetable.addTeacher(26, new Teacher(2, "小华", 2, "语文"));

		timetable.addTeacher(27, new Teacher(3, "小东", 2, "英语"));
		timetable.addTeacher(28, new Teacher(3, "小东", 2, "英语"));
		
		timetable.addTeacher(29, new Teacher(1, "小明", 2, "数学"));
		timetable.addTeacher(30, new Teacher(1, "小明", 2, "数学"));

		timetable.addTeacher(31, new Teacher(9, "小雪", 2, "生物"));
		timetable.addTeacher(32, new Teacher(9, "小狗", 2, "地理"));

		timetable.addTeacher(33, new Teacher(5, "王二", 2, "化学"));

		timetable.addTeacher(34, new Teacher(6, "张三", 2, "物理"));

		timetable.addTeacher(35, new Teacher(7, "李四", 2, "美术"));

		timetable.addTeacher(35, new Teacher(8, "王五", 2, "体育"));*/
		
		return timetable;
	}
}
