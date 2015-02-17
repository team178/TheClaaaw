package org.usfirst.frc.team178.robot;

public abstract class ActionHelper {
	
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
						if(done)
							whenDone();
						else
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
