package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.SuspendingMutex;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
/**
 * This action try to allocate one computer from warehouse and check for each student if he meets some administrative obligations.
 * The computer generates a signature and saves it in privatestate of students.
 * Initially submitted to departments actor.
 * 
 *
 */
public class AdministrativeCheck extends Action<Boolean> {
	
	private Warehouse warehouse;
	private String comp;
	private String department;
	private List<String> students;
	private List<String> courses;
/**
 * Constructor gets warehouse of computers,department name list of students to be checked and action name.	
 * @param warehouse
 * @param department
 * @param comp
 * @param students
 * @param courses
 * @param name
 */
	public AdministrativeCheck(Warehouse warehouse,String department, String comp,List<String> students,List<String> courses,String name) { 
		this.students=students;
		this.courses=courses;
		this.comp=comp;
		this.warehouse=warehouse;
		this.department=department;
		this.setActionName(name);
	}
/**
 * Action implementation ,try to fetch computer required if its free and do the action using helper action calling to its start function.
 * Otherwise if computer is locked we subscribe callback to promise we got from warehouse in which we send the helper function to be submitted and done once the computer is free.
 * The promise will be solved by the thread releasing the computer calling the callback .In such way all threads are blocking free.
 * 
 */
	@Override
	protected void start() {
		SuspendingMutex sm=warehouse.getComputers().get(comp);
		Promise<Computer> tmp=sm.down();
		AdminstrativeHelper action=new AdminstrativeHelper(warehouse,department,comp,students,courses,"AdminHelper");
		DepartmentPrivateState d=(DepartmentPrivateState) Simulator.actorThreadPool.getPrivateState(department);
		List <Action<Boolean>> a=new ArrayList<>();
		a.add(action);
		tmp.subscribe(()->{					
			sendMessage(action, department, d);	
		});
		this.then(a, ()->{
			this.complete(true);
		});
	}
}
