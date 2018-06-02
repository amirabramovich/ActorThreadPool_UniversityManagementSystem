package bgu.spl.a2.sim.privateStates;

import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * This class describe department's private state
 */
public class DepartmentPrivateState extends PrivateState{
	private List<String> courseList;
	private List<String> studentList;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 * Constructor initializes all fields 
	 */
	public DepartmentPrivateState() {
		courseList=new LinkedList<>();
		studentList=new LinkedList<>();
	}
    /**
     * Getter for courselist
     * @return List<String>
     */
	public List<String> getCourseList() {
		return courseList;
	}
    /**
     * Getter for studentlist
     * @return List<String>
     */
	public List<String> getStudentList() {
		return studentList;
	}
	/**
	 * Adds to courselist a course 
	 * @param course
	 * @return true  add succeed 
	 */
	public boolean addToCourseList(String course) {
		return courseList.add(course);
	}
	/**
	 * Adds to studentlist a student
	 * @param student
	 * @return true add succeed
	 */
	public boolean addToStudentList(String student) {
		return studentList.add(student);
	}
	/**
	 * Removes from courselist a course
	 * @param course
	 * @return true add succeed
	 */
	public boolean removeFromCourseList(String course) {
		return courseList.remove(course);
	}
	
	
}
