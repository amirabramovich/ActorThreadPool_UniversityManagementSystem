package bgu.spl.a2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {

	private final VersionMonitor version;
	private final Thread[] threads;
	private final ConcurrentHashMap <String, ConcurrentLinkedQueue<Action<?>>> ActorActions;
	private final ConcurrentHashMap <String, PrivateState> ActorStates;
	private final ConcurrentHashMap <String, AtomicBoolean> FreeActors;
	private final AtomicBoolean running;
	
	/**
	 * creates a {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 *
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads			
	 *            the number of threads that should be started by this thread
	 *            pool
	 */
	
	public ActorThreadPool(int nthreads) { 
		version=new VersionMonitor();
		threads=new Thread[nthreads];
		ActorActions = new ConcurrentHashMap<>();
		ActorStates = new ConcurrentHashMap<>();
		FreeActors = new ConcurrentHashMap<>();
		running=new AtomicBoolean(true);
		for(int i=0;i<nthreads;i++){		
			threads[i]=new Thread(()->{	
				task(); 
			});	
		}
	}
	/**
	 * Helper function represents the task thread gets to do.We use Event loop as design pattern here.
	 * We set flag running which we will set to false when we want the thread to finish its work.While flag is true the thread keep searching for actions.
	 * every time we enter the task we iterate over the actions actors searching for free actor.if no action was found in free actor we send the thread to sleep until new action is inserted and wake up all threads.
	 *This mechanism is using the version monitor and flag handled to determine if action was handled or not.
	 *We enable one thread lock an actor with blocking free implementation using atomic operation.
	 * @return void
	 * 
	 * 
	 */
	private void task() {
		while(running.get()) {
			boolean handled=false;//flag which checks if thread did any action
			int v=version.getVersion();
			for(ConcurrentHashMap.Entry<String, AtomicBoolean> entry : FreeActors.entrySet()) {
				
				if(entry.getValue().compareAndSet(true, false)) {//changes the value in map in order to "lock" the actor(queue)
					String key=entry.getKey();
					if(!ActorActions.get(key).isEmpty()) {//checks if actor has action to handle
						ActorActions.get(key).poll().handle(this, key, ActorStates.get(key)); //handle the action
						handled=true;
						if(!ActorActions.get(key).isEmpty()) //after finishing the action, check if queue is not empty    	
					    	version.inc();//does inc only if actor has actions
					}	
					FreeActors.put(key, new AtomicBoolean(true));//after finished handling changes to true 
				}	
			}
			if((!handled)&&(running.get())) {//only if finished loop without doing any actions
				try {
					version.await(v); //goes sleep after finish loop in case no changes in version
				} catch (InterruptedException e) {}
			}
		}		
	}
	
	/**
	 * getter for actors
	 * @return actors
	 */
	public Map<String, PrivateState> getActors(){
		return ActorStates;
	}
	
	
	
	/**
	 * getter for actor's private state
	 * @param actorId actor's id
	 * @return actor's private state
	 */
	public PrivateState getPrivateState(String actorId){
		return ActorStates.get(actorId);
	}

	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action
	 *            the action to execute
	 * @param actorId
	 *            corresponding actor's id
	 * @param actorState
	 *            actor's private state (actor's information)
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		ConcurrentLinkedQueue<Action<?>> q= new ConcurrentLinkedQueue<Action<?>>();//creates new queue
		ActorActions.putIfAbsent(actorId, q);//add new actor(+queue) to the map only if absent
		if(action!=null)//used in order we want send to the function null action
			ActorActions.get(actorId).add(action); //add action to the actor(queue)
		ActorStates.putIfAbsent(actorId, actorState);//add new state in case new actor
		FreeActors.putIfAbsent(actorId, new AtomicBoolean(true));//add true flag in case new actor, for existing actor the flag does not change
		version.inc();
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 *
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException
	 *             if the thread that shut down the threads is interrupted
	 */
	public void shutdown() throws InterruptedException {
		running.compareAndSet(true, false);
		version.inc();
		for(Thread t:threads) 
			t.join();//waiting for threads to finish .
	}
	

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		for(Thread t:threads)
			t.start();//enables threads to run
	}

}
