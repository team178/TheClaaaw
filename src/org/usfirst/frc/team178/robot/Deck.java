package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Deck implements RunningComponent {
	
	private Joystick joystick;
	private DigitalInput frontLimit;
	private DigitalInput backLimit;
	private Talon motor;
	
	public Deck(Joystick joystick, DigitalInput backLimit,
			DigitalInput frontLimit, Talon motor) {
		super();
		this.joystick = joystick;
		this.frontLimit = frontLimit;
		this.backLimit = backLimit;
		this.motor = motor;
	}

	@Override
	public void teleop() {
		double direction;
		if(joystick.getRawButton(3)) // towards the front
			direction= -1;
		else if(joystick.getRawButton(4)) // towards the back
			direction = 1;
		else
			direction=0;
		
		setDirection(direction);
		//this.motor.set(joystick.getY()*.25);
		
		SmartDashboard.putBoolean("Front Limit", this.frontLimit.get());
		SmartDashboard.putBoolean("Back Limit", this.backLimit.get());
	}

	private boolean setDirection(double direction) {
		
		boolean isNotSafe = 
				(direction<0 && !this.frontLimit.get()) || //if we're moving out but not too far OR
				(direction>0 && !this.backLimit.get()); //if we're moving in but not too close OR
		
		if(isNotSafe){
			direction = 0; //we're not safe; prevent the motor from moving
		}
		
		//move the motor (or don't if unsafe)
		SmartDashboard.putBoolean("Motor is not safe?", isNotSafe); //notify users of safety
		SmartDashboard.putNumber("Direction",direction);// for testing purposes
		
		this.motor.set(direction);
		
		return isNotSafe;
		
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
