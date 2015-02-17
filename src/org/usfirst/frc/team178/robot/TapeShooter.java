package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class TapeShooter {
	Talon tapeMotor = new Talon(8);
	Joystick joystick = new Joystick(2);
	
	public void shoot(){
		if(joystick.getRawButton(3)){
			tapeMotor.set(1);
		}
		
		else if(joystick.getRawButton(4)){
			tapeMotor.set(-1);
		}
		
		else{
			tapeMotor.set(0);
		}
	}
}
