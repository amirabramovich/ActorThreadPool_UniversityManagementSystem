/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.actions.AddSpots;
import bgu.spl.a2.sim.actions.AddStudent;
import bgu.spl.a2.sim.actions.AdministrativeCheck;
import bgu.spl.a2.sim.actions.CloseCourse;
import bgu.spl.a2.sim.actions.OpenCourse;
import bgu.spl.a2.sim.actions.Register;
import bgu.spl.a2.sim.actions.RegisterWithPreferences;
import bgu.spl.a2.sim.actions.Unregister;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	
	public static ActorThreadPool actorThreadPool;
	
	private static JsonObject jsonObject;//will hold the parsed json file as java object
	
	/**This methos parse the Json file given and Submit the actions to the Thread pool.
	 * The json file has threads filed computers field and phase 1 phase phase 3 fileds each has actions.
	 * We parse the threads field in main method in order to initialize  actor thread pool  and calling to attach method
	*  the Computer field is parsed to array and used to create warehouse and its suspending mutexes.
	*  The phases are parsed one by one getting from each one its actions.
	*  We use CountDownlatch in order to separate between phases, and verifying that the program will not end untill all tasks are done.
	*  We use helper function to pare each phase actions and submit them.
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
    	JsonArray computers=jsonObject.get("Computers").getAsJsonArray();//parse computers 
    	Warehouse warehouse=new Warehouse();
    	for(JsonElement e:computers){
    		JsonObject computer=e.getAsJsonObject();
    		String type=(computer.get("Type").getAsString());
    		long failSig=(computer.get("Sig Success").getAsLong());
    		long successSig=(computer.get("Sig Fail").getAsLong());
    		warehouse.addComputer(type, failSig, successSig);//finish creating warehouse
    	}
    	JsonArray phase1=jsonObject.get("Phase 1").getAsJsonArray();//parse phase1
    	CountDownLatch latch1 = new CountDownLatch(phase1.size());
    	JsonArray phase2=jsonObject.get("Phase 2").getAsJsonArray(); //parse phase2
    	CountDownLatch latch2 = new CountDownLatch(phase2.size());
    	JsonArray phase3=jsonObject.get("Phase 3").getAsJsonArray();//parse phase3
    	CountDownLatch latch3 = new CountDownLatch(phase3.size());
    	phaseOrder(latch1,warehouse,phase1);
    	try {
    		latch1.await();//main thread waits for phase one to finish
		} catch (InterruptedException e1) {	}

    	phaseOrder(latch2,warehouse,phase2);
    	try {
			latch2.await();
		} catch (InterruptedException e1) {	}

    	phaseOrder(latch3,warehouse,phase3);
    	try {
			latch3.await();
		} catch (InterruptedException e1) {	}


    }
    
    /**
     * Helper function parses the phases to its actions and submit them
     * @param latch latch for this phase every action has callback that countdown latch
     * @param warehouse holds all computers needed to administrative actions
     * @param phaseX the phase object to be parsed
     */
    private static void phaseOrder(CountDownLatch latch,Warehouse warehouse,JsonArray phaseX){
    	
    	for(JsonElement e:phaseX){//iteratring on phase to parse actions
	  		
    		JsonObject action=e.getAsJsonObject();
    		String Action=(action.get("Action").getAsString());
    		
    		if(Action.equals("Open Course")){//classify actions
    			String Department=(action.get("Department").getAsString());
    			String Course=(action.get("Course").getAsString());
    			Integer Space=(action.get("Space").getAsInt());
    			JsonArray Prerequisites=(action.get("Prerequisites").getAsJsonArray());
    			LinkedList<String> PrerequisitesA=new LinkedList<>();
    			for(JsonElement p:Prerequisites)
    				PrerequisitesA.add(p.getAsString());
    	    	OpenCourse openCourse=new OpenCourse(Course, Department, Space, PrerequisitesA, "Open Course");
    	    	openCourse.getResult().subscribe(()->{latch.countDown();});
    	    	
    	    	actorThreadPool.submit(openCourse, Department, new DepartmentPrivateState());
    		}
    		
    		else if(Action.equals("Add Student")){
    			String Department=(action.get("Department").getAsString());
    			String Student=(action.get("Student").getAsString());
    			AddStudent addstudent=new AddStudent(Student, Department, "Add Student");
    	    	addstudent.getResult().subscribe(()->{latch.countDown();});
    	    	
    	    	actorThreadPool.submit(addstudent, Department, new DepartmentPrivateState());
    		}
    		
    		else if(Action.equals("Participate In Course")){			
    			String Student=(action.get("Student").getAsString());
    			String Course=(action.get("Course").getAsString());
    			JsonArray Grades=(action.get("Grade").getAsJsonArray());
    			String Grade=Grades.get(0).getAsString();
    			Register r=new Register(Student, Course, Grade, "Participate In Course");
    	    	r.getResult().subscribe(()->{latch.countDown();});
    	    	
    			actorThreadPool.submit(r, Course, new CoursePrivateState());
    		}
    		
    		else if(Action.equals("Register With Preferences")){			
    			String Student=(action.get("Student").getAsString());
    			JsonArray Preferences=(action.get("Preferences").getAsJsonArray());
    			JsonArray Grades=(action.get("Grade").getAsJsonArray());
    			LinkedList<String> PreferencesL=new LinkedList<>();
    			LinkedList<String> GradesL=new LinkedList<>();
    			for(int i=0;i<Grades.size();i++){
    				PreferencesL.add(Preferences.get(i).getAsString());
    				GradesL.add(Grades.get(i).getAsString());
    			}
    			RegisterWithPreferences r=new RegisterWithPreferences(Student, PreferencesL, GradesL, "Register With Preferences");
    	    	r.getResult().subscribe(()->{latch.countDown();});

    			actorThreadPool.submit(r, Student, new StudentPrivateState()); 	    	
    		}
    		
    		else if(Action.equals("Unregister")){			
    			String Student=(action.get("Student").getAsString());
    			String Course=(action.get("Course").getAsString());
    			Unregister u=new Unregister(Student,Course,"Unregister");
    	    	u.getResult().subscribe(()->{latch.countDown();});

    			actorThreadPool.submit(u, Course, new CoursePrivateState()); 	    	
    		}
    		
    		else if(Action.equals("Add Spaces")){			
    			String Course=(action.get("Course").getAsString());
    			Integer Number=(action.get("Number").getAsInt());
    			AddSpots a=new AddSpots(Course, Number, "Add Spaces");
    	    	a.getResult().subscribe(()->{latch.countDown();});

    			actorThreadPool.submit(a, Course, new CoursePrivateState()); 	    	
    		}
    		
    		else if(Action.equals("Close Course")){			
    			String Department=(action.get("Department").getAsString());
    			String Course=(action.get("Course").getAsString());
    			CloseCourse c=new CloseCourse(Course, Department, "Close Course");
    	    	c.getResult().subscribe(()->{latch.countDown();});

    			actorThreadPool.submit(c, Department, new DepartmentPrivateState()); 	    	
    		}
    		
    		else if(Action.equals("Administrative Check")){			
    			String Department=(action.get("Department").getAsString());
    			JsonArray Students=(action.get("Students").getAsJsonArray());
    			String Computer=(action.get("Computer").getAsString());
    			JsonArray Conditions=(action.get("Conditions").getAsJsonArray());
    			LinkedList<String> StudentsL=new LinkedList<>();
    			LinkedList<String> ConditionsL=new LinkedList<>();
    			for(JsonElement s:Students)
    				StudentsL.add(s.getAsString());
    			for(JsonElement c:Conditions)
    				ConditionsL.add(c.getAsString());
    			AdministrativeCheck a=new AdministrativeCheck(warehouse, Department, Computer, StudentsL, ConditionsL,"Administrative Check");
    	    	a.getResult().subscribe(()->{latch.countDown();});

    			actorThreadPool.submit(a, Department, new DepartmentPrivateState()); 	    	
    		}  		
    	}
    }
    
	/**
	* attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	* 
	* @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	*/
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		actorThreadPool=myActorThreadPool;
	}
	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static HashMap<String,PrivateState> end(){
		try {
			actorThreadPool.shutdown();
		} catch (InterruptedException e) {	}
		HashMap<String, PrivateState> tmp=new HashMap<String, PrivateState>();
		for(Entry<String, PrivateState> entry : actorThreadPool.getActors().entrySet()) 
			tmp.put(entry.getKey(), entry.getValue());	
		return tmp;
	}
	
	/**The main method should run only after attach actorthreadpool method
	 * Its goal to parse the threads field from json object to initialize actor threadpool,and start it.
	 * Then it starts the start method and after it is finished it shutdowns safely all threads 
	 * returning the log of actions to file.
	 * 
	 * @param args will include the json file to be parsed
	 */
	public static void main(String [] args){
		try{
			jsonObject = new JsonParser().parse(new FileReader(args[0])).getAsJsonObject();
			int threads=jsonObject.get("threads").getAsInt();
			attachActorThreadPool(new ActorThreadPool(threads));
			actorThreadPool.start();
			start();
		}
		catch(FileNotFoundException e){
			System.out.println("file not found");
		}
		
		HashMap<String,PrivateState> SimulationResult=end();
		
		try {
			FileOutputStream fout = new FileOutputStream("result.ser");
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(fout);
			oos.writeObject(SimulationResult);
			oos.close();
		} catch (IOException e) {	}			
	}
}
