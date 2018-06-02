package bgu.spl.a2.sim.privateStates;

import java.util.HashMap;

import bgu.spl.a2.PrivateState;

/**
 * this class describe student private state
 */
public class StudentPrivateState extends PrivateState{

	private HashMap<String, Integer> grades;
	private long signature;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 * Constructor initializes grades.
	 */
	public StudentPrivateState() {
		grades=new HashMap<>();
	}
    /**
     * Getter for grades
     * @return grades
     */
	public HashMap<String, Integer> getGrades() {
		return grades;
	}
    /**
     * Getter for signature
     * @return signature
     */
	public long getSignature() {
		return signature;
	}
	/**
	 * Setter for grades list
	 * @param grades
	 */
	public void setGrades(HashMap<String, Integer> grades) {
		this.grades=grades;
	}
	/**
	 * Add grade of course to list
	 * @param s name of course
	 * @param grade of course
	 */
	public void addGrade(String course, Integer grade) {
		this.grades.put(course, grade);
	}
	/**
	 * Removes grade from student
	 * @param s course given
	 */
	public void removeGrade(String course) {
		this.grades.remove(course);
	}
    /**
     * Setter for signature 
     * @param signature
     */
	public void setSignature(long signature) {
		this.signature=signature;
	}
}
