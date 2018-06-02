package bgu.spl.a2.sim;

import java.util.List;
import java.util.Map;
/**
 * This class represents a computer that is used in Administrative check action .each computer has its unique id ,failsig and success sig.
 * The signutares are used to identify the status of each student according to the computer result on action related to the student.
 * 
 *
 */
public class Computer {

	private String computerType;
	private long failSig;
	private long successSig;
/**
 * Constructor gets the id of the computer
 * @param computerType 
 */
	public Computer(String computerType) {
		this.computerType = computerType;
	}
/**
 * Sets	the successSig and failSig of computer
 * @param failSig
 * @param successSig
 */
	public void SetSig(long failSig,long successSig) {
		this.failSig = failSig;
		this.successSig = successSig;
	}
/**
 * 	Returns the computertype
 * @return String id  
 */
	public String getComputer() {
		return computerType;
	}
/**
 * Return the failsig
 * @return long failsig
 */
	public long getFSig() {
		return failSig;
	}
/**
 * Returns the successSig	
 * @return the successSig
 */
	public long getSSig() {
		return successSig;
	}
	
	/**
	 * this method checks if the courses' grades fulfill the conditions
	 * @param courses
	 * 							courses that should be pass
	 * @param coursesGrades
	 * 							courses' grade
	 * @return a signature if couersesGrades grades meet the conditions
	 */
	public long checkAndSign(List<String> courses, Map<String, Integer> coursesGrades){
		for(String course:courses) {
			if(coursesGrades==null) {
				return failSig;
			}
			else if(coursesGrades.containsKey(course)==false) {
				return failSig;
			}	
			else if(coursesGrades.get(course)==null) {
				return failSig;
			}
			else if(coursesGrades.get(course).intValue()<56) {
				return failSig;
			}
		}
		return successSig;
	}
}
