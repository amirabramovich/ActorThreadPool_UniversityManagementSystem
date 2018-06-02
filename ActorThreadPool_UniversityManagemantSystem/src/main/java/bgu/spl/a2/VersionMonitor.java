package bgu.spl.a2;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitor {
	private  AtomicInteger  _version=new AtomicInteger(0);

    public int getVersion() {
        return _version.get();
    }

    public synchronized void inc() { //added synchronized in order to solve visibility (cache updates to cpu)and thread saftey
    	_version.getAndIncrement();
    	notifyAll();
    }

    public synchronized void await(int version) throws InterruptedException {//added synchronized to solve thread safety
    	while(this._version.get()==version)
    		 try{ 
                 this.wait(); 
         } catch (InterruptedException ignored){} 
    }
}