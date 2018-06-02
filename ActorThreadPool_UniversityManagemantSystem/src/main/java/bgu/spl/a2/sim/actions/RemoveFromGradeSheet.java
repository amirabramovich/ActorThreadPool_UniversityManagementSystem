package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class RemoveFromGradeSheet extends Action<Boolean> {

	private StudentPrivateState studentPS;
	private String course;
	
	public RemoveFromGradeSheet(StudentPrivateState studentPS, String course){
		this.course=course;
		this.studentPS=studentPS;
		this.setActionName("Remove from GradeSheet");
	}
	@Override
	protected void start() {
		studentPS.removeGrade(course);
		this.complete(true);	
	}

}
