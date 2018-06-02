package bgu.spl.a2.sim.actions;


import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

/**
 * This class represents an action submitted to course actor,adding specified num of spaces to course.
 */
public class AddSpots extends Action<Boolean> {

	private String course;
	private Integer availableSpots;
/**
 * Constructor for action,gets course name num of spaces to add and action name
 * @param course
 * @param availableSpots
 * @param aName
 */
	
	public AddSpots(String course,Integer availableSpots,String aName) { 
		this.course=course;
		this.availableSpots=availableSpots;
		this.setActionName(aName);
	}
/**
 * Action updates the privatestate of course with new available spaces 
 */
	@Override
	protected void start() {
		CoursePrivateState c=(CoursePrivateState) Simulator.actorThreadPool.getPrivateState(course);
		if(c.getAvailableSpots()<0)
			this.complete(false);
		else {
			c.setAvailableSpots(c.getAvailableSpots()+availableSpots);
			this.complete(true);
		}
	}
}
