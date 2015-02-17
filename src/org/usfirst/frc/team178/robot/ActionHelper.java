package org.usfirst.frc.team178.robot;

import java.util.ArrayList;

public abstract class ActionHelper {
	
	public static ArrayList<ActionHelper> registeredActionHelpers = new ArrayList<ActionHelper>();
	protected boolean alreadyFinished = false;
	public ActionHelper() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					int interruptions = 0;
					if (shouldRun()) {
						boolean done = false;
						while(!done && shouldRun()){
							done = toRun(interruptions);
							Thread.currentThread().interrupt();
						}
						if(done){
							whenDone();
							alreadyFinished = true;
						}else
							interruptions++;
						break;
					} else {
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							
						}
					}
				}
			}
		}).start();
	}
	
	public static void clearActionCompletion() {
		for (ActionHelper actionHelper : registeredActionHelpers) {
			actionHelper.alreadyFinished = false;
		}
	}
	
	public abstract boolean shouldRun();
	public abstract boolean toRun(int interruptions);
	public abstract void whenDone();
	
	
}
