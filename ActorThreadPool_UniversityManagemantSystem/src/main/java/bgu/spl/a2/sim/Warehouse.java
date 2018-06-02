package bgu.spl.a2.sim;

import java.util.concurrent.ConcurrentHashMap;

/**
 * represents a warehouse that holds a finite amount of computers
 *  and their suspended mutexes.
 * 
 */
public class Warehouse {
	
	private ConcurrentHashMap<String, SuspendingMutex> computers;//maps unique id to suspendingMutex 
	/**
	 * Constructor initializes the map of computers 
	 */
	public Warehouse() {
		this.computers=new ConcurrentHashMap<>();
	}
	/**
	 * adds the computer to warehouse creating for it SuspendingMutex and Computer object
	 * @param computer the computer to be added
	 * @param failSig fail sig param of computer
	 * @param successSig success sig param of computer
	 */
	public void addComputer(String computer,long failSig,long successSig) {
		Computer comp=new Computer(computer);
		comp.SetSig(failSig, successSig);
		computers.putIfAbsent(computer, new SuspendingMutex(comp));
	}
	/**
	 * returns the map that holds the ids and suspendingmutex
	 * @return ConcurrentHashMap<String,SuspendingMutex> 
	 */
	public ConcurrentHashMap<String, SuspendingMutex> getComputers() {
		return computers;
	}
	
}
