package bgu.spl.a2.sim.actions;

import java.util.LinkedList;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
/**
 * This action closes a course,unregistering all students from course and from grade list of students
 * 
 * 
 *
 */
public class CloseCourse extends Action<Boolean> {
	
	private String course;
	private String department;

	
	public CloseCourse(String course,String department,String aName) { 
		this.course=course;
		this.department=department;
		this.setActionName(aName);
	}
/**
 * Sets available spots to -1 of course and unregister all students from course
 */
	@Override
	protected void start() {
		DepartmentPrivateState d=(DepartmentPrivateState) Simulator.actorThreadPool.getPrivateState(department);
		d.removeFromCourseList(course);
		CoursePrivateState c=(CoursePrivateState) Simulator.actorThreadPool.getPrivateState(course);
		CloseCourseHelper action=new CloseCourseHelper(c, course);
		LinkedList<Action<Boolean>> actions=new LinkedList<>();
		actions.add(action);
		sendMessage(action, course, c);
		this.then(actions, ()->{	
			this.complete(true);
		});				
	}
}
