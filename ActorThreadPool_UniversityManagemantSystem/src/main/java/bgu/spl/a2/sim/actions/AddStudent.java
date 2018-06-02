package bgu.spl.a2.sim.actions;




import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
/**
 * Action adds new student to specified department.
 * Initially submitted to department actor.
 * 
 * 
 *
 */
public class AddStudent extends Action<Boolean> {

	private String name;
	private String department;
/**
 * Constructor gets name of course to add,department which holds the course and action name.	
 * @param name
 * @param department
 * @param aName
 */
	
	public AddStudent(String name,String department,String aName) { 
		this.name=name;
		this.department=department;
		this.setActionName(aName);
	}
/**
 * action implementation gets department private state adds course to actor and to department course list
 */
	@Override
	protected void start() {
		DepartmentPrivateState d=(DepartmentPrivateState) Simulator.actorThreadPool.getPrivateState(department);
		StudentPrivateState s=new StudentPrivateState();
		Simulator.actorThreadPool.submit(null, name, s);
		d.addToStudentList(name);
		this.complete(true);
		
		
	}

}
