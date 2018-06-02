package bgu.spl.a2.sim.actions;

import java.util.LinkedList;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class CloseCourseHelper extends Action<Boolean> {

	private CoursePrivateState coursePS;
	private String course;
	
	protected CloseCourseHelper(CoursePrivateState coursePS,String course) { 
		this.coursePS=coursePS;
		this.course=course;
		this.setActionName("close course helper");
	}
	@Override
	protected void start() {
		coursePS.setAvailableSpots(-1);
		LinkedList<Action<Boolean>> actions=new LinkedList<>();
		for(String s:coursePS.getRegStudents()) {		
			Unregister a=new Unregister(s,course,"unregister helper");
			actions.add(a);
			sendMessage(a, course, coursePS);
		}
		this.then(actions, ()->{
			this.complete(true);
		});	
		
		
		
	}

}
