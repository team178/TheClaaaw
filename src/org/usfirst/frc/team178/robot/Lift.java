package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class Lift implements RunningComponent{
	private Joystick joystick;
	private Talon motor;
	private DigitalInput bottomLimit;
	
	public Lift(Joystick joystick, Talon motor, DigitalInput bottomLimit) {
		super();
		this.joystick = joystick;
		this.motor = motor;
		this.bottomLimit = bottomLimit;
	}
	@Override
	public void teleop() {
		
		boolean bottomLSPressed = bottomLimit.get();
		
		if (joystick.getRawButton(11)) { //going up
			motor.set(1);
		} else if (joystick.getRawButton(12)) { //going down
			if (bottomLSPressed){
				motor.set(0);
			} else{
				motor.set(-1);
			}
		} else 
			motor.set(0);
		
		
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





