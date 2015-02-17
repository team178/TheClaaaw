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
		});
	}
	
	public abstract boolean shouldRun();
	public abstract boolean toRun(int interruptions);
	public abstract void whenDone();
	
	
}
