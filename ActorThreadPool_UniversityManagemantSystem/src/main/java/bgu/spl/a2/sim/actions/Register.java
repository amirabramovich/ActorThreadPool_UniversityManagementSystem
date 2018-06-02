package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;
import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
/**
 * This action registers student with grade to course.if succeeds add the course to grade sheet of student.
 * 
 *
 */
public class Register extends Action<Boolean> {
	

	private String studentName;
	private String course;
	private Integer grade;

/**
 * Constructor gets student name course name grade and actions name
 * @param name
 * @param course
 * @param grade
 * @param aName
 */
	
	public Register(String name,String course,String grade, String aName) { 
		this.studentName=name;
		this.course=course;
		this.setActionName(aName);
		if(grade.equals("-")==false) 
			this.grade=Integer.valueOf(grade);
		else
			this.grade=-1;

	}
/**
 * This function try to register student to course.The action succeeds if there are available spaces and student meets prerequisites of course
 * If succeeded updates the student grade . 
 */
	@Override
	protected void start() {
		StudentPrivateState studentPS=(StudentPrivateState)Simulator.actorThreadPool.getPrivateState(studentName);
		CoursePrivateState c=(CoursePrivateState)Simulator.actorThreadPool.getPrivateState(course);
		List<String> pre=new ArrayList<>();
		for(String prec:c.getPrequisites())
			pre.add(prec);
		CheckPrequisites check=new CheckPrequisites(studentPS, pre);
		List <Action<Boolean>> actions= new ArrayList<>();
		actions.add(check);
		sendMessage(check, studentName, studentPS);	
		this.then(actions, ()->{
			if(c.getAvailableSpots()>0&&check.getResult().get()==true) {
				c.setAvailableSpots(c.getAvailableSpots().intValue()-1);
				c.setRegistered(c.getRegistered().intValue()+1);
				c.addStudent(studentName);
				AddToGradeSheet action=new AddToGradeSheet(studentPS, course, grade);
				List <Action<Boolean>> a=new ArrayList<>();
				a.add(action);
				sendMessage(action, studentName, studentPS);	
				this.then(a, ()->{
					this.complete(true);	
				});
			}
			else
				this.complete(false);
		});	
	}
}
