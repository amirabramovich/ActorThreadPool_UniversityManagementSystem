package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

/**
 * This action Register student to one course according to preference list.
 * Student will be registered to one course.
 * 
 *
 */
public class RegisterWithPreferences extends Action<Boolean> {
	
	private String studentName;
	private LinkedList<String> courses;
	private LinkedList<String> grade;
	private int index;
	private List <Action<Boolean>> attempts;

/**
 * Constructor gets name of student name of courses list of grades and action name	
 * @param name
 * @param courses
 * @param grade
 * @param aName
 */
	public RegisterWithPreferences(String name,LinkedList<String> courses,LinkedList<String> grade, String aName) { 
		this.studentName=name;
		this.courses=courses;
		this.grade=grade;
		this.setActionName(aName);
		index=0;
		attempts=new ArrayList<>();
	}
/**
 * Iterates on courses list and try to register to all of them.
 * then, if registered to more than one course, unregister from the rest.
 */
	@Override
	protected void start() {
		if(index==courses.size()-1||courses.size()==0)
			this.complete(false);
		else {
			Register attempt = new Register(studentName,courses.get(index),grade.get(index),"register");
			attempts.add(attempt);
			sendMessage(attempt, courses.get(index), (CoursePrivateState)Simulator.actorThreadPool.getPrivateState(courses.get(index)));	
			this.then(attempts, ()->{
				if(attempt.getResult().get()==true) //suceeded to register
					this.complete(true);		
				else {
					index=index+1;
					start();					
				}
			});
		}
	}	
}
