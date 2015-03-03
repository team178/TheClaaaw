package org.usfirst.frc.team178.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

public class DipSwitches implements RunningComponent {
	public static DigitalInput RightSwitch;

	public DipSwitches(DigitalInput rightSwitch) {
		super();
		RightSwitch = rightSwitch;
		
	}

	@Override
	public void teleop(Joystick driver, Joystick aux) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void test(Joystick driver) {
		// TODO Auto-generated method stub
		
	}
	
}
