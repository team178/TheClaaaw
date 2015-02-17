package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class TapeShooter {
	Talon tapeMotor = new Talon(8);
	Joystick joystick = new Joystick(2);
	
	public void shoot(){
		tapeMotor.set(joystick.getRawAxis(3));
		
	}
}
