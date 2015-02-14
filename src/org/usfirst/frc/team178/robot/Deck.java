package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Deck implements RunningComponent {
	
	private Joystick joystick;
	private DigitalInput outerLimit;
	private DigitalInput innerLimit;
	private Talon motor;
	
	public Deck(Joystick joystick, DigitalInput outerLimit,
			DigitalInput innerLimit, Talon motor) {
		super();
		this.joystick = joystick;
		this.outerLimit = outerLimit;
		this.innerLimit = innerLimit;
		this.motor = motor;
	}

	@Override
	public void teleop() {
		if(joystick.getRawButton(3))
			moveMotor(-1.0);
		else if(joystick.getRawButton(4))
			moveMotor(1.0);
	}

	private boolean moveMotor(double d) {
		boolean isSafe = 
				(d>0 && !this.outerLimit.get()) || //if we're moving out but not too far OR
				(d<0 && !this.innerLimit.get()) || //if we're moving in but not too close OR
				(d<0.001 && d >-0.001); //if the motor shouldn't move...
		
		if(!isSafe){
			d = 0; //we're not safe; prevent the motor from moving
		}
		
		motor.set(d); //move the motor (or don't if unsafe)

		SmartDashboard.putBoolean("Motor is safe?", isSafe); //notify users of safety
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
