package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;

/**
 * 
 * this class is helper action for unregister action, it is used in order to make sure register is done before unregister in case we have both of them in same phase.
 * register is using 2 helper actions and unregister is using one, so we made this action in order to fix this case
 */
public class EmptyAction extends Action<Boolean> {

	public EmptyAction() {
		this.setActionName("Empty Action");
	}
	
	@Override
	protected void start() {
		this.complete(true);			
	}

}
