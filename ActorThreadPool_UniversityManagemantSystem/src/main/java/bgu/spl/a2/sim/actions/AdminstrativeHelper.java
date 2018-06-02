package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.SuspendingMutex;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
/**Helper action for administrativecheck action.It performs the actual check the administrative action needs to do
 * guarantees the computer is free for the action.
 * 
 * 
 *
 */
public class AdminstrativeHelper extends Action<Boolean> {
	
	private Warehouse warehouse;
	private String comp;
	private List<String> students;
	private List<String> courses;
/**
 * Constructor is identitcal to Administrativecheck 	
 * @param warehouse
 * @param department
 * @param comp
 * @param students
 * @param courses
 * @param name
 */
	public AdminstrativeHelper(Warehouse warehouse,String department,String comp,List<String> students,List<String> courses,String name) { 
		this.students=students;
		this.courses=courses;
		this.comp=comp;
		this.warehouse=warehouse;
		this.setActionName(name);

	}
/**
 *This function is activated via administrativecheck if computer is not free and this action is submitted ,therefore we need to complete this action in addition to the check of the students
 * with helperstart func.
 */
	@Override
	protected void start() {
		SuspendingMutex sm=warehouse.getComputers().get(comp);
		Computer c=sm.getComputer();
		List <Action<Boolean>> actions=new ArrayList<>();
		for(String student:students) {//only the thread who handles this does this loop
			StudentPrivateState studentPS=(StudentPrivateState)Simulator.actorThreadPool.getPrivateState(student);	
			SetSig action=new SetSig(student,c,courses);
			actions.add(action);
			Simulator.actorThreadPool.submit(action, student, studentPS);
		}
		this.then(actions, ()->{
			sm.up();
			this.complete(true);
		});	
	}
}