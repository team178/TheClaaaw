package org.usfirst.frc.team178.robot;

import java.util.ArrayList;
import java.util.Arrays;

import edu.wpi.first.wpilibj.Timer;

public abstract class ActionHelper {
	
	private static ArrayList<ActionHelper> actions = new ArrayList<>();
	
	protected Timer timer = new Timer();
	{
		timer.start();
	}
	protected boolean finishedRunning = false;
	public ActionHelper() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					int interruptions = 0;
					if (shouldRun()) {
						timer.reset();
						boolean done = false;
						while(!done && shouldRun()){
							done = toRun(interruptions);
							System.out.println(Thread.currentThread().getName()+" is running");
							try {
								Thread.sleep(166);
							} catch (InterruptedException e) {
								
							}
						}
						if(done){
							whenDone();
							finishedRunning = true;
						} else
							interruptions++;
						break;
					} else 
						try {
							Thread.sleep(333);
						} catch (InterruptedException e) {
							
						}
					
				}
			}
		});
		t.setName(Thread.currentThread().getStackTrace()[2].toString());
		t.start();
	}
	
	public static void resetAllActionCompletions() {
		for (ActionHelper actionHelper : actions) {
			actionHelper.finishedRunning = false;
		}
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
	
	
}
