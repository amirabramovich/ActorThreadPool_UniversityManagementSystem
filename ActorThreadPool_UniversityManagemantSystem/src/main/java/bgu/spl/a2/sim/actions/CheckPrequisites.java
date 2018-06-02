package bgu.spl.a2.sim.actions;

import java.util.HashMap;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class CheckPrequisites extends Action<Boolean> {

	private StudentPrivateState studentPS;
	private List<String> Prequisites;
	
	protected CheckPrequisites(StudentPrivateState studentPS,List<String> Prequisites) {
		this.Prequisites=Prequisites;
		this.studentPS=studentPS;
		this.setActionName("Check Prequisites");
		
	}
	
	@Override
	protected void start() {
		HashMap<String, Integer> grades=studentPS.getGrades();
		boolean flag=true;
		for(String course:Prequisites) {
			if(grades.containsKey(course)==false) {
				flag=false;
			}
		}
		this.complete(flag);	
	}
}
