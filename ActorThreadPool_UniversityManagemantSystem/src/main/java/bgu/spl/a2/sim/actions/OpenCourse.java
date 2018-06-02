/**
 * 
 */
package bgu.spl.a2.sim.actions;

import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
/**
 * This action opens course in department
 * 
 *
 */
public class OpenCourse extends Action<Boolean> {
	
	private String course;
	private String department;
	private Integer availableSpots;
	private List<String> prerequisites;
/**
 * Constructor gets course name department name prerequisites of course and availableSpots of course 	
 * @param course
 * @param department
 * @param availableSpots
 * @param prerequisites
 * @param aName
 */
	public OpenCourse(String course,String department,Integer availableSpots,List<String> prerequisites,String aName) { 
		this.course=course;
		this.department=department;
		this.availableSpots=availableSpots;
		this.prerequisites=prerequisites;
		this.setActionName(aName);
	}
/**
 * Open actor for course and update its privatestate
 */
	@Override
	protected void start() {
		DepartmentPrivateState d=(DepartmentPrivateState) Simulator.actorThreadPool.getPrivateState(department);
		CoursePrivateState c=new CoursePrivateState();
		c.setAvailableSpots(availableSpots);
		c.setPrequisites(prerequisites);
		Simulator.actorThreadPool.submit(null, course, c);
		d.addToCourseList(course);
		this.complete(true);//no need "then" there are no pre actions
		
	}

}
