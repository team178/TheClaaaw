package org.usfirst.frc.team178.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;

public abstract class ActionHelper {
	protected Timer timer = new Timer();
	{
		timer.start();
	}
	static ArrayList<ActionHelper> actions = new ArrayList<> ();
	public ActionHelper() {
		actions.add(this);
	}
	
	/**Contains boolean expr. Condition for start*/
	public abstract boolean shouldRun();
	/** loopable things to run
	 * return true when the actions have finished 
	 * interruptions increase by one if shouldRun switches to false*/
	public abstract boolean toRun(int interruptions);
	/**final message, this sets off the next lines
	 * automatically called when true is returned from {@link}shouldRun() */
	public abstract void whenDone();
	int interruptions = 0;
	boolean done = false;
	void run() {
		
		if (shouldRun()) {
			timer.reset();
			while(!done && shouldRun()){
				done = toRun(interruptions);
			}
			if(done)
				whenDone();
			else
				interruptions++;
		} else {
		
		}
	}
	static protected void resetAllCompletionFlags() {
		for (ActionHelper actionHelper : actions) {
			actionHelper.done = false;
		}
	}
	
}
