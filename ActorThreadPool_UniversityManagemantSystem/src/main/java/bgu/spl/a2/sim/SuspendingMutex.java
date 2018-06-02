package bgu.spl.a2.sim;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl.a2.Promise;

/**
 * 
 * this class is related to {@link Computer}
 * it indicates if a computer is free or not
 * 
 * Note: this class can be implemented without any synchronization. 
 * However, using synchronization will be accepted as long as the implementation is blocking free.
 *
 */
public class SuspendingMutex {
	
	private Computer computer;
	private AtomicBoolean isFree;
	private ConcurrentLinkedQueue<Promise<Computer>> promises;
	

	
	/**
	 * Constructor
	 * @param computer
	 */
	public SuspendingMutex(Computer computer){
		this.computer=computer;
		isFree=new AtomicBoolean(true);
		promises=new ConcurrentLinkedQueue<>();
	}
	/**
	 * Returns the flag if computer is free or not
	 * @return AtomicBoolean flag
	 */
	public AtomicBoolean isFree(){
		return isFree;
	}
	

	/**
	 * Computer acquisition procedure
	 * Note that this procedure is non-blocking and should return immediatly
	 * 
	 * @return a promise for the requested computer
	 */
	public Promise<Computer> down(){
		Promise<Computer> tmp= new Promise<Computer>();
		if(isFree.compareAndSet(true, false)) {
			
			tmp.resolve(this.computer);
			return tmp;
		}
		else {
			promises.add(tmp);
			return tmp;
		}
		
			
	}
	/**
	 * Computer return procedure
	 * releases a computer which becomes available in the warehouse upon completion
	 */
	public void up(){
		if(!promises.isEmpty()) 
			promises.poll().resolve(computer);		
		else
			isFree.compareAndSet(false, true);
	}
	/**
	 * Returns  Computer 
	 * @return Computer
	 */
	
	public Computer getComputer(){
		return computer;
	}
}
