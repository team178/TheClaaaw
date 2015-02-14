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
	private double direction;
	
	public Deck(Joystick joystick, DigitalInput backLimit,
			DigitalInput frontLimit, Talon motor) {
		super();
		this.joystick = joystick;
		this.frontLimit = frontLimit;
		this.backLimit = backLimit;
		this.motor = motor;
		this.direction = 0;
	}

	@Override
	public void teleop() {
		this.motor.set(direction);
		setDirection();
		//this.motor.set(joystick.getY()*.25);
		
		SmartDashboard.putBoolean("Front Limit", this.frontLimit.get());
		SmartDashboard.putBoolean("Back Limit", this.backLimit.get());
	}

	private boolean setDirection() {
		
		if(joystick.getRawButton(3))
			direction= -.5;
		else if(joystick.getRawButton(4))
			direction = .5;
		else if(joystick.getRawButton (5))
			direction=0;
		
		boolean isNotSafe = 
				(direction<0 && !this.frontLimit.get()) || //if we're moving out but not too far OR
				(direction>0 && !this.backLimit.get()); //if we're moving in but not too close OR
		
		if(isNotSafe){
			direction = 0; //we're not safe; prevent the motor from moving
		}
		
		//move the motor (or don't if unsafe)
		SmartDashboard.putBoolean("Motor is not safe?", isNotSafe); //notify users of safety
		SmartDashboard.putNumber("Direction",direction);// for testing purposes
		
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
