package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class TapeShooter implements RunningComponent{

	private Talon tapeMotor; 
	
	
	public TapeShooter(Talon tapeMotor) {
		super();
		this.tapeMotor = tapeMotor;
	}

	@Override
	public void teleop(Joystick driver, Joystick aux) {
		// TODO Auto-generated method stub
		tapeMotor.set(aux.getRawAxis(3));
	}

	@Override
	public void test(Joystick driver) {
		// TODO Auto-generated method stub
		
	}

}