package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class AddToGradeSheet extends Action<Boolean> {
	

	private String course;
	private Integer grade;
	private StudentPrivateState studentPS;

	public AddToGradeSheet(StudentPrivateState studentPS, String course, Integer grade) {
		this.course=course;
		this.grade=grade;
		this.studentPS=studentPS;
		this.setActionName("AddToGradeSheet");
	}
	
	@Override
	protected void start() {
		studentPS.addGrade(course, grade);
		this.complete(true);
	}

}
