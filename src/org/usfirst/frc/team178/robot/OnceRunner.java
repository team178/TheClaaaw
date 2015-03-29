package org.usfirst.frc.team178.robot;

public abstract class OnceRunner {
	private boolean firstRun = true;
	public boolean tick(Object...objects){
		boolean canRun = this.canRun(objects);
		if(canRun){
			if(firstRun){
				firstRun = false;
				once(objects);
			} else {
				then(objects);
			}
		} else {
			if(!firstRun){
				end(objects);
			}
			firstRun = true;
		}
		return canRun;
	}
	
	public abstract boolean canRun(Object[] objects);
	
	public abstract void once(Object[] objects);
	public abstract void then(Object[] objects);
	public abstract void end(Object[] objects);
}
