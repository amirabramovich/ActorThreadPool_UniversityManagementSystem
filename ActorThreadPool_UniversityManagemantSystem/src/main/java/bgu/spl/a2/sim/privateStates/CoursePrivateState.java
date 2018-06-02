package bgu.spl.a2.sim.privateStates;

import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState{

	private Integer availableSpots;
	private Integer registered;
	private List<String> regStudents;
	private List<String> prequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 * Constructor initializes all fields
	 */
	public CoursePrivateState() {
		 availableSpots=0;
		 registered=0;
		 regStudents=new LinkedList<String>();
		 prequisites=new LinkedList<String>();
	}
    /**
     * Getter for available spots
     * @return Integer availablespots
     */
	public Integer getAvailableSpots() {
		return availableSpots;
	}
   /**
    * Getter for number of registered students
    * @return Integer registered
    */
	public Integer getRegistered() {
		return registered;
	}
  /**
   * Getter for registered students
   * @return List<String> regstudents
   */
	public List<String> getRegStudents() {
		return regStudents;
	}
  /**
   * Getter for prerequisites of course
   * @return List<String> prequisites
   */
	public List<String> getPrequisites() {
		return prequisites;
	}
	/**
	 * Setter for Available spots 
	 * @param Integer  availableSpots
	 */
	public void setAvailableSpots(Integer availableSpots) {
		this.availableSpots=availableSpots;
	}
    /**
     * Setter for number of registered students
     * @param Integer registered
     */
	public void setRegistered(Integer registered) {
		this.registered=registered;
	}
    /**
     * Setter to add student to course
     * @param String student
     */
	public void addStudent(String student) {
		this.regStudents.add(student);
	}
	/**
	 * Removes student from course
	 * @param String studnet
	 */
	public void removeStudent(String student) {
		this.regStudents.remove(student);
	}
    /**
     * Setter for Prequisites 
     * @param List<String> prequisites
     */
	public void setPrequisites(List<String> prequisites) {
		this.prequisites=prequisites;
	}
}
