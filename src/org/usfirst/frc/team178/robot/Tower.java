package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Tower implements RunningComponent{
	private Joystick joystick;
	private DigitalInput upperLimit;
	private DigitalInput lowerLimit;
	private Talon tower1;
	
	public Tower(Joystick joystick, DigitalInput outerLimit, 
			DigitalInput innerLimit, Talon motor) {
		super();
		this.joystick = joystick;
		this.upperLimit = outerLimit;
		this.lowerLimit = innerLimit;
		this.tower1 = motor;
	}
	
	public void teleop() {
		if(joystick.getRawButton(3))
			moveTower(-1.0);
		else if(joystick.getRawButton(4))
			moveTower(1.0);
	}
	
	private boolean moveTower(double d) {
		boolean isSafe =
				(d>0 && !this.upperLimit.get()) || //if we go too far up
				(d<0 && !this.lowerLimit.get()) || //if we go too far down
				(d == 0);
		if(!isSafe){
			d = 0; //not safe, stop motor from moving
		} 
		
		tower1.set(d); //motor movement
		
		SmartDashboard.putBoolean("Tower is safe?", isSafe); //tell drivers
		
		return isSafe;
	}

	@Override
	public void auto() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub
		
	}
}
