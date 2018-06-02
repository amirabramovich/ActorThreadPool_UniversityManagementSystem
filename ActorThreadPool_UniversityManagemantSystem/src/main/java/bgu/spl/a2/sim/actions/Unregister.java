package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
/**
 * This action unregisters student from course if registerd to it.
 * Updates the list of students in course remove course from grade sheet of student
 * increasing number of available spaces
 *
 *
 */
public class Unregister extends Action<Boolean> {
	
	private String studentName;
	private String course;


/**
 * Constructor gets student name course name and action name	
 * @param name
 * @param course
 * @param aName
 */
	public Unregister(String name,String course,String aName) { 
		this.studentName=name;
		this.course=course;
		this.setActionName(aName);
	}
/**
 * Sets all course privatestates fields and removes grade from student 
 * using two helper actions, first is empty action and the second is the RemoveFromGradeSheet.
 * The first helper is used in order to ensure the register is done before unregister if they both were submitted in same phase.
 */
	@Override
	protected void start() {
		CoursePrivateState c=(CoursePrivateState)Simulator.actorThreadPool.getPrivateState(course);
		StudentPrivateState s=(StudentPrivateState)Simulator.actorThreadPool.getPrivateState(studentName);	
		EmptyAction empty=new EmptyAction();
		List <Action<Boolean>> emptylist=new ArrayList<>();
		emptylist.add(empty);
		sendMessage(empty, studentName, s);	
		this.then(emptylist, ()->{
			if(c.getRegStudents().contains(studentName)){
				if(c.getAvailableSpots()>-1)//in case course not closed
					c.setAvailableSpots(c.getAvailableSpots().intValue()+1);
				c.setRegistered(c.getRegistered().intValue()-1);
				c.removeStudent(studentName);
				RemoveFromGradeSheet action=new RemoveFromGradeSheet(s, course);
				List <Action<Boolean>> a=new ArrayList<>();
				a.add(action);
				sendMessage(action, studentName, s);	
				this.then(a, ()->{
					this.complete(true);	
				});
			}
			else
				this.complete(false);	
		});
	}
}
