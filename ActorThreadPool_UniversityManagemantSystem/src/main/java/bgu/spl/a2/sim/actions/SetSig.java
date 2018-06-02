package bgu.spl.a2.sim.actions;

import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class SetSig extends Action<Boolean> {
	
	private String student;
	private Computer comp;
	private List<String> courses;
	
	public SetSig(String student, Computer comp, List<String> courses ) {
		this.student=student;
		this.comp=comp;
		this.courses=courses;
		this.setActionName("set signature");
	}

	@Override
	protected void start() {
		StudentPrivateState s=(StudentPrivateState)Simulator.actorThreadPool.getPrivateState(student);
		long sig=(comp.checkAndSign(courses, s.getGrades()));		
		s.setSignature(sig);	
		this.complete(true);
	}
}
